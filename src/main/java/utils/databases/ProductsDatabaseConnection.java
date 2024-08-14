package utils.databases;

import javax.swing.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import utils.Product;

public class ProductsDatabaseConnection extends DatabaseConnection {

    private CategoriesDatabaseConnection categoriesDatabaseConnection = new CategoriesDatabaseConnection();

    @Override
    protected void createTable(Connection connection) {
        String productSQL = "CREATE TABLE IF NOT EXISTS Productos (" +
                "ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                "Nombre TEXT NOT NULL," +
                "Descripción TEXT," +
                "Precio REAL NOT NULL," +
                "Categoria_ID INTEGER NOT NULL," +
                "FOREIGN KEY(Categoria_ID) REFERENCES Categorias(ID)" +
                ")";
        try (Statement stmt = connection.createStatement()) {
            stmt.setQueryTimeout(QUERY_TIMEOUT);
            stmt.execute(productSQL);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public int insertProduct(String nombre, String descripcion, double precio, int categoriaID) throws SQLException {
        int idGenerado = -1;
        String sql = "INSERT INTO Productos(Nombre, Descripcion, Precio, Categoria_ID) VALUES(?, ?, ?, ?)";
        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, nombre);
            pstmt.setString(2, descripcion);
            pstmt.setDouble(3, precio);
            pstmt.setInt(4, categoriaID);
            //pstmt.executeUpdate();
            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                // Intenta obtener las claves generadas (el ID del producto)
                try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        // Recupera el primer valor de las claves generadas, que es el ID del producto
                        idGenerado = generatedKeys.getInt(1);
                    }
                }
            }
            JOptionPane.showMessageDialog(null, "Producto creado con éxito!");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al crear el producto.", "Error", JOptionPane.ERROR_MESSAGE);
            System.out.println(e.getMessage());
        }

        return idGenerado;
    }

    public ArrayList<Product> getProducts(String searchText) throws SQLException {
        int categoryID = -1;
        String categoryName = "";
        String sql = "SELECT * FROM Productos WHERE (Nombre LIKE ?)";

        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, "%" + searchText + "%");
            ResultSet resultSet = pstmt.executeQuery();
            ArrayList<Product> products = new ArrayList<>();
            while (resultSet.next()) {
                categoryID = getCategoryID(resultSet.getString("Nombre"));
                categoryName = categoriesDatabaseConnection.getCategoryName(categoryID);

                Product product = new Product(
                        resultSet.getString("Nombre"),
                        resultSet.getString("Descripcion"),
                        resultSet.getDouble("Precio"),
                        categoryName
                );
                products.add(product);
            }

            resultSet.close();
            pstmt.close();
            conn.close();
            return products;
        }
    }

    public int getCategoryID(String productName) throws SQLException {
        String sql = "SELECT Categoria_ID FROM Productos WHERE Nombre = ?";
        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, productName);
            ResultSet resultSet = pstmt.executeQuery();
            int categoryID = -1;
            while (resultSet.next()) {
                categoryID = resultSet.getInt("Categoria_ID");
            }
            resultSet.close();
            pstmt.close();
            conn.close();
            return categoryID;
        }
    }

    public void deleteProductFromDB(List<Integer> productIDs) throws SQLException {
        int IDsCount = 0;
        for (int ID : productIDs) {
            IDsCount++;
        }
        if (IDsCount == 1) {
            int productID = productIDs.get(0);
            String sql = "DELETE FROM Productos WHERE ID = ?";
            try (Connection conn = connect();
                 PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setInt(1, productID);
                pstmt.executeUpdate();
                JOptionPane.showMessageDialog(null, "Producto eliminado con éxito!");
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(null, "Error al eliminar el producto.", "Error", JOptionPane.ERROR_MESSAGE);
                System.out.println(e.getMessage());
            }
        } else {
            for (int ID : productIDs) {
                String sql = "DELETE FROM Productos WHERE ID = ?";
                try (Connection conn = connect();
                     PreparedStatement pstmt = conn.prepareStatement(sql)) {
                    pstmt.setInt(1, ID);
                    pstmt.executeUpdate();
                } catch (SQLException e) {
                    JOptionPane.showMessageDialog(null, "Error al eliminar algun producto.", "Error", JOptionPane.ERROR_MESSAGE);
                    System.out.println(e.getMessage());
                }
            }
            JOptionPane.showMessageDialog(null, "Productos eliminados con éxito!");
        }
    }

    public int getProductID(String product) throws SQLException {
        String sql = "SELECT ID FROM Productos WHERE Nombre = ?";
        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, product);
            ResultSet resultSet = pstmt.executeQuery();
            int productID = 0;
            while (resultSet.next()) {
                productID = resultSet.getInt("ID");
            }
            resultSet.close();
            pstmt.close();
            conn.close();
            return productID;
        }
    }

    public ArrayList<Product> getAllProducts() throws SQLException {
        ArrayList<Product> products = new ArrayList<>();
        String sql = "SELECT * FROM Productos";
        Connection connection = connect();

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                int categoryID= rs.getInt("Categoria_ID");
                String categoryName = categoriesDatabaseConnection.getCategoryName(categoryID);

                String productName = rs.getString("Nombre");
                String productDescription = rs.getString("Descripcion");
                double productPrice = rs.getDouble("Precio");

                products.add(new Product(productName, productDescription, productPrice, categoryName));
            }
        }

        return products;
    }
}




