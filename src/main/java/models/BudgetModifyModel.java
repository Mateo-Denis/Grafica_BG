package models;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import utils.databases.BudgetsDatabaseConnection;

import java.util.ArrayList;

public class BudgetModifyModel implements IBudgetModifyModel {
    private final BudgetsDatabaseConnection budgetsDBConnection;


    public BudgetModifyModel(BudgetsDatabaseConnection budgetsDBConnection) {
        this.budgetsDBConnection = budgetsDBConnection;
    }

    @Override
    public void updateBudget(String oldClientName, String newClientName, String date, String clientType, int budgetNumber, Multimap<Integer,String> products, ArrayList<String> productObservations, ArrayList<String> productMeasures) {
        try {
            budgetsDBConnection.updateBudgetTable(newClientName, date, clientType, budgetNumber);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public ArrayList<String> getProductObservations(int budgetNumber, String budgetName) {
        try {
            return budgetsDBConnection.getProductObservations(budgetName, budgetNumber);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    @Override
    public ArrayList<String> getProductMeasures(int budgetNumber, String budgetName) {
        try {
            return budgetsDBConnection.getProductMeasures(budgetName, budgetNumber);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    public Multimap<Integer,String> getSavedProducts(int budgetNumber, String budgetName) {
        try {
            return budgetsDBConnection.getSavedProducts(budgetName, budgetNumber);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ArrayListMultimap.create();
    }

    public ArrayList<String> getSelectedBudgetData(int budgetNumber) {
        try {
            return budgetsDBConnection.getSelectedBudgetData(budgetNumber);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    @Override
    public String getOldClientName(int budgetNumber) {
        return budgetsDBConnection.getOldClientName(budgetNumber);
    }

    @Override
    public void deleteFromBudgetProductsTable(int oldBudgetID, String clientName, int budgetNumber, boolean updating) {
        try {
            budgetsDBConnection.deleteBudgetProducts(oldBudgetID, clientName, budgetNumber, updating);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
