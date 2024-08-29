package models;

import models.listeners.failed.CitiesFetchingFailureListener;
import models.listeners.successful.BudgetCreationSuccessListener;
import models.listeners.successful.BudgetSearchSuccessListener;
import models.listeners.failed.BudgetSearchFailureListener;
import models.listeners.failed.BudgetCreationFailureListener;
import models.listeners.successful.CitiesFetchingSuccessListener;
import utils.Budget;
import utils.Client;
import utils.Product;

import java.util.ArrayList;
import java.util.Map;

public interface IBudgetModel {
    void createBudget(String budgetName, String budgetDate, String budgetClientType, int budgetNumber);

    void addBudgetCreationSuccessListener(BudgetCreationSuccessListener listener);
    void addBudgetCreationFailureListener(BudgetCreationFailureListener listener);
    void addBudgetSearchSuccessListener(BudgetSearchSuccessListener listener);
    void addBudgetSearchFailureListener(BudgetSearchFailureListener listener);
    void queryBudgets(String budgetSearch);
    ArrayList<Budget> getLastBudgetsQuery();
    ArrayList<String> getProductNamesByCategory(String category);
    ArrayList<Product> getProducts(String productName);
    ArrayList<String> getCitiesName();
    ArrayList<Client> getClients(String name, String city);
    int getBudgetID(String budgetName);
    void deleteOneBudget(int budgetID);
    void deleteMultipleBudgets(ArrayList<Integer> budgetIDs);
    void saveProducts(String budgetName, Map<Integer,String> products);
    Map<Integer,String> getSavedProducts(String budgetName);
}
