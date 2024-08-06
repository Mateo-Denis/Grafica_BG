package models;

import models.listeners.failed.CategoryCreationFailureListener;
import models.listeners.failed.CategorySearchFailureListener;
import models.listeners.successful.CategoryCreationSuccessListener;
import models.listeners.successful.CategorySearchSuccessListener;
import utils.ProductCategory;
import java.util.ArrayList;
import java.util.List;


public interface ICategoryModel {
    void createCategory(String categoryName);
    void addCategoryCreationSuccessListener(CategoryCreationSuccessListener listener);
    void addCategoryCreationFailureListener(CategoryCreationFailureListener listener);
    void addCategorySearchSuccessListener(CategorySearchSuccessListener listener);
    void addCategorySearchFailureListener(CategorySearchFailureListener listener);
    List<String> obtenerNombresCategorias();
}
