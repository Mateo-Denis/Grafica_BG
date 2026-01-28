package utils.databases;

import utils.Client;

import javax.swing.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class ClientsDatabaseConnection extends DatabaseConnection {
    private static Logger LOGGER;

    protected void createTable(Connection connection) {
        String clientSQL = "CREATE TABLE IF NOT EXISTS Clientes (" +
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

    public void insertClient(String name, String address, String city, String phone, String clientType) throws SQLException {
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

    public void updateClient(int clientID, String name, String address, String city, String phone, String clientType) throws SQLException {
        String sql = "UPDATE Clientes SET Nombre = ?, Direccion = ?, Localidad = ?, Telefono = ?, TipoCliente = ? WHERE ID = ?";
        Connection conn = connect();
        PreparedStatement pstmt = conn.prepareStatement(sql);

        pstmt.setString(1, name);
        pstmt.setString(2, address);
        pstmt.setString(3, city);
        pstmt.setString(4, phone);
        pstmt.setString(5, clientType);
        pstmt.setInt(6, clientID);
        pstmt.executeUpdate();

        pstmt.close();
        conn.close();
    }

    public String getClientNameByID(int clientID) {
        String sql = "SELECT Nombre FROM Clientes WHERE ID = ?";
        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, clientID);
            try (ResultSet resultSet = pstmt.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getString("Nombre");
                }
            }
        } catch (SQLException e) {
            LOGGER.log(null,"ERROR IN METHOD 'getClientNameByID' IN CLASS->'ClientsDatabaseConnection'",e);
        }
        return null;
    }

    public ArrayList<String> getCities() throws SQLException {
        String sql = "SELECT DISTINCT Localidad FROM Clientes";
        Connection conn = connect();
        Statement stmt = conn.createStatement();
        ResultSet resultSet = stmt.executeQuery(sql);

        ArrayList<String> cities = new ArrayList<>();
        while (resultSet.next()) {
            cities.add(resultSet.getString("Localidad"));
        }
        resultSet.close();
        stmt.close();
        conn.close();

        return cities;
    }

    public ArrayList<Client> getClientsFromNameAndCity(String clientName, String clientCity) throws SQLException {
        String sql;
        if (clientCity.isEmpty()) {
            sql = "SELECT * FROM Clientes WHERE (Nombre LIKE ?) AND (Localidad LIKE ?)";
            clientCity = "%" + clientCity + "%";
        } else {
            sql = "SELECT * FROM Clientes WHERE (Nombre LIKE ?) AND (Localidad = ?)";
        }


        Connection conn = connect();
        PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.setString(1, "%" + clientName + "%");
        pstmt.setString(2, clientCity);
        ResultSet resultSet = pstmt.executeQuery();

        ArrayList<Client> clientes = new ArrayList<>();

        while (resultSet.next()) {
            Client cliente;
            if (resultSet.getString("TipoCliente").equals("Cliente")) {
                cliente = new Client(
                        resultSet.getString("Nombre"),
                        resultSet.getString("Direccion"),
                        resultSet.getString("Localidad"),
                        resultSet.getString("Telefono"),
                        true
                );
            } else {
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

    public ArrayList<Client> getAllClients() {
        String sql = "SELECT * FROM Clientes";
        try (Connection conn = connect();
             Statement stmt = conn.createStatement();
             ResultSet resultSet = stmt.executeQuery(sql)) {
            ArrayList<Client> clients = new ArrayList<>();
            while (resultSet.next()) {
                Client client = new Client(
                        resultSet.getString("Nombre"),
                        resultSet.getString("Direccion"),
                        resultSet.getString("Localidad"),
                        resultSet.getString("Telefono"),
                        resultSet.getString("TipoCliente").equals("Cliente")
                );
                clients.add(client);
            }
            return clients;
        } catch (SQLException e) {
            LOGGER.log(null,"ERROR IN METHOD 'getAllClients' IN CLASS->'ClientsDatabaseConnection'",e);
        }
        return new ArrayList<>();
    }

    public int getClientID(String clientName, String clientType) {
        String sql = "SELECT ID FROM Clientes WHERE Nombre = ? AND TipoCliente = ?";
        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, clientName);
            pstmt.setString(2, clientType);
            try (ResultSet resultSet = pstmt.executeQuery()) {
                return resultSet.getInt("ID");
            }
        } catch (SQLException e) {
            LOGGER.log(null,"ERROR IN METHOD 'getClientID' IN CLASS->'ClientsDatabaseConnection'",e);

        }
        return 0;
    }

    public void deleteOneClient(int clientID) {
        String sql = "DELETE FROM Clientes WHERE ID = ?";
        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, clientID);
            pstmt.executeUpdate();
            JOptionPane.showMessageDialog(null, "¡Cliente eliminado con éxito!");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "¡Error al eliminar el cliente!");
            LOGGER.log(null,"ERROR IN METHOD 'deleteOneClient' IN CLASS->'ClientsDatabaseConnection'",e);

        }
    }

    public Client getOneClient(int clientID) {
        String sql = "SELECT * FROM Clientes WHERE ID = ?";
        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, clientID);
            try (ResultSet resultSet = pstmt.executeQuery()) {
                if (resultSet.next()) {
                    return new Client(
                            resultSet.getString("Nombre"),
                            resultSet.getString("Direccion"),
                            resultSet.getString("Localidad"),
                            resultSet.getString("Telefono"),
                            resultSet.getString("TipoCliente").equals("Cliente")
                    );
                }
            }
        } catch (SQLException e) {
            LOGGER.log(null,"ERROR IN METHOD 'getOneClient' IN CLASS->'ClientsDatabaseConnection'",e);

        }
        return null;
    }
}
