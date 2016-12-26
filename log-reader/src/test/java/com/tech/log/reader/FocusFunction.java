package com.tech.log.reader;

import java.io.BufferedReader;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/*
 * To change this template, choose Tools | Templates and open the template in
 * the editor.
 */
/**
 *
 * @author admin
 */
public class FocusFunction {

	public static class ThreadId {

		String threadIdInLog;
		int threadIdBySeq;
	}

	private static final int indexOf(List<ThreadId> tIds, String threadInLog) {
		int res = -1;
		for (int i = 0; i < tIds.size(); i++) {
			if (!tIds.get(i).threadIdInLog.equals(threadInLog)) {
				continue;
			}
			res = i;
			break;
		}
		return res;
	}

	private static final String BEGIN_CREATE_BOOK_URL = "begin controller http://cmes.crownproperty.or.th/cpbesb/general/create_book.htm";
	// begin sql(x) txn [getFolderPathToCreateObjectQueryAndUpdate]
	// dql session created [getFolderPathToCreateObjectQueryAndUpdate]
	// sql connection created
	// [net.techsphere.cpb.service.CPBDocumentXqlService.getDocNameQuery]
	// dql session created [getDocNameQuery]
	private static final String END_CREATE_BOOK_URL = "end controller http://cmes.crownproperty.or.th/cpbesb/general/create_book.htm";
	private static final SimpleDateFormat dateFormat1 = new SimpleDateFormat(
			"yyyy-MM-dd HH:mm:ss");
	private static final SimpleDateFormat dateFormat2 = new SimpleDateFormat(
			"yyyy-MM-dd");
	private static final SimpleDateFormat dateFormat3 = new SimpleDateFormat(
			"dd-MM-yyyy HH:mm:ss");
	private static final SimpleDateFormat dateFormat4 = new SimpleDateFormat(
			"dd-MM-yyyy");

	public static void main(String[] args) throws Exception {

		Class.forName("oracle.jdbc.driver.OracleDriver");
		Connection conn = DriverManager.getConnection(
				"jdbc:oracle:thin:@localhost:1521:XE", "FC", "password");

		StringBuilder sql = new StringBuilder();
		sql.append(" INSERT INTO CMES_APP_LOG ( ");
		sql.append("    LOG_ID, LOG_TIME, THREAD_ID,  ");
		sql.append("    LOG_LINE, THREAD_ID_ROUND, APPSERV_INSANCE)  ");
		sql.append(" VALUES ( CMES_APP_LOG_SEQ.NEXTVAL, ");
		sql.append("  ?, ");
		sql.append("  ?, ");
		sql.append("  ?, ");
		sql.append("  ?, ");
		sql.append("  ? ) ");

		// Calendar calendar = GregorianCalendar.getInstance();
		//
		// Date startTime = dateFormat1.parse("2016-11-01 00:00:00");
		// calendar.setTime(startTime);
		// Object preriodInDay = new Object[24][2];
		// for (int i = 0; i < 24; i++) {
		// if(i > 0){
		// calendar.add(Calendar.SECOND, -1);
		// }
		// calendar.add(Calendar.HOUR, 1);
		// Date endTime = calendar.getTime();
		// System.out.println(dateFormat1.format(startTime) + " - " +
		// dateFormat1.format(endTime));
		// calendar.setTime(endTime);
		// calendar.add(Calendar.SECOND, 1);
		// startTime = calendar.getTime();
		// }
		//
		//
		// Date endTime = dateFormat1.parse("2016-11-01 23:59:59");

		String instanceStr = "01";
		BufferedReader br = new BufferedReader(
				new FileReader(
						/*"D:\\_00_complete_projects\\cmes-problem2016\\cpbesb.log2016-11-01"*/
						"D:\\_00_complete_projects\\cmes-problem2016\\cpbesb.log2014-12-30"));
		String line = null;
		List<ThreadId> threads = new ArrayList();

		int running = 0;
		while ((line = br.readLine()) != null) {
			//System.out.println("line:" + line);
			String[] parts = line.split(" ");
			try {
				dateFormat4.parse(parts[0]);
			} catch (Exception ex) {
				continue;
			}
			if (line.contains(BEGIN_CREATE_BOOK_URL)) {
				System.out.println("parts[2]:begin" + parts[2]);

				ResultSet rs = conn.prepareStatement(
						"SELECT CMES_APP_LOG_THREAD_NO_SEQ.NEXTVAL FROM DUAL")
						.executeQuery();
				rs.next();
				running = rs.getInt(1);
				System.out.println("running:" + running);
				rs.close();

				ThreadId ti = new ThreadId();
				ti.threadIdBySeq = running;
				ti.threadIdInLog = parts[2];
				threads.add(ti);

				Date logTime = dateFormat3.parse(parts[0] + " " + parts[1]);
				PreparedStatement pp = conn.prepareStatement(sql.toString());
				pp.setTimestamp(1, new java.sql.Timestamp(logTime.getTime()));
				pp.setString(2, ti.threadIdInLog);
				pp.setString(3, line);
				pp.setInt(4, ti.threadIdBySeq);
				pp.setString(5, instanceStr);
				pp.executeUpdate();
				pp.close();
			}
			// if (threads.contains(parts[2])) {
			// Date logTime = dateFormat3.parse(parts[0] + " " + parts[1]);
			// PreparedStatement pp = conn.prepareStatement(sql.toString());
			// pp.setTimestamp(1, new java.sql.Timestamp(logTime.getTime()));
			// pp.setString(2, parts[2]);
			// pp.setString(3, line);
			// pp.setInt(4, running);
			// pp.executeUpdate();
			// pp.close();
			// }
			if (line.contains(END_CREATE_BOOK_URL)/*
					|| line.contains("begin sql(x) txn [getFolderPathToCreateObjectQueryAndUpdate]")
					|| line.contains("dql session created [getFolderPathToCreateObjectQueryAndUpdate]")
					|| line.contains("sql connection created [net.techsphere.cpb.service.CPBDocumentXqlService.getDocNameQuery]")
					|| line.contains("dql session created [getDocNameQuery]")*/) {

				System.out.println("parts[2]:end" + parts[2]);
				if (indexOf(threads, parts[2]) == -1) {
				} else {
					ThreadId ti = threads.get(indexOf(threads, parts[2]));

					System.out.println("running:" + ti.threadIdBySeq);
					System.out.println("size:" + threads.size());

					Date logTime = dateFormat3.parse(parts[0] + " " + parts[1]);
					PreparedStatement pp = conn
							.prepareStatement(sql.toString());
					pp.setTimestamp(1,
							new java.sql.Timestamp(logTime.getTime()));
					pp.setString(2, ti.threadIdInLog);
					pp.setString(3, line);
					pp.setInt(4, ti.threadIdBySeq);
					pp.setString(5, instanceStr);
					pp.executeUpdate();
					pp.close();
				}
			}
			if (line.contains(END_CREATE_BOOK_URL)) {
				ThreadId ti = threads.get(indexOf(threads, parts[2]));
				threads.remove(ti);
			}
		}

		conn.close();
	}
}
