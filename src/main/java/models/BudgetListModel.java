package models;

import utils.Budget;
import utils.databases.BudgetsDatabaseConnection;
import java.util.ArrayList;

public class BudgetListModel implements IBudgetListModel {
    private final BudgetsDatabaseConnection budgetsDBConnection;
    private ArrayList<Budget> budgets;

    public BudgetListModel(BudgetsDatabaseConnection budgetsDBConnection) {
        this.budgetsDBConnection = budgetsDBConnection;
        budgets = new ArrayList<>();
    }

    @Override
    public ArrayList<Budget> getBudgetsFromDB() {
        return budgetsDBConnection.getAllBudgets();
    }
}