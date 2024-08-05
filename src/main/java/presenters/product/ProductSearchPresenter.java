package presenters.product;

import models.IProductModel;
import presenters.StandardPresenter;
import views.products.IProductSearchView;
import utils.Product;

import java.util.ArrayList;

import static utils.MessageTypes.CLIENT_SEARCH_FAILURE;
import static utils.MessageTypes.PRODUCT_SEARCH_FAILURE;

public class ProductSearchPresenter extends StandardPresenter {
    private final IProductSearchView productSearchView;
    private final IProductModel productModel;

    public ProductSearchPresenter(IProductSearchView productSearchView, IProductModel productModel) {
        this.productSearchView = productSearchView;
        view = productSearchView;
        this.productModel = productModel;
    }



    @Override
    protected void initListeners() {
        productModel.addProductSearchSuccessListener(() -> {
            ArrayList<Product> products = productModel.getLastProductsQuery();
            int rowCount = 0;
            for (Product product : products) {
                productSearchView.setStringTableValueAt(rowCount, 0, product.getName());
                productSearchView.setStringTableValueAt(rowCount, 1, product.getDescription());
                productSearchView.setDoubleTableValueAt(rowCount, 2, product.getPrice());
                rowCount++;
            }
        });

        productModel.addProductSearchFailureListener(() -> productSearchView.showMessage(PRODUCT_SEARCH_FAILURE));

    }

    public void onSearchButtonClicked() {
        productSearchView.setWorkingStatus();

        String searchedName = productSearchView.getNameSearchText();

        productModel.queryProducts(searchedName);

        productSearchView.setWaitingStatus();
    }

    public void onSearchProductButtonClicked() {
        productSearchView.showView();
    }
}
