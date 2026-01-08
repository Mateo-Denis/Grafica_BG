package utils.databases;

import org.javatuples.Pair;
import utils.Budget;

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

	public int insertWorkBudget(String clientID, String date, String logistics, String logisticsPrice,
								 String placer, String placingCost, String profit, String total) throws SQLException {
		String sql = "INSERT INTO Presupuestos_Trabajo(ID_Cliente, Fecha, Numero_presupuesto, Desc_logistica, Precio_logistica," +
				"Colocador, Precio_colocacion, Ganancia, Precio_Total) " +
				"VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?)";

		Connection conn = connect();
		PreparedStatement pstmt = conn.prepareStatement(sql);
		String budgetNumber = Integer.toString(getNextBudgetNumber());
		pstmt.setString(1, clientID);
		pstmt.setString(2, date);
		pstmt.setString(3, budgetNumber);
		pstmt.setString(4, logistics);
		pstmt.setString(5, logisticsPrice);
		pstmt.setString(6, placer);
		pstmt.setString(7, placingCost);
		pstmt.setString(8, profit);
		pstmt.setString(9, total);


		pstmt.executeUpdate();
		pstmt.close();
		conn.close();

		return Integer.parseInt(budgetNumber);
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

	public void insertDescriptions(ArrayList<Pair<String, String>> descriptionsList, int budgetID) throws SQLException {
		String sql = "INSERT INTO PRESUPUESTO_DESCRIPCION(ID_PRESUPUESTO, DESCRIPCION_MATERIAL, PRECIO) " +
				"VALUES(?, ?, ?)";
		Connection conn = connect();
		PreparedStatement pstmt = conn.prepareStatement(sql);

		for (Pair<String, String> material : descriptionsList) {
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

	public boolean checkFailedInserts(int id){
		String sqlMaterials = "SELECT COUNT(*) AS count FROM PRESUPUESTO_MATERIAL WHERE ID_PRESUPUESTO = ?";
		String sqlDescriptions = "SELECT COUNT(*) AS count FROM PRESUPUESTO_DESCRIPCION WHERE ID_PRESUPUESTO = ?";

		try (Connection conn = connect();
			 PreparedStatement pstmtMaterials = conn.prepareStatement(sqlMaterials);
			 PreparedStatement pstmtDescriptions = conn.prepareStatement(sqlDescriptions)) {

			pstmtMaterials.setInt(1, id);
			ResultSet rsMaterials = pstmtMaterials.executeQuery();
			int materialsCount = rsMaterials.getInt("count");

			pstmtDescriptions.setInt(1, id);
			ResultSet rsDescriptions = pstmtDescriptions.executeQuery();
			int descriptionsCount = rsDescriptions.getInt("count");

			return materialsCount == 0 || descriptionsCount == 0;

		} catch (SQLException e) {
			System.out.println(e.getMessage());
			return true; // Assume failure if there's an exception
		}
	}


	public void deleteBudgetById(int id) {
		//copilot, delete every row in Presupuestos_Trabajo, PRESUPUESTO_MATERIAL and PRESUPUESTO_DESCRIPCION with the given id
		String sqlWorkBudget = "DELETE FROM Presupuestos_Trabajo WHERE ID = ?";
		String sqlMaterials = "DELETE FROM PRESUPUESTO_MATERIAL WHERE ID_PRESUPUESTO = ?";
		String sqlDescriptions = "DELETE FROM PRESUPUESTO_DESCRIPCION WHERE ID_PRESUPUESTO = ?";
		try (Connection conn = connect();
			 PreparedStatement pstmtWorkBudget = conn.prepareStatement(sqlWorkBudget);
			 PreparedStatement pstmtMaterials = conn.prepareStatement(sqlMaterials);
			 PreparedStatement pstmtDescriptions = conn.prepareStatement(sqlDescriptions)) {

			pstmtWorkBudget.setInt(1, id);
			pstmtWorkBudget.executeUpdate();

			pstmtMaterials.setInt(1, id);
			pstmtMaterials.executeUpdate();

			pstmtDescriptions.setInt(1, id);
			pstmtDescriptions.executeUpdate();

		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
	}

	public ArrayList<Budget> getBudgets(String budgetSearch) throws SQLException {
		String sql;
		Connection conn = connect();
		PreparedStatement pstmt;

		// Verifica si el budgetSearch es numérico
		if (budgetSearch.matches("\\d+")) {
			// Si es numérico, buscar por número de presupuesto
			sql = "SELECT * FROM Presupuestos WHERE Numero_presupuesto= ?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, Integer.parseInt(budgetSearch));
		} else {
			// Si no es numérico, buscar por nombre
			sql = "SELECT * FROM Presupuestos WHERE Nombre_Cliente LIKE ?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, "%" + budgetSearch + "%");
		}

		ResultSet resultSet = pstmt.executeQuery();
		ArrayList<Budget> budgets = new ArrayList<>();

		while (resultSet.next()) {
			Budget budget = new Budget(
					resultSet.getString("ID_Cliente"),
					resultSet.getString("Fecha"),
					resultSet.getString("Precio_Total"),
					resultSet.getInt("Numero_presupuesto")
			);
			budgets.add(budget);
		}

		pstmt.close();
		conn.close();
		return budgets;
	}
}
