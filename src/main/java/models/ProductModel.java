package models;

import models.listeners.failed.ProductCreationFailureListener;
import models.listeners.successful.ProductCreationSuccessListener;
import models.listeners.failed.ProductSearchFailureListener;
import models.listeners.successful.ProductSearchSuccessListener;
import utils.Product;
import utils.databases.ProductsDatabaseConnection;
import views.products.modular.*;

import javax.swing.*;
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
/*
    private final List<ProductSubCategoriesQueryFailureListener> productSubCategoriesQueryFailureListeners;
    private final List<ProductSubCategoriesQuerySuccessListeners> productSubCategoriesQuerySuccessListeners;
*/

    private ArrayList<Product> products;
    private ArrayList<String> subCategories;

    public ProductModel(ProductsDatabaseConnection dbConnection) {
        this.dbConnection = dbConnection;
        products = new ArrayList<>();

        this.productCreationSuccessListeners = new LinkedList<>();
        this.productCreationFailureListeners = new LinkedList<>();

        this.productSearchSuccessListeners = new LinkedList<>();
        this.productSearchFailureListeners = new LinkedList<>();

/*        this.productSubCategoriesQueryFailureListeners = new LinkedList<>();
        this.productSubCategoriesQuerySuccessListeners = new LinkedList<>();*/
    }

    public void createProduct(String productName, String productDescription, double productPrice, String productCategory, String productSubCategory) {
        try {
            dbConnection.insertProduct(productName, productDescription, productPrice, productCategory, productSubCategory);
            notifyProductCreationSuccess();
        } catch (Exception e) {
            notifyProductCreationFailure();
        }
    }

    @Override
    public ArrayList<Product> getLastProductsQuery() {
        return products;
    }

/*    public ArrayList<String> getQueriedSubCategories() {
        return subCategories;
    }*/

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

/*    @Override
    public void addSubCategoriesQueryFailureListener(ProductSubCategoriesQueryFailureListener listener) {
        productSubCategoriesQueryFailureListeners.add(listener);
    }

    @Override
    public void addSubCategoriesQuerySuccessListener(ProductSubCategoriesQuerySuccessListeners listener) {
        productSubCategoriesQuerySuccessListeners.add(listener);
    }*/

    @Override
    public void queryProducts(String searchedName) {
        try {
            products = dbConnection.getProducts(searchedName);
            notifyProductSearchSuccess();
        } catch (SQLException e) {
            notifyProductSearchFailure();
        }
    }

/*    public void querySubCategories(String category) {
        try {
            dbConnection.getSubCategories(category);
            notifySubCategoriesQuerySuccess();
        } catch (SQLException e) {
            notifySubCategoriesQueryFailure();
        }
    }*/

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

/*    private void notifySubCategoriesQueryFailure(){
        for(ProductSubCategoriesQueryFailureListener listener : productSubCategoriesQueryFailureListeners){
            listener.onFailure();
        }
    }

    private void notifySubCategoriesQuerySuccess(){
        for(ProductSubCategoriesQuerySuccessListeners listener : productSubCategoriesQuerySuccessListeners){
            listener.onSuccess();
        }
    }*/

    public IModularCategoryView getCorrespondingModularView(String category) {
        if (category.equals("Remeras")) {
            return new ModularTShirtView();
        } else if(category.equals("Tazas")) {
            return new ModularCupView();
        } else if(category.equals("Banderas")) {
            return new ModularFlagView();
        }
        return null;
    }
}
