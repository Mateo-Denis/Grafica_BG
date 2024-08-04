package presenters.client;

import models.IClientModel;
import presenters.StandardPresenter;
import utils.Client;
import views.client.IClientSearchView;

import java.util.ArrayList;

import static utils.MessageTypes.CLIENT_SEARCH_FAILURE;

public class ClientSearchPresenter extends StandardPresenter {
	private final IClientSearchView clientSearchView;
	private final IClientModel clientModel;

	public ClientSearchPresenter(IClientSearchView clientSearchView, IClientModel clientModel) {
		this.clientSearchView = clientSearchView;
		view = clientSearchView;
		this.clientModel = clientModel;
	}


	public void onSearchClientButtonClicked() {
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
	}

	public void onSearchButtonClicked() {

		clientSearchView.setWorkingStatus();

		String searchedName = clientSearchView.getnameSearchText();
		String searchedAddress = clientSearchView.getSelectedCity();

		if (searchedAddress.equals("Cualquier localidad")) { searchedAddress = ""; }

		clientModel.queryClients(searchedName, searchedAddress);

		clientSearchView.setWaitingStatus();

	}

	public void onClientListOpenButtonClicked() {
	}
}
