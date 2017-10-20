package Main;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class DBManager {
//	private String server;
//	private String infoDBName;
//	private String scoreDBName;
//	private String userName;
//	private String password;
//
//	DBManager(String server, String infoDBName, String scoreDBName, String userName, String password) {
//		this.server = server;
//		this.infoDBName = infoDBName;
//		this.scoreDBName = scoreDBName;
//		this.userName = userName;
//		this.password = password;
//	}
//
//	private Connection con = null;
//	private Statement st = null;
//	private ResultSet rs = null;
//
//	double[] getInfo(String table, int limit, int maxData) throws DBException {
//		int i = 0, j = 0;
//		Info info = new Info();
//		double[] data = new double[maxData];
//		double heightAverage = 0;
//		String time;
//		double last, first, max, min, last5m, first5m = 0, max5m = 0, min5m = Double.MAX_VALUE;
//		double lfDiff, flDiff, lfDiff5m, flDiff5m;
//		double base = 0;
//		try {
//			Class.forName("com.mysql.jdbc.Driver").newInstance();
//			con = DriverManager.getConnection("jdbc:mysql://" + server + "/" + infoDBName, userName, password);
//			st = con.createStatement();
//			String query = "SELECT * FROM " + table + " order by id desc limit " + limit;
//			rs = st.executeQuery(query);
//			rs.next();
//			System.out.println(rs.getString("time"));
//			base = rs.getDouble("first");
//			do {
//				data[j++] = (rs.getDouble("first") - base) / 1000;
//				data[j++] = (rs.getDouble("last") - base) / 1000;
//				data[j++] = (rs.getDouble("max") - base) / 1000;
//				data[j++] = (rs.getDouble("min") - base) / 1000;
//				data[j++] = rs.getDouble("volume") / 1000;
////				data[j++] = rs.getDouble("last") - rs.getDouble("first") >= 0? 1.0: 0;
//			} while (rs.next());
//			st.close();
//			con.close();
//			return data;
//		} catch (Exception e) {
//			e.printStackTrace();
//			throw new DBException();
//		}
//	}

}
class DBException extends Exception {

}