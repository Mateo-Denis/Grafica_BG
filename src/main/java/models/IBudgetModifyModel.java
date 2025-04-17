package models;

import java.util.ArrayList;

public interface IBudgetModifyModel {
    ArrayList<String> getProductMeasures(int budgetNumber, String budgetName);
    ArrayList<String> getProductObservations(int budgetNumber, String budgetName);
    ArrayList<String> getSavedProductNames(int budgetNumber, String budgetName);
    ArrayList<Integer> getSavedProductAmounts(int budgetNumber, String budgetName);
    ArrayList<String> getSelectedBudgetData(int budgetNumber);
    String getOldClientName(int budgetNumber);
    ArrayList<Double> getProductPrices(int budgetNumber, String budgetName);
}