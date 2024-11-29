package common;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class JdbcUtill {
		static {
			try {
				Class.forName("oracle.jdbc.driver.OracleDriver");
			} 
			catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
		}

		public static Connection connect() {
			try {
				Connection connection = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe", "icia", "1111");
				connection.setAutoCommit(false);
				return connection;
			} 
			catch (SQLException e) {
				e.printStackTrace();
				return null;
			}
		}
		
		// #1 싱글톤
		public static void close(ResultSet resultSet) {
			try {
				if (resultSet != null) {
					resultSet.close();
				}
			} 
			catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		// #1 싱글톤
		public static void close(PreparedStatement preparedStatement) {
			try {
				if (preparedStatement != null) {
					preparedStatement.close();
				}
			} 
			catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		// #1 싱글톤
		public static void close(Connection connection) {
			try {
				if (connection != null) {
					connection.close();
				}
			} 
			catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		public static void commit(Connection connection) {
			try {
				connection.commit();
			}
			catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		public static void rollback(Connection connection) {
			try {
				connection.rollback();
			}
			catch (SQLException e) {
				e.printStackTrace();
			}
		}
}