package models;

import models.listeners.failed.ProductCreationFailureListener;
import models.listeners.failed.ProductSearchFailureListener;
import models.listeners.failed.ProductSubCategoriesQueryFailureListener;
import models.listeners.successful.ProductCreationSuccessListener;
import models.listeners.successful.ProductSearchSuccessListener;
import utils.Product;

import java.util.ArrayList;

public interface IProductModel {
    void createProduct(String productName, String productDescription, double productPrice, String productCategory, String productSubCategory);

    void addProductCreationSuccessListener(ProductCreationSuccessListener listener);
    void addProductCreationFailureListener(ProductCreationFailureListener listener);
    void addProductSearchSuccessListener(ProductSearchSuccessListener listener);
    void addProductSearchFailureListener(ProductSearchFailureListener listener);
    void addSubCategoriesQuerySuccessListener(ProductSubCategoriesQueryFailureListener listener);
    void queryProducts(String searchedName);
    void querySubCategories(String category);
    ArrayList<Product> getLastProductsQuery();
    ArrayList<String> getQueriedSubCategories();
}
