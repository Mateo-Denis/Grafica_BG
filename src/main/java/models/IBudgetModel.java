package models;
import com.google.common.collect.Multimap;
import models.listeners.failed.*;
import models.listeners.successful.*;
import utils.Budget;
import utils.Client;
import utils.Product;

import java.util.ArrayList;

public interface IBudgetModel {
    void addBudgetCreationSuccessListener(BudgetCreationSuccessListener listener);
    void addBudgetCreationFailureListener(BudgetCreationFailureListener listener);
    void addBudgetSearchSuccessListener(BudgetSearchSuccessListener listener);
    void addBudgetSearchFailureListener(BudgetSearchFailureListener listener);

    Client GetOneClientByID(int clientID);
    ArrayList<Product> getProducts(String productName, String productCategory);
    ArrayList<Client> getClients(String name, String city);
    int getClientID(String clientName, String clientType);
    void createBudget(String budgetName, String budgetDate, String budgetClientType, int budgetNumber);
    int getNextBudgetNumber();
    void saveProducts(int budgetID, Multimap<Integer,String> products, ArrayList<String> observations, ArrayList<String> productMeasures, ArrayList<Double> productPrices);
    int getBudgetID(int budgetNumber, String budgetName);
    ArrayList<String> getCitiesName();
    ArrayList<Budget> getLastBudgetsQuery();
    void queryBudgets(String budgetSearch);
    void deleteOneBudget(int budgetID);
    void deleteBudgetProducts(String budgetName, int budgetID, int budgetNumber);
    int getMaxBudgetID();

}
