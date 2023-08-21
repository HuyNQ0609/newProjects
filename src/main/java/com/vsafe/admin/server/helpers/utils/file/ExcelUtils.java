package com.vsafe.admin.server.helpers.utils.file;

import com.poiji.bind.Poiji;
import com.poiji.exception.PoijiExcelType;
import com.poiji.option.PoijiOptions;
import com.vsafe.admin.server.helpers.constants.SystemConstants;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.RegionUtil;
import org.apache.poi.xssf.usermodel.*;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
@Slf4j
public class ExcelUtils {

    private static final String DATE_FORMAT = "dd/MM/yyyy hh:mm:ss";

    public static <T> List<T> readExcel(File file, Class<T> clazz) throws IOException {
        PoijiOptions options = PoijiOptions.PoijiOptionsBuilder.settings()
                .caseInsensitive(false)
                .ignoreWhitespaces(false)
                .build();
        List<T> data = new ArrayList<>();
        String fileName = file.getName();
        String extension = fileName.lastIndexOf(".") == -1 ? ""
                : fileName.substring(fileName.lastIndexOf(".") + 1);
        if ("xls".equals(extension) || "xlsx".equals(extension)) {
            data = Poiji.fromExcel(file, clazz , options);
        } else {
            throw new IOException("Lỗi đọc file excel");
        }
        return data;
    }

    public static <T> List<T> readExcel(InputStream is,String suffix, Class<T> clazz) throws IOException {
        PoijiOptions options = PoijiOptions.PoijiOptionsBuilder.settings()
                .caseInsensitive(false)
                .ignoreWhitespaces(false)
                .build();
        List<T> data = new ArrayList<>();
        if (SystemConstants.File.SUFFIX_XLS.equals(suffix)) {
            data = Poiji.fromExcel(is, PoijiExcelType.XLS, clazz , options);
        } else if (SystemConstants.File.SUFFIX_XLSX.equals(suffix)) {
            data = Poiji.fromExcel(is, PoijiExcelType.XLSX, clazz , options);
        } else {
            throw new IOException("Lỗi đọc file excel");
        }
        return data;
    }

    public static List<List<Object>> readExcel(File file) throws IOException {
        return readExcel(file,0,0,0);
    }

    public static  List<List<Object>> readExcel(File file, int sheetIndex) throws IOException {
        return readExcel(file,0,0,sheetIndex);
    }


    public static  List<List<Object>> readExcel(InputStream is, String suffix, int sheetIndex) throws IOException {
        return readExcel(is, suffix, 0,0, sheetIndex);
    }

    public static  List<List<Object>> readExcel(InputStream is, String suffix) throws IOException {
        return readExcel(is, suffix, 0,0, 0);
    }



    public static  List<List<Object>> readExcel(File file, int startRow, int startCol, int sheetIndex ) throws IOException {
        String fileName = file.getName();
        String extension = fileName.lastIndexOf(".") == -1 ? ""
                : fileName.substring(fileName.lastIndexOf(".") + 1);
        if ("xls".equals(extension)) {
            return readExcel2003(file, startRow, startCol, sheetIndex);
        } else if ("xlsx".equals(extension)) {
            return readExcel2007(file, startRow, startCol, sheetIndex);
        } else {
            throw new IOException("Lỗi đọc file excel");
        }
    }


    public static  List<List<Object>> readExcel(InputStream is, String suffix, int startRow, int startCol, int sheetIndex) throws IOException {
        if (SystemConstants.File.SUFFIX_XLS.equals(suffix)) {
            return readExcel2003(is, startRow, startCol,sheetIndex);
        } else if (SystemConstants.File.SUFFIX_XLSX.equals(suffix)) {
            return readExcel2007(is, startRow, startCol,sheetIndex);
        } else {
            throw new IOException("Lỗi đọc file excel");
        }
    }

    //    Read excel 2003
    private static  List<List<Object>> readExcel2003(InputStream is, int startRow, int startCol, int sheetIndex)
            throws IOException {
        HSSFWorkbook hwb = new HSSFWorkbook(is);
        HSSFSheet sheet = hwb.getSheetAt(sheetIndex);
        Object value = null;
        HSSFRow row = null;
        HSSFCell cell = null;
        return readExcel(hwb,sheet, row, cell, startRow, startCol);
    }

    private static List<List<Object>> readExcel2003(File file, int startRow, int startCol, int sheetIndex) throws IOException {
        return readExcel2003(new FileInputStream(file), startRow, startCol, sheetIndex);
    }

    //    Read excel 2007
    private static  List<List<Object>> readExcel2007(InputStream is, int startRow, int startCol, int sheetIndex)
            throws IOException {
        XSSFWorkbook xwb = new XSSFWorkbook(is);
        XSSFSheet sheet = xwb.getSheetAt(sheetIndex);
        XSSFRow row = null;
        XSSFCell cell = null;
        return readExcel(xwb,sheet, row, cell, startRow, startCol);
    }

    private static  List<List<Object>> readExcel2007(File file, int startRow, int startCol, int sheetIndex) throws IOException {
        return readExcel2007(new FileInputStream(file), startRow, startCol, sheetIndex);
    }

    private static  List readExcel(Workbook wb, Sheet sheet, Row row, Cell cell, int startRow, int startCol) throws IOException {
        List<List<Object>> list = new LinkedList<List<Object>>();
        Object value = null;
        int counter = startRow;
        for (int i = startRow; counter < sheet
                .getPhysicalNumberOfRows(); i++) {
            row = sheet.getRow(i);
            if (row == null) {
                continue;
            } else {
                counter++;
            }
            List<Object> linked = new LinkedList<Object>();
            for (int j = startCol; j <= row
                    .getLastCellNum(); j++) {
                cell = row.getCell(j);
                if (cell == null) {
                    continue;
                }
                DecimalFormat df = new DecimalFormat("0");
                SimpleDateFormat sdf = new SimpleDateFormat(SystemConstants.Date.BASE_DATE_FULL_TIME_FORMAT);
                DecimalFormat nf = new DecimalFormat("0.00");
                switch (cell.getCellType()) {
                    case STRING:
                        log.trace(i + "-" + j + " is String Type");
                        value = cell.getStringCellValue();
                        break;
                    case NUMERIC:
                        log.debug(i+"-"+j +" is Number Type; DateFomt: " + cell.getCellStyle().getDataFormatString());
                        if ("@".equals(cell.getCellStyle().getDataFormatString())) {
                            value = df.format(cell.getNumericCellValue());
                        } else if ("General".equals(
                                cell.getCellStyle().getDataFormatString())) {
                            value = nf.format(cell.getNumericCellValue());
                        } else {
                            value = sdf.format(org.apache.poi.ss.usermodel.DateUtil
                                    .getJavaDate(cell.getNumericCellValue()));
                        }
                        break;
                    case BOOLEAN:
                        log.trace(i + "-" + j + " is Boolean Type");
                        value = cell.getBooleanCellValue();
                        break;
                    case BLANK:
                        log.trace(i + "-" + j + " is Blank Type");
                        value = "";
                        break;
                    default:
                        log.trace(i + "-" + j + " is Default Type");
                        value = cell.toString();
                }
                linked.add(value);
            }
            list.add(linked);
        }
        wb.close();
        return list;
    }

    public static XSSFCellStyle cellStyleCustom(XSSFWorkbook workbook, String type, String alignment, short textSize, boolean border) {
        XSSFFont timeNewRomanFont = workbook.createFont();
        timeNewRomanFont.setFontName("Times New Roman");
        if (type.equals("bold"))
            timeNewRomanFont.setBold(true);
        if (type.equals("italic"))
            timeNewRomanFont.setItalic(true);
        if (type.equals("bold-italic")) {
            timeNewRomanFont.setBold(true);
            timeNewRomanFont.setItalic(true);
        }
        timeNewRomanFont.setFontHeightInPoints(textSize);

        XSSFCellStyle style = workbook.createCellStyle();
        if (border) {
            style = ExcelUtils.cellBorder(workbook);
        }
        if (alignment == "left")
            style.setAlignment(HorizontalAlignment.LEFT);
        if (alignment == "right")
            style.setAlignment(HorizontalAlignment.RIGHT);
        if (alignment == "center") {
            style.setAlignment(HorizontalAlignment.CENTER);
            style.setVerticalAlignment(VerticalAlignment.CENTER);
        }
        style.setFont(timeNewRomanFont);
        return style;
    }

    private static XSSFCellStyle cellBorder(XSSFWorkbook workbook) {
        XSSFCellStyle style = workbook.createCellStyle();
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderBottom(BorderStyle.THIN);
        return style;
    }

    public static void createCell(XSSFSheet sheet, int rowNum, int columNum, String value, XSSFCellStyle style) {
        Row row;
        if (sheet.getRow(rowNum) == null) {
            row = sheet.createRow(rowNum);
        }
        row = sheet.getRow(rowNum);
        Cell cell = row.createCell(columNum);
        cell.setCellValue(value);
        if (style != null) {
            cell.setCellStyle(style);
        }
    }

    public static void createLongCell(XSSFSheet sheet, int rowNum, int columNum, long value, XSSFCellStyle style) {
        Row row;
        if (sheet.getRow(rowNum) == null) {
            row = sheet.createRow(rowNum);
        }
        row = sheet.getRow(rowNum);
        Cell cell = row.createCell(columNum);
        cell.setCellValue(value);
        if (style != null) {
            style.setDataFormat((short) 1);
            cell.setCellStyle(style);
        }
    }

    public static void createDoubleCell(XSSFSheet sheet, int rowNum, int columNum, double value, XSSFCellStyle style) {
        Row row;
        if (sheet.getRow(rowNum) == null) {
            row = sheet.createRow(rowNum);
        }
        row = sheet.getRow(rowNum);
        Cell cell = row.createCell(columNum);
        cell.setCellValue(value);
        if (style != null) {
            style.setDataFormat((short) 1);
            cell.setCellStyle(style);
        }
    }

    public static XSSFCellStyle dateCellStyle(XSSFWorkbook workbook, boolean border) {
        return dateCellStyle(workbook, border, DATE_FORMAT);
    }

    public static XSSFCellStyle dateCellStyle(XSSFWorkbook workbook, boolean border, String dateFormat) {
        if (StringUtils.isBlank(dateFormat)) {
            dateFormat = DATE_FORMAT;
        }

        XSSFFont timeNewRomanFont = workbook.createFont();
        timeNewRomanFont.setFontName("Times New Roman");

        CreationHelper createHelper = workbook.getCreationHelper();
        XSSFCellStyle style = workbook.createCellStyle();
        if (border) {
            style = ExcelUtils.cellBorder(workbook);
        }
        style.setDataFormat(createHelper.createDataFormat().getFormat(dateFormat));
        style.setAlignment(HorizontalAlignment.RIGHT);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        style.setFont(timeNewRomanFont);
        return style;
    }

    public static XSSFCellStyle moneyCellStyle(XSSFWorkbook workbook, boolean border) {
        XSSFFont timeNewRomanFont = workbook.createFont();
        timeNewRomanFont.setFontName("Times New Roman");

        CreationHelper createHelper = workbook.getCreationHelper();
        XSSFCellStyle style = workbook.createCellStyle();
        if (border) {
            style = ExcelUtils.cellBorder(workbook);
        }
        style.setDataFormat(createHelper.createDataFormat().getFormat("#,##0"));
        style.setAlignment(HorizontalAlignment.RIGHT);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        style.setFont(timeNewRomanFont);
        return style;
    }

    public static void createDateCell(XSSFSheet sheet, int rowNum, int columNum, Date date, XSSFCellStyle style) {
        Row row;
        if (sheet.getRow(rowNum) == null) {
            row = sheet.createRow(rowNum);
        }
        row = sheet.getRow(rowNum);
        Cell cell = row.createCell(columNum);
        cell.setCellValue(date);
        if (style != null)
            cell.setCellStyle(style);
    }

    public static void mergeCellCustom(int firstRow, int lastRow, int firstCol, int lastCol, XSSFSheet sheet, boolean border) {

        CellRangeAddress mergedCell = new CellRangeAddress(
                firstRow, lastRow, firstCol, lastCol);
        sheet.addMergedRegion(mergedCell);

        if (border) {
            RegionUtil.setBorderTop(BorderStyle.THIN, mergedCell, sheet);
            RegionUtil.setBorderBottom(BorderStyle.THIN, mergedCell, sheet);
            RegionUtil.setBorderLeft(BorderStyle.THIN, mergedCell, sheet);
            RegionUtil.setBorderRight(BorderStyle.THIN, mergedCell, sheet);
        }
    }

    public static void createLongCellMoney(XSSFSheet sheet, int rowNum, int columNum, Long value, XSSFCellStyle style) {
        Row row;
        if (sheet.getRow(rowNum) == null) {
            row = sheet.createRow(rowNum);
        }
        row = sheet.getRow(rowNum);
        Cell cell = row.createCell(columNum);
        cell.setCellValue( value != null ? value : 0);
        if (style != null) {

            style.setDataFormat((short) 1);
            cell.setCellStyle(style);

        }
        cell.getCellStyle().setDataFormat(HSSFDataFormat.getBuiltinFormat("#,##0"));
    }

    public static void resizeColumn(XSSFSheet sheet, int headerColumn) {
        resizeColumn(sheet, headerColumn, true);
    }

    public static void resizeColumn(XSSFSheet sheet, int headerColumn, boolean useMergedCells) {
        Row row = sheet.getRow(headerColumn);
        for (int colNum = 0; colNum < row.getLastCellNum(); colNum++) {
            sheet.autoSizeColumn(colNum, useMergedCells);
        }
    }

    public static boolean checkRowIsEmpty(Row row) {
        if (row == null) {
            return true;
        }
        if (row.getLastCellNum() <= 0) {
            return true;
        }
        for (int cellNum = row.getFirstCellNum(); cellNum < row.getLastCellNum(); cellNum++) {
            Cell cell = row.getCell(cellNum);
            if (cell != null && cell.getCellType() != CellType.BLANK && StringUtils.isNotBlank(cell.toString())) {
                return false;
            }
        }
        return true;
    }

    public static List<Map<String, String>> getListCommissionTransFromExcel(Workbook wb, String headerData, int startRowDataIndex) {
        List<Map<String, String>> tempList = new ArrayList<Map<String, String>>();
        Map<Integer, String> header = new HashMap<Integer, String>();
        String[] arg = headerData.split(",");

        for (int i = 0; i < arg.length; i++) {
            if (!arg[i].equals(""))
                header.put(i, arg[i]);
        }

        for (Row row : wb.getSheetAt(0)) {
            if (checkRowIsEmpty(row)) break;
            if (row.getRowNum() < startRowDataIndex)
                continue;
            Map<String, String> tempMap = new HashMap<String, String>();

            for (Cell cell : row) {
                if (header.get(cell.getColumnIndex()) != null) {
                    switch (cell.getCellType()) {
                        case STRING:
                            tempMap.put(header.get(cell.getColumnIndex()), cell.getRichStringCellValue().getString());
                            break;
                        case NUMERIC:
                            tempMap.put(header.get(cell.getColumnIndex()), Long.toString((long) cell.getNumericCellValue()));
                            break;
                        case FORMULA:
                            tempMap.put(header.get(cell.getColumnIndex()), cell.getCellFormula());
                            break;
                    }
                }
            }
            tempList.add(tempMap);
        }

        return tempList;
    }

    public static void createStringCell(XSSFSheet sheet, int rowNum, int columNum, String value, XSSFCellStyle style, DataFormat fmt) {
        Row row;
        if (sheet.getRow(rowNum) == null) {
            row = sheet.createRow(rowNum);
        }
        row = sheet.getRow(rowNum);
        Cell cell = row.createCell(columNum);
        cell.setCellValue(value);
        if (style != null) {
            style.setDataFormat(
                    fmt.getFormat("@"));
            cell.setCellStyle(style);
        }
    }

    public static Workbook getWorkBook(FileInputStream inputStream) {
        Workbook workbook = null;
        try {
            workbook = WorkbookFactory.create(inputStream);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return workbook;
    }

    public static Row getRow(Sheet sheet, int index) {
        Row row = sheet.getRow(index);
        if (row == null) {
            row = sheet.createRow(index);
        }
        return row;
    }

    public static Cell getCell(Row row, int index) {
        Cell cell = row.getCell(index);
        if (cell == null) {
            cell = row.createCell(index);
        }
        return cell;
    }

    public static void createIntCell(XSSFSheet sheet, int rowNum, int columNum, int value, XSSFCellStyle style) {
        Row row;
        if (sheet.getRow(rowNum) == null) {
            row = sheet.createRow(rowNum);
        }
        row = sheet.getRow(rowNum);
        Cell cell = row.createCell(columNum);
        cell.setCellValue(value);
        if (style != null)
            cell.setCellStyle(style);
    }
    public static void createCellText(XSSFSheet sheet, int rowNum, int columNum, String value, XSSFCellStyle style, XSSFWorkbook workbook) {
        Row row;
        if (sheet.getRow(rowNum) == null) {
            row = sheet.createRow(rowNum);
        }
        row = sheet.getRow(rowNum);
        Cell cell = row.createCell(columNum);
        cell.setCellValue(value);
        XSSFDataFormat format = workbook.createDataFormat();
        if (style != null) {
            style.setDataFormat(format.getFormat("Text"));
            cell.setCellStyle(style);
        }
    }

    public static void export(HttpServletResponse response, byte[] data, String fileName) throws IOException {
        response.setContentType("application/octet-stream");
        response.setHeader("Content-Disposition", "attachment; filename=" + fileName);
        response.setContentLength(data.length);
        OutputStream outputStream = response.getOutputStream();
        outputStream.write(data);
        outputStream.close();
    }
}
