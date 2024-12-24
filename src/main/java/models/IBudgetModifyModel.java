package models;

import com.google.common.collect.Multimap;

import java.util.ArrayList;

public interface IBudgetModifyModel {
    void updateBudget(String oldClientName, String clientName, String date, String clientType, int budgetNumber, Multimap<Integer,String> products, ArrayList<String> productObservations, ArrayList<String> productMeasures);
    ArrayList<String> getProductMeasures(int budgetNumber, String budgetName);
    ArrayList<String> getProductObservations(int budgetNumber, String budgetName);
    ArrayList<String> getSavedProductNames(int budgetNumber, String budgetName);
    ArrayList<Integer> getSavedProductAmounts(int budgetNumber, String budgetName);
    ArrayList<String> getSelectedBudgetData(int budgetNumber);
    String getOldClientName(int budgetNumber);
    ArrayList<Double> getProductPrices(int budgetNumber, String budgetName);
}