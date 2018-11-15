package metier;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

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
}
