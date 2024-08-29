package models;

import utils.Client;
import utils.databases.ClientsDatabaseConnection;
import java.util.ArrayList;

public class ClientListModel implements IClientListModel {
    private final ClientsDatabaseConnection clientsDBConnection;
    private ArrayList<Client> clients;

    public ClientListModel( ClientsDatabaseConnection clientsDBConnection) {
        this.clientsDBConnection = clientsDBConnection;
        clients = new ArrayList<>();
    }

    @Override
    public ArrayList<Client> getClientsFromDB() {
        return clientsDBConnection.getAllClients();
    }
}
