package presenters.budget;

import presenters.StandardPresenter;
import models.IBudgetModel;
import views.budget.IBudgetSearchView;
import views.budget.IBudgetCreateView;
import utils.Budget;

import java.util.ArrayList;

//IMPORT DE SHAREDMODEL PARA EL UPDATE DE COMBOBOX

import static utils.MessageTypes.*;

public class BudgetSearchPresenter extends StandardPresenter {
    private final IBudgetSearchView budgetSearchView;
    private final IBudgetModel budgetModel;
    private final IBudgetCreateView budgetCreateView;
    private BudgetCreatePresenter budgetCreatePresenter;

    public BudgetSearchPresenter(IBudgetSearchView budgetSearchView, IBudgetCreateView budgetCreateView, IBudgetModel budgetModel) {
        this.budgetSearchView = budgetSearchView;
        this.budgetCreateView = budgetCreateView;
        view = budgetSearchView;
        this.budgetModel = budgetModel;
    }

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
        int budgetNumber = budgetSearchView.getSelectedBudgetNumber();
        String budgetName = budgetSearchView.getSelectedBudgetName();
        if (budgetNumber != 0 && budgetID != -1 && budgetID != 0) {
            budgetModel.deleteOneBudget(budgetID);
            budgetModel.deleteBudgetProducts(budgetName, budgetNumber, budgetID, false);
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
        int selectedBudgetNumber = budgetSearchView.getSelectedBudgetNumber();
        if (selectedRow != -1 && selectedBudgetNumber != 0) {
            String selectedBudgetName = (String) budgetSearchView.getSelectedBudgetName();
            return budgetModel.getBudgetID(selectedBudgetNumber, selectedBudgetName);
        }
        return -1;
    }
}
