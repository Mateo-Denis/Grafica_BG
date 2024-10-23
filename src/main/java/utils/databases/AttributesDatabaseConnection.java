package utils.databases;

import javax.swing.*;
import java.sql.*;
import java.util.ArrayList;


public class AttributesDatabaseConnection extends DatabaseConnection {

    @Override
    protected void createTable(Connection connection) {
        createAttributesTable(connection);
        createProductsAttributesTable(connection);
    }


    private void createAttributesTable(Connection connection) {
        try (Statement stmt = connection.createStatement()) {
            String attributeSQL = "CREATE TABLE IF NOT EXISTS Atributos (" +
                    "ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "Nombre TEXT NOT NULL," +
                    "ID_CATEGORIA INTEGER NOT NULL," +
                    "FOREIGN KEY(ID_CATEGORIA) REFERENCES Categorias(ID)" +
                    ")";
            stmt.setQueryTimeout(QUERY_TIMEOUT);
            stmt.execute(attributeSQL);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    private void createProductsAttributesTable(Connection connection) {
        try (Statement stmt = connection.createStatement()) {
            String productAttributeSQL = "CREATE TABLE IF NOT EXISTS Productos_Atributos (" +
                    "ID INTEGER AUTOINCREMENT PRIMARY KEY," +
                    "ID_PRODUCTO INTEGER NOT NULL," +
                    "ID_ATRIBUTO INTEGER NOT NULL," +
                    "Valor TEXT NOT NULL," +
                    "Dependiente BOOLEAN NOT NULL," +
                    "FOREIGN KEY(ID_PRODUCTO) REFERENCES Productos(ID)," +
                    "FOREIGN KEY(ID_ATRIBUTO) REFERENCES Atributos(ID)" +
                    ")";
            stmt.setQueryTimeout(QUERY_TIMEOUT);
            stmt.execute(productAttributeSQL);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public int getAttributeID(String attribute) {
        String sql = "SELECT ID FROM Atributos WHERE Nombre = ?";
        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, attribute);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("ID");
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return -1;
    }

    public void insertProductAttributeRow(int productID, int attributeID, String value) {
        String sql = "INSERT INTO Producto_Atributos(ID_PRODUCTO, ID_ATRIBUTO, Valor) VALUES(?, ?, ?)";
        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, productID);
            pstmt.setInt(2, attributeID);
            pstmt.setString(3, value);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void insertAttributeRow(String attribute, int categoryID) {
        String sql = "INSERT INTO Atributos(Nombre, ID_CATEGORIA) VALUES(?, ?)";
        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, attribute);
            pstmt.setInt(2, categoryID);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}