package models;

import utils.Budget;
import utils.Client;
import utils.databases.BudgetsDatabaseConnection;
import utils.databases.ClientsDatabaseConnection;

import java.util.ArrayList;

public class BudgetHistoryModel implements IBudgetHistoryModel {
    private final BudgetsDatabaseConnection budgetsDatabaseConnection;
    private final ClientsDatabaseConnection clientsDatabaseConnection;

    private ArrayList<Budget> budgets;
    private ArrayList<Client> clientNames;

    public BudgetHistoryModel(BudgetsDatabaseConnection budgetsDatabaseConnection,
                              ClientsDatabaseConnection clientsDatabaseConnection) {
        this.budgetsDatabaseConnection = budgetsDatabaseConnection;
        this.clientsDatabaseConnection = clientsDatabaseConnection;
    }

    public ArrayList<Budget> getClientBudgets(int clientId) {
        try {
            this.budgets = this.budgetsDatabaseConnection.getBudgetsByClientId(clientId);
            return this.budgets;
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public double getBudgetTotal(int budgetNumber, String clientName) {
        try {
            int budgetId = this.budgetsDatabaseConnection.getBudgetID(clientName, budgetNumber);
            return this.budgetsDatabaseConnection.getBudgetTotalPrice(budgetId);
        } catch (Exception e) {
            e.printStackTrace();
            return 0.0;
        }
    }
}
