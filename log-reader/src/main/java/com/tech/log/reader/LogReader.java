/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tech.log.reader;

import java.io.FileInputStream;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 *
 * @author admin
 */
public class LogReader {

    private XSSFWorkbook workBook;

    public void setWorkBook(XSSFWorkbook myWorkBook) {
        this.workBook = myWorkBook;
    }

    public String read(int rowNum, int colNum) {
        XSSFSheet mySheet = workBook.getSheetAt(0);
        XSSFRow row = mySheet.getRow(rowNum);
        XSSFCell cell = row.getCell(colNum);
        return cell.getStringCellValue();
    }

    public void write(int rowNum, int colNum, String fc) {
        XSSFSheet mySheet = workBook.getSheetAt(0);
        XSSFRow row = mySheet.getRow(rowNum );
        if(row == null){
            row = mySheet.createRow(rowNum);
        }
        XSSFCell cell = row.getCell(colNum);
        if(cell == null){
            cell = row.createCell(colNum);
        }
        cell.setCellValue(fc);
    }

}
