package models;

import models.listeners.failed.BudgetCreationFailureListener;
import models.listeners.failed.BudgetSearchFailureListener;
import models.listeners.successful.BudgetCreationSuccessListener;
import models.listeners.successful.BudgetSearchSuccessListener;
import utils.Budget;
import utils.Client;
import utils.Product;
import utils.databases.BudgetsDatabaseConnection;
import utils.databases.ClientsDatabaseConnection;
import utils.databases.ProductsDatabaseConnection;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class BudgetModel implements IBudgetModel {
    private final BudgetsDatabaseConnection budgetsDBConnection;
    private final ProductsDatabaseConnection productsDBConnection;
    private final ClientsDatabaseConnection clientsDBConnection;
    private final List<BudgetCreationSuccessListener> budgetCreationSuccessListeners;
    private final List<BudgetCreationFailureListener> budgetCreationFailureListeners;
    private final List<BudgetSearchSuccessListener> budgetSearchSuccessListeners;
    private final List<BudgetSearchFailureListener> budgetSearchFailureListeners;

    private ArrayList<Budget> budgets;

    public BudgetModel(BudgetsDatabaseConnection budgetsDBConnection, ProductsDatabaseConnection productsDBConnection, ClientsDatabaseConnection clientsDBConnection) {
        this.budgetsDBConnection = budgetsDBConnection;
        this.productsDBConnection = productsDBConnection;
        this.clientsDBConnection = clientsDBConnection;

        budgets = new ArrayList<>();

        this.budgetCreationSuccessListeners = new LinkedList<>();
        this.budgetCreationFailureListeners = new LinkedList<>();

        this.budgetSearchSuccessListeners = new LinkedList<>();
        this.budgetSearchFailureListeners = new LinkedList<>();
    }

    @Override
    public void createBudget(String budgetName, String budgetDate, String budgetClientType, int budgetNumber) {
        try {
            budgetsDBConnection.insertBudget(budgetName, budgetDate, budgetClientType, budgetNumber);
            notifyBudgetCreationSuccess();
        } catch (Exception e) {
            notifyBudgetCreationFailure();
        }
    }

    @Override
    public void addBudgetCreationSuccessListener(BudgetCreationSuccessListener listener) {
        budgetCreationSuccessListeners.add(listener);
    }

    @Override
    public void addBudgetCreationFailureListener(BudgetCreationFailureListener listener) {
        budgetCreationFailureListeners.add(listener);
    }

    @Override
    public void addBudgetSearchSuccessListener(BudgetSearchSuccessListener listener) {
        budgetSearchSuccessListeners.add(listener);
    }

    @Override
    public void addBudgetSearchFailureListener(BudgetSearchFailureListener listener) {
        budgetSearchFailureListeners.add(listener);
    }

/*    @Override
    public void queryBudgets(String budgetSearch) {
        try {
            budgets = budgetsDBConnection.getBudgets(budgetSearch);
            notifyBudgetSearchSuccess();
        } catch (Exception e) {
            notifyBudgetSearchFailure();
        }
    }*/

    @Override
    public void queryBudgets(String budgetSearch) {
        try {
            budgets = budgetsDBConnection.getBudgets(budgetSearch);
            notifyBudgetSearchSuccess();
        } catch (Exception e) {
            notifyBudgetSearchFailure();
        }
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

    @Override
    public ArrayList<Budget> getLastBudgetsQuery() {
        return budgets;
    }

    @Override
    public ArrayList<String> getProductNamesByCategory(String category) {

        try {
            ArrayList<Product> products = productsDBConnection.getAllProducts();
            ArrayList<String> productNames = new ArrayList<>();
            for (Product product : products) {
                if (product.getCategoryName().equals(category)) {
                    productNames.add(product.getName());
                }
            }
            return productNames;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public ArrayList<Product> getProducts() {
        try {
            return productsDBConnection.getAllProducts();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public ArrayList<String> getCitiesName() {
        try {
            return clientsDBConnection.getCities();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public ArrayList<Client> getClients(String name, String city) {
        try {
            return clientsDBConnection.getClientsFromNameAndCity(name, city);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
