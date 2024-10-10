package utils.databases;

import org.javatuples.Pair;

import java.sql.*;
import java.util.ArrayList;

import static utils.databases.SettingsTableNames.*;

public class SettingsDatabaseConnection extends DatabaseConnection{
	@Override
	protected void createTable(Connection connection) {
		createModularTable(connection, GENERAL);
		createModularTable(connection, BAJADA_PLANCHA);
		createModularTable(connection, TELAS);
		createModularTable(connection, CORTE);
		createModularTable(connection, PRENDAS);
		createModularTable(connection, SERVICIOS);
		createModularTable(connection, IMPRESIONES);
		createModularTable(connection, VINILOS);
		createModularTable(connection, LONAS);
		createModularTable(connection, GANANCIAS);
	}

	private void createModularTable(Connection connection, SettingsTableNames tableName) {
		String generalSQL = "CREATE TABLE IF NOT EXISTS " + tableName.getName() + " (" +
				"Campo TEXT PRIMARY KEY NOT NULL," +
				"Valor DOUBLE NOT NULL" +
				")";
		try (Statement stmt = connection.createStatement()) {
			stmt.setQueryTimeout(QUERY_TIMEOUT);
			stmt.execute(generalSQL);
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
	}

	public ArrayList<Pair<String, Double>> getTableAsList(SettingsTableNames tableName) {
		String sql = "SELECT * FROM " + tableName.getName();

		try (Connection conn = connect();
			Statement stmt = conn.createStatement()) {
			stmt.setQueryTimeout(QUERY_TIMEOUT);
			ResultSet resultSet = stmt.executeQuery(sql);

			ArrayList<Pair<String, Double>> values = new ArrayList<>();
			while (resultSet.next()) {
				Pair<String, Double> pair = new Pair<>(resultSet.getString("Campo"), resultSet.getDouble("Valor"));
				values.add(pair);
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

	public void insertOrUpdate(SettingsTableNames tableName, String field, double value){
		String sql = "INSERT OR REPLACE INTO " + tableName.getName() + " (Campo, Valor) VALUES ('" + field + "', " + value + ")";
		try (Connection conn = connect();
			 Statement stmt = conn.createStatement()) {
			stmt.setQueryTimeout(QUERY_TIMEOUT);
			stmt.executeUpdate(sql);
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
	}

	public void insertOrUpdateBatch(SettingsTableNames tableName, ArrayList<Pair<String, Double>> rows) throws SQLException {
		String sql = "INSERT OR REPLACE INTO " + tableName.getName() + " (Campo, Valor) VALUES (?, ?)";

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
}