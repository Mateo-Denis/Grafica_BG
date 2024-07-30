package utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class databaseConnection {
    private static final String URL = "jdbc:sqlite:D:/GITHUB_LocalRepositories/Grafica_BG/PRUEBAS.db";

    public static Connection connect() throws SQLException {
        return DriverManager.getConnection(URL);
    }

    public static void createTable() {
        String sql = "CREATE TABLE Clientes ("+
                "ID INTEGER PRIMARY KEY AUTOINCREMENT,"+
                "Nombre TEXT NOT NULL,"+
                "Dirección TEXT NOT NULL,"+
                "Localidad TEXT NOT NULL,"+
                "Teléfono TEXT NOT NULL,"+
                "TipoCliente TEXT NOT NULL CHECK (TipoCliente IN('Cliente', 'Particular'))"+
                ")";
        try (Connection conn = connect();
             Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}
