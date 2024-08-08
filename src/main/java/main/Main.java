package main;

import PdfFormater.PdfConverter;
import PdfFormater.Row;
import models.ClientModel;
import models.IClientModel;
import models.ProductModel;
import models.IProductModel;
import presenters.client.ClientCreatePresenter;
import presenters.client.ClientSearchPresenter;
import presenters.product.ProductSearchPresenter;
import utils.Client;
import utils.databases.ClientsDatabaseConnection;
import utils.databases.ProductsDatabaseConnection;
import views.client.ClientCreateView;
import views.client.ClientSearchView;
import views.products.ProductSearchViewClasses.ProductSearchView;
import views.home.IHomeView;
import views.home.HomeView;

import java.io.FileNotFoundException;

import static PdfFormater.SamplePDFCreation.createWeirdAahPDF;

public class Main {
	public static void main(String[] args) {

		createWeirdAahPDF();
		ClientsDatabaseConnection clientsDB = new ClientsDatabaseConnection();
		clientsDB.loadDatabase();
		ProductsDatabaseConnection productsDB = new ProductsDatabaseConnection();
		productsDB.loadDatabase();

		IClientModel clientModel = new ClientModel(clientsDB);
		IProductModel productModel = new ProductModel(productsDB);

		ClientCreateView clientCreateView = new ClientCreateView();
		ClientSearchView clientSearchView = new ClientSearchView();
		ProductSearchView productSearchView = new ProductSearchView();

		ClientCreatePresenter clientCreatePresenter = new ClientCreatePresenter(clientCreateView, clientModel);
		ClientSearchPresenter clientSearchPresenter = new ClientSearchPresenter(clientSearchView, clientModel);
		ProductSearchPresenter productSearchPresenter = new ProductSearchPresenter(productSearchView, productModel);
		clientCreatePresenter.start();
		clientSearchPresenter.start();
		productSearchPresenter.start();

		clientCreateView.start();
		clientSearchView.start();
		productSearchView.start();



		IHomeView home = new HomeView(clientCreatePresenter, clientSearchPresenter, productSearchPresenter);
	}
}