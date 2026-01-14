package utils.databases;

import org.javatuples.Pair;
import utils.Budget;
import utils.WorkBudget;
import utils.WorkBudgetData;

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
                                String profit, String total) throws SQLException {
		String sql = "INSERT INTO Presupuestos_Trabajo(ID_Cliente, Fecha, Numero_presupuesto, Desc_logistica, Precio_logistica," +
				"Ganancia, Precio_Total) " +
				"VALUES(?, ?, ?, ?, ?, ?, ?)";

		Connection conn = connect();
		PreparedStatement pstmt = conn.prepareStatement(sql);
		String budgetNumber = Integer.toString(getNextBudgetNumber());
		pstmt.setString(1, clientID);
		pstmt.setString(2, date);
		pstmt.setString(3, budgetNumber);
		pstmt.setString(4, logistics);
		pstmt.setString(5, logisticsPrice);
		pstmt.setString(6, profit);
		pstmt.setString(7, total);


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

    public void insertPlacers(ArrayList<Pair<String, String>> placerList, int budgetID) throws SQLException {
        String sql = "INSERT INTO PRESUPUESTO_COLOCADOR(ID_PRESUPUESTO, NOMBRE, PRECIO) " +
                "VALUES(?, ?, ?)";
        Connection conn = connect();
        PreparedStatement pstmt = conn.prepareStatement(sql);

        for (Pair<String, String> palcer : placerList) {
            pstmt.setInt(1, budgetID);
            pstmt.setString(2, palcer.getValue0());
            pstmt.setString(3, palcer.getValue1());
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
        String sqlPlacers = "SELECT COUNT(*) AS count FROM PRESUPUESTO_COLOCADOR WHERE ID_PRESUPUESTO = ?";

        try (Connection conn = connect();
			 PreparedStatement pstmtMaterials = conn.prepareStatement(sqlMaterials);
			 PreparedStatement pstmtDescriptions = conn.prepareStatement(sqlDescriptions);
             PreparedStatement pstmtPlacers = conn.prepareStatement(sqlPlacers)){

			pstmtMaterials.setInt(1, id);
			ResultSet rsMaterials = pstmtMaterials.executeQuery();
			int materialsCount = rsMaterials.getInt("count");

			pstmtDescriptions.setInt(1, id);
			ResultSet rsDescriptions = pstmtDescriptions.executeQuery();
			int descriptionsCount = rsDescriptions.getInt("count");

            pstmtPlacers.setInt(1, id);
            ResultSet rsPlacers = pstmtPlacers.executeQuery();
            int placersCount = rsPlacers.getInt("count");

			return materialsCount == 0 || descriptionsCount == 0 || placersCount == 0;

		} catch (SQLException e) {
			System.out.println(e.getMessage());
			return true; // Assume failure if there's an exception
		}
	}


	public void deleteBudgetById(int id) {
		//copilot, delete every row in Presupuestos_Trabajo, PRESUPUESTO_MATERIAL and PRESUPUESTO_DESCRIPCION with the given id
		String sqlWorkBudget = "DELETE FROM Presupuestos_Trabajo WHERE Numero_presupuesto = ?";
		String sqlMaterials = "DELETE FROM PRESUPUESTO_MATERIAL WHERE ID_PRESUPUESTO = ?";
		String sqlDescriptions = "DELETE FROM PRESUPUESTO_DESCRIPCION WHERE ID_PRESUPUESTO = ?";
        String sqlPlacers = "DELETE FROM PRESUPUESTO_COLOCADOR WHERE ID_PRESUPUESTO = ?";
		try (Connection conn = connect();
			 PreparedStatement pstmtWorkBudget = conn.prepareStatement(sqlWorkBudget);
			 PreparedStatement pstmtMaterials = conn.prepareStatement(sqlMaterials);
			 PreparedStatement pstmtDescriptions = conn.prepareStatement(sqlDescriptions);
             PreparedStatement pstmtPlacers = conn.prepareStatement(sqlPlacers)){

			pstmtWorkBudget.setInt(1, id);
			pstmtWorkBudget.executeUpdate();

			pstmtMaterials.setInt(1, id);
			pstmtMaterials.executeUpdate();

			pstmtDescriptions.setInt(1, id);
			pstmtDescriptions.executeUpdate();

            pstmtPlacers.setInt(1, id);
            pstmtPlacers.executeUpdate();

		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
	}

	public ArrayList<WorkBudget> getBudgets(String budgetSearch) throws SQLException {
		ArrayList<WorkBudget> budgets = new ArrayList<>();

		String sql;
		boolean searchIsNumber = budgetSearch.matches("\\d+");

		if (searchIsNumber) {
			// Search by budget number
			sql = """
              SELECT PT.Fecha,
              		 PT.ID,
              		 PT.ID_Cliente,
                     PT.Precio_Total,
                     PT.Numero_presupuesto,
                     C.Nombre AS ClientName
              FROM Presupuestos_Trabajo PT
              JOIN Clientes C ON PT.ID_Cliente = C.ID
              WHERE PT.Numero_presupuesto = ?
              """;
		} else {
			// Search by client name
			sql = """
              SELECT PT.Fecha,
              	   	 PT.ID,
              	   	 PT.ID_Cliente,
                     PT.Precio_Total,
                     PT.Numero_presupuesto,
                     C.Nombre AS ClientName
              FROM Presupuestos_Trabajo PT
              JOIN Clientes C ON PT.ID_Cliente = C.ID
              WHERE C.Nombre LIKE ?
              """;
		}

		try (Connection conn = connect();
			 PreparedStatement pstmt = conn.prepareStatement(sql)) {

			if (searchIsNumber) {
				pstmt.setInt(1, Integer.parseInt(budgetSearch));
			} else {
				pstmt.setString(1, "%" + budgetSearch + "%");
			}

			try (ResultSet rs = pstmt.executeQuery()) {
				while (rs.next()) {
					WorkBudget budget = new WorkBudget(
							rs.getInt("ID"),
							rs.getInt("ID_Cliente"),
							rs.getString("ClientName"),
							rs.getString("Fecha"),
							rs.getString("Precio_Total"),
							rs.getInt("Numero_presupuesto")
					);
					budgets.add(budget);
				}
			}
		}
		return budgets;
	}

	public void updateWorkBudget(
			int budgetId,
			String clientID,
			String date,
			String logistics,
			String logisticsPrice,
			String profit,
			String total,
			ArrayList<Pair<String, String>> materials,
			ArrayList<Pair<String, String>> descriptions,
            ArrayList<Pair<String, String>> placers
	) throws SQLException {

		String sqlUpdate =
				"UPDATE Presupuestos_Trabajo SET " +
						"ID_Cliente = ?, Fecha = ?, Desc_logistica = ?, Precio_logistica = ?, " +
						"Ganancia = ?, Precio_Total = ? " + "WHERE Numero_presupuesto = ?";

		String sqlDeleteMaterials =
				"DELETE FROM PRESUPUESTO_MATERIAL WHERE ID_PRESUPUESTO = ?";

		String sqlDeleteDescriptions =
				"DELETE FROM PRESUPUESTO_DESCRIPCION WHERE ID_PRESUPUESTO = ?";

        String sqlDeletePlacers =
                "DELETE FROM PRESUPUESTO_COLOCADOR WHERE ID_PRESUPUESTO = ?";

		String sqlInsertMaterial =
				"INSERT INTO PRESUPUESTO_MATERIAL(ID_PRESUPUESTO, NOMBRE_MATERIAL, PRECIO_MATERIAL) VALUES(?, ?, ?)";

		String sqlInsertDescription =
				"INSERT INTO PRESUPUESTO_DESCRIPCION(ID_PRESUPUESTO, DESCRIPCION_MATERIAL, PRECIO) VALUES(?, ?, ?)";

        String sqlInsertPlacer =
                "INSERT INTO PRESUPUESTO_COLOCADOR(ID_PRESUPUESTO, NOMBRE, PRECIO) VALUES(?, ?, ?)";

		Connection conn = connect();
		conn.setAutoCommit(false); // ---- BEGIN TRANSACTION ----

		try (
				PreparedStatement pstmtUpdate = conn.prepareStatement(sqlUpdate);
				PreparedStatement pstmtDeleteMat = conn.prepareStatement(sqlDeleteMaterials);
				PreparedStatement pstmtDeleteDesc = conn.prepareStatement(sqlDeleteDescriptions);
                PreparedStatement pstmtDeletePlac = conn.prepareStatement(sqlDeletePlacers);
				PreparedStatement pstmtInsertMat = conn.prepareStatement(sqlInsertMaterial);
				PreparedStatement pstmtInsertDesc = conn.prepareStatement(sqlInsertDescription);
                PreparedStatement pstmtInsertPlac = conn.prepareStatement(sqlInsertPlacer);
		) {

			// --- Update main budget ---
			pstmtUpdate.setString(1, clientID);
			pstmtUpdate.setString(2, date);
			pstmtUpdate.setString(3, logistics);
			pstmtUpdate.setString(4, logisticsPrice);
			pstmtUpdate.setString(5, profit);
			pstmtUpdate.setString(6, total);
			pstmtUpdate.setInt(7, budgetId);
			pstmtUpdate.executeUpdate();

			// --- Clear previous materials/descriptions ---
			pstmtDeleteMat.setInt(1, budgetId);
			pstmtDeleteMat.executeUpdate();

			pstmtDeleteDesc.setInt(1, budgetId);
			pstmtDeleteDesc.executeUpdate();

            pstmtDeletePlac.setInt(1, budgetId);
            pstmtDeletePlac.executeUpdate();

			// --- Reinsert materials ---
			for (Pair<String, String> material : materials) {
				pstmtInsertMat.setInt(1, budgetId);
				pstmtInsertMat.setString(2, material.getValue0());
				pstmtInsertMat.setString(3, material.getValue1());
				pstmtInsertMat.executeUpdate();
			}

			// --- Reinsert descriptions ---
			for (Pair<String, String> desc : descriptions) {
				pstmtInsertDesc.setInt(1, budgetId);
				pstmtInsertDesc.setString(2, desc.getValue0());
				pstmtInsertDesc.setString(3, desc.getValue1());
				pstmtInsertDesc.executeUpdate();
			}

            // --- Reinsert placers ---
            for (Pair<String, String> plac : placers) {
                pstmtInsertPlac.setInt(1, budgetId);
                pstmtInsertPlac.setString(2, plac.getValue0());
                pstmtInsertPlac.setString(3, plac.getValue1());
                pstmtInsertPlac.executeUpdate();
            }

			conn.commit(); // ---- SUCCESS ----
		} catch (SQLException e) {
			conn.rollback(); // ---- FAILURE → revert everything ----
			throw e;
		} finally {
			conn.setAutoCommit(true);
			conn.close();
		}
	}

	public WorkBudgetData getWorkBudgetData(int budgetId) throws SQLException {

		String sqlMain = """
        SELECT Numero_presupuesto,
        	   ID_Cliente,
               Desc_logistica,
               Precio_logistica,
               Colocador,
               Precio_colocacion,
               Ganancia
        FROM Presupuestos_Trabajo
        WHERE Numero_presupuesto = ?
    """;

		String sqlMaterials = """
        SELECT NOMBRE_MATERIAL, PRECIO_MATERIAL
        FROM PRESUPUESTO_MATERIAL
        WHERE ID_PRESUPUESTO = ?
    """;

		String sqlDescriptions = """
        SELECT DESCRIPCION_MATERIAL, PRECIO
        FROM PRESUPUESTO_DESCRIPCION
        WHERE ID_PRESUPUESTO = ?
    """;

		try (Connection conn = connect();
			 PreparedStatement pstmtMain = conn.prepareStatement(sqlMain);
			 PreparedStatement pstmtMat = conn.prepareStatement(sqlMaterials);
			 PreparedStatement pstmtDesc = conn.prepareStatement(sqlDescriptions)) {

			// ---- Main Budget Row ----
			pstmtMain.setInt(1, budgetId);
			ResultSet rsMain = pstmtMain.executeQuery();

			if (!rsMain.next()) {
				return null; // or throw if you prefer strict behavior
			}

			int budgetNumber = rsMain.getInt("Numero_presupuesto");
			int clientID = rsMain.getInt("ID_Cliente");
			String logistics = rsMain.getString("Desc_logistica");
			String logisticsCost = rsMain.getString("Precio_logistica");
			String placer = rsMain.getString("Colocador");
			String placingCost = rsMain.getString("Precio_colocacion");
			String profit = rsMain.getString("Ganancia");

			// ---- Materials ----
			ArrayList<Pair<String, String>> materials = new ArrayList<>();
			pstmtMat.setInt(1, budgetId);
			ResultSet rsMat = pstmtMat.executeQuery();

			while (rsMat.next()) {
				materials.add(new Pair<>(
						rsMat.getString("NOMBRE_MATERIAL"),
						rsMat.getString("PRECIO_MATERIAL")
				));
			}

			// ---- Descriptions ----
			ArrayList<Pair<String, String>> descriptions = new ArrayList<>();
			pstmtDesc.setInt(1, budgetId);
			ResultSet rsDesc = pstmtDesc.executeQuery();

			while (rsDesc.next()) {
				descriptions.add(new Pair<>(
						rsDesc.getString("DESCRIPCION_MATERIAL"),
						rsDesc.getString("PRECIO")
				));
			}

			return new WorkBudgetData(
					budgetId,
					budgetNumber,
					clientID,
					materials,
					descriptions,
					logistics,
					logisticsCost,
					placer,
					placingCost,
					profit
			);
		}
	}


}
