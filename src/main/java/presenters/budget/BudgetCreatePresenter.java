package presenters.budget;


import presenters.StandardPresenter;
import models.IBudgetModel;
import utils.Budget;
import views.budget.*;
import java.util.ArrayList;

import static utils.MessageTypes.*;

public class BudgetCreatePresenter extends StandardPresenter {
    private final IBudgetCreateView budgetCreateView;
    private final IBudgetModel budgetModel;

    public BudgetCreatePresenter(IBudgetCreateView budgetCreateView, IBudgetModel budgetModel){
        this.budgetCreateView = budgetCreateView;
        view = budgetCreateView;
        this.budgetModel = budgetModel;
    }

    @Override
    protected void initListeners() {
        budgetModel.addBudgetCreationFailureListener(() -> budgetCreateView.showMessage(BUDGET_CREATION_SUCCESS));
        budgetModel.addBudgetCreationFailureListener(() -> budgetCreateView.showMessage(BUDGET_CREATION_FAILURE));

        budgetModel.addBudgetSearchSuccessListener(() -> {
            ArrayList<Budget> budgets = budgetModel.getLastBudgetsQuery();
            int rowCount = 0;
            for (Budget budget : budgets) {
                budgetCreateView.setStringTableValueAt(rowCount, 0, budget.getName());
                budgetCreateView.setStringTableValueAt(rowCount, 1, budget.getDate());
                budgetCreateView.setStringTableValueAt(rowCount, 2, budget.getClientType());
                budgetCreateView.setStringTableValueAt(rowCount, 3, budget.getBudgetNumber());
                rowCount++;
            }
        });
    }

    public void onHomeCreateBudgetButtonClicked() {
        budgetCreateView.showView();
    }

    public void onCreateButtonClicked() {
        budgetCreateView.setWorkingStatus();

        budgetModel.createBudget(
                budgetCreateView.getBudgetClientName(),
                budgetCreateView.getBudgetDate(),
                budgetCreateView.getBudgetClientType(),
                budgetCreateView.getBudgetNumber());
        budgetCreateView.setWaitingStatus();
    }
}
