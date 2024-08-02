package main;

import models.ClientModel;
import models.IClientModel;
import presenters.client.ClientCreatePresenter;
import presenters.client.ClientSearchPresenter;
import utils.databases.ClientsDatabaseConnection;
import views.client.ClientCreateView;
import views.client.ClientSearchView;
import views.home.IHomeView;
import views.home.HomeView;

public class Main {
	public static void main(String[] args) {


		ClientsDatabaseConnection clientsDB = new ClientsDatabaseConnection();
		clientsDB.loadDatabase();

		IClientModel clientModel = new ClientModel(clientsDB);

		ClientCreateView clientCreateView = new ClientCreateView();
		ClientSearchView clientSearchView = new ClientSearchView();

		ClientCreatePresenter clientCreatePresenter = new ClientCreatePresenter(clientCreateView, clientModel);
		ClientSearchPresenter clientSearchPresenter = new ClientSearchPresenter(clientSearchView, clientModel);
		clientCreatePresenter.start();


		clientCreateView.start();


		IHomeView home = new HomeView(clientCreatePresenter, clientSearchPresenter);
	}
}