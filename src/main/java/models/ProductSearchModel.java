//CLASE CREADA HOY 30-7-2024

package models;

public class ProductSearchModel {
    private int ID;
    private String name;
    private String description;
    private double price;

    public ProductSearchModel(int ID, String name, String description, double price) {
        this.ID = ID;
        this.name = name;
        this.description = description;
        this.price = price;
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
}
