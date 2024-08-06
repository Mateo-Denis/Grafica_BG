package presenters.product;

import static utils.MessageTypes.*;
import models.IProductModel;
import presenters.StandardPresenter;
import views.products.*;

import javax.swing.*;

public class ProductCreatePresenter extends StandardPresenter{
    private final IProductCreateView productCreateView;
    private final IProductModel productModel;

    public ProductCreatePresenter(IProductCreateView productCreateView, IProductModel productModel) {
        this.productCreateView = productCreateView;
        view = productCreateView;
        this.productModel = productModel;
    }

    public void initListeners() {
        productModel.addProductCreationSuccessListener(() -> productCreateView.showMessage(PRODUCT_CREATION_SUCCESS));
        productModel.addProductCreationFailureListener(() -> productCreateView.showMessage(PRODUCT_CREATION_FAILURE));

/*        productModel.addSubCategoriesQuerySuccessListener(() -> {
            ArrayList<String> subCategoryList = productModel.getQueriedSubCategories();
            productCreateView.clearSubCategories();
            for (String subCategory : subCategoryList) {
                productCreateView.addSubCategory(subCategory);
            }
        });*/
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
                productCreateView.getProductCategory(),
                productCreateView.getProductSubCategory());

        productCreateView.setWaitingStatus();
    }

    public void onCategoryItemSelected() {
        String category = productCreateView.getProductCategory();
        JPanel modularPanel = productModel.getCorrespondingModularView(category).getContainerPanel();
        productCreateView.showCategoryOptions(modularPanel);
    }
}
