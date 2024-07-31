//CLASE CREADA HOY 30-7-2024

package presenters;

import utils.databases.ProductsDatabaseConnection;
import views.products.ProductSearchViewClasses.ProductSearchView;
import views.home.HomeView;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class HomePresenter {
    private HomeView homeview;

    public HomePresenter(HomeView homeview) {
        this.homeview = homeview;
        this.homeview.setProductsButtonListener(new ProductsButtonListener());
    }
}

    class ProductsButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            ProductSearchView productSearchView = new ProductSearchView();
            ProductsDatabaseConnection databaseconn = new ProductsDatabaseConnection();
            new ProductSearchPresenter(productSearchView, databaseconn);
            productSearchView.setVisible(true);
        }
    }