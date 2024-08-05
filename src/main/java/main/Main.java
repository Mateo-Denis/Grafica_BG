package main;

import models.ClientModel;
import models.IClientModel;
import models.ProductModel;
import models.IProductModel;
import presenters.client.ClientCreatePresenter;
import presenters.client.ClientSearchPresenter;
import presenters.product.ProductCreatePresenter;
import presenters.product.ProductSearchPresenter;
import utils.databases.ClientsDatabaseConnection;
import utils.databases.ProductsDatabaseConnection;
import views.client.ClientCreateView;
import views.client.ClientSearchView;
import views.products.ProductCreateView;
import views.products.ProductSearchView;
import views.home.IHomeView;
import views.home.HomeView;

public class Main {
	public static void main(String[] args) {


		ClientsDatabaseConnection clientsDB = new ClientsDatabaseConnection();
		clientsDB.loadDatabase();
		ProductsDatabaseConnection productsDB = new ProductsDatabaseConnection();
		productsDB.loadDatabase();

		IClientModel clientModel = new ClientModel(clientsDB);
		IProductModel productModel = new ProductModel(productsDB);

		ClientCreateView clientCreateView = new ClientCreateView();
		ClientSearchView clientSearchView = new ClientSearchView();
		ProductCreateView productCreateView = new ProductCreateView();
		ProductSearchView productSearchView = new ProductSearchView();

		ClientCreatePresenter clientCreatePresenter = new ClientCreatePresenter(clientCreateView, clientModel);
		ClientSearchPresenter clientSearchPresenter = new ClientSearchPresenter(clientSearchView, clientModel);
		ProductCreatePresenter productCreatePresenter = new ProductCreatePresenter(productCreateView, productModel);
		ProductSearchPresenter productSearchPresenter = new ProductSearchPresenter(productSearchView, productModel);
		clientCreatePresenter.start();
		clientSearchPresenter.start();
		productCreatePresenter.start();
		productSearchPresenter.start();

		clientCreateView.start();
		clientSearchView.start();
		productCreateView.start();
		productSearchView.start();



		IHomeView home = new HomeView(clientCreatePresenter, clientSearchPresenter, productSearchPresenter, productCreatePresenter);
	}
}