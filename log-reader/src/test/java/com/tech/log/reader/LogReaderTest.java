/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tech.log.reader;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author admin
 */
public class LogReaderTest {
    
    public LogReaderTest() {
    }
    
    @Test
    public void row1col1_should_return_FC(){
        LogReader logReader = new LogReader();
        assertEquals("FC", logReader.read(1,1));
    }
}
