package models;

import models.listeners.failed.ProductCreationFailureListener;
import models.listeners.successful.ProductCreationSuccessListener;
import models.listeners.failed.ProductSearchFailureListener;
import models.listeners.successful.ProductSearchSuccessListener;
import utils.Product;
import utils.Category;
import utils.databases.AttributesDatabaseConnection;
import utils.databases.ProductsDatabaseConnection;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class ProductModel implements IProductModel {

    private final AttributesDatabaseConnection attributesDBConnection;
    private final ProductsDatabaseConnection productsDBConnection;
    private final List<ProductCreationSuccessListener> productCreationSuccessListeners;
    private final List<ProductCreationFailureListener> productCreationFailureListeners;
    private final List<ProductSearchSuccessListener> productSearchSuccessListeners;
    private final List<ProductSearchFailureListener> productSearchFailureListeners;

    private ArrayList<Product> products;

    public ProductModel(ProductsDatabaseConnection dbConnection, AttributesDatabaseConnection attributesDBConnection) {
        this.productsDBConnection = dbConnection;
        this.attributesDBConnection = attributesDBConnection;
        products = new ArrayList<>();

        this.productCreationSuccessListeners = new LinkedList<>();
        this.productCreationFailureListeners = new LinkedList<>();

        this.productSearchSuccessListeners = new LinkedList<>();
        this.productSearchFailureListeners = new LinkedList<>();
    }

    public int createProduct(String productName, String productDescription, double productPrice, String productCategory) {
        try {
            int categoryID = getCategoryID(productCategory);
            int productID = productsDBConnection.insertProduct(productName, productDescription, productPrice, categoryID);
            notifyProductCreationSuccess();
            return productID;
        } catch (Exception e) {
            notifyProductCreationFailure();
        }
        return -1;
    }

    private int getCategoryID(String productCategory) {
        try {
            return productsDBConnection.getCategoryID(productCategory);
        } catch (SQLException e) {
            return -1;
        }
    }

    @Override
    public ArrayList<Product> getLastProductsQuery() {
        return products;
    }

    @Override
    public int getProductID(String productName) {
        int productID = 0;
        try {
            productID = productsDBConnection.getProductID(productName);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return  productID;
    }

    @Override
    public void deleteProduct(List<Integer> oneProductID) {
        try {
            productsDBConnection.deleteProductFromDB(oneProductID);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

/*    @Override
    public void deleteAllVisibleProducts(ArrayList<String> visibleProductNames) {
        List<Integer> visibleProductIDs = new ArrayList<>();
        for (String productName : visibleProductNames) {
            visibleProductIDs.add(getProductID(productName));
        }
        try {
            productsDBConnection.deleteProductFromDB(visibleProductIDs);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
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

    @Override
    public void queryProducts(String searchedName) {
        try {
            products = productsDBConnection.getProducts(searchedName);
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
