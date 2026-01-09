package presenters.workbudget;

import models.WorkBudgetModel;
import presenters.StandardPresenter;
import utils.WorkBudget;
import views.workbudget.WorkBudgetSearchView;

import java.util.ArrayList;

import static utils.MessageTypes.BUDGET_SEARCH_FAILURE;

public class WorkBudgetSearchPresenter extends StandardPresenter {

	private final  WorkBudgetSearchView workBudgetSearchView;
	private final WorkBudgetModel workBudgetModel;

	public WorkBudgetSearchPresenter(WorkBudgetSearchView workBudgetSearchView, WorkBudgetModel workBudgetModel) {
		this.workBudgetSearchView = workBudgetSearchView;
		view = workBudgetSearchView;

		this.workBudgetModel = workBudgetModel;
	}

	@Override
	protected void initListeners() {
		workBudgetModel.addBudgetSearchSuccessListener(() -> {
			ArrayList<WorkBudget> budgets = workBudgetModel.getLastBudgetsQuery();
			int rowCount = 0;
			for (WorkBudget budget : budgets) {
				workBudgetSearchView.setStringTableValueAt(rowCount, 0, budget.getName());
				workBudgetSearchView.setStringTableValueAt(rowCount, 1, budget.getDate());
				workBudgetSearchView.setStringTableValueAt(rowCount, 2, budget.getWorkBudgetNumber());
				workBudgetSearchView.setStringTableValueAt(rowCount, 3, budget.getFinalPrice());
				rowCount++;
			}
		});

		workBudgetModel.addBudgetSearchFailureListener(() -> workBudgetSearchView.showMessage(BUDGET_SEARCH_FAILURE));
	}

	public void onSearchButtonClicked() {
		workBudgetSearchView.setWorkingStatus();
		String budgetSearch = workBudgetSearchView.getSearchText();
		workBudgetSearchView.clearTable();
		workBudgetModel.queryBudgets(budgetSearch);
		workBudgetSearchView.setWaitingStatus();
	}

	public void onHomeSearchWorkBudgetButtonClicked(){
		workBudgetSearchView.showView();
	}
}
