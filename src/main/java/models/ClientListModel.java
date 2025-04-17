package models;

import utils.Client;
import utils.databases.ClientsDatabaseConnection;
import java.util.ArrayList;

public class ClientListModel implements IClientListModel {
    private final ClientsDatabaseConnection clientsDBConnection;

    public ClientListModel( ClientsDatabaseConnection clientsDBConnection) {
        this.clientsDBConnection = clientsDBConnection;
        ArrayList<Client> clients = new ArrayList<>();
    }

    @Override
    public ArrayList<Client> getClientsFromDB() {
        return clientsDBConnection.getAllClients();
    }
}
