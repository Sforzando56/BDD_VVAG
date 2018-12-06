package persistence;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import metier.Utilisateur;

public class BddConnection {
	private static Connection con;

	static Connection getConnection() {
		if(con == null) {
			try {
				DriverManager.registerDriver(new oracle.jdbc.driver.OracleDriver());
				con = DriverManager.getConnection("jdbc:oracle:thin:@ensioracle1.imag.fr:1521:ensioracle1", "donnea", "donnea");
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return con;
	}

	static void setAutoCommitConnexion(boolean autoCommitConnexioncommit){
		try {
			getConnection().setAutoCommit(autoCommitConnexioncommit);
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
	}
}
