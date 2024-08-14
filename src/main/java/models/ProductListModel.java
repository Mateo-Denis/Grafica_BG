package models;

import utils.Product;
import utils.databases.ProductsDatabaseConnection;
import models.listeners.failed.ProductListOpeningFailureListener;
import models.listeners.successful.ProductListOpeningSuccessListener;

import java.sql.Array;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class ProductListModel implements IProductListModel {

    private final List<ProductListOpeningSuccessListener> productListOpeningSuccessListeners;
    private final List<ProductListOpeningFailureListener> productListOpeningFailureListeners;
    private final ProductsDatabaseConnection productsDBConnection;

    private ArrayList<Product> products;

    public ProductListModel(ProductsDatabaseConnection productsDBConnection) {
        this.productsDBConnection = productsDBConnection;
        products = new ArrayList<>();

        this.productListOpeningSuccessListeners = new LinkedList<>();
        this.productListOpeningFailureListeners = new LinkedList<>();
    }

    @Override
    public void queryAllProducts() {
        try {
            products = productsDBConnection.getAllProducts();
        } catch (SQLException e) {
            notifyListOpeningFailure();
        }
    }

    @Override
    public void addListOpeningSuccessListener(ProductListOpeningSuccessListener listener) {
        productListOpeningSuccessListeners.add(listener);
    }

    @Override
    public void addListOpeningFailureListener(ProductListOpeningFailureListener listener) {
        productListOpeningFailureListeners.add(listener);
    }

    public void notifyListOpeningSuccess() {
        for (ProductListOpeningSuccessListener listener : productListOpeningSuccessListeners) {
            listener.onSuccess();
        }
    }

    public void notifyListOpeningFailure() {
        for (ProductListOpeningFailureListener listener : productListOpeningFailureListeners) {
            listener.onFailure();
        }
    }

    @Override
    public ArrayList<Integer> getAllProductIDs() {
        ArrayList<Integer> productIDs = new ArrayList<>();
        for (Product product : products) {
            try {
                String productName = product.getName();
                int productID = productsDBConnection.getProductID(productName);
                productIDs.add(productID);
            } catch (SQLException e) {
                notifyListOpeningFailure();
            }
        }
        return productIDs;
    }

    @Override
    public ArrayList<Product> getLastQuery() {
        return products;
    }

    public ArrayList<Product> getProductsFromDB() {
        ArrayList <Product> products = new ArrayList<>();
        ProductsDatabaseConnection Prod = new ProductsDatabaseConnection();
        try {
            return products = Prod.getAllProducts();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void deleteProduct(List<Integer> oneProductID) {
        try {
            productsDBConnection.deleteProductFromDB(oneProductID);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
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

}
