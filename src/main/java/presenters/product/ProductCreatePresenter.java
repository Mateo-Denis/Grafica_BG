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
    private ModularPrintingView printingView;
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
                updatePriceField(modularView.getPrice());
                setModularPrices();
            }
        });


    }

    public void onUpdatePriceButtonClicked() {
        if (modularView == null) {
            productCreateView.showMessage(MISSING_MODULAR_VIEW);
        } else {
            modularView = productCreateView.getModularView();

            updatePriceField(modularView.getPrice());
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
        productCreateView.setWorkingStatus();
        String categoryName = productCreateView.getProductCategoryEnglish();
        int categoryID = categoryModel.getCategoryID(categoryName);
        //IModularCategoryView modularView = productCreateView.getCorrespondingModularView(categoryName);
        modularView = productCreateView.getModularView();
        if (modularView == null || productCreateView.getProductName().equals("")) {
            if (modularView == null) {
                productCreateView.showMessage(MISSING_MODULAR_VIEW);
            } else {
                productCreateView.showMessage(MISSING_PRODUCT_NAME);
            }
        } else {

/*            Map<String,String> allAttributes = modularView.getModularAttributes();
            ArrayList<String> attributesNames = new ArrayList<>();

            for(Map.Entry<String, String> entry : allAttributes.entrySet()) {
                attributesNames.add(entry.getKey());
            }

            ArrayList<String> attributesValues = new ArrayList<>();
            for(Map.Entry<String, String> entry : allAttributes.entrySet()) {
                attributesValues.add(entry.getValue());
            }*/

            //categoryModel.addAttributes(categoryName, attributesNames);

            int productID = productModel.createProduct(
                    productCreateView.getProductName(),
                    productCreateView.getProductPrice(),
                    categoryID);

            ArrayList<String> attributesValues = modularView.getExhaustiveInformation();
            categoryModel.addProductAttributes(productID, categoryID, attributesValues);
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

    public double getProfitFor(String category) {
        double profit = 0.0;
        ArrayList<Pair<String, Double>> ganancias = settingsModel.getModularValues(GANANCIAS);
        for (Pair<String, Double> ganancia : ganancias) {
            if (ganancia.getValue0().equals(category)) {
                profit = ganancia.getValue1();
                break;
            }
        }
        return profit;
    }

    public double getClothPriceFor(String material) {
        ArrayList<Pair<String, Double>> clothPrices = settingsModel.getModularValues(TELAS);
        double correctClothPrice = 0.0;

        for (Pair<String, Double> cloth : clothPrices) {
            if (cloth.getValue0().equals(material)) {
                correctClothPrice = cloth.getValue1();
                break;
            }
        }
        return correctClothPrice;
    }

    private void setModularPrices() {
        if(modularView != null) {
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

    public double getPrintingPriceFor(String category) {
        ArrayList<Pair<String, Double>> printingPrices = settingsModel.getModularValues(IMPRESIONES);
        double correctPrintingPrice = 0.0;
        for (Pair<String, Double> printing : printingPrices) {
            switch (category) {
                case "Prenda" -> {
                    if (printing.getValue0().equals("En sublimación")) {
                        correctPrintingPrice = printing.getValue1();
                    }
                }
            }
        }
        return correctPrintingPrice;
    }

    public double getServicesPrice(String service) {
        ArrayList<Pair<String, Double>> servicePrices = settingsModel.getModularValues(SERVICIOS);
        double correctServicePrice = 0.0;
        for (Pair<String, Double> serviceP : servicePrices) {
            switch (service) {
                case "Costurera" -> {
                    if (serviceP.getValue0().equals("Costurera")) {
                        correctServicePrice = serviceP.getValue1();
                    }
                }
                case "Cierre" -> {
                    if (serviceP.getValue0().equals("Cierre")) {
                        correctServicePrice = serviceP.getValue1();
                    }
                }
            }
        }
        return correctServicePrice;
    }

    public double getPlankLoweringPrice(String category) {
        ArrayList<Pair<String, Double>> plankLoweringPrices = settingsModel.getModularValues(BAJADA_PLANCHA);
        double correctPlankLoweringPrice = 0.0;
        for (Pair<String, Double> price : plankLoweringPrices) {
            switch (category) {
                case "Prenda" -> {
                    if (price.getValue0().equals("Bajada de plancha prendas")) {
                        correctPlankLoweringPrice = price.getValue1();
                    }
                }
                case "Bandera" -> {
                    if (price.getValue0().equals("Bajada de plancha banderas")) {
                        correctPlankLoweringPrice = price.getValue1();
                    }
                }
                case "Gorra" -> {
                    if (price.getValue0().equals("Bajada de plancha gorras")) {
                        correctPlankLoweringPrice = price.getValue1();
                    }
                }
                case "Taza" -> {
                    if (price.getValue0().equals("Bajada de plancha tazas")) {
                        correctPlankLoweringPrice = price.getValue1();
                    }
                }
            }
        }
        return correctPlankLoweringPrice;
    }
}