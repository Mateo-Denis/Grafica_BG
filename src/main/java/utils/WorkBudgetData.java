package utils;

import lombok.Getter;
import org.javatuples.Pair;

import java.util.ArrayList;

public class WorkBudgetData {
	@Getter
	private final int id;
	@Getter
	private final int budgetNumber;
	@Getter
	private int clientID;
	@Getter
	private ArrayList<Pair<String, String>> materials;
	@Getter
	private ArrayList<Pair<String, String>> descriptions;
	@Getter
	private String logistics;
	@Getter
	private String logisticsCost;
	@Getter
	private String placer;
	@Getter
	private String placingCost;
	@Getter
	private String profit;

	public WorkBudgetData(
			int id,
			int budgetNumber,
			int clientID,
			ArrayList<Pair<String, String>> materials,
			ArrayList<Pair<String, String>> descriptions,
			String logistics,
			String logisticsCost,
			String placer,
			String placingCost,
			String profit
	) {
		this.id = id;
		this.budgetNumber = budgetNumber;
		this.clientID = clientID;
		this.materials = materials;
		this.descriptions = descriptions;
		this.logistics = logistics;
		this.logisticsCost = logisticsCost;
		this.placer = placer;
		this.placingCost = placingCost;
		this.profit = profit;
	}
}
