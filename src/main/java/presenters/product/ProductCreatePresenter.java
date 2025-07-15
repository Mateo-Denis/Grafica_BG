package presenters.product;

import static utils.CategoryParser.parseCategory;
import static utils.MessageTypes.*;

import models.IProductModel;
import models.ICategoryModel;
import models.settings.ISettingsModel;
import org.javatuples.Pair;
import org.javatuples.Triplet;

import java.awt.event.ItemEvent;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import utils.Attribute;
import utils.databases.SettingsTableNames;
import views.products.IProductCreateView;
import views.products.modular.*;

public class ProductCreatePresenter extends ProductPresenter {
    private final IProductCreateView productCreateView;
    private final IProductModel productModel;
    private final ICategoryModel categoryModel;
    private final ISettingsModel settingsModel;
    private IModularCategoryView modularView;

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
                SetModularPriceTextFields(modularView);
            }
        });
    }

    public void SetModularPriceTextFields(IModularCategoryView paramModularView)
    {
            modularView.comboBoxListenerSet(e -> {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    paramModularView.setPriceTextFields();
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

/*    private void updatePriceField(double price) {
        productCreateView.setProductPriceField(Double.toString(price));
    }*/

    public int onCreateButtonClicked() {
        int idToReturn = -1;
        ArrayList<Attribute> instancedAttribute;
        productCreateView.setWorkingStatus();
        String categoryName = productCreateView.getProductCategoryEnglish();
        int categoryID = categoryModel.getCategoryID(categoryName);
        modularView = productCreateView.getModularView();
        if (modularView == null || productCreateView.getProductName().isEmpty()) {
            if (modularView == null) {
                productCreateView.showMessage(MISSING_MODULAR_VIEW);
            } else {
                productCreateView.showMessage(MISSING_PRODUCT_NAME);
            }
        } else {
            String productName = productCreateView.getProductName();
            int productID = productModel.createProduct(productName, categoryID);
            instancedAttribute = modularView.getAttributes();
            productModel.instantiateProductAttributes(productID, instancedAttribute, categoryID);
            idToReturn = productID;
        }
        productCreateView.setWaitingStatus();
        return idToReturn;
    }

    public void clearView() {
        productCreateView.clearView();
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

    @Override
    public double getIndividualPrice(SettingsTableNames tableName, String selectedValue) {
        return Double.parseDouble(settingsModel.getModularValue(tableName, selectedValue));
    }

    private void setModularPrices() {
        if (modularView != null) {
            modularView.setPriceTextFields();
        }
    }
}