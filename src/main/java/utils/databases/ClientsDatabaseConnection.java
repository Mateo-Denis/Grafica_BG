package utils.databases;

import javax.swing.*;
import java.sql.*;

public class ClientsDatabaseConnection {
    private static final String URL = "jdbc:sqlite:D:/GITHUB_LocalRepositories/Grafica_BG/PRUEBAS.db";

    public static Connection connect() throws SQLException {
        return DriverManager.getConnection(URL);
    }

    public static void createClientsTable() {
        String clientSQL = "CREATE TABLE IF NOT EXISTS Clientes (" +
                "ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                "Nombre TEXT NOT NULL," +
                "Dirección TEXT NOT NULL," +
                "Localidad TEXT NOT NULL," +
                "Teléfono TEXT NOT NULL," +
                "TipoCliente TEXT NOT NULL CHECK (TipoCliente IN('Cliente', 'Particular'))" +
                ")";
        try (Connection conn = connect();
             Statement stmt = conn.createStatement()) {
            stmt.execute(clientSQL);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void insertClient(String name, String address, String city, String phone, String clientType) throws SQLException{
        String sql = "INSERT INTO Clientes(Nombre, Dirección, Localidad, Teléfono, TipoCliente) VALUES(?, ?, ?, ?, ?)";
        Connection conn = connect();
        PreparedStatement pstmt = conn.prepareStatement(sql);

        pstmt.setString(1, name);
        pstmt.setString(2, address);
        pstmt.setString(3, city);
        pstmt.setString(4, phone);
        pstmt.setString(5, clientType);
        pstmt.executeUpdate();

        pstmt.close();
        conn.close();
    }
}
