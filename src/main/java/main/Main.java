package main;


import static PdfFormater.SamplePDFCreation.createWeirdAahPDF;

//import icm.sphynx.MyStyles;
//import icm.sphynx.ui.metro.tools.MetroUIComponent;
import com.formdev.flatlaf.FlatLightLaf;
import models.*;
import models.settings.ISettingsModel;
import models.settings.SettingsModel;
import presenters.budget.*;
import presenters.client.ClientCreatePresenter;
import presenters.client.ClientListPresenter;
import presenters.client.ClientSearchPresenter;
import presenters.product.ProductCreatePresenter;
import presenters.product.ProductSearchPresenter;
import presenters.product.ProductListPresenter;
import presenters.categories.CategoryCreatePresenter;
import presenters.settings.SettingsPresenter;
import utils.databases.*;
import views.budget.modify.BudgetModifyView;
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
import views.settings.ISettingsView;
import views.settings.SettingsView;

public class Main {
    public static void main(String[] args) {


        /*ESTILOSSSSSSSSS
        MyStyles.install(MyStyles.METRO_UI, NewStyles.COLOR_BLACK);
        ESTILOSSSSSSSSS*/

        FlatLightLaf.setup();



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
        SettingsDatabaseConnection settingsDB = new SettingsDatabaseConnection();
        settingsDB.loadDatabase();

        IClientModel clientModel = new ClientModel(clientsDB);
        IProductModel productModel = new ProductModel(productsDB, attributesDB, categoriesDB);
        IBudgetModel budgetModel = new BudgetModel(budgetsDB, productsDB,clientsDB, categoriesDB);
        ICategoryModel categoryModel = new CategoryModel(categoriesDB, attributesDB);
        IProductListModel productListModel = new ProductListModel(productsDB);
        IClientListModel clientListModel = new ClientListModel(clientsDB);
        IBudgetListModel budgetListModel = new BudgetListModel(budgetsDB);
        IBudgetModifyModel budgetModifyModel = new BudgetModifyModel(budgetsDB);
        ISettingsModel settingsModel = new SettingsModel(settingsDB);

        ClientCreateView clientCreateView = new ClientCreateView();
        ClientListView clientListView = new ClientListView();
        ProductCreateView productCreateView = new ProductCreateView();
        ProductListView productListView = new ProductListView();
        BudgetCreateView budgetCreateView = new BudgetCreateView();
        BudgetListView budgetListView = new BudgetListView();
        CategoryCreateView categoryCreateView = new CategoryCreateView();
        BudgetModifyView budgetModifyView = new BudgetModifyView();
        SettingsView settingsView = new SettingsView();



        ClientCreatePresenter clientCreatePresenter = new ClientCreatePresenter(clientCreateView, clientModel);
        ClientListPresenter clientListPresenter = new ClientListPresenter(clientListView, clientListModel);
        ProductCreatePresenter productCreatePresenter = new ProductCreatePresenter(productCreateView, productModel, categoryModel);
        ProductListPresenter productListPresenter = new ProductListPresenter(productListView, productListModel);
        CategoryCreatePresenter categoryCreatePresenter = new CategoryCreatePresenter(categoryCreateView);
        BudgetListPresenter budgetListPresenter = new BudgetListPresenter(budgetListView, budgetListModel);
        ProductSearchView productSearchView = new ProductSearchView(productListPresenter);
        ClientSearchView clientSearchView = new ClientSearchView(clientListPresenter);
        ProductSearchPresenter productSearchPresenter = new ProductSearchPresenter(productSearchView, productModel);
        ClientSearchPresenter clientSearchPresenter = new ClientSearchPresenter(clientSearchView, clientModel);
        BudgetCreatePresenter budgetCreatePresenter = new BudgetCreatePresenter(budgetCreateView, budgetModel, categoryModel, productModel);
        BudgetModifyPresenter budgetModifyPresenter = new BudgetModifyPresenter(budgetModifyView, budgetModifyModel, categoryModel, budgetModel, productModel);
        BudgetSearchView budgetSearchView = new BudgetSearchView(budgetListPresenter, budgetModifyPresenter);
        BudgetSearchPresenter budgetSearchPresenter = new BudgetSearchPresenter(budgetSearchView, budgetCreateView, budgetModel);
        SettingsPresenter settingsPresenter = new SettingsPresenter(settingsView, settingsModel);

        clientCreatePresenter.start();
        clientSearchPresenter.start();
        productCreatePresenter.start();
        productListPresenter.start();
        productSearchPresenter.start();
        budgetCreatePresenter.start();
        budgetSearchPresenter.start();
        categoryCreatePresenter.start();
        budgetListPresenter.start();
        budgetModifyPresenter.start();
        settingsPresenter.start();

        clientCreateView.start();
        clientSearchView.start();
        productCreateView.start();
        productSearchView.start();
        productListView.start();
        budgetCreateView.start();
        budgetSearchView.start();
        categoryCreateView.start();
        budgetListView.start();
        budgetModifyView.start();
        settingsView.start();

        IHomeView home = new HomeView(clientCreatePresenter, clientSearchPresenter, productSearchPresenter,
                productCreatePresenter, budgetSearchPresenter, budgetCreatePresenter, categoryCreatePresenter, settingsPresenter);
    }

}