package models;

import models.listeners.failed.ProductCreationFailureListener;
import models.listeners.failed.ProductSearchFailureListener;
import models.listeners.successful.ProductCreationSuccessListener;
import models.listeners.successful.ProductSearchSuccessListener;
import utils.Product;

import java.util.ArrayList;
import java.util.List;

public interface IProductModel {
    int createProduct(String productName, double productPrice, int categoryID);

    void addProductCreationSuccessListener(ProductCreationSuccessListener listener);
    void addProductCreationFailureListener(ProductCreationFailureListener listener);
    void addProductSearchSuccessListener(ProductSearchSuccessListener listener);
    void addProductSearchFailureListener(ProductSearchFailureListener listener);
    void queryProducts(String searchedName, String category);
    ArrayList<Product> getLastProductsQuery();
    int getProductID(String productName);
    void deleteOneProduct(int productID);
    void deleteMultipleProducts(List<Integer> productIDs);
    //void deleteAllVisibleProducts(ArrayList<String> visibleProductNames);
    String getCategoryName(int categoryID);
    int getCategoryID(String categoryName);
    Product getOneProduct(int productID);
}
