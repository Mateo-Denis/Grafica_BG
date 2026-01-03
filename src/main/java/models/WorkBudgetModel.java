package models;

import utils.Client;
import utils.databases.ClientsDatabaseConnection;

import java.util.ArrayList;
import java.util.logging.Logger;

public class WorkBudgetModel {

	private final ClientsDatabaseConnection clientsDBConnection;
	private static Logger LOGGER;

	public WorkBudgetModel(ClientsDatabaseConnection clientsDBConnection) {
		this.clientsDBConnection = clientsDBConnection;
	}

	public ArrayList<String> getCitiesName() {
		try {
			return clientsDBConnection.getCities();
		} catch (Exception e) {
			LOGGER.log(null, "Error getting cities");
		}
		return new ArrayList<>();
	}

	public ArrayList<Client> getClients(String name, String city) {
		try {
			return clientsDBConnection.getClientsFromNameAndCity(name, city);
		} catch (Exception e) {
			LOGGER.log(null, "Error getting clients");
		}
		return new ArrayList<>();
	}

	public int getClientID(String name, String clientType) {
		try {
			return clientsDBConnection.getClientID(name, clientType);
		} catch (Exception e) {
			LOGGER.log(null, "Error getting client ID");
		}
		return -1;
	}
}
