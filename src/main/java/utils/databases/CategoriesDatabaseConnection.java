package utils.databases;

import java.sql.Connection;

public class CategoriesDatabaseConnection extends DatabaseConnection{
	@Override
	protected void createTable(Connection connection) {
		String SQL = "CREATE TABLE IF NOT EXISTS Categorias (" +
				"ID INTEGER PRIMARY KEY AUTOINCREMENT," +
				"Nombre TEXT NOT NULL," +
				"Descripción TEXT," +
				"Precio REAL NOT NULL" +
				")";
	}
}
