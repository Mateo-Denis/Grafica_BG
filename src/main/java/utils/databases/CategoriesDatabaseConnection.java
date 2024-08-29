package utils.databases;

import javax.swing.*;
import java.sql.*;
import java.util.ArrayList;

public class CategoriesDatabaseConnection extends DatabaseConnection {

    @Override
    protected void createTable(Connection connection) {
        String categorySQL = "CREATE TABLE IF NOT EXISTS Categorias (" +
                "ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                "Nombre TEXT NOT NULL" +
                ")";
        try (Statement stmt = connection.createStatement()) {
            stmt.setQueryTimeout(QUERY_TIMEOUT);
            stmt.execute(categorySQL);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void insertCategory(String nombre) throws SQLException {
        String sql = "INSERT INTO Categorias(Nombre) VALUES(?)";
        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, nombre);
            pstmt.executeUpdate();
            JOptionPane.showMessageDialog(null, "Categoria creada con éxito!");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al crear la categoria.", "Error", JOptionPane.ERROR_MESSAGE);
            System.out.println(e.getMessage());
        }
    }

    public ArrayList<String> getCategories() throws SQLException {
        ArrayList<String> categorias = new ArrayList<>();
        String sql = "SELECT Nombre FROM Categorias";
        try (Connection conn = connect();
             PreparedStatement statement = conn.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                categorias.add(resultSet.getString("Nombre"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return categorias;
    }

    public String getCategoryName(int categoryID) {
        String sql = "SELECT Nombre FROM Categorias WHERE ID = ?";
        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, categoryID);
            try (ResultSet resultSet = pstmt.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getString("Nombre");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "";
    }

    public int getCategoryID(String categoryName) throws SQLException {
        return getIdByNameFromTable(categoryName, "Categorias");
    }

    private int getIdByNameFromTable(String name, String table) {
        int id = 0;
        String sql = "SELECT ID FROM " + table + " WHERE Nombre = ?";

        try (Connection conn = connect();
             PreparedStatement statement = conn.prepareStatement(sql);){

            statement.setString(1, name);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                id = resultSet.getInt("ID");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return id;
    }

    public ArrayList<String> getCategoryAttributesNames(int categoryID) throws SQLException {
        ArrayList<String> attributes = new ArrayList<>();
        String sql = "SELECT Nombre FROM Atributos WHERE ID_CATEGORIA = ? ORDER BY ID";

        Connection conn = connect();
        PreparedStatement statement = conn.prepareStatement(sql);
        statement.setInt(1, categoryID);

        ResultSet resultSet = statement.executeQuery();

        while (resultSet.next()) {
            attributes.add(resultSet.getString("Nombre"));
        }


        resultSet.close();
        statement.close();
        conn.close();

        return attributes;
    }


}
