package utils.databases;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class WorkBudgetMaterialsDatabaseConnection extends DatabaseConnection{
	@Override
	protected void createTable(Connection connection) {
		String budgetMaterialsSQL = "CREATE TABLE IF NOT EXISTS PRESUPUESTO_MATERIAL(" +
				"ID_PRESUPUESTO_TRABAJO INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
				"ID_PRESUPUESTO INT NOT NULL," +
				"NOMBRE_MATERIAL TEXT NOT NULL," +
				"PRECIO_MATERIAL TEXT NOT NULL," +
				"FOREIGN KEY (ID_PRESUPUESTO_TRABAJO) REFERENCES Presupuestos_Trabajo(ID) ON DELETE CASCADE" +
				");";
		String budgetDescriptionSQL = "CREATE TABLE IF NOT EXISTS PRESUPUESTO_DESCRIPCION(" +
				"ID_PRESUPUESTO_TRABAJO INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
				"ID_PRESUPUESTO INT NOT NULL," +
				"DESCRIPCION_MATERIAL TEXT NOT NULL," +
				"PRECIO TEXT NOT NULL," +
				"FOREIGN KEY (ID_PRESUPUESTO_TRABAJO) REFERENCES Presupuestos_Trabajo(ID) ON DELETE CASCADE" +
				");";
		try (Statement stmt = connection.createStatement()) {
			stmt.setQueryTimeout(QUERY_TIMEOUT);
			stmt.execute(budgetMaterialsSQL);
			stmt.execute(budgetDescriptionSQL);
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
	}
}
