package presenters.product;

import static utils.CategoryParser.parseCategory;
import static utils.MessageTypes.*;
import static utils.databases.SettingsTableNames.*;

import models.IProductModel;
import models.ICategoryModel;
import models.settings.ISettingsModel;
import org.javatuples.Pair;
import presenters.StandardPresenter;

import java.awt.event.ItemEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import utils.databases.SettingsTableNames;
import views.products.IProductCreateView;
import views.products.modular.*;

import javax.swing.*;

public class ProductCreatePresenter extends StandardPresenter {
    private final IProductCreateView productCreateView;
    private final IProductModel productModel;
    private final ICategoryModel categoryModel;
    private final ISettingsModel settingsModel;
    private IModularCategoryView modularView;
    private ModularCanvasView canvasView;
    private ModularCapView capView;
    private ModularClothView clothView;
    private ModularCupView cupView;
    private ModularFlagView flagView;
    private ModularJacketView jacketView;
    private ModularPrintingView printingView;
    private ModularShirtView shirtView;
    private ModularSweaterView sweaterView;
    private ModularVinylView vinylView;



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

                modularView = productCreateView.getCorrespondingModularView(selectedCategory);

                updatePriceField(modularView.getPrice());
            }
        });

    }

    public void onUpdatePriceButtonClicked() {
        if(modularView == null){
            productCreateView.showMessage(MISSING_MODULAR_VIEW);
        }else {
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

    public void onModularOptionsClicked(double price){
        updatePriceField(price);
    }

    private void updatePriceField(double price) {
        productCreateView.setProductPriceField(Double.toString(price));
    }

    public void onCreateButtonClicked() {
        productCreateView.setWorkingStatus();
        String categoryName = productCreateView.getProductCategory();
        int categoryID = categoryModel.getCategoryID(categoryName);
        IModularCategoryView modularView = productCreateView.getCorrespondingModularView(categoryName);

        if(modularView == null || productCreateView.getProductName().equals("")){
            if(modularView == null) {
                productCreateView.showMessage(MISSING_MODULAR_VIEW);
            } else {
                productCreateView.showMessage(MISSING_PRODUCT_NAME);
            }
        } else {
            Map<String,String> allAttributes = modularView.getModularAttributes();
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
                    productCreateView.getProductPrice(),
                    categoryID);

            categoryModel.addAttributes(categoryName, attributesNames);
            categoryModel.addProductAttributes(productID, attributesNames, attributesValues);
        }

        productCreateView.setWaitingStatus();

 /*       Component modularView = productCreateView.getModularView();
        if (modularView instanceof IModularCategoryView) {
            attributesValues = getModularAttributes((IModularCategoryView) modularView);
        } else {
            // Handle the error, e.g., log it or throw an exception
            throw new ClassCastException("Expected IModularCategoryView but found " + modularView.getClass().getName());
        }*/

    }

/*    private ArrayList<String> getModularAttributes(IModularCategoryView modularContainer) {
        return modularContainer.getModularAttributes();
    }*/


    private void cargarCategorias() {
        List<String> categories = categoryModel.getCategoriesName();
        List<String> categoriesInSpanish = new ArrayList<>();
        for (String category : categories) {
            categoriesInSpanish.add(parseCategory(category));
        }
        productCreateView.setCategorias(categoriesInSpanish);
    }

    private IModularCategoryView getCorrespondingModularView(String category) {
        IModularCategoryView modularView = null;
        modularView = (IModularCategoryView) productCreateView.getCorrespondingModularView(category);
        return modularView;
    }

    public void onAdultRadioButtonClicked() {
        System.out.println("Adult selected");
    }

    public double calculatePrice(String productCategory) {
        double totalPrice = 0.0;
        double profit = 0.0;
        ArrayList<String> info = modularView.getRelevantInformation();
        switch (productCategory) {
            case "canvas" -> {


                double canvasPrice = getIndividualPrice(LONAS, info.get(0));
                profit = getProfitFor("Lonas");

                totalPrice = (canvasPrice * profit);
            }
            case "cap" -> {

                double capPrice = getIndividualPrice(PRENDAS, info.get(0));
                profit = getProfitFor("Gorras");

                totalPrice = (capPrice * profit);
            }
            case "cloth" -> {

                double clothPrice = getIndividualPrice(TELAS, info.get(0));
                profit = getProfitFor("Telas");

                totalPrice = (clothPrice * profit);
            }
            case "cup" -> {


                double cupPrice = getIndividualPrice(GENERAL, info.get(0));
                profit = getProfitFor("Tazas");

                totalPrice = (cupPrice * profit);

                if(info.get(1).equals("Sobre taza")){
                    totalPrice += getIndividualPrice(BAJADA_PLANCHA, "Sobre taza");
                }
            }
            case "flag" ->{

                double flagPrice = getIndividualPrice(TELAS, info.get(0));
                profit = getProfitFor("Banderas");

                double sizePriceMultiplier = getIndividualPrice(TELAS, info.get(1));

                totalPrice = (flagPrice * sizePriceMultiplier);

                totalPrice += getIndividualPrice(GENERAL, "Costurera Bandera");

                totalPrice += getIndividualPrice(BAJADA_PLANCHA, "Sobre bandera");

                totalPrice *= profit;
            }
            case "jacket" -> {

                double jacketPrice = getIndividualPrice(PRENDAS, info.get(0));
                profit = getProfitFor("Camperas");

                double jacketSize = getIndividualPrice(PRENDAS, info.get(1));

                totalPrice = (jacketPrice * jacketSize * profit);
            }
            case "printing" -> {

                double printingPrice = getIndividualPrice(IMPRESIONES, info.get(0));
                profit = getProfitFor("Impresiones");

                totalPrice = (printingPrice * profit);
            }
            case "shirt" -> {

                double shirtPrice = getIndividualPrice(PRENDAS, info.get(0));
                profit = getProfitFor("Remeras");

                double shirtSizePrice = getIndividualPrice(PRENDAS, info.get(1));

                double materialCost = getIndividualPrice(TELAS, info.get(2));

                totalPrice = ((shirtPrice + (shirtSizePrice * materialCost)) * profit);
            }
            case "sweater" -> {

                double sweaterPrice = getIndividualPrice(PRENDAS, info.get(0));
                profit = getProfitFor("Buzos");

                double sweaterSize = getIndividualPrice(PRENDAS, info.get(1));

                totalPrice = (sweaterPrice * sweaterSize * profit);
            }
            case "vinyl" -> {
//                ArrayList<String> info = modularView.getRelevantInformation();
//                ModularVinylView instanceView = (ModularVinylView) modularView;
//                String selectedVinyl = instanceView.getVinylComboBoxSelection();
//
//                double vinylPrice = getIndividualPrice(TELAS, selectedVinyl);
//                profit = getProfitFor("Vinilos");
//
//                totalPrice = (vinylPrice * profit);
            }

        }
        return totalPrice;
    }

    private double getIndividualPrice(SettingsTableNames tableName, String selectedValue){
        ArrayList<Pair<String, Double>> telas = settingsModel.getModularValues(tableName);
        double individualPrice = 0.0;
        for (Pair<String, Double> tela : telas) {
            if (tela.getValue0().equals(selectedValue)) {
                individualPrice = tela.getValue1();
            }
        }
        return individualPrice;
    }

    private double getProfitFor(String category){
        double profit = 0.0;
        ArrayList<Pair<String, Double>> ganancias = settingsModel.getModularValues(GANANCIAS);
        for (Pair<String, Double> ganancia : ganancias) {
            if(ganancia.getValue0().equals(category)){
                profit = ganancia.getValue1();
                break;
            }
        }
        return profit;
    }
}