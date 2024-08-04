package utils.databases;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.SQLException;

public abstract class DatabaseConnection {

	protected static final String URL = "jdbc:sqlite:./PRUEBAS.db";
	protected static final int QUERY_TIMEOUT = 30;
	public Connection connect() throws SQLException {
		return DriverManager.getConnection(URL);
	}
	public void loadDatabase() {
		try (Connection connection = connect()) {
			if (connection != null) {
				DatabaseMetaData meta = connection.getMetaData();
				System.out.println("The driver name is " + meta.getDriverName());

				createTable(connection);
			}
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
	}

	public void deleteTable(String tableName) {
		try (Connection connection = connect()) {
			if (connection != null) {
				DatabaseMetaData meta = connection.getMetaData();
				System.out.println("The driver name is " + meta.getDriverName());

				String sql = "DROP TABLE IF EXISTS " + tableName;
				connection.createStatement().executeUpdate(sql);

			}
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
	}

	protected abstract void createTable(Connection connection);

}
