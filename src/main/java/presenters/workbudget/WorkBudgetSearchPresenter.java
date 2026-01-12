package presenters.workbudget;

import models.WorkBudgetModel;
import presenters.StandardPresenter;
import utils.WorkBudget;
import views.workbudget.WorkBudgetCreateView;
import views.workbudget.WorkBudgetSearchView;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import static utils.MessageTypes.*;

public class WorkBudgetSearchPresenter extends StandardPresenter {

	private final WorkBudgetSearchView workBudgetSearchView;
	private final WorkBudgetCreateView workBudgetCreateView;
	private final WorkBudgetModel workBudgetModel;

	public WorkBudgetSearchPresenter(WorkBudgetSearchView workBudgetSearchView, WorkBudgetCreateView workBudgetCreateView, WorkBudgetModel workBudgetModel) {
		this.workBudgetSearchView = workBudgetSearchView;
		this.workBudgetCreateView = workBudgetCreateView;
		view = workBudgetSearchView;

		this.workBudgetModel = workBudgetModel;
	}

	@Override
	protected void initListeners() {
		workBudgetModel.addBudgetSearchSuccessListener(() -> {
			ArrayList<WorkBudget> budgets = workBudgetModel.getLastBudgetsQuery();
			int rowCount = 0;
			for (WorkBudget budget : budgets) {
				workBudgetSearchView.setStringTableValueAt(rowCount, 0, Integer.toString(budget.getClientID()));
				workBudgetSearchView.setStringTableValueAt(rowCount, 1, budget.getName());
				workBudgetSearchView.setStringTableValueAt(rowCount, 2, budget.getDate());
				workBudgetSearchView.setStringTableValueAt(rowCount, 3, budget.getWorkBudgetNumber());
				workBudgetSearchView.setStringTableValueAt(rowCount, 4, budget.getFinalPrice());
				rowCount++;
			}
		});

		workBudgetModel.addBudgetSearchFailureListener(() -> workBudgetSearchView.showMessage(BUDGET_SEARCH_FAILURE));

		workBudgetModel.addBudgetUpdateSuccessListener(workBudgetSearchView::clearTable);
	}

	public void onSearchButtonClicked() {
		workBudgetSearchView.setWorkingStatus();
		String budgetSearch = workBudgetSearchView.getSearchText();
		workBudgetSearchView.clearTable();
		workBudgetModel.queryBudgets(budgetSearch);
		workBudgetSearchView.setWaitingStatus();
	}

	public void onModifyButtonClicked(){
		if(workBudgetSearchView.isTableSelected()){
			workBudgetCreateView.showView();
			workBudgetCreateView.clearView();
			workBudgetCreateView.loadExistingBudget(workBudgetSearchView.getSelectedBudgetNumber());
		}else {
			workBudgetSearchView.showMessage(NO_ROW_SELECTED_FOR_MODIFYING);
		}
	}

	public void onDeleteButtonClicked(){
		if(workBudgetSearchView.isTableSelected()) {
			int budgetNumber = workBudgetSearchView.getSelectedBudgetNumber();
			workBudgetModel.deleteWorkBudget(budgetNumber);
			workBudgetSearchView.clearTable();
			workBudgetModel.queryBudgets("");
		}else {
			workBudgetSearchView.showMessage(NO_ROW_SELECTED_FOR_DELETION);
		}
	}

	public void onDoubleClickBudget(){
		File pdf1 = new File("C:/path/to/file1.pdf");
		File pdf2 = new File("C:/path/to/file2.pdf");

		try {
			Desktop.getDesktop().open(pdf1);
			Desktop.getDesktop().open(pdf2);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public void onHomeSearchWorkBudgetButtonClicked(){
		workBudgetSearchView.showView();
		workBudgetSearchView.clearView();
	}
}
