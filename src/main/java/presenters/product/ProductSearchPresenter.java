package presenters.product;


import models.IProductModel;
import presenters.StandardPresenter;
import views.products.IProductSearchView;
import utils.Product;


import java.util.ArrayList;
import java.util.List;

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
                productSearchView.setStringTableValueAt(rowCount, 3, product.getCategoryName());
                rowCount++;
            }
        });

        productModel.addProductSearchFailureListener(() -> productSearchView.showMessage(PRODUCT_SEARCH_FAILURE));

    }

    public void onSearchButtonClicked() {
        productSearchView.setWorkingStatus();

        String searchedName = productSearchView.getNameSearchText();

        productSearchView.clearView();

        productModel.queryProducts(searchedName);

        productSearchView.setWaitingStatus();
    }

    public void onHomeSearchProductButtonClicked() {
        productSearchView.showView();
    }

    public void onDeleteOneProductButtonClicked() {
        List<Integer> oneProductID = getOneProductID();
        if (!(oneProductID.isEmpty())) {
            productModel.deleteProduct(getOneProductID());
            productSearchView.setWorkingStatus();
            productSearchView.clearView();
            String searchedName = productSearchView.getNameSearchText();
            productModel.queryProducts(searchedName);
            productSearchView.deselectAllRows();
            productSearchView.setWaitingStatus();
        }
    }

/*    public void onDeleteAllProductsButtonClicked() {
        ArrayList<String> visibleProductNames = productSearchView.getVisibleProductNames();
        productModel.deleteAllVisibleProducts(visibleProductNames);

        productSearchView.setWorkingStatus();
        productSearchView.clearView();
        productSearchView.setWaitingStatus();
    }*/

    public List<Integer> getOneProductID() {
        String selectedProductName = productSearchView.getSelectedProductName();
        List<Integer> oneProductID = new ArrayList<>();
        oneProductID.add(productModel.getProductID(selectedProductName));
        return oneProductID;
    }
}
