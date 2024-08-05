package utils.databases;

import javax.swing.*;
import java.sql.*;
import java.util.ArrayList;
import utils.Product;

public class ProductsDatabaseConnection extends DatabaseConnection {

    @Override
    protected void createTable(Connection connection) {
        String productSQL = "CREATE TABLE IF NOT EXISTS Productos (" +
                "ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                "Nombre TEXT NOT NULL," +
                "Descripción TEXT," +
                "Precio REAL NOT NULL" +
                ")";
        try (Statement stmt = connection.createStatement()) {
            stmt.setQueryTimeout(QUERY_TIMEOUT);
            stmt.execute(productSQL);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void insertProduct(String nombre, String descripcion, double precio) throws SQLException {
        String sql = "INSERT INTO Productos(Nombre, Descripcion, Precio) VALUES(?, ?, ?)";
        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, nombre);
            pstmt.setString(2, descripcion);
            pstmt.setDouble(3, precio);
            pstmt.executeUpdate();
            JOptionPane.showMessageDialog(null, "Producto creado con éxito!");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al crear el producto.", "Error", JOptionPane.ERROR_MESSAGE);
            System.out.println(e.getMessage());
        }
    }

    private static final String URL = "jdbc:sqlite:D:/GITHUB_LocalRepositories/Grafica_BG/PRUEBAS.db";

    @Override
    public Connection connect() throws SQLException {
        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            throw new SQLException("SQLite JDBC driver not found.", e);
        }
        return DriverManager.getConnection(URL);
    }

    public ArrayList<Product> getProducts(String searchText) throws SQLException {
        String sql = "SELECT * FROM Productos WHERE (Nombre LIKE ?)";
        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, "%" + searchText + "%");
            ResultSet resultSet = pstmt.executeQuery();

            ArrayList<Product> products = new ArrayList<>();

            while (resultSet.next()) {
                Product product = new Product(
                        resultSet.getString("Nombre"),
                        resultSet.getString("Descripcion"),
                        resultSet.getDouble("Precio")
                );
                products.add(product);
            }
            return products;
        }
    }
}

