package utils;

public class ProductCategory {
    private int ID;
    private String name;

    public ProductCategory(int ID, String name) {
        this.ID = ID;
        this.name = name;
    }

    public int getID() {
        return ID;
    }

    public String getName() {
        return name;
    }
}
