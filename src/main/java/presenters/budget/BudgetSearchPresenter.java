package presenters.budget;

import presenters.StandardPresenter;
import models.IBudgetModel;
import views.budget.IBudgetSearchView;
import views.budget.IBudgetCreateView;
import utils.Budget;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

//IMPORT DE SHAREDMODEL PARA EL UPDATE DE COMBOBOX

import static utils.MessageTypes.*;

public class BudgetSearchPresenter extends StandardPresenter {
    private final IBudgetSearchView budgetSearchView;
    private final IBudgetModel budgetModel;
    private final IBudgetCreateView budgetCreateView;

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
        if(selectedRows.length == 1) {
            deleteOneBudget();
        } else if(selectedRows.length > 1) {
            deleteMultipleBudgets();
        }
    }

    public void deleteOneBudget() {
        int budgetID = getOneBudgetID();
        if (budgetID != -1 && budgetID != 0) {
            budgetModel.deleteOneBudget(budgetID);
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
            String budgetName = selectedBudgetNames.get(i);
            int budgetNumber = selectedBudgetNumbers.get(i);
            int budgetID = budgetModel.getBudgetID(budgetNumber, budgetName);
            budgetIDs.add(budgetID);
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

    public void onModifyButtonClicked() {
        int selectedRow = budgetSearchView.getBudgetResultTable().getSelectedRow();
        Map<Integer,String> savedProducts = new HashMap<>();
        ArrayList<String> productNames = new ArrayList<>();

        if (selectedRow != -1) {
            String selectedBudgetName = (String) budgetSearchView.getSelectedBudgetName();
            int selectedBudgetNumber = budgetSearchView.getSelectedBudgetNumber();
            savedProducts = budgetModel.getSavedProducts(selectedBudgetNumber, selectedBudgetName);
            for(Map.Entry<Integer,String> entry : savedProducts.entrySet()) {
                productNames.add(entry.getValue());
            }
            int selectedID = budgetModel.getBudgetID(selectedBudgetNumber, selectedBudgetName);
            setModifyView(budgetCreateView, selectedRow, productNames);
        }
    }

    public void setModifyView(IBudgetCreateView createView, int selectedBudgetRow, ArrayList<String> products) {
        String clientName = budgetSearchView.getStringValueAt(selectedBudgetRow, 0);
        String date = budgetSearchView.getStringValueAt(selectedBudgetRow, 1);
        String clientType = budgetSearchView.getStringValueAt(selectedBudgetRow, 2);
        int budgetNumber = Integer.parseInt(budgetSearchView.getStringValueAt(selectedBudgetRow, 3));

        createView.setPreviewStringTableValueAt(0, 0, clientName);
        createView.setPreviewStringTableValueAt(0, 2, date);

        for(int i = 0; i < products.size(); i++) {
            createView.setPreviewStringTableValueAt(i, 1, products.get(i));
        }

        createView.setPreviewStringTableValueAt(0, 3, clientType);
        createView.setPreviewIntTableValueAt(0, 4, budgetNumber);

        createView.showView();
    }

    public Map<Integer,String> getBudgetProducts(int budgetNumber, String budgetName) {
        return budgetModel.getSavedProducts(budgetNumber, budgetName);
    }
}
