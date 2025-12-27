package utils;

import lombok.Getter;

import static utils.TextUtils.truncateAndRound;

public class CuttingService {
    @Getter
    private final String description;
    @Getter
    private final double materialCost;
    @Getter
    private final double linealMeters;
    @Getter
    private final double profit;
    @Getter
    private final int amount;
    @Getter
    private final double total;

    public CuttingService(String description, double materialCost, double linealMeters, double profit, int amount, double total) {
        this.description = description;
        this.materialCost = materialCost;
        this.linealMeters = linealMeters;
        this.profit = profit;
        this.amount = amount;
        this.total = Double.parseDouble(truncateAndRound(String.valueOf(total)));
    }
}
