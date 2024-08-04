package utils.databases;

import utils.Client;

import java.sql.*;
import java.util.ArrayList;

public class ClientsDatabaseConnection extends DatabaseConnection{
    protected void createTable(Connection connection) {
        String clientSQL =  "CREATE TABLE IF NOT EXISTS Clientes (" +
                            "ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                            "Nombre TEXT NOT NULL," +
                            "Direccion TEXT NOT NULL," +
                            "Localidad TEXT NOT NULL," +
                            "Telefono TEXT NOT NULL," +
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
        String sql = "INSERT INTO Clientes(Nombre, Direccion, Localidad, Telefono, TipoCliente) VALUES(?, ?, ?, ?, ?)";
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

    public ArrayList<Client> getClientsFromNameAndCity(String clientName, String clientCity) throws SQLException {
        String sql = "SELECT * FROM Clientes WHERE (Nombre LIKE ?) AND (Direccion LIKE ?)";
        Connection conn = connect();
        PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.setString(1, "%" + clientName + "%");
        pstmt.setString(2, "%" + clientCity + "%");
        ResultSet resultSet = pstmt.executeQuery();

        ArrayList<Client> clientes = new ArrayList<>();

        while (resultSet.next()) {
            Client cliente;
            if(resultSet.getString("TipoCliente").equals("Cliente")) {
                cliente = new Client(
                        resultSet.getString("Nombre"),
                        resultSet.getString("Direccion"),
                        resultSet.getString("Localidad"),
                        resultSet.getString("Telefono"),
                        true
                );
            }else {
                cliente = new Client(
                        resultSet.getString("Nombre"),
                        resultSet.getString("Direccion"),
                        resultSet.getString("Localidad"),
                        resultSet.getString("Telefono"),
                        false
                );
            }
            clientes.add(cliente);
        }
        resultSet.close();
        pstmt.close();
        conn.close();

        return clientes;
    }
}
