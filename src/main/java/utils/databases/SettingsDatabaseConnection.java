package utils.databases;

import org.javatuples.Pair;
import org.javatuples.Triplet;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static utils.databases.SettingsTableNames.*;

public class SettingsDatabaseConnection extends DatabaseConnection{
	@Override
	protected void createTable(Connection connection) {
		createModularTable(connection, GENERAL);
		createModularTable(connection, TELAS);
		createModularTable(connection, SERVICIOS);
		createModularTable(connection, MATERIALES);
	}

	private void createModularTable(Connection connection, SettingsTableNames tableName) {
		String generalSQL;
		if(tableName == GENERAL){
			generalSQL = "CREATE TABLE IF NOT EXISTS " + tableName.getName() + " (" +
					"Nombre TEXT PRIMARY KEY NOT NULL," +
					"Valor DOUBLE NOT NULL" +
					")";
		}else {
			generalSQL = "CREATE TABLE IF NOT EXISTS " + tableName.getName() + " (" +
					"Nombre TEXT PRIMARY KEY NOT NULL" +
					")";
		}

		try (Statement stmt = connection.createStatement()) {
			stmt.setQueryTimeout(QUERY_TIMEOUT);
			stmt.execute(generalSQL);
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
	}

	public ArrayList<String> getOtherTablesAsList(SettingsTableNames tableName) {
		String sql = "SELECT Nombre FROM " + tableName.getName();

		try (Connection conn = connect();
			Statement stmt = conn.createStatement()) {
			stmt.setQueryTimeout(QUERY_TIMEOUT);
			ResultSet resultSet = stmt.executeQuery(sql);

			ArrayList<String> values = new ArrayList<>();
			while (resultSet.next()) {
				if(tableName != GENERAL) {
					values.add(resultSet.getString("Nombre"));
				}
			}

			resultSet.close();
			stmt.close();
			conn.close();
			return values;
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		return new ArrayList<>();
	}


	public ArrayList<Pair<String, Double>> getGeneralTableAsList(SettingsTableNames tableName) {
		String sql = "SELECT * FROM " + tableName.getName();

		try (Connection conn = connect();
			Statement stmt = conn.createStatement()) {
			stmt.setQueryTimeout(QUERY_TIMEOUT);
			ResultSet resultSet = stmt.executeQuery(sql);

			ArrayList<Pair<String, Double>> values = new ArrayList<>();
			while (resultSet.next()) {
				if(tableName == GENERAL) {
					Pair<String, Double> pair = new Pair<>(resultSet.getString("Nombre"), resultSet.getDouble("Valor"));
					values.add(pair);
				}
			}

			resultSet.close();
			stmt.close();
			conn.close();
			return values;
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		return new ArrayList<>();
	}


	public void insertOrUpdateBatch(SettingsTableNames tableName, ArrayList<Pair<String, Double>> rows) throws SQLException {
		String sql = "INSERT OR REPLACE INTO " + tableName.getName() + " (Nombre, Valor) VALUES (?, ?)";

		Connection conn = connect();
		PreparedStatement pstmt = conn.prepareStatement(sql);

		conn.setAutoCommit(false); // Disable auto-commit for batch execution

		pstmt.setQueryTimeout(QUERY_TIMEOUT);

		// Iterate through the list of rows and add each row to the batch
		for (Pair<String, Double> row : rows) {
			pstmt.setString(1, row.getValue0());
			pstmt.setDouble(2, row.getValue1());
			pstmt.addBatch();                   // Add to batch
		}

		pstmt.executeBatch(); // Execute the batch of SQL statements
		conn.commit();        // Commit the transaction
	}
	public void insertOrUpdateBatchNames(SettingsTableNames tableName, ArrayList<String> rows) throws SQLException {
		String sql = "INSERT OR REPLACE INTO " + tableName.getName() + " (Nombre) VALUES (?)";

		Connection conn = connect();
		PreparedStatement pstmt = conn.prepareStatement(sql);

		conn.setAutoCommit(false); // Disable auto-commit for batch execution

		pstmt.setQueryTimeout(QUERY_TIMEOUT);

		// Iterate through the list of rows and add each row to the batch
		for (String row : rows) {
			pstmt.setString(1, row);
			pstmt.addBatch();                   // Add to batch
		}

		pstmt.executeBatch(); // Execute the batch of SQL statements
		conn.commit();        // Commit the transaction
	}

	public void updateModularPrices(List<Triplet<String, String, Double>> modularPrices) throws SQLException {

		List<String> tableNames = new ArrayList<>();

		for(Triplet<String, String, Double> item : modularPrices){
			String tableName = item.getValue0();
			tableNames.add(tableName);
		}

		for(String tableName : tableNames){
			String sql = "INSERT OR REPLACE INTO " + tableName + " (Nombre, Valor) VALUES (?, ?)";
			Connection conn = connect();
			PreparedStatement pstmt = conn.prepareStatement(sql);
			conn.setAutoCommit(false); // Disable auto-commit for batch execution
			pstmt.setQueryTimeout(QUERY_TIMEOUT);
			for(Triplet<String, String, Double> item : modularPrices){
				if(item.getValue0().equals(tableName)){
					pstmt.setString(1, item.getValue1());
					pstmt.setDouble(2, item.getValue2());
					pstmt.addBatch();                   // Add to batch
				}
			}
			pstmt.executeBatch(); // Execute the batch of SQL statements
			conn.commit();        // Commit the transaction
		}
	}

	public String getModularValue(SettingsTableNames settingsTableNames, String field) {
		String sql = "SELECT Valor FROM " + settingsTableNames.getName() + " WHERE Nombre = '" + field + "'";
		try (Connection conn = connect();
			 Statement stmt = conn.createStatement()) {
			stmt.setQueryTimeout(QUERY_TIMEOUT);
			ResultSet resultSet = stmt.executeQuery(sql);

			String value = "";
			if (resultSet.next()) {
				value = resultSet.getString("Valor");
			}

			resultSet.close();
			stmt.close();
			conn.close();
			return value;
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		return "";
	}

	public void removeRow(SettingsTableNames tableName, String field) {
		String sql = "DELETE FROM " + tableName.getName() + " WHERE Nombre = '" + field + "'";
		try (Connection conn = connect();
			 Statement stmt = conn.createStatement()) {
			stmt.setQueryTimeout(QUERY_TIMEOUT);
			stmt.executeUpdate(sql);
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
	}
}