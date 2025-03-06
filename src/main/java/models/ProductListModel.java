package models;

import models.listeners.failed.ProductListOpeningFailureListener;
import models.listeners.successful.ProductListOpeningSuccessListener;
import utils.Product;
import utils.databases.ProductsDatabaseConnection;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Logger;

public class ProductListModel implements IProductListModel {

    private final List<ProductListOpeningSuccessListener> productListOpeningSuccessListeners;
    private final List<ProductListOpeningFailureListener> productListOpeningFailureListeners;
    private final ProductsDatabaseConnection productsDBConnection;
    private static Logger LOGGER;

    private final ArrayList<Product> products;

    public ProductListModel(ProductsDatabaseConnection productsDBConnection) {
        this.productsDBConnection = productsDBConnection;
        products = new ArrayList<>();

        this.productListOpeningSuccessListeners = new LinkedList<>();
        this.productListOpeningFailureListeners = new LinkedList<>();
    }

    public ArrayList<Product> getProductsFromDB() {
        ArrayList <Product> products = new ArrayList<>();
        ProductsDatabaseConnection Prod = new ProductsDatabaseConnection();
        try {
            return products = Prod.getAllProducts();
        } catch (SQLException e) {
            LOGGER.log(null,"ERROR IN METHOD 'getProductsFromDB' IN CLASS->'ProductListModel'",e);

        }
        return null;
    }

}
