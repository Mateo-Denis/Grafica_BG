package models;
import utils.databases.BudgetsDatabaseConnection;
import java.util.ArrayList;

public class BudgetModifyModel implements IBudgetModifyModel {
    private final BudgetsDatabaseConnection budgetsDBConnection;


    public BudgetModifyModel(BudgetsDatabaseConnection budgetsDBConnection) {
        this.budgetsDBConnection = budgetsDBConnection;
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