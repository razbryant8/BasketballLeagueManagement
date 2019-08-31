package main;

import databaseConnection.OracleConnectionDB;
import gui.LoginFrame;
import oracle.jdbc.driver.OracleConnection;

public class StartProject {
	public static void main(String [] args) throws Exception {
		OracleConnectionDB orcdb = new OracleConnectionDB();
		new LoginFrame("Login Page",orcdb);
	}
}
