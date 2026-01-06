package utils.databases;

import org.javatuples.Pair;

import java.sql.*;
import java.util.ArrayList;

public class WorkBudgetsDatabaseConnection extends DatabaseConnection{
	@Override
	protected void createTable(Connection connection) {
		String workBudgetSQL =  "CREATE TABLE IF NOT EXISTS Presupuestos_Trabajo (" +
				"ID INTEGER PRIMARY KEY AUTOINCREMENT," +
				"ID_Cliente TEXT NOT NULL," +
				"Fecha TEXT NOT NULL," +
				"Numero_presupuesto INTEGER NOT NULL," +
				"Desc_logistica TEXT NOT NULL," +
				"Precio_logistica TEXT NOT NULL," +
				"Colocador TEXT NOT NULL," +
				"Precio_colocacion TEXT NOT NULL," +
				"Ganancia TEXT NOT NULL," +
				"Precio_Total TEXT NOT NULL" +
				")";
		try (Statement stmt = connection.createStatement()) {
			stmt.setQueryTimeout(QUERY_TIMEOUT);
			stmt.execute(workBudgetSQL);
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
	}

	public int insertWorkBudget(String clientID, String date, String budgetNumber, String logistics, String logisticsPrice,
								 String placer, String placingCost, String profit, String total) throws SQLException {
		String sql = "INSERT INTO Presupuestos_Trabajo(ID_Cliente, Fecha, Numero_presupuesto, Desc_logistica, Precio_logistica," +
				"Colocador, Precio_colocacion, Ganancia, Precio_Total) " +
				"VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?)";
		Connection conn = connect();
		PreparedStatement pstmt = conn.prepareStatement(sql);

		pstmt.setString(1, clientID);
		pstmt.setString(2, date);
		pstmt.setString(3, budgetNumber);
		pstmt.setString(4, logistics);
		pstmt.setString(5, logisticsPrice);
		pstmt.setString(6, placer);
		pstmt.setString(7, placingCost);
		pstmt.setString(8, profit);
		pstmt.setString(9, total);
		//i want to get the created budget ID after the insert. do this, copilot
		ResultSet rs = pstmt.executeQuery("SELECT last_insert_rowid() AS ID");

		pstmt.executeUpdate();

		pstmt.close();
		conn.close();

		return rs.getInt("ID");
	}

	public void insertMaterials(ArrayList<Pair<String, String>> materialsList, int budgetID) throws SQLException {
		String sql = "INSERT INTO PRESUPUESTO_MATERIAL(ID_PRESUPUESTO, NOMBRE_MATERIAL, PRECIO_MATERIAL) " +
				"VALUES(?, ?, ?)";
		Connection conn = connect();
		PreparedStatement pstmt = conn.prepareStatement(sql);

		for (Pair<String, String> material : materialsList) {
			pstmt.setInt(1, budgetID);
			pstmt.setString(2, material.getValue0());
			pstmt.setString(3, material.getValue1());
			pstmt.executeUpdate();
		}

		pstmt.close();
		conn.close();

	}

	public int getNextBudgetNumber() {
		int bnumber = 1;
		String sql = "SELECT MAX(Numero_presupuesto) FROM Presupuestos_Trabajo";

		try (Connection conn = connect();
			 Statement stmt = conn.createStatement();
			 ResultSet resultSet = stmt.executeQuery(sql)) {
			if (resultSet.next()) {
				bnumber = resultSet.getInt(1) + 1;
			}
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}

		return bnumber;
	}
}
