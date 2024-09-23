package utils.databases;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class SettingsDatabaseConnection extends DatabaseConnection{
	@Override
	protected void createTable(Connection connection) {
		String settingsSQL = "CREATE TABLE IF NOT EXISTS Settings (" +
				"ID INTEGER PRIMARY KEY AUTOINCREMENT," +
				"Dolar TEXT NOT NULL," +
				"Bajada_Plancha TEXT NOT NULL," +
				"Gorra TEXT NOT NULL," +
				"Taza TEXT NOT NULL," +
				"Tinta TEXT NOT NULL," +
				"Costurera TEXT NOT NULL" +
				")";
		try (Statement stmt = connection.createStatement()) {
			stmt.setQueryTimeout(QUERY_TIMEOUT);
			stmt.execute(settingsSQL);
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
	}


	public void updateTable(String dollarValue, String plankLoweringValue, String capValue, String cupValue, String inkValue, String seamstressValue) {
		String sql = "UPDATE Settings SET Dolar = '" + dollarValue + "', Bajada_Plancha = '" + plankLoweringValue +
				"', Gorra = '" + capValue + "', Taza = '" + cupValue + "', Tinta = '" + inkValue + "', Costurera = '" +
				seamstressValue + "'";
		try (Connection conn = connect();
			 Statement stmt = conn.createStatement()) {
			stmt.setQueryTimeout(QUERY_TIMEOUT);
			stmt.executeUpdate(sql);
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
	}
}