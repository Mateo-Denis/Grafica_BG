package main;

import models.ClientModel;
import models.IClientModel;
import models.ProductModel;
import models.IProductModel;
import models.BudgetModel;
import models.IBudgetModel;
import presenters.client.ClientCreatePresenter;
import presenters.client.ClientSearchPresenter;
import presenters.product.ProductCreatePresenter;
import presenters.product.ProductSearchPresenter;
import presenters.budget.BudgetCreatePresenter;
import presenters.budget.BudgetSearchPresenter;
import utils.databases.ClientsDatabaseConnection;
import utils.databases.ProductsDatabaseConnection;
import utils.databases.BudgetsDatabaseConnection;
import views.client.ClientCreateView;
import views.client.ClientSearchView;
import views.products.ProductCreateView;
import views.products.ProductSearchView;
import views.budget.BudgetCreateView;
import views.budget.BudgetSearchView;
import views.home.IHomeView;
import views.home.HomeView;

public class Main {
	public static void main(String[] args) {


		ClientsDatabaseConnection clientsDB = new ClientsDatabaseConnection();
		clientsDB.loadDatabase();
		ProductsDatabaseConnection productsDB = new ProductsDatabaseConnection();
		productsDB.loadDatabase();
		BudgetsDatabaseConnection budgetsDB = new BudgetsDatabaseConnection();
		budgetsDB.loadDatabase();

		IClientModel clientModel = new ClientModel(clientsDB);
		IProductModel productModel = new ProductModel(productsDB);
		IBudgetModel budgetModel = new BudgetModel(budgetsDB);

		ClientCreateView clientCreateView = new ClientCreateView();
		ClientSearchView clientSearchView = new ClientSearchView();
		ProductCreateView productCreateView = new ProductCreateView();
		ProductSearchView productSearchView = new ProductSearchView();
		BudgetCreateView budgetCreateView = new BudgetCreateView();
		BudgetSearchView budgetSearchView = new BudgetSearchView();

		ClientCreatePresenter clientCreatePresenter = new ClientCreatePresenter(clientCreateView, clientModel);
		ClientSearchPresenter clientSearchPresenter = new ClientSearchPresenter(clientSearchView, clientModel);
		ProductCreatePresenter productCreatePresenter = new ProductCreatePresenter(productCreateView, productModel);
		ProductSearchPresenter productSearchPresenter = new ProductSearchPresenter(productSearchView, productModel);
		//BudgetCreatePresenter budgetCreatePresenter = new BudgetCreatePresenter(budgetCreateView, budgetModel);
		BudgetSearchPresenter budgetSearchPresenter = new BudgetSearchPresenter(budgetSearchView, budgetModel);
		clientCreatePresenter.start();
		clientSearchPresenter.start();
		productCreatePresenter.start();
		productSearchPresenter.start();
		//budgetCreatePresenter.start();
		budgetSearchPresenter.start();

		clientCreateView.start();
		clientSearchView.start();
		productCreateView.start();
		productSearchView.start();
		//budgetCreateView.start();
		budgetSearchView.start();



		IHomeView home = new HomeView(clientCreatePresenter, clientSearchPresenter, productSearchPresenter, productCreatePresenter, budgetSearchPresenter);
	}
}