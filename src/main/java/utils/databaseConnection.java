package utils;

import javax.swing.*;
import java.sql.*;
import java.awt.*;

public class databaseConnection extends JDialog{

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

    public static void createProductsTable() {
        String productSQL = "CREATE TABLE IF NOT EXISTS Productos (" +
                "ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                "Nombre TEXT NOT NULL," +
                "Descripción TEXT," +
                "Precio REAL NOT NULL" +
                ")";
        try (Connection conn = connect();
             Statement stmt = conn.createStatement()) {
            stmt.execute(productSQL);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }


    private void insertClient(String nombre, String direccion, String localidad, String telefono, String tipoCliente) {
        String sql = "INSERT INTO Clientes(Nombre, Dirección, Localidad, Teléfono, TipoCliente) VALUES(?, ?, ?, ?, ?)";
        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, nombre);
            pstmt.setString(2, direccion);
            pstmt.setString(3, localidad);
            pstmt.setString(4, telefono);
            pstmt.setString(5, tipoCliente);
            pstmt.executeUpdate();
            JOptionPane.showMessageDialog(this, "Cliente creado con éxito!");
            dispose();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error al crear el cliente.", "Error", JOptionPane.ERROR_MESSAGE);
            System.out.println(e.getMessage());
        }
    }

    private void insertProduct(String nombre, String descripcion, double precio) {
        String sql = "INSERT INTO Productos(Nombre, Descripción, Precio) VALUES(?, ?, ?)";
        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, nombre);
            pstmt.setString(2, descripcion);
            pstmt.setDouble(3, precio);
            pstmt.executeUpdate();
            JOptionPane.showMessageDialog(this, "Producto creado con éxito!");
            dispose();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error al crear el producto.", "Error", JOptionPane.ERROR_MESSAGE);
            System.out.println(e.getMessage());
        }
    }
}

