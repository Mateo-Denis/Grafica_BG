package presenters.product;

import static utils.MessageTypes.*;
import models.IProductModel;
import models.ICategoryModel;
import presenters.StandardPresenter;
import views.products.*;

import javax.swing.*;
import java.awt.event.ItemEvent;
import java.util.List;

import views.products.IProductCreateView;


public class ProductCreatePresenter extends StandardPresenter {
    private final IProductCreateView productCreateView;
    private final IProductModel productModel;
    private final ICategoryModel categoryModel;

    public ProductCreatePresenter(IProductCreateView productCreateView, IProductModel productModel, ICategoryModel categoryModel) {
        this.productCreateView = productCreateView;
        view = productCreateView;
        this.productModel = productModel;
        this.categoryModel = categoryModel;
        cargarCategorias();
    }

    public void initListeners() {
        productModel.addProductCreationSuccessListener(() -> productCreateView.showMessage(PRODUCT_CREATION_SUCCESS));
        productModel.addProductCreationFailureListener(() -> productCreateView.showMessage(PRODUCT_CREATION_FAILURE));
    }

    public void onHomeCreateProductButtonClicked() {
        productCreateView.showView();
    }

    public void onCreateButtonClicked() {
        productCreateView.setWorkingStatus();

        productModel.createProduct(
                productCreateView.getProductName(),
                productCreateView.getProductDescription(),
                productCreateView.getProductPrice(),
                productCreateView.getProductCategory());
                productCreateView.setWaitingStatus();
    }

    public void onCategoryItemSelected() {
        String category = productCreateView.getProductCategory();
        productCreateView.setCorrespondingModularView(category);
    }

    private void cargarCategorias() {
        List<String> categorias = categoryModel.getCategoriesName();
        productCreateView.setCategorias(categorias);
    }

}
