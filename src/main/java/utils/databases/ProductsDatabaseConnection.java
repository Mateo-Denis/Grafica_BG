//30-7-2024 --> DIVISIÓN DE CLASES DE BASES DE DATOS EN: "ProductsDatabaseConnection" y "ClientsDatabaseConnection"
//30-7-2024 --> AGRUPACIÓN DE LAS CLASES EN EL DIRECTORIO: "utils/databases"
package utils.databases;

import javax.swing.*;
import java.sql.*;
//30-7-2024 --> SE AGREGA EL IMPORT "java.util.ArrayList":
import java.util.ArrayList;
//30-7-2024 --> SE AGREGA EL IMPORT "java.util.List":
import java.util.List;
//30-7-2024 --> SE AGREGA EL IMPORT "models.ProductSearchModel":
import models.ProductSearchModel;

public class ProductsDatabaseConnection extends JDialog {

    private static final String URL = "jdbc:sqlite:D:/GITHUB_LocalRepositories/Grafica_BG/PRUEBAS.db";

    public static Connection connect() throws SQLException {
        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            throw new SQLException("SQLite JDBC driver not found.", e);
        }
        return DriverManager.getConnection(URL);
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

    //30-7-2024 --> SE AGREGA EL METODO getProducts(...):
    public List<ProductSearchModel> getProducts(String searchText) {
        String sql = "SELECT * FROM Productos WHERE Nombre LIKE ?";
        List<ProductSearchModel> products = new ArrayList<>();

        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, "%" + searchText + "%");
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                products.add(new ProductSearchModel(rs.getInt("ID"), rs.getString("Nombre"), rs.getString("Descripción"), rs.getDouble("Precio")));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return products;
    }
}

