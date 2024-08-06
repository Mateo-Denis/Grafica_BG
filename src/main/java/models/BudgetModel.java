package models;

import models.listeners.failed.BudgetCreationFailureListener;
import models.listeners.failed.BudgetSearchFailureListener;
import models.listeners.successful.BudgetCreationSuccessListener;
import models.listeners.successful.BudgetSearchSuccessListener;
import utils.Budget;
import utils.databases.BudgetsDatabaseConnection;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class BudgetModel implements IBudgetModel {
    private final BudgetsDatabaseConnection dbConnection;
    private final List<BudgetCreationSuccessListener> budgetCreationSuccessListeners;
    private final List<BudgetCreationFailureListener> budgetCreationFailureListeners;
    private final List<BudgetSearchSuccessListener> budgetSearchSuccessListeners;
    private final List<BudgetSearchFailureListener> budgetSearchFailureListeners;

    private ArrayList<Budget> budgets;

    public BudgetModel(BudgetsDatabaseConnection dbConnection) {
        this.dbConnection = dbConnection;
        budgets = new ArrayList<>();

        this.budgetCreationSuccessListeners = new LinkedList<>();
        this.budgetCreationFailureListeners = new LinkedList<>();

        this.budgetSearchSuccessListeners = new LinkedList<>();
        this.budgetSearchFailureListeners = new LinkedList<>();
    }

    @Override
    public void createBudget(String budgetName, String budgetDate, String budgetClientType, int budgetNumber) {
        try {
            dbConnection.insertBudget(budgetName, budgetDate, budgetClientType, budgetNumber);
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
            budgets = dbConnection.getBudgets(budgetSearch);
            notifyBudgetSearchSuccess();
        } catch (Exception e) {
            notifyBudgetSearchFailure();
        }
    }*/

    @Override
    public void queryBudgets(String budgetSearch) {
        try {
            budgets = dbConnection.getBudgets(budgetSearch);
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
}
