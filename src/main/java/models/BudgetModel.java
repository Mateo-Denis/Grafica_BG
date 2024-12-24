package models;

import java.util.*;

import models.listeners.failed.*;
import models.listeners.successful.*;
import utils.Budget;
import utils.Client;
import utils.Product;
import utils.databases.BudgetsDatabaseConnection;
import utils.databases.ClientsDatabaseConnection;
import utils.databases.ProductsDatabaseConnection;


public class BudgetModel implements IBudgetModel {
    // LISTENERS FOR BUDGET CREATION SUCCESS AND FAILURE
    private final List<BudgetCreationSuccessListener> budgetCreationSuccessListeners;
    private final List<BudgetCreationFailureListener> budgetCreationFailureListeners;
    private final List<BudgetSearchSuccessListener> budgetSearchSuccessListeners;
    private final List<BudgetSearchFailureListener> budgetSearchFailureListeners;

    // DATABASE CONNECTIONS
    private final BudgetsDatabaseConnection budgetsDBConnection;
    private final ProductsDatabaseConnection productsDBConnection;
    private final ClientsDatabaseConnection clientsDBConnection;

    // BUDGETS
    private ArrayList<Budget> budgets;



    // CONSTRUCTOR
    public BudgetModel(BudgetsDatabaseConnection budgetsDBConnection,
                       ProductsDatabaseConnection productsDBConnection,
                       ClientsDatabaseConnection clientsDBConnection)
    {
        // INITIALIZE LISTENERS
        this.budgetCreationSuccessListeners = new LinkedList<>();
        this.budgetCreationFailureListeners = new LinkedList<>();
        this.budgetSearchSuccessListeners = new LinkedList<>();
        this.budgetSearchFailureListeners = new LinkedList<>();

        // INITIALIZE DATABASE CONNECTIONS
        this.budgetsDBConnection = budgetsDBConnection;
        this.productsDBConnection = productsDBConnection;
        this.clientsDBConnection = clientsDBConnection;
    }



    // ---------> METHODS AND FUNCTIONS START HERE <-------------
    // ---------> METHODS AND FUNCTIONS START HERE <-------------



    @Override
    public void addBudgetCreationSuccessListener(BudgetCreationSuccessListener listener) { // ADD BUDGET CREATION SUCCESS LISTENER
        budgetCreationSuccessListeners.add(listener);
    }

    @Override
    public void addBudgetCreationFailureListener(BudgetCreationFailureListener listener) { // ADD BUDGET CREATION FAILURE LISTENER
        budgetCreationFailureListeners.add(listener);
    }

    @Override
    public void addBudgetSearchSuccessListener(BudgetSearchSuccessListener listener) { // ADD BUDGET SEARCH SUCCESS LISTENER
        budgetSearchSuccessListeners.add(listener);
    }

    @Override
    public void addBudgetSearchFailureListener(BudgetSearchFailureListener listener) { // ADD BUDGET SEARCH FAILURE LISTENER
        budgetSearchFailureListeners.add(listener);
    }

    private void notifyBudgetCreationSuccess() {
        for (BudgetCreationSuccessListener listener : budgetCreationSuccessListeners) {
            listener.onSuccess();
        }
    }

    private void notifyBudgetCreationFailure() {
        for (BudgetCreationFailureListener listener : budgetCreationFailureListeners) {
            listener.onFailure();
        }
    }

    private void notifyBudgetSearchSuccess() {
        for (BudgetSearchSuccessListener listener : budgetSearchSuccessListeners) {
            listener.onSuccess();
        }
    }

    private void notifyBudgetSearchFailure() {
        for (BudgetSearchFailureListener listener : budgetSearchFailureListeners) {
            listener.onFailure();
        }
    }



    // GET PRODUCTS
    public ArrayList<Product> getProducts(String productName, String productCategory) {
        try {
            return productsDBConnection.getProducts(productName, productCategory);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }


    // GET CLIENTS
    @Override
    public ArrayList<Client> getClients(String name, String city) {
        try {
            return clientsDBConnection.getClientsFromNameAndCity(name, city);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }


    // GET CLIENT ID
    public int getClientID(String clientName, String clientType) {
        try {
            return clientsDBConnection.getClientID(clientName, clientType);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }



    //CREATE BUDGET:
    @Override
    public void createBudget(String budgetName, String budgetDate, String budgetClientType, int budgetNumber) {
        try {
            budgetsDBConnection.insertBudget(budgetName, budgetDate, budgetClientType, budgetNumber);
        } catch (Exception e) {
            notifyBudgetCreationFailure();
        }
    }


    //GET NEXT BUDGET NUMBER:
    public int getNextBudgetNumber() {
        try {
            return budgetsDBConnection.getNextBudgetNumber();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }



    //SAVE BUDGET PRODUCTS ON BUDGET_PRODUCTS TABLE:
    public void saveProducts(int budgetID, ArrayList<Integer> productAmounts, ArrayList<String> productNames, ArrayList<String> observations, ArrayList<String> productMeasures, ArrayList<Double> productPrices) {
        try {
            budgetsDBConnection.saveProducts(budgetID, productAmounts, productNames, observations, productMeasures, productPrices);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    //GET BUDGET ID:
    @Override
    public int getBudgetID(int budgetNumber, String budgetName) {
        try {
            return budgetsDBConnection.getBudgetID(budgetName, budgetNumber);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }


    // GET CITIES NAME
    @Override
    public ArrayList<String> getCitiesName() {
        try {
            return clientsDBConnection.getCities();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }


    // GET LAST BUDGETS QUERY
    @Override
    public ArrayList<Budget> getLastBudgetsQuery() {
        return budgets;
    }

    @Override
    public void queryBudgets(String budgetSearch) {
        try {
            budgets = budgetsDBConnection.getBudgets(budgetSearch);
            notifyBudgetSearchSuccess();
        } catch (Exception e) {
            notifyBudgetSearchFailure();
        }
    }

    public int getMaxBudgetID() {
        try {
            return budgetsDBConnection.getMaxBudgetID();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }


    @Override
    public void deleteOneBudget(int budgetID) {
        try {
            budgetsDBConnection.deleteOneBudget(budgetID);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public Client GetOneClientByID(int clientID) {
        try {
            return clientsDBConnection.getOneClient(clientID);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }



    public void deleteBudgetProducts(String budgetName, int budgetID, int budgetNumber) {
        try {
            budgetsDBConnection.deleteBudgetProducts(budgetName, budgetID, budgetNumber);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ---------> METHODS AND FUNCTIONS END HERE <-------------
    // ---------> METHODS AND FUNCTIONS END HERE <-------------
}
