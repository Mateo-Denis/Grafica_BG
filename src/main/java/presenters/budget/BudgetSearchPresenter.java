package presenters.budget;

import static utils.MessageTypes.*;
import models.IBudgetModel;
import presenters.StandardPresenter;
import utils.Budget;
import views.budget.IBudgetCreateView;
import views.budget.IBudgetSearchView;

import java.util.ArrayList;


// BudgetSearchPresenter CLASS
public class BudgetSearchPresenter extends StandardPresenter {
    private final IBudgetSearchView budgetSearchView;
    private final IBudgetModel budgetModel;
    private final IBudgetCreateView budgetCreateView;
    private BudgetCreatePresenter budgetCreatePresenter;

    // CONSTRUCTOR
    public BudgetSearchPresenter(IBudgetSearchView budgetSearchView, IBudgetCreateView budgetCreateView, IBudgetModel budgetModel) {
        this.budgetSearchView = budgetSearchView;
        this.budgetCreateView = budgetCreateView;
        view = budgetSearchView;
        this.budgetModel = budgetModel;
    }




    // ---------> METHODS AND FUNCTIONS START HERE <-------------
    // ---------> METHODS AND FUNCTIONS START HERE <-------------




    //  LISTENERS
    @Override
    protected void initListeners() {
        budgetModel.addBudgetSearchSuccessListener(() -> {
            ArrayList<Budget> budgets = budgetModel.getLastBudgetsQuery();
            int rowCount = 0;
            for (Budget budget : budgets) {
                budgetSearchView.setStringTableValueAt(rowCount, 0, budget.getName());
                budgetSearchView.setStringTableValueAt(rowCount, 1, budget.getDate());
                budgetSearchView.setStringTableValueAt(rowCount, 2, budget.getClientType());
                budgetSearchView.setStringTableValueAt(rowCount, 3, budget.getBudgetNumber());
                rowCount++;
            }
        });

        budgetModel.addBudgetSearchFailureListener(() -> budgetSearchView.showMessage(BUDGET_SEARCH_FAILURE));
    }




    public void onSearchButtonClicked() {
        budgetSearchView.setWorkingStatus();
        String budgetSearch = budgetSearchView.getSearchText();
        budgetSearchView.clearTable();
        budgetModel.queryBudgets(budgetSearch);
        budgetSearchView.setWaitingStatus();
    }




    public void onCleanTableButtonClicked() {
        budgetSearchView.clearTable();
    }




    public void onHomeSearchBudgetButtonClicked() {
        budgetSearchView.showView();
    }




    public void onDeleteButtonClicked() {
        int[] selectedRows = budgetSearchView.getBudgetResultTable().getSelectedRows();
        if (selectedRows.length == 1) {
            deleteOneBudget();
        } else {
            budgetSearchView.showMessage(BUDGET_DELETE_FAILURE);
        }
    }

    public void deleteOneBudget() {
        int budgetID = getOneBudgetID();
        System.out.println("Budget ID: " + budgetID);
        int budgetNumber = budgetSearchView.getSelectedBudgetNumber();
        System.out.println("Budget Number: " + budgetNumber);
        String budgetName = budgetSearchView.getSelectedBudgetName();
        System.out.println("Budget Name: " + budgetName);

        if (budgetNumber != 0 && budgetID != -1 && budgetID != 0) {
            budgetModel.deleteOneBudget(budgetID);
            budgetModel.deleteBudgetProducts(budgetName, budgetID, budgetNumber);
            budgetSearchView.setWorkingStatus();
            budgetSearchView.clearTable();
            String budgetSearch = budgetSearchView.getSearchText();
            budgetModel.queryBudgets(budgetSearch);
            budgetSearchView.deselectAllRows();
            budgetSearchView.setWaitingStatus();
        } else {
            budgetSearchView.showMessage(BUDGET_DELETE_FAILURE);
        }
    }




    public int getOneBudgetID() {
        int selectedRow = budgetSearchView.getBudgetResultTable().getSelectedRow();
        String selectedBudgetName = "";
        int selectedBudgetNumber = -1;
        int budgetId = -1;

        if (selectedRow != -1) {
            selectedBudgetName = (String) budgetSearchView.getSelectedBudgetName();
            if(selectedBudgetName != null && !selectedBudgetName.isEmpty()) {
                selectedBudgetNumber = Integer.parseInt(budgetSearchView.getStringValueAt(selectedRow, 3));
                budgetId = budgetModel.getBudgetID(selectedBudgetNumber, selectedBudgetName);
            }
        }
        return budgetId;
    }
}
