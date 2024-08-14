package utils;

public class Product {
    private int ID;
    private String name;
    private String description;
    private double price;
    private String categoryName;

    public Product(String name, String description, double price, String categoryName) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.categoryName = categoryName;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public double getPrice() {
        return price;
    }

    public String getCategoryName() {
        return categoryName;
    }
}
