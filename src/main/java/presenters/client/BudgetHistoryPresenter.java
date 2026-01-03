package presenters.client;

import models.IBudgetHistoryModel;
import presenters.StandardPresenter;
import utils.Budget;
import utils.PDFOpener;
import views.client.IClientSearchView;
import views.client.BudgetHistory.IBudgetHistoryView;

import java.util.ArrayList;

public class BudgetHistoryPresenter extends StandardPresenter {
    private final IBudgetHistoryView budgetHistoryView;
    private final IClientSearchView clientSearchView;
    private final IBudgetHistoryModel budgetHistoryModel;
    private final PDFOpener pdfOpener = new PDFOpener();

    public BudgetHistoryPresenter(IBudgetHistoryModel budgetHistoryModel, IBudgetHistoryView budgetHistoryView, IClientSearchView clientSearchView) {
        this.budgetHistoryView = budgetHistoryView;
        this.clientSearchView = clientSearchView;
        this.budgetHistoryModel = budgetHistoryModel;

        view = budgetHistoryView;
    }

    public void start() {
        super.start();
    }

    public double getBudgetTotal(int budgetNumber, String clientName) {
        return budgetHistoryModel.getBudgetTotal(budgetNumber, clientName);
    }

    public void setBudgetHistoryTable() {
        ArrayList<Budget> budgets = getClientBudgets();
        budgetHistoryView.clearView();
        int rowCount = 0;
        double budgetTotal = 0.0;
        budgetHistoryView.setClientName(budgets.get(0).getName());

        for (Budget budget : budgets) {
            budgetTotal = getBudgetTotal(Integer.parseInt(budget.getBudgetNumber()), budget.getName());


            budgetHistoryView.setTableValueAt(rowCount, 0, String.valueOf(budget.getName()));
            budgetHistoryView.setTableValueAt(rowCount, 1, budget.getBudgetNumber());
            budgetHistoryView.setTableValueAt(rowCount, 2, budget.getDate());
            budgetHistoryView.setTableValueAt(rowCount, 3, String.valueOf(budgetTotal));
            rowCount++;
        }
    }

    public void onDoubleClickBudget() {
        int selectedBudgetRow = budgetHistoryView.getBudgetHistoryTable().getSelectedRow();
        if(selectedBudgetRow != -1 && budgetHistoryView.getBudgetHistoryTable().getValueAt(selectedBudgetRow, 1) != null) {
            openBudgetPDF(selectedBudgetRow);
        }
    }

    public void openBudgetPDF(int selectedBudgetRow) {

        int budgetNumber = Integer.parseInt((String) budgetHistoryView.getBudgetHistoryTable().getValueAt(selectedBudgetRow, 1));
        String clientName = (String) budgetHistoryView.getBudgetHistoryTable().getValueAt(selectedBudgetRow, 0);
        int budgetId = budgetHistoryModel.getBudgetID(clientName, budgetNumber);
        Budget budget = budgetHistoryModel.getOneBudget(budgetId);
        String budgetDate = budget.getDate();


        pdfOpener.openPDF(budgetNumber, clientName, budgetDate);
    }

    public ArrayList<Budget> getClientBudgets() {
        int selectedClientID = clientSearchView.getSelectedClientID();
        return budgetHistoryModel.getClientBudgets(selectedClientID);
    }

    @Override
    protected void initListeners() {
    }
}
