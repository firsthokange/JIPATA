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

    public String read(int rowNum, int colNum) {
        String result = null;
        try {
            XSSFWorkbook myWorkBook = new XSSFWorkbook(
                    new FileInputStream("src/resources/Book1.xlsx"));
            XSSFSheet mySheet = myWorkBook.getSheetAt(0);
            XSSFRow row = mySheet.getRow(rowNum - 1);
            XSSFCell cell = row.getCell(colNum - 1);
            result = cell.getStringCellValue();
        } catch (Exception ioe) {
            ioe.printStackTrace(System.err);
        }
        return result;
    }

}
