package presenters.client;

import models.IClientModel;
import views.client.IClientSearchView;

public class ClientSearchPresenter {
	private final IClientSearchView clientSearchView;
	private final IClientModel clientModel;

	public ClientSearchPresenter(IClientSearchView clientSearchView, IClientModel clientModel) {
		this.clientSearchView = clientSearchView;
		this.clientModel = clientModel;
	}


}
