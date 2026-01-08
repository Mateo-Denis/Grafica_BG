package models;

import models.listeners.failed.BudgetCreationFailureListener;
import models.listeners.failed.WorkBudgetCreationFailureListener;
import models.listeners.successful.BudgetCreationSuccessListener;
import models.listeners.successful.WorkBudgetCreationSuccessListener;
import org.javatuples.Pair;
import utils.Client;
import utils.databases.ClientsDatabaseConnection;
import utils.databases.WorkBudgetsDatabaseConnection;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class WorkBudgetModel {

	private final List<WorkBudgetCreationSuccessListener> workBudgetCreationSuccessListeners;
	private final List<WorkBudgetCreationFailureListener> workBudgetCreationFailureListeners;

	private final ClientsDatabaseConnection clientsDBConnection;
	private final WorkBudgetsDatabaseConnection budgetsDBConnection;
	private static Logger LOGGER;

	public WorkBudgetModel(ClientsDatabaseConnection clientsDBConnection,
						   WorkBudgetsDatabaseConnection budgetsDBConnection) {
		this.clientsDBConnection = clientsDBConnection;
		this.budgetsDBConnection = budgetsDBConnection;

		this.workBudgetCreationSuccessListeners = new ArrayList<>();
		this.workBudgetCreationFailureListeners = new ArrayList<>();
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
		try {
			LocalDate fechaActual = LocalDate.now();
			DateTimeFormatter formato = DateTimeFormatter.ofPattern("yyyy-MM-dd");
			fechaActual.format(formato);
			int newNumber = budgetsDBConnection.insertWorkBudget(Integer.toString(selectedClientId), fechaActual.format(formato), logisticsData.getValue0(), logisticsData.getValue1()
					, placingData.getValue0(), placingData.getValue1(), profitMargin, finalPrice);

			budgetsDBConnection.insertMaterials(materialsList, newNumber);
			budgetsDBConnection.insertDescriptions(clientInfoItems, newNumber);

			notifyBudgetCreationSuccess();
		} catch (Exception e) {
			notifyBudgetCreationFailure();
		}
	}

	public int getNextBudgetNumber() {
		try {
			return budgetsDBConnection.getNextBudgetNumber();
		} catch (Exception e) {
			LOGGER.log(null, "Error getting next budget number");
		}
		return -1;
	}

	private void notifyBudgetCreationSuccess() {
		for (WorkBudgetCreationSuccessListener listener : workBudgetCreationSuccessListeners) {
			listener.onSuccess();
		}
	}

	private void notifyBudgetCreationFailure() {
		for (WorkBudgetCreationFailureListener listener : workBudgetCreationFailureListeners) {
			listener.onFailure();
		}
	}

	public void addBudgetCreationSuccessListener(WorkBudgetCreationSuccessListener listener) { // ADD BUDGET CREATION SUCCESS LISTENER
		workBudgetCreationSuccessListeners.add(listener);
	}

	public void addBudgetCreationFailureListener(WorkBudgetCreationFailureListener listener) { // ADD BUDGET CREATION FAILURE LISTENER
		workBudgetCreationFailureListeners.add(listener);
	}

	public void rollbackWorkBudgetCreation() {
		int id = budgetsDBConnection.getNextBudgetNumber() - 1;

	}
}
