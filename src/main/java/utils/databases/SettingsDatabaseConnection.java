package utils.databases;

import org.javatuples.Pair;
import utils.Product;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class SettingsDatabaseConnection extends DatabaseConnection{
	@Override
	protected void createTable(Connection connection) {
		String settingsSQL = "CREATE TABLE IF NOT EXISTS Configuraciones (" +
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
		String sql = "UPDATE Configuraciones SET Dolar = '" + dollarValue + "', Bajada_Plancha = '" + plankLoweringValue +
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

	public ArrayList<Pair<String, String>> getTableContent(){
		String sql = "SELECT * FROM Configuraciones";
		try (Connection conn = connect();
			Statement stmt = conn.createStatement()) {
			stmt.setQueryTimeout(QUERY_TIMEOUT);
			ResultSet resultSet = stmt.executeQuery(sql);

			ArrayList<Pair<String, String>> prices = new ArrayList<>();
			Pair<String, String> pair = new Pair<>("Dollar", resultSet.getString("Dolar"));
			prices.add(pair);
			pair = new Pair<>("PlankLowering", resultSet.getString("Bajada_Plancha"));
			prices.add(pair);
			pair = new Pair<>("Cap", resultSet.getString("Gorra"));
			prices.add(pair);
			pair = new Pair<>("Cup", resultSet.getString("Taza"));
			prices.add(pair);
			pair = new Pair<>("Ink", resultSet.getString("Tinta"));
			prices.add(pair);
			pair = new Pair<>("Seamstress", resultSet.getString("Costurera"));
			prices.add(pair);


			resultSet.close();
			stmt.close();
			conn.close();
			return prices;
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		return new ArrayList<>();
	}
}