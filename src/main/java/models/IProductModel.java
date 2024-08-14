package models;

import models.listeners.failed.ProductCreationFailureListener;
import models.listeners.failed.ProductSearchFailureListener;
import models.listeners.successful.ProductCreationSuccessListener;
import models.listeners.successful.ProductSearchSuccessListener;
import utils.Product;
import utils.Category;

import java.util.ArrayList;
import java.util.List;

public interface IProductModel {
    int createProduct(String productName, String productDescription, double productPrice, String productCategory);

    void addProductCreationSuccessListener(ProductCreationSuccessListener listener);
    void addProductCreationFailureListener(ProductCreationFailureListener listener);
    void addProductSearchSuccessListener(ProductSearchSuccessListener listener);
    void addProductSearchFailureListener(ProductSearchFailureListener listener);
    void queryProducts(String searchedName);
    ArrayList<Product> getLastProductsQuery();
    int getProductID(String productName);
    void deleteProduct(List<Integer> productID);
    //void deleteAllVisibleProducts(ArrayList<String> visibleProductNames);
}
