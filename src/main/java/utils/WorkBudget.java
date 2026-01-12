package utils;

import lombok.Getter;

public class WorkBudget {
	@Getter
	private final int id;
	@Getter
	private final int clientID;
	@Getter
	private final String name;
	@Getter
	private final String date;
	@Getter
	private final String finalPrice;
	private final int budgetNumber;

	public WorkBudget(int id, int clientID, String name, String date, String finalPrice, int budgetNumber) {
		this.id = id;
		this.clientID = clientID;
		this.name = name;
		this.date = date;
		this.finalPrice = finalPrice;
		this.budgetNumber = budgetNumber;
	}

	public String getWorkBudgetNumber() {
		return String.valueOf(budgetNumber);
	}
}
