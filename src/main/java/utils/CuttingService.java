package utils;

import lombok.Getter;

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
    @Getter
    private final double subTotal;

    public CuttingService(String description, double materialCost, double linealMeters, double profit, int amount, double subTotal, double total) {
        this.description = description;
        this.materialCost = materialCost;
        this.linealMeters = linealMeters;
        this.profit = profit;
        this.amount = amount;
        this.subTotal = subTotal;
        this.total = total;
    }
}
