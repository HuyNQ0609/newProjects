package com.vsafe.admin.server.business.response.system.excel;

import com.poiji.annotation.ExcelCell;
import com.poiji.annotation.ExcelSheet;
import com.poiji.annotation.ExcelUnknownCells;
import lombok.Data;

@Data
@ExcelSheet("Sheet1")
public class AdministrativeUnitExcelRow {
    @ExcelCell(7)
    private String provinceName;

    @ExcelCell(6)
    private String provinceCode;

    @ExcelCell(5)
    private String districtName;
    @ExcelCell(4)
    private String districtCode;

    @ExcelCell(1)
    private String name;
    @ExcelCell(0)
    private String code;
}
