package presenters.product;

import presenters.StandardPresenter;
import utils.Product;
import utils.databases.ProductsDatabaseConnection;
import views.products.list.IProductListView;
import models.IProductListModel;
import views.products.IProductSearchView;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

public class ProductListPresenter extends StandardPresenter {
    private final IProductListView productListView;
    private final IProductListModel productListModel;
    private IProductSearchView productSearchView;
    private ProductsDatabaseConnection productsDatabaseConnection;

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

            for (Product product : products) {
                try {
                    productID = productsDatabaseConnection.getProductID(product.getName());
                } catch (Exception e) {
                    e.printStackTrace();
                }

                productListView.setIntTableValueAt(rowCount, 0, productID);
                productListView.setStringTableValueAt(rowCount, 1, product.getName());
                productListView.setStringTableValueAt(rowCount, 2, product.getDescription());
                productListView.setDoubleTableValueAt(rowCount, 3, product.getPrice());
                productListView.setStringTableValueAt(rowCount, 4, product.getCategoryName());
                rowCount++;
        }
    }

    public void onDeleteOneProductButtonClicked() {
        ArrayList<Product> products;
        List<Integer> oneProductID = getOneProductID();

        if (!(oneProductID.isEmpty())) {
            productListModel.deleteProduct(getOneProductID());
            products = productListModel.getProductsFromDB();
            productListView.setWorkingStatus();

            if(!products.isEmpty())
            {
                productListView.clearView();
                setProductsOnTable();
                productListView.deselectAllRows();
                productListView.setWaitingStatus();
            }
            else
            {
                JOptionPane.showMessageDialog(null, "No hay productos en la base de datos");
                JFrame frame = productListView.getJFrame();
                frame.dispose();
            }
        }
    }

    public List<Integer> getOneProductID() {
        String selectedProductName = productListView.getSelectedProductName();
        List<Integer> oneProductID = new ArrayList<>();
        oneProductID.add(productListModel.getProductID(selectedProductName));
        return oneProductID;
    }
}
