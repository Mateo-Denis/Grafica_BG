package presenters.product;


import models.ICategoryModel;
import models.IProductModel;
import presenters.StandardPresenter;
import utils.CategoryParser;
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
                double price = product.calculateRealTimePrice();
                productSearchView.setStringTableValueAt(rowCount, 0, product.getName());
                productSearchView.setStringTableValueAt(rowCount, 1, CategoryParser.parseCategory(categoryName));
                productSearchView.setDoubleTableValueAt(rowCount, 2, price);
                rowCount++;
            }
        });

        productModel.addProductSearchFailureListener(() -> productSearchView.showMessage(PRODUCT_SEARCH_FAILURE));

    }

    public void onSearchButtonClicked() {
        productSearchView.clearView();
        String productName = productSearchView.getNameSearchText();
        JComboBox categoryComboBox = productSearchView.getCategoriesComboBox();
        String selectedCategory = CategoryParser.getProductCategoryEnglish((String) categoryComboBox.getSelectedItem());
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
        } else {
            productSearchView.showMessage(PRODUCT_DELETION_FAILURE);
        }
    }

    public void deleteOneProduct() { //TOMA EL ID DEL PRODUCTO SELECCIONADO
        int selectedRow = productSearchView.getProductResultTable().getSelectedRow();
        int productID = -1;
        Object rowValue = "";

        if(selectedRow != -1) {
            rowValue = productSearchView.getProductResultTable().getValueAt(selectedRow, 0);
            if(rowValue != null && !rowValue.equals("")) {
                String selectedProductName = (String) rowValue;
                productID = productModel.getProductID(selectedProductName);
                productModel.deleteOneProduct(productID);
                productSearchView.setWorkingStatus();
                productSearchView.clearView();
                String searchedName = productSearchView.getNameSearchText();
                productModel.queryProducts(searchedName, "Seleccione una categoría");
                productSearchView.deselectAllRows();
                productSearchView.setWaitingStatus();
            } else {productSearchView.showMessage(PRODUCT_DELETION_FAILURE);}
        } else{productSearchView.showMessage(PRODUCT_DELETION_FAILURE);}
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
        int productID = -1;
        if(selectedRow != -1) {
            if(productSearchView.getProductResultTable().getValueAt(selectedRow, 0) != null) {
                String selectedProductName = productSearchView.getSelectedProductName();
                productID = productModel.getProductID(selectedProductName);
            }
        }
        return productID;
    }

    private void cargarCategorias() {
        List<String> categorias = categoryModel.getCategoriesName();
        productSearchView.setCategoriesComboBox(categorias);
    }
}
