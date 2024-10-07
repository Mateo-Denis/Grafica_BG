package models;

import com.google.common.collect.Multimap;
import models.listeners.successful.BudgetCreationSuccessListener;
import models.listeners.successful.BudgetSearchSuccessListener;
import models.listeners.failed.BudgetSearchFailureListener;
import models.listeners.failed.BudgetCreationFailureListener;
import utils.Budget;
import utils.Client;
import utils.Product;

import java.util.ArrayList;

public interface IBudgetModel {
    void createBudget(String budgetName, String budgetDate, String budgetClientType, int budgetNumber);

    void addBudgetCreationSuccessListener(BudgetCreationSuccessListener listener);
    void addBudgetCreationFailureListener(BudgetCreationFailureListener listener);
    void addBudgetSearchSuccessListener(BudgetSearchSuccessListener listener);
    void addBudgetSearchFailureListener(BudgetSearchFailureListener listener);
    void queryBudgets(String budgetSearch);
    ArrayList<Budget> getLastBudgetsQuery();
    ArrayList<String> getProductNamesByCategory(String category);
    ArrayList<Product> getProducts(String productName, String productCategory);
    ArrayList<String> getCitiesName();
    ArrayList<Client> getClients(String name, String city);
    int getBudgetID(int budgetNumber, String budgetName);
    void deleteOneBudget(int budgetID);
    void deleteMultipleBudgets(ArrayList<Integer> budgetIDs);
    void saveProducts(int budgetID, Multimap<Integer, String> products, ArrayList<String> observations, ArrayList<String> productMeasures);
    void deleteBudgetProducts(String budgetName, int budgetNumber, boolean updating);
    ArrayList<Client> getOneClient(int clientID);
    int getClientID(String clientName);
    int getBudgetNumber(int budgetID);
    int getNextBudgetNumber();
    int getMaxBudgetID();
}
