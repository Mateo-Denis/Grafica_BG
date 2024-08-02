package models;

import models.listeners.ClientCreationFailureListener;
import models.listeners.ClientCreationSuccessListener;
import utils.Client;
import utils.databases.ClientsDatabaseConnection;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class ClientModel implements IClientModel{

	private final ClientsDatabaseConnection dbConnection;
	private final List<ClientCreationSuccessListener> clientCreationSuccessListeners;
	private final List<ClientCreationFailureListener> clientCreationFailureListeners;

	public ClientModel(ClientsDatabaseConnection dbConnection) {
		this.dbConnection = dbConnection;

		this.clientCreationSuccessListeners = new LinkedList<>();
		this.clientCreationFailureListeners = new LinkedList<>();
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

	public ArrayList<Client> getClients(String clientName, String clientAddress) {

		dbConnection.getClients(clientName);


		return null;
	}
	@Override
	public void addClientCreationSuccessListener(ClientCreationSuccessListener listener) {
		clientCreationSuccessListeners.add(listener);
	}

	public void addClientCreationFailureListener(ClientCreationFailureListener listener) {
		clientCreationFailureListeners.add(listener);
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
}
