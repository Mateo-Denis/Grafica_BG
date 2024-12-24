package models;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import utils.databases.BudgetsDatabaseConnection;

import java.lang.reflect.Array;
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

    public ArrayList<Double> getProductPrices(int budgetNumber, String budgetName) {
        try {
            return budgetsDBConnection.getProductPrices(budgetName, budgetNumber);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
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

    public ArrayList<String> getSavedProductNames(int budgetNumber, String budgetName) {
        try {
            return budgetsDBConnection.getSavedProductNames(budgetName, budgetNumber);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    public ArrayList<Integer> getSavedProductAmounts(int budgetNumber, String budgetName) {
        try {
            return budgetsDBConnection.getSavedProductAmounts(budgetName, budgetNumber);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
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
}