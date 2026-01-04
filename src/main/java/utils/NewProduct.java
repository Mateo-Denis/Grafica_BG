package utils;

import lombok.Getter;

public class NewProduct {
    @Getter
    private final String description;
    @Getter
    private final double total;

    public NewProduct(String description, double total) {
        this.description = description;
        this.total = total;
    }
}
