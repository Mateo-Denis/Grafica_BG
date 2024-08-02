package main;

import models.ClientModel;
import models.IClientModel;
import presenters.client.ClientCreatePresenter;
import utils.databases.ClientsDatabaseConnection;
import views.client.ClientCreateView;
import views.home.IHomeView;
import views.home.HomeView;

public class Main {
	public static void main(String[] args) {


		ClientsDatabaseConnection clientsDB = new ClientsDatabaseConnection();
		clientsDB.loadDatabase();

		IClientModel clientModel = new ClientModel(clientsDB);

		ClientCreateView clientCreateView = new ClientCreateView();

		ClientCreatePresenter clientCreatePresenter = new ClientCreatePresenter(clientCreateView, clientModel);
		clientCreatePresenter.start();


		clientCreateView.start();


		IHomeView home = new HomeView(clientCreatePresenter);
	}
}