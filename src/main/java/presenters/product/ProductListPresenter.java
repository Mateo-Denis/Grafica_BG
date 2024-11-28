package presenters.product;

import presenters.StandardPresenter;
import utils.Product;
import utils.databases.CategoriesDatabaseConnection;
import utils.databases.ProductsDatabaseConnection;
import views.products.list.IProductListView;
import models.IProductListModel;
import views.products.IProductSearchView;

import javax.swing.*;
import java.util.ArrayList;

public class ProductListPresenter extends StandardPresenter {
    private final IProductListView productListView;
    private final IProductListModel productListModel;
    private IProductSearchView productSearchView;
    private ProductsDatabaseConnection productsDatabaseConnection;
    private CategoriesDatabaseConnection categoriesDatabaseConnection;

    public ProductListPresenter(IProductListView productListView, IProductListModel productListModel) {
        this.productListView = productListView;
        view = productListView;
        this.productListModel = productListModel;
        productsDatabaseConnection = new ProductsDatabaseConnection();
    }

    @Override
    protected void initListeners() {

    }

    public void onSearchViewOpenListButtonClicked() {
        ArrayList<Product> products = productListModel.getProductsFromDB();
        if(products.isEmpty()) {
            JOptionPane.showMessageDialog(null, "No hay productos en la base de datos");
        }
        else {
            productListView.showView();
            productListView.setWorkingStatus();
            productListView.clearView();
            setProductsOnTable();
            productListView.setWaitingStatus();
        }
    }

    public void setProductsOnTable() {
        ArrayList<Product> products = productListModel.getProductsFromDB();
        int rowCount = 0;
        int productID = 0;
        int productCategoryID = 0;
        String productCategoryName = "";

            for (Product product : products) {
                try {
                    productID = productsDatabaseConnection.getProductID(product.getName());
                    productCategoryID = productsDatabaseConnection.getCategoryID(product.getName());
                    productCategoryName = productsDatabaseConnection.getCategoryName(productCategoryID);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                productListView.setIntTableValueAt(rowCount, 0, productID);
                productListView.setStringTableValueAt(rowCount, 1, product.getName());
                productListView.setDoubleTableValueAt(rowCount, 2, 0.0);
                productListView.setStringTableValueAt(rowCount, 3, productCategoryName);
                rowCount++;
        }
    }
}
