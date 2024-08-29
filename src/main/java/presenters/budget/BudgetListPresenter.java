package presenters.budget;

import models.IBudgetListModel;
import presenters.StandardPresenter;
import utils.Budget;
import utils.databases.BudgetsDatabaseConnection;
import views.budget.IBudgetSearchView;
import views.budget.list.IBudgetListView;

import javax.swing.*;
import java.util.ArrayList;


public class BudgetListPresenter extends StandardPresenter{
    private final IBudgetListModel budgetListModel;
    private final IBudgetListView budgetListView;
    private IBudgetSearchView budgetSearchView;
    private BudgetsDatabaseConnection budgetsDatabaseConnection;

    public BudgetListPresenter(IBudgetListView budgetListView, IBudgetListModel budgetListModel) {
        this.budgetListView = budgetListView;
        view = budgetListView;
        this.budgetListModel = budgetListModel;
        budgetsDatabaseConnection = new BudgetsDatabaseConnection();
    }


    @Override
    protected void initListeners() {

    }

    public void onSearchViewOpenListButtonClicked() {
        ArrayList<Budget> budgets = budgetListModel.getBudgetsFromDB();
        if (budgets.isEmpty()) {
            JOptionPane.showMessageDialog(null, "No hay presupuestos en la base de datos");
        } else {
            budgetListView.showView();
            budgetListView.setWorkingStatus();
            budgetListView.clearView();
            setBudgetsOnTable();
            budgetListView.setWaitingStatus();
        }
    }

    public void setBudgetsOnTable() {
        ArrayList<Budget> budgets = budgetListModel.getBudgetsFromDB();
        int rowCount = 0;
        int budgetID = 0;

        for (Budget budget : budgets) {
            try {
                budgetID = budgetsDatabaseConnection.getBudgetID(budget.getName());
                System.out.println(budget.getName());
            } catch (Exception e) {
                e.printStackTrace();
            }

            budgetListView.setIntTableValueAt(rowCount, 0, budgetID);
            budgetListView.setStringTableValueAt(rowCount, 1, budget.getName());
            budgetListView.setStringTableValueAt(rowCount, 2, budget.getDate());
            budgetListView.setStringTableValueAt(rowCount, 3, budget.getClientType());
            budgetListView.setStringTableValueAt(rowCount, 4, budget.getBudgetNumber());
            rowCount++;
        }
    }
}
