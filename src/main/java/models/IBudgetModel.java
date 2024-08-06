package models;

import models.listeners.successful.BudgetCreationSuccessListener;
import models.listeners.successful.BudgetSearchSuccessListener;
import models.listeners.failed.BudgetSearchFailureListener;
import models.listeners.failed.BudgetCreationFailureListener;
import utils.Budget;

import java.util.ArrayList;

public interface IBudgetModel {
    void createBudget(String budgetName, String budgetDate, String budgetClientType, int budgetNumber);

    void addBudgetCreationSuccessListener(BudgetCreationSuccessListener listener);
    void addBudgetCreationFailureListener(BudgetCreationFailureListener listener);
    void addBudgetSearchSuccessListener(BudgetSearchSuccessListener listener);
    void addBudgetSearchFailureListener(BudgetSearchFailureListener listener);
    void queryBudgets(String budgetSearch);
    ArrayList<Budget> getLastBudgetsQuery();
}
