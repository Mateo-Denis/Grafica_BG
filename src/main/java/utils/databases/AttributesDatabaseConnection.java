package utils.databases;

import java.sql.*;
import java.util.HashMap;
import java.util.Map;


public class AttributesDatabaseConnection extends DatabaseConnection {

    @Override
    protected void createTable(Connection connection) {
        createAttributesTable(connection);
    }


    private void createAttributesTable(Connection connection) {
        try (Statement stmt = connection.createStatement()) {
            String attributeSQL = "CREATE TABLE IF NOT EXISTS Atributos (" +
                    "ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "Nombre TEXT NOT NULL," +
                    "ID_PRODUCTO INTEGER NOT NULL," +
                    "VALOR VARCHAR(255)," +
                    "FOREIGN KEY(ID_PRODUCTO) REFERENCES Productos(ID)" +
                    ")";
            stmt.setQueryTimeout(QUERY_TIMEOUT);
            stmt.execute(attributeSQL);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void insertAttributeRow(String attribute, int productID, String value) {
        String sql = "INSERT INTO Atributos(Nombre, ID_PRODUCTO, VALOR) VALUES(?, ?, ?)";
        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, attribute);
            pstmt.setInt(2, productID);
            pstmt.setString(3, value);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public String getAttributeValue(int id, String attributeName) {
        String sql = "SELECT VALOR FROM Atributos WHERE ID_PRODUCTO = ? AND Nombre = ?";
        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.setString(2, attributeName);
            ResultSet rs = pstmt.executeQuery();
            return rs.getString("VALOR");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    public void deleteProductAttributesByID(int productID) throws SQLException
    {
        String sql = "DELETE FROM Atributos WHERE ID_PRODUCTO = ?";
        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, productID);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public Map<String, String> getProductAttributes(int productID) throws SQLException {
        String sql = "SELECT Nombre, VALOR FROM Atributos WHERE ID_PRODUCTO = ?";
        Map<String, String> attributes = new HashMap<>();
        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, productID);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                String attributeName = rs.getString("Nombre");
                String attributeValue = rs.getString("VALOR");
                attributes.put(attributeName, attributeValue);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return attributes;
    }
}