package models;

import models.listeners.failed.CategoryCreationFailureListener;
import models.listeners.failed.CategorySearchFailureListener;
import models.listeners.successful.CategoryCreationSuccessListener;
import models.listeners.successful.CategorySearchSuccessListener;
import utils.databases.AttributesDatabaseConnection;
import utils.databases.CategoriesDatabaseConnection;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class CategoryModel implements ICategoryModel {

    private final CategoriesDatabaseConnection categoriesDBConnection;
    private final AttributesDatabaseConnection attributesDBConnection;
    private final List<CategoryCreationSuccessListener> categoryCreationSuccessListener;
    private final List<CategoryCreationFailureListener> categoryCreationFailureListener;
    private final List<CategorySearchSuccessListener> categorySearchSuccessListener;
    private final List<CategorySearchFailureListener> categorySearchFailureListener;

    public CategoryModel(CategoriesDatabaseConnection categoriesDBConnection, AttributesDatabaseConnection attributesDBConnection) {
        this.categoriesDBConnection = categoriesDBConnection;
        this.attributesDBConnection = attributesDBConnection;


        this.categoryCreationSuccessListener = new LinkedList<>();
        this.categoryCreationFailureListener = new LinkedList<>();

        this.categorySearchSuccessListener = new LinkedList<>();
        this.categorySearchFailureListener = new LinkedList<>();
    }

    @Override
    public void createCategory(String categoryName) {
        try {
            categoriesDBConnection.insertCategory(categoryName);
            notifyCategoryCreationSuccess();
        } catch (Exception e) {
            notifyCategoryCreationFailure();
        }
    }

    @Override
    public void addCategoryCreationSuccessListener(CategoryCreationSuccessListener listener) {
        categoryCreationSuccessListener.add(listener);
    }

    @Override
    public void addCategoryCreationFailureListener(CategoryCreationFailureListener listener) {
        categoryCreationFailureListener.add(listener);
    }

    @Override
    public void addCategorySearchSuccessListener(CategorySearchSuccessListener listener) {
        categorySearchSuccessListener.add(listener);
    }

    @Override
    public void addCategorySearchFailureListener(CategorySearchFailureListener listener) {
        categorySearchFailureListener.add(listener);
    }

/*    @Override
    public void queryCategories(String searchedName) {
        try {
            categories = categoriesDBConnection.getCategories(searchedName);
            notifyCategorySearchSuccess();
        } catch (Exception e) {
            notifyCategorySearchFailure();
        }
    }*/


    private void notifyCategoryCreationSuccess() {
        for (CategoryCreationSuccessListener listener : categoryCreationSuccessListener) {
            listener.onSuccess();
        }
    }

    private void notifyCategoryCreationFailure() {
        for (CategoryCreationFailureListener listener : categoryCreationFailureListener) {
            listener.onFailure();
        }
    }

    private void notifyCategorySearchSuccess() {
        for (CategorySearchSuccessListener listener : categorySearchSuccessListener) {
            listener.onSuccess();
        }
    }

    private void notifyProductSearchFailure() {
        for (CategorySearchFailureListener listener : categorySearchFailureListener) {
            listener.onFailure();
        }
    }

    public List<String> getCategoriesName() {
        try {
            return categoriesDBConnection.getCategories();
        } catch (SQLException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    @Override
    public ArrayList<String> getCategoryAttributesNames(int categoryID) {
        try {
            return categoriesDBConnection.getCategoryAttributesNames(categoryID);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void addAttributes(String categoryName, ArrayList<String> attributes) {
        try {
            int categoryID = categoriesDBConnection.getCategoryID(categoryName);
            for (String attribute : attributes) {
                attributesDBConnection.insertAttributeRow(attribute, categoryID);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void addProductAttributes(int productID, int categoryID, ArrayList<String> attributesValues) {

        ArrayList<String> attributesNames = getCategoryAttributesNames(categoryID);
        if (attributesNames.size() == attributesValues.size()) {
            String attributeName;
            int attributeID;
            for (int i = 0; i < attributesNames.size(); i++) {
                attributeName = attributesNames.get(i);
                attributeID = attributesDBConnection.getAttributeID(attributeName);
                attributesDBConnection.insertProductAttributeRow(productID, attributeID, attributesValues.get(i));
            }
        } else {
            System.out.println("The lists are not of the same size.");
        }
    }

    public int getCategoryID(String categoryName) {
        try {
            return categoriesDBConnection.getCategoryID(categoryName);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }
}
