package presenters.client;

import models.IClientModel;
import models.listeners.failed.ClientCreationEmptyFieldListener;
import presenters.StandardPresenter;
import utils.Client;
import views.client.IClientSearchView;

import java.util.ArrayList;

import static utils.MessageTypes.*;
public class ClientSearchPresenter extends StandardPresenter {
	private final IClientSearchView clientSearchView;
	private final IClientModel clientModel;

	public ClientSearchPresenter(IClientSearchView clientSearchView, IClientModel clientModel) {
		this.clientSearchView = clientSearchView;
		view = clientSearchView;
		this.clientModel = clientModel;
	}
	@Override
	public void start() {
		super.start();
		clientModel.queryCities();
	}

	public void onHomeSearchClientButtonClicked() {
		clientSearchView.clearView();
		clientSearchView.showView();
	}

	@Override
	protected void initListeners() {
		clientModel.addClientSearchSuccessListener(() -> {
			ArrayList<Client> clients = clientModel.getLastClientsQuery();
			int rowCount = 0;
			for (Client client : clients) {
				clientSearchView.setTableValueAt(rowCount, 0, client.getName());
				clientSearchView.setTableValueAt(rowCount, 1, client.getAddress());
				clientSearchView.setTableValueAt(rowCount, 2, client.getCity());
				clientSearchView.setTableValueAt(rowCount, 3, client.getPhone());
				clientSearchView.setTableValueAt(rowCount, 4, client.isClient() ? "Cliente" : "Particular");
				rowCount++;
			}
		});

		clientModel.addClientSearchFailureListener(() -> clientSearchView.showMessage(CLIENT_SEARCH_FAILURE));
		clientModel.addClientCreationEmptyFieldListener((ClientCreationEmptyFieldListener) () -> clientSearchView.showMessage(ANY_CREATION_EMPTY_FIELDS));

		clientModel.addCitiesFetchingSuccessListener(() -> {
			ArrayList<String> cities = clientModel.getQueriedCities();
			for (String city : cities) {
				clientSearchView.addCityToComboBox(city);
			}
		});

		clientModel.addCitiesFetchingFailureListener(() -> clientSearchView.showMessage(CITY_FETCH_FAILURE));

		clientModel.addClientCreationSuccessListener(() -> {
			String lastCityAdded = clientModel.getLastCityAdded();
			if(!clientSearchView.isCityInComboBox(lastCityAdded)){
				clientSearchView.addCityToComboBox(lastCityAdded);
			}
		});
	}


	public void onSearchButtonClicked() {

		clientSearchView.setWorkingStatus();

		String searchedName = clientSearchView.getnameSearchText();
		String searchedCity = clientSearchView.getSelectedCity();

		clientSearchView.clearTable();

		if (searchedCity.equals("Cualquier localidad")) { searchedCity = ""; }

		clientModel.queryClients(searchedName, searchedCity);

		clientSearchView.setWaitingStatus();

	}

	public void onDeleteClientButtonClicked() {
		int[] selectedRows = clientSearchView.getClientResultTable().getSelectedRows();
		if(selectedRows.length == 1) {
			deleteOneClient();
		} else {
			clientSearchView.showMessage(CLIENT_DELETION_FAILURE);
		}
	}

	public void deleteOneClient() {
		int selectedRow = clientSearchView.getSelectedTableRow();
		int oneClientID = getOneClientID(selectedRow);
		if (oneClientID != -1 && oneClientID != 0) {
			clientModel.deleteOneClient(oneClientID);
			clientSearchView.setWorkingStatus();
			clientSearchView.clearTable();
			String searchedName = clientSearchView.getnameSearchText();
			String searchedCity = clientSearchView.getSelectedCity();
			if (searchedCity.equals("Cualquier localidad")) { searchedCity = ""; }
			clientModel.queryClients(searchedName, searchedCity);
			clientSearchView.deselectAllRows();
			clientSearchView.setWaitingStatus();
		}
	}

	public int getOneClientID(int selectedRow) {
		String clientName = clientSearchView.getClientStringTableValueAt(selectedRow, 0);
		String clientType = clientSearchView.getClientStringTableValueAt(selectedRow, 4);
		int clientID = -1;

		if(selectedRow != -1 && !clientName.isEmpty() && !clientType.isEmpty()) {
			if(clientType.equals("Cliente")) {
				clientID = clientModel.getClientID(clientName, "Cliente");
			} else {
				clientID = clientModel.getClientID(clientName, "Particular");
			}
		}

		return clientID;
	}
}
