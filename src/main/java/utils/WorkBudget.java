package utils;

import lombok.Getter;

public class WorkBudget {
	@Getter
	private final String name;
	@Getter
	private final String date;
	@Getter
	private final String clientType;
	private final int budgetNumber;

	public WorkBudget(String name, String date, String clientType, int budgetNumber) {
		this.name = name;
		this.date = date;
		this.clientType = clientType;
		this.budgetNumber = budgetNumber;
	}

	public String getWorkBudgetNumber() {
		return String.valueOf(budgetNumber);
	}
}
