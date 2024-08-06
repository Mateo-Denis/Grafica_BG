package utils;

public class Budget {
    private String name;
    private String date;
    private String clientType;
    private int budgetNumber;

    public Budget(String name, String date, String clientType, int budgetNumber) {
        this.name = name;
        this.date = date;
        this.clientType = clientType;
        this.budgetNumber = budgetNumber;
    }

    public String getName() {
        return name;
    }

    public String getDate() {
        return date;
    }

    public String getClientType() {
        return clientType;
    }

    public String getBudgetNumber() {
        return String.valueOf(budgetNumber);
    }
}
