package models;

import models.listeners.failed.ProductListOpeningFailureListener;
import models.listeners.successful.ProductListOpeningSuccessListener;
import utils.Product;

import java.util.ArrayList;
import java.util.List;

public interface IProductListModel {

    void queryAllProducts();
    void addListOpeningSuccessListener(ProductListOpeningSuccessListener listener);
    void addListOpeningFailureListener(ProductListOpeningFailureListener listener);
    void notifyListOpeningSuccess();
    void notifyListOpeningFailure();
    ArrayList<Integer> getAllProductIDs();
    ArrayList<Product> getLastQuery();
    ArrayList<Product> getProductsFromDB();
    void deleteProduct(List<Integer> oneProductID);
    int getProductID(String productName);
}
