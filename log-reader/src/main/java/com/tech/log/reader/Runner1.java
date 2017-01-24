package com.tech.log.reader;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;

import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class Runner1 {
	public static void main(String[] args) throws Exception {
//		args = new String[2];
//		args[0] = "D:\\_00_complete_projects\\cmes-problem2016\\cpbesbLog3";
//		args[1] = "c:/output/";

		System.out.println("Input:" + args[0]);
		System.out.println("Output:" + args[1]);

		File inputPath = new File(args[0]);
		File[] inputFiles = inputPath.listFiles();
		for (File inputFile : inputFiles) {
			System.out.println("Reading InputFile:" + inputFile.getName());
			boolean fail = true;
			try {
				LogReader logReader = new LogReader();
				String xlsFileStr = "src/resources/template.xlsx";
				XSSFWorkbook workBook = new XSSFWorkbook(new FileInputStream(
						xlsFileStr));
				logReader.setWorkBook(workBook);

				BufferedReader br = new BufferedReader(
						new FileReader(inputFile));

				LogFileAnalyzer logFileAnalyzer = new LogFileAnalyzer();
				logFileAnalyzer.analysis(logReader, br);

				workBook.write(new FileOutputStream(args[1]
						+ inputFile.getName() + ".xlsx"));
				
				fail = false;
			} catch (Exception ex) {
				System.err.println(ex.getClass().getName());
				ex.printStackTrace(System.err);
				throw ex;
			}
			if(!fail){
				System.out.println("Write OutputFile:" + args[1]
						+ inputFile.getName() + ".xlsx");
			}else{
				System.err.println("Error");
			}
			
		}
	}
}
