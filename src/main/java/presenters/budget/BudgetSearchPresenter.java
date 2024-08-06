package presenters.budget;

import presenters.StandardPresenter;
import models.IBudgetModel;
import views.budget.IBudgetSearchView;
import utils.Budget;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;

//IMPORT DE SHAREDMODEL PARA EL UPDATE DE COMBOBOX
import utils.Product;

import static utils.MessageTypes.*;

public class BudgetSearchPresenter extends StandardPresenter {
    private final IBudgetSearchView budgetSearchView;
    private final IBudgetModel budgetModel;

    public BudgetSearchPresenter(IBudgetSearchView budgetSearchView, IBudgetModel budgetModel){
        this.budgetSearchView = budgetSearchView;
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

    public void onCleanTableButtonClicked(){
        budgetSearchView.clearTable();
    }

    public void onHomeSearchBudgetButtonClicked() {
        budgetSearchView.showView();
    }
}
