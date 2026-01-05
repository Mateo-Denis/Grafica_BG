package models;

import org.javatuples.Pair;
import utils.Client;
import utils.databases.ClientsDatabaseConnection;
import utils.databases.WorkBudgetsDatabaseConnection;

import java.util.ArrayList;
import java.util.logging.Logger;

public class WorkBudgetModel {

	private final ClientsDatabaseConnection clientsDBConnection;
	private final WorkBudgetsDatabaseConnection budgetsDBConnection;
	private static Logger LOGGER;

	public WorkBudgetModel(ClientsDatabaseConnection clientsDBConnection, WorkBudgetsDatabaseConnection budgetsDBConnection) {
		this.clientsDBConnection = clientsDBConnection;
		this.budgetsDBConnection = budgetsDBConnection;
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

	public void saveWorkBudget(int selectedClientId, ArrayList<Pair<String, String>> materialsList,
							   Pair<String, String> logisticsData, Pair<String, String> placingData,
							   String profitMargin, String finalPrice, ArrayList<Pair<String, String>> clientInfoItems) {

	}

	public int getNextBudgetNumber() {
		try {
			return budgetsDBConnection.getNextBudgetNumber();
		} catch (Exception e) {
			LOGGER.log(null, "Error getting next budget number");
		}
		return -1;
	}
}
