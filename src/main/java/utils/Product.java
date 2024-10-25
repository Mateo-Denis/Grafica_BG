package utils;

import utils.databases.CategoriesDatabaseConnection;

public class Product {
    private int ID;
    private String name;
    private String description;
    private double price;
    private int categoryID;

    public Product(String name, String description, double price, int categoryID) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.categoryID = categoryID;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public int getCategoryID() {
        return categoryID;
    }
}
