//CLASE CREADA HOY 30-7-2024
package presenters.product;

import models.ProductSearchModel;
import utils.databases.ProductsDatabaseConnection;
import views.products.ProductSearchViewClasses.ProductSearchView;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class    ProductSearchPresenter {
    private ProductSearchView view;
    private ProductsDatabaseConnection databaseconn;

    public ProductSearchPresenter(ProductSearchView view, ProductsDatabaseConnection databaseconn) {
        this.view = view;
        this.databaseconn = databaseconn;
        this.view.setSearchListener(new SearchButtonListener());
    }

    class SearchButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            List<ProductSearchModel> products = databaseconn.getProducts(view.getSearchText());
            view.updateTable(products);
        }
    }
}
