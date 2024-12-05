package utils.databases;

import javax.swing.*;
import java.sql.*;
import java.util.ArrayList;


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
                    "ID_CATEGORIA INTEGER NOT NULL," +
                    "FOREIGN KEY(ID_CATEGORIA) REFERENCES Categorias(ID)" +
                    ")";
            stmt.setQueryTimeout(QUERY_TIMEOUT);
            stmt.execute(attributeSQL);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }


    public int getAttributeID(String attribute, int categoryID) {
        String sql = "SELECT ID FROM Atributos WHERE Nombre = ? AND ID_CATEGORIA = ?";
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

    public int insertAttributeRow(String attribute, int categoryID) {
        String sql = "INSERT INTO Atributos(Nombre, ID_CATEGORIA) VALUES(?, ?)";
        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, attribute);
            pstmt.setInt(2, categoryID);
            pstmt.executeUpdate();
            ResultSet rs = pstmt.getGeneratedKeys();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return -1;
    }
}