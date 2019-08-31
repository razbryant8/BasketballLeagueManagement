package databaseConnection;

import java.sql.*;

public class OracleConnectionDB{
	private Connection con;
	private Statement st;
	private ResultSet rs;
	private CallableStatement callSt;
	private final String url = "localhost";
	private final String port = "1521";
	private final String serviceName = "orcl";
	
	
	public void connectToDB(String username, String password) throws Exception{
		Class.forName("oracle.jdbc.driver.OracleDriver");
		con = DriverManager.getConnection("jdbc:oracle:thin:@"+url+":"+port+":"+serviceName, username, password);
		
	}
	
	public void printTableByName(String tableName) throws Exception {
		st = con.createStatement();
		rs = st.executeQuery("select * from "+tableName);
		
		while(rs.next()) {
			System.out.println(rs.getInt(1)+" "+rs.getString(2)+" "+rs.getInt(3));
		}
		
	}
	
	public Connection getConnection() {return con;}
	public Statement getStatment() {return st;}
	public ResultSet getResultSet() {return rs;}
	public CallableStatement getCalStat() {return callSt;}
	
	public void setStatment(Statement s) {this.st = s;}
	public void setResultSet(ResultSet rs) {this.rs = rs;}
	public void setCallableStatement(CallableStatement callSt) {this.callSt = callSt;}
	
	
	public void closeConnection() throws Exception{
		rs.close();
		st.close();
		con.close();
	}
	
}
