package models;

import models.listeners.failed.CategoryCreationFailureListener;
import models.listeners.failed.CategorySearchFailureListener;
import models.listeners.successful.CategoryCreationSuccessListener;
import models.listeners.successful.CategorySearchSuccessListener;

import java.util.ArrayList;
import java.util.List;


public interface ICategoryModel {
    void createCategory(String categoryName);
    void addCategoryCreationSuccessListener(CategoryCreationSuccessListener listener);
    void addCategoryCreationFailureListener(CategoryCreationFailureListener listener);
    void addCategorySearchSuccessListener(CategorySearchSuccessListener listener);
    void addCategorySearchFailureListener(CategorySearchFailureListener listener);
    List<String> getCategoriesName();
    void addProductAttributes(int productID, int categoryID, ArrayList<String> attributesValues);

    ArrayList<String> getCategoryAttributesNames(int categoryID);

    void addAttributes(String categoryName, ArrayList<String> attributesNames);
    int getCategoryID(String categoryName);
}
