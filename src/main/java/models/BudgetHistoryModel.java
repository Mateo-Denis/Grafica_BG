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

    public Budget getOneBudget(int budgetId) {
        try {
            return this.budgetsDatabaseConnection.getOneBudget(budgetId);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public int getBudgetID(String clientName, int budgetNumber) {
        try {
            return this.budgetsDatabaseConnection.getBudgetID(clientName, budgetNumber);
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
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
