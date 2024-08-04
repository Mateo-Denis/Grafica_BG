package models;

import models.listeners.failed.ClientCreationFailureListener;
import models.listeners.successful.ClientCreationSuccessListener;
import models.listeners.failed.ClientSearchFailureListener;
import models.listeners.successful.ClientSearchSuccessListener;
import utils.Client;
import utils.databases.ClientsDatabaseConnection;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class ClientModel implements IClientModel{

	private final ClientsDatabaseConnection dbConnection;
	private final List<ClientCreationSuccessListener> clientCreationSuccessListeners;
	private final List<ClientCreationFailureListener> clientCreationFailureListeners;
	private final List<ClientSearchSuccessListener> clientSearchSuccessListeners;
	private final List<ClientSearchFailureListener> clientSearchFailureListeners;

	private ArrayList<Client> clients;

	public ClientModel(ClientsDatabaseConnection dbConnection) {
		this.dbConnection = dbConnection;
		clients = new ArrayList<>();

		this.clientCreationSuccessListeners = new LinkedList<>();
		this.clientCreationFailureListeners = new LinkedList<>();

		this.clientSearchSuccessListeners = new LinkedList<>();
		this.clientSearchFailureListeners = new LinkedList<>();
	}

	@Override
	public void createClient(String clientName, String clientAddress, String clientCity, String clientPhone, boolean isClient) {
		try {
			if(isClient){
				dbConnection.insertClient(clientName, clientAddress, clientCity, clientPhone, "Cliente");
			}else {
				dbConnection.insertClient(clientName, clientAddress, clientCity, clientPhone, "Particular");
			}
			notifyClientCreationSuccess();
		} catch (Exception e) {
			notifyClientCreationFailure();
		}
	}

	public void queryClients(String clientName, String clientCity) {
		try {
			clients = dbConnection.getClientsFromNameAndCity(clientName, clientCity);
			
			notifyClientSearchSuccess();
		} catch (SQLException e) {
			notifyClientSearchFailure();
		}
	}
	@Override
	public ArrayList<Client> getLastClientsQuery() {
		return clients;
	}
	@Override
	public void addClientCreationSuccessListener(ClientCreationSuccessListener listener) {
		clientCreationSuccessListeners.add(listener);
	}
	@Override
	public void addClientCreationFailureListener(ClientCreationFailureListener listener) {
		clientCreationFailureListeners.add(listener);
	}

	@Override
	public void addClientSearchSuccessListener(ClientSearchSuccessListener listener) {
		clientSearchSuccessListeners.add(listener);
	}
	@Override
	public void addClientSearchFailureListener(ClientSearchFailureListener listener) {
		clientSearchFailureListeners.add(listener);
	}


	private void notifyClientCreationSuccess() {
		for (ClientCreationSuccessListener listener : clientCreationSuccessListeners) {
			listener.onSuccess();
		}
	}
	private void notifyClientCreationFailure() {
		for (ClientCreationFailureListener listener : clientCreationFailureListeners) {
			listener.onFailure();
		}
	}

	private void notifyClientSearchSuccess() {
		for (ClientSearchSuccessListener listener : clientSearchSuccessListeners) {
			listener.onSuccess();
		}
	}
	private void notifyClientSearchFailure() {
		for (ClientSearchFailureListener listener : clientSearchFailureListeners) {
			listener.onFailure();
		}
	}
}
