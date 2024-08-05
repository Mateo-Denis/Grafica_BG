package models;

import models.listeners.failed.ProductCreationFailureListener;
import models.listeners.successful.ProductCreationSuccessListener;
import models.listeners.failed.ProductSearchFailureListener;
import models.listeners.successful.ProductSearchSuccessListener;
import utils.Product;
import utils.databases.ProductsDatabaseConnection;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class ProductModel implements IProductModel {
    private final ProductsDatabaseConnection dbConnection;
    private final List<ProductCreationSuccessListener> productCreationSuccessListeners;
    private final List<ProductCreationFailureListener> productCreationFailureListeners;
    private final List<ProductSearchSuccessListener> productSearchSuccessListeners;
    private final List<ProductSearchFailureListener> productSearchFailureListeners;

    private ArrayList<Product> products;

    public ProductModel(ProductsDatabaseConnection dbConnection) {
        this.dbConnection = dbConnection;
        products = new ArrayList<>();

        this.productCreationSuccessListeners = new LinkedList<>();
        this.productCreationFailureListeners = new LinkedList<>();

        this.productSearchSuccessListeners = new LinkedList<>();
        this.productSearchFailureListeners = new LinkedList<>();
    }

    public void createProduct(String productName, String productDescription, double productPrice) {
        try {
            dbConnection.insertProduct(productName, productDescription, productPrice);
            notifyProductCreationSuccess();
        } catch (Exception e) {
            notifyProductCreationFailure();
        }
    }

    @Override
    public ArrayList<Product> getLastProductsQuery() {
        return products;
    }

    @Override
    public void addProductCreationSuccessListener(ProductCreationSuccessListener listener) {
        productCreationSuccessListeners.add(listener);
    }

    @Override
    public void addProductCreationFailureListener(ProductCreationFailureListener listener) {
        productCreationFailureListeners.add(listener);
    }

    @Override
    public void addProductSearchSuccessListener(ProductSearchSuccessListener listener) {
        productSearchSuccessListeners.add(listener);
    }

    @Override
    public void addProductSearchFailureListener(ProductSearchFailureListener listener) {
        productSearchFailureListeners.add(listener);
    }

    @Override
    public void queryProducts(String searchedName) {
        try {
            products = dbConnection.getProducts(searchedName);
            notifyProductSearchSuccess();
        } catch (SQLException e) {
            notifyProductSearchFailure();
        }
    }

    private void notifyProductCreationSuccess() {
        for (ProductCreationSuccessListener listener : productCreationSuccessListeners) {
            listener.onSuccess();
        }
    }

    private void notifyProductCreationFailure() {
        for (ProductCreationFailureListener listener : productCreationFailureListeners) {
            listener.onFailure();
        }
    }

    private void notifyProductSearchSuccess() {
        for (ProductSearchSuccessListener listener : productSearchSuccessListeners) {
            listener.onSuccess();
        }
    }

    private void notifyProductSearchFailure() {
        for (ProductSearchFailureListener listener : productSearchFailureListeners) {
            listener.onFailure();
        }
    }



}
