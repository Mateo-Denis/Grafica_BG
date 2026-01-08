package presenters.workbudget;

import models.WorkBudgetModel;
import presenters.StandardPresenter;
import views.workbudget.WorkBudgetSearchView;

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

	}

	public void onSearchButtonClicked() {
		workBudgetSearchView.setWorkingStatus();
		String budgetSearch = workBudgetSearchView.getSearchText();
		workBudgetSearchView.clearTable();
		workBudgetModel.queryBudgets(budgetSearch);
		workBudgetSearchView.setWaitingStatus();
	}


}
