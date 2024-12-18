package presenters.product;


import models.ICategoryModel;
import models.IProductModel;
import presenters.StandardPresenter;
import views.products.IProductSearchView;
import utils.Product;


import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

import static utils.MessageTypes.PRODUCT_DELETION_FAILURE;
import static utils.MessageTypes.PRODUCT_SEARCH_FAILURE;

public class ProductSearchPresenter extends StandardPresenter {
    private final IProductSearchView productSearchView;
    private final IProductModel productModel;
    private final ICategoryModel categoryModel;

    public ProductSearchPresenter(IProductSearchView productSearchView, IProductModel productModel, ICategoryModel categoryModel) {
        this.productSearchView = productSearchView;
        view = productSearchView;
        this.productModel = productModel;
        this.categoryModel = categoryModel;
        cargarCategorias();
    }

    @Override
    protected void initListeners() {
        productModel.addProductSearchSuccessListener(() -> {
            ArrayList<Product> products = productModel.getLastProductsQuery();
            int rowCount = 0;
            for (Product product : products) {
                String categoryName = productModel.getCategoryName(product.getCategoryID());
                productSearchView.setStringTableValueAt(rowCount, 0, product.getName());
                productSearchView.setDoubleTableValueAt(rowCount, 1, 0.0);
                productSearchView.setStringTableValueAt(rowCount, 2, categoryName);
                productSearchView.setDoubleTableValueAt(rowCount, 3, product.calculateRealTimePrice());
                rowCount++;
            }
        });

        productModel.addProductSearchFailureListener(() -> productSearchView.showMessage(PRODUCT_SEARCH_FAILURE));

    }

    public void onSearchButtonClicked() {
        productSearchView.clearView();
        String productName = productSearchView.getNameSearchText();
        List<String> categoriesName = categoryModel.getCategoriesName();
        JComboBox categoryComboBox = productSearchView.getCategoriesComboBox();
        String selectedCategory = (String) categoryComboBox.getSelectedItem();
        productModel.queryProducts(productName, selectedCategory);
        String productCategoryName = "";
    }

    public void onHomeSearchProductButtonClicked() {
        productSearchView.showView();
    }

    public void onDeleteProductButtonClicked() {
        int[] selectedRows = productSearchView.getProductResultTable().getSelectedRows();
        if(selectedRows.length == 1) {
            deleteOneProduct();
        } else if(selectedRows.length > 1) {
            deleteMultipleProducts();
        } else {
            productSearchView.showMessage(PRODUCT_DELETION_FAILURE);
        }
    }

    public void deleteOneProduct() {
        int oneProductID = getSelectedProductID(); //TOMA EL ID DEL PRODUCTO SELECCIONADO
        if (oneProductID != -1 && oneProductID != 0) {
            productModel.deleteOneProduct(oneProductID);
            productSearchView.setWorkingStatus();
            productSearchView.clearView();
            String searchedName = productSearchView.getNameSearchText();
            productModel.queryProducts(searchedName, "Seleccione una categoría");
            productSearchView.deselectAllRows();
            productSearchView.setWaitingStatus();
        }
    }

    public void deleteMultipleProducts() {
        ArrayList<Integer> productIDs = new ArrayList<>();
        ArrayList<String> selectedProductNames = productSearchView.getMultipleSelectedProductNames();
        int[] selectedRows = productSearchView.getProductResultTable().getSelectedRows();
        for (int i = 0; i < selectedRows.length; i++) {
            String selectedProductName = selectedProductNames.get(i);
            int productID = productModel.getProductID(selectedProductName);
            productIDs.add(productID);
        }



        productModel.deleteMultipleProducts(productIDs);
        productSearchView.setWorkingStatus();
        productSearchView.clearView();
        String searchedName = productSearchView.getNameSearchText();
        productModel.queryProducts(searchedName, "Seleccione una categoría");
        productSearchView.deselectAllRows();
        productSearchView.setWaitingStatus();
    }

    public int getSelectedProductID() {
        int selectedRow = productSearchView.getProductResultTable().getSelectedRow();
        if(selectedRow != -1) {
            String selectedProductName = productSearchView.getSelectedProductName();
            return productModel.getProductID(selectedProductName);
        }
        return -1;
    }

    private void cargarCategorias() {
        List<String> categorias = categoryModel.getCategoriesName();
        productSearchView.setCategoriesComboBox(categorias);
    }
}
