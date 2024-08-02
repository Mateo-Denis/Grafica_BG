package utils.databases;

import java.sql.*;

public class ClientsDatabaseConnection extends DatabaseConnection{
    protected void createTable(Connection connection) {
        String clientSQL =  "CREATE TABLE IF NOT EXISTS Clientes (" +
                            "ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                            "Nombre TEXT NOT NULL," +
                            "Dirección TEXT NOT NULL," +
                            "Localidad TEXT NOT NULL," +
                            "Teléfono TEXT NOT NULL," +
                            "TipoCliente TEXT NOT NULL CHECK (TipoCliente IN('Cliente', 'Particular'))" +
                            ")";
        try (Statement stmt = connection.createStatement()) {
            stmt.setQueryTimeout(QUERY_TIMEOUT);
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
