package main;


import static PdfFormater.SamplePDFCreation.createWeirdAahPDF;


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
import presenters.client.ClientCreatePresenter;
import presenters.client.ClientSearchPresenter;
import presenters.product.ProductCreatePresenter;
import presenters.product.ProductSearchPresenter;
import presenters.product.ProductListPresenter;
import presenters.budget.BudgetCreatePresenter;
import presenters.budget.BudgetSearchPresenter;
import utils.databases.ClientsDatabaseConnection;
import utils.databases.ProductsDatabaseConnection;
import utils.databases.BudgetsDatabaseConnection;
import utils.databases.CategoriesDatabaseConnection;
import utils.databases.AttributesDatabaseConnection;
import views.client.ClientCreateView;
import views.client.ClientSearchView;
import views.products.ProductCreateView;
import views.products.ProductSearchView;
import views.products.list.ProductListView;
import views.budget.BudgetCreateView;
import views.budget.BudgetSearchView;
import views.home.IHomeView;
import views.home.HomeView;

public class Main {
    public static void main(String[] args) {


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
        IProductModel productModel = new ProductModel(productsDB, attributesDB);
        IBudgetModel budgetModel = new BudgetModel(budgetsDB, productsDB,clientsDB);
        ICategoryModel categoryModel = new CategoryModel(categoriesDB, attributesDB);
        IProductListModel productListModel = new ProductListModel(productsDB);

        ClientCreateView clientCreateView = new ClientCreateView();
        ClientSearchView clientSearchView = new ClientSearchView();
        ProductCreateView productCreateView = new ProductCreateView();
        ProductListView productListView = new ProductListView();
        BudgetCreateView budgetCreateView = new BudgetCreateView();
        BudgetSearchView budgetSearchView = new BudgetSearchView();

        ClientCreatePresenter clientCreatePresenter = new ClientCreatePresenter(clientCreateView, clientModel);
        ClientSearchPresenter clientSearchPresenter = new ClientSearchPresenter(clientSearchView, clientModel);
        ProductCreatePresenter productCreatePresenter = new ProductCreatePresenter(productCreateView, productModel, categoryModel);
        ProductListPresenter productListPresenter = new ProductListPresenter(productListView, productListModel);


        ProductSearchView productSearchView = new ProductSearchView(productListPresenter);


        ProductSearchPresenter productSearchPresenter = new ProductSearchPresenter(productSearchView, productModel);
        BudgetCreatePresenter budgetCreatePresenter = new BudgetCreatePresenter(budgetCreateView, budgetModel, categoryModel);
        BudgetSearchPresenter budgetSearchPresenter = new BudgetSearchPresenter(budgetSearchView, budgetModel);
        clientCreatePresenter.start();
        clientSearchPresenter.start();
        productCreatePresenter.start();
        productListPresenter.start();
        productSearchPresenter.start();
        budgetCreatePresenter.start();
        budgetSearchPresenter.start();

        clientCreateView.start();
        clientSearchView.start();
        productCreateView.start();
        productSearchView.start();
        productListView.start();
        budgetCreateView.start();
        budgetSearchView.start();

        IHomeView home = new HomeView(clientCreatePresenter, clientSearchPresenter, productSearchPresenter, productCreatePresenter, budgetSearchPresenter, budgetCreatePresenter);
    }
}