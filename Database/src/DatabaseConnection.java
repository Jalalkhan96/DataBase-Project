import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

public class DatabaseConnection {

    private static final String URL = "jdbc:sqlserver://localhost:1433;databaseName=Police_Station_Management;encrypt=true;trustServerCertificate=true;";
    private static final String USER = "jalal"; 
    private static final String PASSWORD = "111";
    public static Connection connect() throws SQLException {
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver"); 

            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (SQLException e) {
            System.err.println("Error connecting to the database: " + e.getMessage()); 
            throw e; 
        } catch (ClassNotFoundException e) {
            System.err.println("SQL Server JDBC driver not found: " + e.getMessage());
            throw new SQLException("Database driver not found", e);
        }
    }
}