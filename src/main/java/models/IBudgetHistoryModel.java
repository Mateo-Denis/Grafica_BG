package models;

import utils.Budget;

import java.util.ArrayList;

public interface IBudgetHistoryModel {
    ArrayList<Budget> getClientBudgets(int clientId);
    double getBudgetTotal(int budgetNumber, String clientName);
}
