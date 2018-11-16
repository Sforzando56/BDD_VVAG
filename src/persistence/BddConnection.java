package persistence;
import metier.Categorie;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class BddConnection {
	private static Connection con;

	public static Connection getConnection() {
		if(con == null) {
			try {
				DriverManager.registerDriver(new oracle.jdbc.driver.OracleDriver());
				con = DriverManager.getConnection("jdbc:oracle:thin:@ensioracle1.imag.fr:1521:ensioracle1", "vandaevi", "vandaevi");
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return con;
	}
	
    public static void main(String[] args) {
    	BddConnection.getConnection();
    	try {
			Categorie.create("test4", "test description");
		} catch (SQLException e) {
			e.printStackTrace();
		}
    }

    public static void closeConnection(Connection dbConnection, Statement statement){

		try {
			if (statement != null) {
				statement.close();
			}

			if (dbConnection != null) {
				dbConnection.close();
			}
		}
		catch (Exception e){
			e.printStackTrace();
		}


	}
}
