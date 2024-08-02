package presenters.client;

import models.IClientModel;
import presenters.StandardPresenter;
import views.client.IClientSearchView;

public class ClientSearchPresenter extends StandardPresenter {
	private final IClientSearchView clientSearchView;
	private final IClientModel clientModel;

	public ClientSearchPresenter(IClientSearchView clientSearchView, IClientModel clientModel) {
		this.clientSearchView = clientSearchView;
		this.clientModel = clientModel;
	}


	public void onSearchClientButtonClicked() {
		clientSearchView.showView();
	}

	@Override
	protected void initListeners() {

	}

	public void onSearchButtonClicked() {

		clientSearchView.setWorkingStatus();

		String searchedName = clientSearchView.getSearchText();
		String searchedAddress = clientSearchView.getSelectedAddress();

		if (searchedAddress.equals("Cualquier localidad")) { searchedAddress = ""; }

		String result = clientModel.getClients(searchedName, searchedAddress);

		clientSearchView.setWaitingStatus();

	}

	public void onClientListOpenButtonClicked() {
	}
}
