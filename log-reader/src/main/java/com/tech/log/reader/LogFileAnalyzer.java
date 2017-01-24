package com.tech.log.reader;

import java.io.BufferedReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class LogFileAnalyzer {

	static int threadIdRoundRunning;

	class ReadModel {

		SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
		SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
		SimpleDateFormat dateTimeFormat = new SimpleDateFormat(
				"dd-MM-yyyy HH:mm:ss");

		String dateStr;
		String timeStr;
		String fileThreadNo;
		String logMessage;
		String fullLine;
		int threadIdRound;

		public ReadModel() {
			threadIdRoundRunning++;
			threadIdRound = threadIdRoundRunning;
		}

		@Override
		public boolean equals(Object obj) {
			if (!(obj instanceof ReadModel)) {
				return false;
			}
			ReadModel rm = (ReadModel) obj;
			return fileThreadNo.equals(rm.fileThreadNo);
		}

		public Date getLogDateTime() throws ParseException {
			return dateTimeFormat.parse(dateStr + " " + timeStr);
		}

		@Override
		public String toString() {
			return fileThreadNo;
		}
	}

	Connection conn;
	StringBuilder insertSql;

	public LogFileAnalyzer() throws Exception {
//		Class.forName("oracle.jdbc.driver.OracleDriver");
//		conn = DriverManager.getConnection(
//				"jdbc:oracle:thin:@localhost:1521:XE", "FC", "password");

		insertSql = new StringBuilder();
		insertSql.append(" INSERT INTO CMES_APP_LOG ( ");
		insertSql.append("    LOG_ID, LOG_TIME, THREAD_ID,  ");
		insertSql.append("    LOG_LINE, THREAD_ID_ROUND, APPSERV_INSANCE)  ");
		insertSql.append(" VALUES ( CMES_APP_LOG_SEQ.NEXTVAL, ");
		insertSql.append("  ?, ");
		insertSql.append("  ?, ");
		insertSql.append("  ?, ");
		insertSql.append("  ?, ");
		insertSql.append("  ? ) ");
	}

	private ReadModel lineSpliter(String line) {
		String lines[] = line.split(" ");
//		System.out.println(lines.length);
		if(lines.length == 0){
			return null;
		}
		ReadModel rm = new ReadModel();
		try {
			rm.dateFormat.parse(lines[0]);
		} catch (ParseException e) {
//			e.printStackTrace(System.err);
			return null;
		}
		try{
		rm.dateStr = lines[0];
		rm.timeStr = lines[1];
		rm.fileThreadNo = lines[2];
		rm.logMessage = line.replace(lines[0], "").replace(lines[1], "")
				.replace(lines[2], "").trim();
		rm.fullLine = line;
		}catch(Exception ex){
			ex.printStackTrace(System.err);
		}
		return rm;
	}

	private List<ReadModel> createBookThreads;
	private List<ReadModel> createDmSessionThreads;

	private int excelLine = 1;

	public void analysis(LogReader logReader, BufferedReader br)
			throws Exception {

		createBookThreads = new ArrayList<>();
		createDmSessionThreads = new ArrayList<>();

		int lineNumber = 0;

		String line;
		while ((line = br.readLine()) != null) {
			lineNumber++;
//			if(lineNumber < 351459){
//				continue;
//			}
//			System.out.println(lineNumber);
			// System.out.println(String.format("%.2f",
			// (lineNumber / 568996f) * 100f));
			ReadModel rm = lineSpliter(line);
			if (rm == null) {
				continue;
			}
			if (line.contains("begin controller http://cmes.crownproperty.or.th/cpbesb/general/create_book.htm")) {
				if (createBookThreads.contains(rm)) {
					System.out.println("duplicateThread@Line:" + lineNumber);
					ReadModel temp = createBookThreads.get(0);
					System.out.println(temp.dateStr+" "+temp.timeStr);
					throw new Exception("No handle fail defined");
				}
				createBookThreads.add(rm);
			} else if (line
					.contains("end controller http://cmes.crownproperty.or.th/cpbesb/general/create_book.htm")) {

				if(createBookThreads
						.indexOf(rm) == -1){
					
				}else{
					ReadModel temp = createBookThreads.get(createBookThreads
							.indexOf(rm));

					logReader.write(excelLine, 0, temp.threadIdRound + "");
					logReader
							.write(excelLine, 1, temp.dateStr + " " + temp.timeStr);
					logReader.write(excelLine, 2, rm.dateStr + " " + rm.timeStr);
					logReader.write(excelLine, 3,
							(rm.getLogDateTime().getTime() - temp.getLogDateTime()
									.getTime()) / 1000);
					
					createBookThreads.remove(rm);
				}
				
				excelLine++;

			}
			if (!createBookThreads.contains(rm)) {
				// do nothing
			} else {
				if (line.contains("begin sql(x) txn [getFolderPathToCreateObjectQueryAndUpdate]")) {
					createDmSessionThreads.add(rm);
				} else if (line
						.contains("dql session created [getFolderPathToCreateObjectQueryAndUpdate]")) {

					ReadModel temp = createBookThreads.get(createBookThreads
							.indexOf(rm));
					logReader.write(excelLine, 4,
							(rm.getLogDateTime().getTime() - temp
									.getLogDateTime().getTime()) / 1000);

					createDmSessionThreads.remove(rm);
				} else if(line
						.contains("]null")){
					/**
					 * Handle fail for ]null
					 */
					System.out.println("ln:" + lineNumber);
					ReadModel temp = createBookThreads.get(createBookThreads
							.indexOf(rm));

					logReader.write(excelLine, 0, temp.threadIdRound + "");
					logReader
							.write(excelLine, 1, temp.dateStr + " " + temp.timeStr);
					logReader.write(excelLine, 2, rm.dateStr + " " + rm.timeStr);
					logReader.write(excelLine, 3, -1);
					logReader.write(excelLine, 4, -1);
					excelLine++;

					createBookThreads.remove(rm);
					createDmSessionThreads.remove(rm);
				}
			}
		}

		br.close();
	}
}
