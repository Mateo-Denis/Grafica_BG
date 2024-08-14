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
		clientModel.addClientCreationEmptyFieldListener((ClientCreationEmptyFieldListener) () -> clientSearchView.showMessage(CLIENT_CREATION_EMPTY_FIELDS));

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

	public void onClientListOpenButtonClicked() {
	}

}
