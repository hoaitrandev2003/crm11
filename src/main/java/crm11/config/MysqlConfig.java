package crm11.config;
import java.sql.Connection;
import java.sql.DriverManager;

public class MysqlConfig {
	
	public static Connection getConnection() {
		
		try {
			String url = "jdbc:mysql://localhost:3307/crm_app";
			String username = "root";
			String password = "admin123";
			
			
			Class.forName("com.mysql.cj.jdbc.Driver");
			return DriverManager.getConnection(url,username,password);
		} catch (Exception e){
			System.out.println("Error connect database" + e.getMessage());
		}
		
		return null;
	}
}
