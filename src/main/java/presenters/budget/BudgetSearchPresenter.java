package presenters.budget;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import presenters.StandardPresenter;
import models.IBudgetModel;
import views.budget.BudgetCreateView;
import views.budget.IBudgetSearchView;
import views.budget.IBudgetCreateView;
import utils.Budget;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

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
        } else if (selectedRows.length > 1) {
            deleteMultipleBudgets();
        }
    }

    public void deleteOneBudget() {
        int budgetID = getOneBudgetID();
        if (budgetID != -1 && budgetID != 0) {
            budgetModel.deleteOneBudget(budgetID);
            budgetModel.deleteBudgetProducts(budgetSearchView.getSelectedBudgetName(), budgetSearchView.getSelectedBudgetNumber(), false);
            budgetSearchView.setWorkingStatus();
            budgetSearchView.clearTable();
            String budgetSearch = budgetSearchView.getSearchText();
            budgetModel.queryBudgets(budgetSearch);
            budgetSearchView.deselectAllRows();
            budgetSearchView.setWaitingStatus();
        }
    }

    public void deleteMultipleBudgets() {
        ArrayList<Integer> budgetIDs = new ArrayList<>();
        ArrayList<Integer> selectedBudgetNumbers = budgetSearchView.getMultipleSelectedBudgetNumbers();
        ArrayList<String> selectedBudgetNames = budgetSearchView.getMultipleSelectedBudgetNames();
        int selectedRows[] = budgetSearchView.getBudgetResultTable().getSelectedRows();
        for (int i = 0; i < selectedRows.length; i++) {
            System.out.println("Selected budget: " + selectedBudgetNames.get(i));
            String budgetName = selectedBudgetNames.get(i);
            int budgetNumber = selectedBudgetNumbers.get(i);
            int budgetID = budgetModel.getBudgetID(budgetNumber, budgetName);
            budgetIDs.add(budgetID);
            budgetModel.deleteBudgetProducts(budgetName, budgetNumber, false);
        }
        budgetModel.deleteMultipleBudgets(budgetIDs);
        budgetSearchView.setWorkingStatus();
        budgetSearchView.clearTable();
        String budgetSearch = budgetSearchView.getSearchText();
        budgetModel.queryBudgets(budgetSearch);
        budgetSearchView.deselectAllRows();
        budgetSearchView.setWaitingStatus();
    }

    public int getOneBudgetID() {
        int selectedRow = budgetSearchView.getBudgetResultTable().getSelectedRow();
        if (selectedRow != -1) {
            String selectedBudgetName = (String) budgetSearchView.getSelectedBudgetName();
            int selectedBudgetNumber = budgetSearchView.getSelectedBudgetNumber();
            return budgetModel.getBudgetID(selectedBudgetNumber, selectedBudgetName);
        }
        return -1;
    }
}
