package com.tech.log.reader;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;

import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class LogFileAnalyzerTest {

	LogReader logReader = new LogReader();
	String xlsFileStr = "src/resources/template.xlsx";
	XSSFWorkbook workBook;

	//@Before
	public void setup_reader() throws Exception {
		workBook = new XSSFWorkbook(new FileInputStream(xlsFileStr));
		logReader.setWorkBook(workBook);
	}

	//@After
	public void save() throws Exception {
		workBook.write(new FileOutputStream("c:/output.xlsx"));
	}

	// @Test
	public void test() throws Exception {
		BufferedReader br = new BufferedReader(
				new FileReader(
						"D:\\_00_complete_projects\\cmes-problem2016\\cpbesb.log2016-11-01"
				/* "D:\\_00_complete_projects\\cmes-problem2016\\cpbesb.log2014-12-30" */));

		LogFileAnalyzer logFileAnalyzer = new LogFileAnalyzer();
		logFileAnalyzer.analysis(logReader, br);
	}

	@Test
	public void test2() throws Exception {
		File inputPath = new File(
				"D:\\_00_complete_projects\\cmes-problem2016\\cpbesbLog3");
		File[] inputFiles = inputPath.listFiles();
		for (File inputFile : inputFiles) {
			System.out.println("inputFile:" + inputFile.getName());
			
			LogReader logReader = new LogReader();
			String xlsFileStr = "src/resources/template.xlsx";
			XSSFWorkbook workBook = new XSSFWorkbook(new FileInputStream(xlsFileStr));
			logReader.setWorkBook(workBook);
			
			BufferedReader br = new BufferedReader(
					new FileReader(inputFile));

			LogFileAnalyzer logFileAnalyzer = new LogFileAnalyzer();
			logFileAnalyzer.analysis(logReader, br);
			
			workBook.write(new FileOutputStream("c:/output/" + inputFile.getName() + ".xlsx"));
		}
	}

}
