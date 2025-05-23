package testing;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import models.BudgetModifyModel;
import models.CategoryModel;
import models.ProductListModel;
import models.ProductModel;
import models.settings.SettingsModel;
import presenters.product.ProductListPresenter;
import presenters.product.ProductSearchPresenter;
import utils.databases.*;
import utils.Product;
import views.products.IProductSearchView;
import views.products.ProductSearchView;
import views.products.list.ProductListView;

import java.awt.*;
import java.sql.*;

public class testingMain {
    private static final Multimap<Integer, Product> productMap = ArrayListMultimap.create();
    private static final BudgetsDatabaseConnection budgetsDatabaseConnection = new BudgetsDatabaseConnection();
    private static final BudgetModifyModel budgetModifyModel = new BudgetModifyModel(budgetsDatabaseConnection);

    private static ProductsDatabaseConnection productsDatabaseConnection = new ProductsDatabaseConnection();
    private static AttributesDatabaseConnection attributesDatabaseConnection = new AttributesDatabaseConnection();
    private static CategoriesDatabaseConnection categoriesDatabaseConnection = new CategoriesDatabaseConnection();
    private static CategoryModel categoryModel = new CategoryModel(categoriesDatabaseConnection, attributesDatabaseConnection);
    private static ProductModel productModel = new ProductModel(productsDatabaseConnection, attributesDatabaseConnection, categoriesDatabaseConnection);
    private static ProductListModel productListModel = new ProductListModel(productsDatabaseConnection);
    private static ProductListView productListView = new ProductListView();
    private static ProductListPresenter productListPresenter = new ProductListPresenter(productListView, productListModel);
    private static ProductSearchView productSearchView = new ProductSearchView(productListPresenter);
    private static SettingsDatabaseConnection settingsDatabaseConnection = new SettingsDatabaseConnection();
    private static SettingsModel settingsModel = new SettingsModel(settingsDatabaseConnection);
    private static ProductSearchPresenter productSearchPresenter = new ProductSearchPresenter(settingsModel, productSearchView, productModel, categoryModel);


    public static void main(String[] args) throws SQLException {
        // Initialize the ProductSearchView and ProductSearchPresenter
        Product product = productSearchPresenter.getSelectedProduct("Gorrinch");
        productSearchPresenter.getProductAttributes(product);
    }

/*    public static void TuVieja() {
        // Obtener el dispositivo gráfico de la pantalla principal
        GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();

        // Obtener la configuración gráfica de la pantalla
        GraphicsConfiguration gc = gd.getDefaultConfiguration();

        // Obtener las dimensiones de la pantalla
        Rectangle bounds = gc.getBounds();

        // Mostrar las dimensiones
        System.out.println("Ancho de la pantalla: " + bounds.width);
        System.out.println("Alto de la pantalla: " + bounds.height);
    }

    public static void TuViejaElComeback()
    {
        // Obtener el dispositivo gráfico principal
        GraphicsDevice graphicsDevice = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();

        // Obtener la configuración gráfica del dispositivo
        GraphicsConfiguration gc = graphicsDevice.getDefaultConfiguration();

        // Obtener las dimensiones de la pantalla
        Rectangle bounds = gc.getBounds();

        // Obtener el tamaño de la pantalla lógica
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

        // Calcular el factor de escala: (dimensión lógica / dimensión física)
        double scaleFactor = (double) screenSize.width / (double) bounds.width;

        // Mostrar el factor de escala
        System.out.println("Factor de escala: " + scaleFactor);
    }*/
}

