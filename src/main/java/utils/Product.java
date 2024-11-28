package utils;

import lombok.Getter;
import utils.databases.CategoriesDatabaseConnection;

public class Product {
    private int ID;
    private String name;
    @Getter
    private double price;
    private int categoryID;

    public Product(String name, double price, int categoryID) {
        this.name = name;
        this.price = price;
        this.categoryID = categoryID;
    }

    public String getName() {
        return name;
    }

    public int getCategoryID() {
        return categoryID;
    }
}
