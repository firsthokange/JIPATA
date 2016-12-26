/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tech.log.reader;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.After;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Before;

/**
 *
 * @author admin
 */
public class LogReaderTest {

    public LogReaderTest() {
    }

    LogReader logReader = new LogReader();
    String xlsFileStr = "src/resources/Book1.xlsx";
    XSSFWorkbook workBook;

    @Before
    public void setup_reader() throws Exception {
        workBook = new XSSFWorkbook(
                new FileInputStream(xlsFileStr));
        logReader.setWorkBook(workBook);
    }
    
    @After
    public void save() throws Exception{
        workBook.write(new FileOutputStream(xlsFileStr));
    }

    @Test
    public void read_row0col0_should_return_FC() {
        assertEquals("FC", logReader.read(0, 0));
    }

    @Test
    public void writeFC_row1col1_read_row1col1_should_returnFC() throws Exception {
        logReader.write(1, 1, "FC");
        assertEquals("FC", logReader.read(1, 1));
    }
    
    
}
