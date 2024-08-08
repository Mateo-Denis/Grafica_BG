package utils;

public class Product {
    private int ID;
    private String name;
    private String description;
    private double price;
    private String category;

    public Product(String name, String description, double price, String category) {
        this.ID = ID;
        this.name = name;
        this.description = description;
        this.price = price;
        this.category = category;
    }

    public int getID() {
        return ID;
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

    public String getCategory() {
        return category;
    }
}
