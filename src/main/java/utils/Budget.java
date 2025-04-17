package utils;

import lombok.Getter;

public class Budget {
    @Getter
    private final String name;
    @Getter
    private final String date;
    @Getter
    private final String clientType;
    private final int budgetNumber;

    public Budget(String name, String date, String clientType, int budgetNumber) {
        this.name = name;
        this.date = date;
        this.clientType = clientType;
        this.budgetNumber = budgetNumber;
    }

    public String getBudgetNumber() {
        return String.valueOf(budgetNumber);
    }
}
