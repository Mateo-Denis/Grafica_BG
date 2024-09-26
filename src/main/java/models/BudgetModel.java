package models;

import models.listeners.failed.BudgetCreationFailureListener;
import models.listeners.failed.BudgetSearchFailureListener;
import models.listeners.successful.BudgetCreationSuccessListener;
import models.listeners.successful.BudgetSearchSuccessListener;
import utils.Budget;
import utils.Client;
import utils.Product;
import utils.databases.BudgetsDatabaseConnection;
import utils.databases.CategoriesDatabaseConnection;
import utils.databases.ClientsDatabaseConnection;
import utils.databases.ProductsDatabaseConnection;
import com.google.common.collect.Multimap;

import java.sql.SQLException;
import java.util.*;

public class BudgetModel implements IBudgetModel {
    private final BudgetsDatabaseConnection budgetsDBConnection;
    private final ProductsDatabaseConnection productsDBConnection;
    private final ClientsDatabaseConnection clientsDBConnection;
    private final CategoriesDatabaseConnection categoriesDBConnection;
    private final List<BudgetCreationSuccessListener> budgetCreationSuccessListeners;
    private final List<BudgetCreationFailureListener> budgetCreationFailureListeners;
    private final List<BudgetSearchSuccessListener> budgetSearchSuccessListeners;
    private final List<BudgetSearchFailureListener> budgetSearchFailureListeners;


    private ArrayList<Budget> budgets;

    public BudgetModel(BudgetsDatabaseConnection budgetsDBConnection, ProductsDatabaseConnection productsDBConnection, ClientsDatabaseConnection clientsDBConnection, CategoriesDatabaseConnection categoriesDBConnection) {
        this.budgetsDBConnection = budgetsDBConnection;
        this.productsDBConnection = productsDBConnection;
        this.clientsDBConnection = clientsDBConnection;
        this.categoriesDBConnection = categoriesDBConnection;

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
            String categoryName = "";
            int categoryID = 0;
            for (Product product : products) {
                categoryID = product.getCategoryID();
                categoryName = categoriesDBConnection.getCategoryName(categoryID);
                if (categoryName.equals(category)) {
                    productNames.add(product.getName());
                }
            }
            return productNames;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return new ArrayList<>();
    }

    public ArrayList<Product> getProducts(String productName, String productCategory) {
        try {
            return productsDBConnection.getProducts(productName, productCategory);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    @Override
    public ArrayList<String> getCitiesName() {
        try {
            return clientsDBConnection.getCities();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    @Override
    public ArrayList<Client> getClients(String name, String city) {
        try {
            return clientsDBConnection.getClientsFromNameAndCity(name, city);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    @Override
    public int getBudgetID(int budgetNumber, String budgetName) {
        try {
            return budgetsDBConnection.getBudgetID(budgetName, budgetNumber);
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

    @Override
    public void deleteMultipleBudgets(ArrayList<Integer> budgetIDs) {
        try {
            budgetsDBConnection.deleteMultipleBudgets(budgetIDs);
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }

    public void saveProducts(int budgetNumber, String budgetName, Multimap<Integer,String> products, ArrayList<String> observations, ArrayList<String> productMeasures) {
        try {
            budgetsDBConnection.saveProducts(budgetNumber, budgetName, products, observations, productMeasures);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void deleteBudgetProducts(String budgetName, int budgetNumber, boolean updating) {
        try {
            budgetsDBConnection.deleteBudgetProducts(budgetName, budgetNumber, updating);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public ArrayList<Client> getOneClient(int clientID) {
        try {
            return clientsDBConnection.getOneClient(clientID);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    public int getClientID(String clientName) {
        try {
            return clientsDBConnection.getClientID(clientName);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }

    @Override
    public int getBudgetNumber(int budgetID) {
        try {
            return budgetsDBConnection.getBudgetNumber(budgetID);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }
}
