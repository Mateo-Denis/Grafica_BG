package utils;

import lombok.Getter;

public class Product {
    private int ID;
    private String name;
    private int categoryID;

    public Product(String name, int categoryID) {
        this.name = name;
        this.categoryID = categoryID;
    }

    public String getName() {
        return name;
    }

    public int getCategoryID() {
        return categoryID;
    }
}
