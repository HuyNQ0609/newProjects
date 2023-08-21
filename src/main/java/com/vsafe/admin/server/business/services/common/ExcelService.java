package com.vsafe.admin.server.business.services.common;

import com.vsafe.admin.server.core.annotations.ExcelColumn;
import com.vsafe.admin.server.helpers.utils.file.ExcelUtils;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.*;
import java.lang.reflect.Field;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import static java.util.Objects.isNull;

@Service
@Slf4j
public class ExcelService {
    public <T> List<T> readData(File file, int rowStart, Class<T> clazz) throws IOException {
        FileInputStream fileInputStream = new FileInputStream(file);
        return readExcel(fileInputStream, rowStart, clazz);
    }

    public <T> List<T> readData(InputStream inputStream, int rowStart, Class<T> clazz) {
        return readExcel(inputStream, rowStart, clazz);
    }

    public List<String> readExcelSimple(InputStream inputStream, int rowStart) throws IOException {
        DataFormatter formatter = new DataFormatter();
        XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
        XSSFSheet worksheet = workbook.getSheetAt(0);
        XSSFRow row;
        List<String> list = new ArrayList<>();
        for (int i = rowStart; i <= worksheet.getPhysicalNumberOfRows(); i++) {
            row = worksheet.getRow(i);
            if (isRowEmpty(row)) {
                break;
            }
            list.add(formatter.formatCellValue(row.getCell(0)));
        }
        return list;
    }

    private <T> List<T> readExcel(InputStream inputStream, int rowStart, Class<T> clazz) {
        List<T> objectList = new ArrayList<>();
        try {
            XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
            XSSFSheet worksheet = workbook.getSheetAt(0);
            DataFormatter formatter = new DataFormatter();

            T object;
            Field[] fields = clazz.getDeclaredFields();
            for (int i = rowStart; i <= worksheet.getPhysicalNumberOfRows(); i++) {
                object = (T) Class.forName(clazz.getName()).newInstance();

                XSSFRow row = worksheet.getRow(i);

                if (isRowEmpty(row)) {
                    break;
                }

                for (int j = 0; j < fields.length; j++) {
                    fields[j].setAccessible(true);
                    fields[j].set(object, formatter.formatCellValue(row.getCell(j)));
                }
                objectList.add(object);
            }
        } catch (Exception e) {
            log.error(e.toString());
        }
        return objectList;
    }

    public <T> byte[] writeExcel(List<T> data, ExcelExportInfo excelExportInfo) {
        XSSFWorkbook workbook;
        byte[] bytes;
        try {
            int SHEET_INDEX = 0;
            InputStream is = new ClassPathResource(excelExportInfo.getTemplatePath()).getInputStream();
            workbook = new XSSFWorkbook(is);
            workbook.setSheetName(SHEET_INDEX, excelExportInfo.getSheetName());
            XSSFSheet sheet = workbook.getSheetAt(SHEET_INDEX);
            int rowBegin = excelExportInfo.getRowBegin();
            int rowIndex = excelExportInfo.getRowIndex();

            Row row;
            Cell cell;
            Object obj;
            Field[] declaredFields;
            ExcelColumn annotation;
            CellStyle dateCellStyle = ExcelUtils.dateCellStyle(workbook, true, "dd/MM/yyyy");
            CellStyle normalRightCellStyle = ExcelUtils.cellStyleCustom(workbook, "", "right", (short) 11, true);
            CellStyle textCellStyle = ExcelUtils.cellStyleCustom(workbook, "", "left", (short) 11, true);
            SimpleDateFormat sdf = new SimpleDateFormat();
            DecimalFormat nf = new DecimalFormat();
            for (T item : data) {
                row = sheet.createRow(rowBegin++);

                int i = 0;
                if (rowIndex > 0) {
                    cell = row.createCell(i++);
                    cell.setCellStyle(normalRightCellStyle);
                    cell.setCellValue(rowIndex++);
                }

                declaredFields = item.getClass().getDeclaredFields();
                for (Field field : declaredFields) {
                    annotation = field.getAnnotation(ExcelColumn.class);
                    if (annotation.isHidden())
                        continue;

                    cell = row.createCell(i++);
                    field.setAccessible(true);
                    if (StringUtils.isNotEmpty(annotation.datePattern())) {
                        cell.setCellStyle(dateCellStyle);
                        obj = field.get(item);
                        if (Objects.nonNull(obj)) {
                            sdf.applyPattern(annotation.datePattern());
                            cell.setCellValue(sdf.format((Date) obj));
                        }
                    } else if (StringUtils.isNotEmpty(annotation.numberPattern())) {
                        cell.setCellStyle(normalRightCellStyle);
                        obj = field.get(item);
                        if (Objects.nonNull(obj)) {
                            nf.applyPattern(annotation.numberPattern());
                            cell.setCellValue(nf.format(obj));
                        }
                    } else {
                        obj = field.get(item);
                        if (obj instanceof String)
                            cell.setCellStyle(textCellStyle);
                        else
                            cell.setCellStyle(normalRightCellStyle);
                        if (Objects.nonNull(obj)) {
                            cell.setCellValue(obj.toString());
                        }
                    }
                }
            }
            ExcelUtils.resizeColumn(sheet, rowBegin - 1);

            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            workbook.write(bos);
            bytes = bos.toByteArray();
            bos.close();
        } catch (IOException | IllegalAccessException e) {
            log.error("Có lỗi khi export excel: ", e);
            bytes = new byte[]{};
        }
        return bytes;
    }

    public static boolean isRowEmpty(XSSFRow row) {
        if (isNull(row)) return true;
        for (int i = row.getFirstCellNum(); i < row.getLastCellNum(); i++) {
            XSSFCell cell = row.getCell(i);
            if (cell != null && cell.getCellType() != CellType.BLANK)
                return false;
        }
        return true;
    }

    public static <T> List<T> readDataWithSheet(InputStream inputStream, int rowStart, Class<T> clazz, int sheetIndex) {
        return readExcelWithSheet(inputStream, rowStart, clazz, sheetIndex);
    }

    private static <T> List<T> readExcelWithSheet(InputStream inputStream, int rowStart, Class<T> clazz, int sheetIndex) {
        List<T> objectList = new ArrayList<>();
        try {
            XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
            XSSFSheet worksheet = workbook.getSheetAt(sheetIndex);
            DataFormatter formatter = new DataFormatter();

            T object;
            Field[] fields = clazz.getDeclaredFields();
            for (int i = rowStart; i <= worksheet.getPhysicalNumberOfRows(); i++) {
                object = (T) Class.forName(clazz.getName()).newInstance();

                XSSFRow row = worksheet.getRow(i);

                if (isRowEmpty(row)) {
                    break;
                }

                for (int j = 0; j < fields.length; j++) {
                    fields[j].setAccessible(true);
                    fields[j].set(object, formatter.formatCellValue(row.getCell(j)));
                }
                objectList.add(object);
            }
        } catch (Exception e) {
            log.error(e.toString());
        }
        return objectList;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ExcelExportInfo {
        private String templatePath;
        private String sheetName = "sheet01";
        /* Dòng bắt đầu ghi data */
        private int rowBegin;
        /* Số thứ tự của dòng */
        private int rowIndex;

        public String getTemplatePath() {
            if (StringUtils.isEmpty(templatePath))
                throw new IllegalArgumentException("templatePath is invalid");
            return templatePath;
        }

        public int getRowBegin() {
            if (rowBegin < 0)
                throw new IllegalArgumentException("rowBegin is invalid");
            return rowBegin;
        }

        public int getRowIndex() {
            if (rowIndex < 0)
                throw new IllegalArgumentException("rowIndex is invalid");
            return rowIndex;
        }
    }
}
