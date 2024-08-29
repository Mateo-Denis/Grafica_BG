package presenters.product;

import static utils.MessageTypes.*;

import models.IProductModel;
import models.ICategoryModel;
import presenters.StandardPresenter;
import javax.swing.*;

import java.awt.*;
import java.awt.event.ItemEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import views.products.IProductCreateView;
import views.products.modular.IModularCategoryView;
import models.CategoryModel;


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
        this.productCreateView.comboBoxListenerSet(e -> {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                productCreateView.showSelectedView(productCreateView.getProductCategory());
            }
        });

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
        String categoryName = productCreateView.getProductCategory();
        int categoryID = categoryModel.getCategoryID(categoryName);
        IModularCategoryView modularView = productCreateView.getCorrespondingModularView(categoryName);
        Map<String,String> allAttributes = modularView.getModularAttributes();


 /*       Component modularView = productCreateView.getModularView();
        if (modularView instanceof IModularCategoryView) {
            attributesValues = getModularAttributes((IModularCategoryView) modularView);
        } else {
            // Handle the error, e.g., log it or throw an exception
            throw new ClassCastException("Expected IModularCategoryView but found " + modularView.getClass().getName());
        }*/

        ArrayList<String> attributesNames = new ArrayList<>();
        for(Map.Entry<String, String> entry : allAttributes.entrySet()) {
            attributesNames.add(entry.getKey());
        }

        ArrayList<String> attributesValues = new ArrayList<>();
        for(Map.Entry<String, String> entry : allAttributes.entrySet()) {
            attributesValues.add(entry.getValue());
        }

        int productID = productModel.createProduct(
                productCreateView.getProductName(),
                productCreateView.getProductDescription(),
                productCreateView.getProductPrice(),
                categoryID);

        categoryModel.addAttributes(categoryName, attributesNames);
        categoryModel.addProductAttributes(productID, attributesNames, attributesValues);
        productCreateView.setWaitingStatus();
    }

/*    private ArrayList<String> getModularAttributes(IModularCategoryView modularContainer) {
        return modularContainer.getModularAttributes();
    }*/


    private void cargarCategorias() {
        List<String> categorias = categoryModel.getCategoriesName();
        productCreateView.setCategorias(categorias);
    }

    private IModularCategoryView getCorrespondingModularView(String category) {
        IModularCategoryView modularView = null;
        modularView = (IModularCategoryView) productCreateView.getCorrespondingModularView(category);
        return modularView;
    }

    public void onAdultRadioButtonClicked() {
        System.out.println("Adult selected");
    }
}
