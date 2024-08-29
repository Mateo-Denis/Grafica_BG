package presenters.client;

import presenters.StandardPresenter;
import utils.Client;
import utils.databases.ClientsDatabaseConnection;
import views.client.IClientSearchView;
import views.client.list.IClientListView;
import models.IClientListModel;

import javax.swing.*;
import java.util.ArrayList;

public class ClientListPresenter extends StandardPresenter {
    private final IClientListView clientListView;
    private final IClientListModel clientListModel;
    private IClientSearchView clientSearchView;
    private ClientsDatabaseConnection clientsDatabaseConnection;

    public ClientListPresenter(IClientListView clientListView, IClientListModel clientListModel) {
        this.clientListView = clientListView;
        view = clientListView;
        this.clientListModel = clientListModel;
        clientsDatabaseConnection = new ClientsDatabaseConnection();
    }

    @Override
    protected void initListeners() {

    }

    public void onSearchViewOpenListButtonClicked() {
        ArrayList<Client> clients = clientListModel.getClientsFromDB();
        if (clients.isEmpty()) {
            JOptionPane.showMessageDialog(null, "No hay clientes en la base de datos");
        } else {
            clientListView.showView();
            clientListView.setWorkingStatus();
            clientListView.clearView();
            setClientsOnTable();
            clientListView.setWaitingStatus();
        }
    }

    public void setClientsOnTable() {
        ArrayList<Client> clients = clientListModel.getClientsFromDB();
        int rowCount = 0;
        int clientID = 0;

        for (Client client : clients) {
            try {
                clientID = clientsDatabaseConnection.getClientID(client.getName());
                System.out.println(client.getName());
            } catch (Exception e) {
                e.printStackTrace();
            }

            clientListView.setIntTableValueAt(rowCount, 0, clientID);
            clientListView.setStringTableValueAt(rowCount, 1, client.getName());
            clientListView.setStringTableValueAt(rowCount, 2, client.getAddress());
            clientListView.setStringTableValueAt(rowCount, 3, client.getCity());
            clientListView.setStringTableValueAt(rowCount, 4, client.getPhone());
            clientListView.setStringTableValueAt(rowCount, 5, client.isClient() ? "Cliente" : "Particular");
            rowCount++;

        }
    }
}

