package presenters.product;

import static utils.CategoryParser.parseCategory;
import static utils.MessageTypes.*;
import static utils.databases.SettingsTableNames.*;

import models.IProductModel;
import models.ICategoryModel;
import models.settings.ISettingsModel;
import org.javatuples.Pair;
import org.javatuples.Triplet;
import presenters.StandardPresenter;

import java.awt.event.ItemEvent;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import utils.databases.SettingsTableNames;
import views.products.IProductCreateView;
import views.products.modular.*;

public class ProductCreatePresenter extends StandardPresenter {
    private final IProductCreateView productCreateView;
    private final IProductModel productModel;
    private final ICategoryModel categoryModel;
    private final ISettingsModel settingsModel;
    private IModularCategoryView modularView;
    private ModularCapView capView;
    private ModularClothView clothView;
    private ModularCupView cupView;
    private ModularFlagView flagView;
    private ModularClothesView shirtView;

    public ProductCreatePresenter(IProductCreateView productCreateView, IProductModel productModel, ICategoryModel categoryModel, ISettingsModel settingsModel) {
        this.productCreateView = productCreateView;
        this.settingsModel = settingsModel;
        view = productCreateView;
        this.productModel = productModel;
        this.categoryModel = categoryModel;
        cargarCategorias();
        this.productCreateView.comboBoxListenerSet(e -> {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                String selectedCategory = productCreateView.getProductCategory();

                productCreateView.showSelectedView(selectedCategory);

                modularView = productCreateView.getModularView();
                modularView.loadComboBoxValues();
                setModularPrices();
            }
        });
    }

    public void onUpdatePriceButtonClicked() {
        if (modularView == null) {
            productCreateView.showMessage(MISSING_MODULAR_VIEW);
        } else {
            modularView = productCreateView.getModularView();
        }
    }

    public void initListeners() {
        productModel.addProductCreationSuccessListener(() -> productCreateView.showMessage(PRODUCT_CREATION_SUCCESS));
        productModel.addProductCreationFailureListener(() -> productCreateView.showMessage(PRODUCT_CREATION_FAILURE));
    }

    public void onHomeCreateProductButtonClicked() {
        productCreateView.showView();
    }

    private void updatePriceField(double price) {
        productCreateView.setProductPriceField(Double.toString(price));
    }

    public void onCreateButtonClicked() {
        Map<String, Double> attributes;
        productCreateView.setWorkingStatus();
        String categoryName = productCreateView.getProductCategoryEnglish();
        int categoryID = categoryModel.getCategoryID(categoryName);
        modularView = productCreateView.getModularView();
        if (modularView == null || productCreateView.getProductName().equals("")) {
            if (modularView == null) {
                productCreateView.showMessage(MISSING_MODULAR_VIEW);
            } else {
                productCreateView.showMessage(MISSING_PRODUCT_NAME);
            }
        } else {
            String productName = productCreateView.getProductName();
            double productPrice = productCreateView.getProductPrice();
            int productID = productModel.createProduct(productName, productPrice, categoryID);
        }

        productCreateView.setWaitingStatus();

    }


    private void cargarCategorias() {
        List<String> categories = categoryModel.getCategoriesName();
        List<String> categoriesInSpanish = new ArrayList<>();
        for (String category : categories) {
            categoriesInSpanish.add(parseCategory(category));
        }
        productCreateView.setCategorias(categoriesInSpanish);
    }

    public ArrayList<Pair<String, Double>> getTableAsArrayList(SettingsTableNames tableName) {
        return settingsModel.getModularValues(tableName);
    }

    public double getIndividualPrice(SettingsTableNames tableName, String selectedValue) {
        ArrayList<Pair<String, Double>> rows = settingsModel.getModularValues(tableName);
        double individualPrice = 0.0;
        for (Pair<String, Double> row : rows) {
            if (row.getValue0().equals(selectedValue)) {
                individualPrice = row.getValue1();
            }
        }
        return individualPrice;
    }

    private void setModularPrices() {
        if (modularView != null) {
            modularView.setPriceTextFields();
        }
    }

    public void onEditCheckBoxClicked() {
        IModularCategoryView catView = modularView;

        if (catView != null) {
            if (!productCreateView.getEditPriceCheckBox().isSelected()) {
                catView.blockTextFields();
            } else {
                catView.unlockTextFields();
            }
        }
    }

    public void onSavePricesButtonClicked() {
        List<Triplet<String, String, Double>> modularPricesList;
        if (modularView != null) {
            try {
                modularPricesList = modularView.getModularPrices();
                settingsModel.updateModularPrices(modularPricesList);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }
}