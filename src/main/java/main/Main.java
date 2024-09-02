package main;


import static PdfFormater.SamplePDFCreation.createWeirdAahPDF;

import icm.sphynx.ui.metro.tools.MetroUIComponent;
import models.ClientModel;
import models.IClientModel;
import models.ProductModel;
import models.IProductModel;
import models.BudgetModel;
import models.IBudgetModel;
import models.CategoryModel;
import models.ICategoryModel;
import models.IProductListModel;
import models.ProductListModel;
import models.IClientListModel;
import models.ClientListModel;
import models.BudgetListModel;
import models.IBudgetListModel;
import presenters.client.ClientCreatePresenter;
import presenters.client.ClientListPresenter;
import presenters.client.ClientSearchPresenter;
import presenters.product.ProductCreatePresenter;
import presenters.product.ProductSearchPresenter;
import presenters.product.ProductListPresenter;
import presenters.budget.BudgetCreatePresenter;
import presenters.budget.BudgetSearchPresenter;
import presenters.budget.BudgetListPresenter;
import presenters.categories.CategoryCreatePresenter;
import utils.databases.ClientsDatabaseConnection;
import utils.databases.ProductsDatabaseConnection;
import utils.databases.BudgetsDatabaseConnection;
import utils.databases.CategoriesDatabaseConnection;
import utils.databases.AttributesDatabaseConnection;
import views.client.ClientCreateView;
import views.client.ClientSearchView;
import views.client.list.ClientListView;
import views.products.ProductCreateView;
import views.products.ProductSearchView;
import views.products.list.ProductListView;
import views.budget.BudgetCreateView;
import views.budget.BudgetSearchView;
import views.budget.list.BudgetListView;
import views.categories.CategoryCreateView;
import views.home.IHomeView;
import views.home.HomeView;
import icm.sphynx.ui.metro.tools.MetroUIComponent;
import icm.sphynx.*;

public class Main {
    public static void main(String[] args) {


        /*ESTILOSSSSSSSSS*/
        MyStyles.install(MyStyles.METRO_UI, MyStyles.COLOR_PURPLE_DARK);
        /*ESTILOSSSSSSSSS*/




        createWeirdAahPDF();

        CategoriesDatabaseConnection categoriesDB = new CategoriesDatabaseConnection();
        categoriesDB.loadDatabase();
        ClientsDatabaseConnection clientsDB = new ClientsDatabaseConnection();
        clientsDB.loadDatabase();
        ProductsDatabaseConnection productsDB = new ProductsDatabaseConnection();
        productsDB.loadDatabase();
        BudgetsDatabaseConnection budgetsDB = new BudgetsDatabaseConnection();
        budgetsDB.loadDatabase();
        AttributesDatabaseConnection attributesDB = new AttributesDatabaseConnection();
        attributesDB.loadDatabase();

        IClientModel clientModel = new ClientModel(clientsDB);
        IProductModel productModel = new ProductModel(productsDB, attributesDB, categoriesDB);
        IBudgetModel budgetModel = new BudgetModel(budgetsDB, productsDB,clientsDB, categoriesDB);
        ICategoryModel categoryModel = new CategoryModel(categoriesDB, attributesDB);
        IProductListModel productListModel = new ProductListModel(productsDB);
        IClientListModel clientListModel = new ClientListModel(clientsDB);
        IBudgetListModel budgetListModel = new BudgetListModel(budgetsDB);

        ClientCreateView clientCreateView = new ClientCreateView();
        ClientListView clientListView = new ClientListView();
        ProductCreateView productCreateView = new ProductCreateView();
        ProductListView productListView = new ProductListView();
        BudgetCreateView budgetCreateView = new BudgetCreateView();
        BudgetListView budgetListView = new BudgetListView();
        CategoryCreateView categoryCreateView = new CategoryCreateView();

        ClientCreatePresenter clientCreatePresenter = new ClientCreatePresenter(clientCreateView, clientModel);
        ClientListPresenter clientListPresenter = new ClientListPresenter(clientListView, clientListModel);
        ProductCreatePresenter productCreatePresenter = new ProductCreatePresenter(productCreateView, productModel, categoryModel);
        ProductListPresenter productListPresenter = new ProductListPresenter(productListView, productListModel);
        CategoryCreatePresenter categoryCreatePresenter = new CategoryCreatePresenter(categoryCreateView);
        BudgetListPresenter budgetListPresenter = new BudgetListPresenter(budgetListView, budgetListModel);
        ProductSearchView productSearchView = new ProductSearchView(productListPresenter);
        ClientSearchView clientSearchView = new ClientSearchView(clientListPresenter);
        BudgetSearchView budgetSearchView = new BudgetSearchView(budgetListPresenter);
        ProductSearchPresenter productSearchPresenter = new ProductSearchPresenter(productSearchView, productModel);
        ClientSearchPresenter clientSearchPresenter = new ClientSearchPresenter(clientSearchView, clientModel);
        BudgetCreatePresenter budgetCreatePresenter = new BudgetCreatePresenter(budgetCreateView, budgetModel, categoryModel);
        BudgetSearchPresenter budgetSearchPresenter = new BudgetSearchPresenter(budgetSearchView, budgetCreateView, budgetModel);
        clientCreatePresenter.start();
        clientSearchPresenter.start();
        productCreatePresenter.start();
        productListPresenter.start();
        productSearchPresenter.start();
        budgetCreatePresenter.start();
        budgetSearchPresenter.start();
        categoryCreatePresenter.start();
        budgetListPresenter.start();

        clientCreateView.start();
        clientSearchView.start();
        productCreateView.start();
        productSearchView.start();
        productListView.start();
        budgetCreateView.start();
        budgetSearchView.start();
        categoryCreateView.start();
        budgetListView.start();

        IHomeView home = new HomeView(clientCreatePresenter, clientSearchPresenter, productSearchPresenter, productCreatePresenter, budgetSearchPresenter, budgetCreatePresenter, categoryCreatePresenter);
    }
}