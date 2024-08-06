package utils;

public class ProductSubCategory {
    private int ID;
    private int categoryID;
    private String name;

    public ProductSubCategory(int ID, int categoryID, String name) {
        this.ID = ID;
        this.categoryID = categoryID;
        this.name = name;
    }

    public int getID() {
        return ID;
    }

    public int getCategoryID() {
        return categoryID;
    }

    public String getName() {
        return name;
    }
}
