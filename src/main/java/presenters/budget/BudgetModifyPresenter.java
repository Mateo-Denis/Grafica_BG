package presenters.budget;

import com.google.common.collect.Multimap;
import models.settings.ISettingsModel;
import presenters.StandardPresenter;

import utils.*;
import utils.Client;
import utils.Product;

import models.ICategoryModel;
import models.IBudgetModel;
import models.IProductModel;
import models.IBudgetModifyModel;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import views.budget.modify.IBudgetModifyView;

import static utils.databases.SettingsTableNames.GENERAL;
import static utils.MessageTypes.*;


public class BudgetModifyPresenter extends StandardPresenter {
    private final IBudgetModifyView budgetModifyView;
    private final IBudgetModel budgetModel;
    private final IProductModel productModel;
    private final ICategoryModel categoryModel;
    private final IBudgetModifyModel budgetModifyModel;

    private int productsRowCountOnPreviewTable = -1;
    private int globalClientID = -1;
    private int globalBudgetNumber = -1;
    private String oldClientName = "";
    private double globalBudgetTotalPrice = 0.0;
    private String globalClientType = "";
    private final ISettingsModel settingsModel;


    public BudgetModifyPresenter(IBudgetModifyView budgetModifyView, IBudgetModel budgetModel, IProductModel productModel,
                                 ICategoryModel categoryModel, IBudgetModifyModel budgetModifyModel, ISettingsModel settingsModel) {
        this.budgetModifyView = budgetModifyView;
        view = budgetModifyView;
        this.budgetModel = budgetModel;
        this.productModel = productModel;
        this.categoryModel = categoryModel;
        this.budgetModifyModel = budgetModifyModel;
        this.settingsModel = settingsModel;

        cargarCategorias();
        cargarCiudades();
    }

// ---------> METHODS AND FUNCTIONS START HERE <-------------
    // ---------> METHODS AND FUNCTIONS START HERE <-------------

    // LISTENERS:
    @Override
    protected void initListeners() {
    }


    private void cargarCategorias() {
        List<String> categorias = categoryModel.getCategoriesName();
        budgetModifyView.setCategoriesComboBox(categorias);
    }

    private void cargarCiudades() {
        ArrayList<String> ciudades = budgetModel.getCitiesName();
        budgetModifyView.setCitiesComboBox(ciudades);
    }


    // IF THE SEARCH PRODUCTS BUTTON IS CLICKED:
    public void OnSearchProductButtonClicked() {
        String productName = budgetModifyView.getProductsTextField().getText(); // PRODUCT NAME SEARCHED
        List<String> categoriesName = categoryModel.getCategoriesName(); // GET CATEGORIES NAMES
        JComboBox categoryComboBox = budgetModifyView.getCategoriesComboBox(); // GET CATEGORIES COMBO BOX
        String selectedCategory = (String) categoryComboBox.getSelectedItem(); // GET SELECTED CATEGORY
        ArrayList<Product> products = budgetModel.getProducts(productName, selectedCategory); // GET PRODUCTS BY NAME AND CATEGORY
        String productCategoryName = ""; // PRODUCT CATEGORY NAME STRING VARIABLE
        budgetModifyView.clearProductTable(); // CLEAR PRODUCT TABLE
        int rowCount = 0; // ROW COUNT VARIABLE
        double productPrice = 0.0; // PRODUCT PRICE VARIABLE

        // SUPER LOOP THROUGH PRODUCTS
        for (Product product : products) {
            int categoryID = product.getCategoryID(); // GET CATEGORY ID

            // INNER LOOP THROUGH CATEGORIES NAMES
            for (String categoryName : categoriesName) {
                if (categoryModel.getCategoryID(categoryName) == categoryID) { // IF CATEGORY ID MATCHES
                    productCategoryName = categoryName; // SET PRODUCT CATEGORY NAME TO CATEGORY NAME
                }
            }
            productPrice = product.calculateRealTimePrice();
            budgetModifyView.setProductStringTableValueAt(rowCount, 0, product.getName()); // SET PRODUCT STRING TABLE VALUE AT ROW COUNT, 0, PRODUCT NAME
            budgetModifyView.setProductStringTableValueAt(rowCount, 1, productCategoryName); // SET PRODUCT STRING TABLE VALUE AT ROW COUNT, 2, PRODUCT CATEGORY NAME
            budgetModifyView.setProductStringTableValueAt(rowCount, 2, String.valueOf(productPrice)); // SET PRODUCT STRING TABLE VALUE AT ROW COUNT, 3, PRODUCT PRICE
            rowCount++; // INCREMENT ROW COUNT
        }
        categoryComboBox.setSelectedIndex(0);
    }


    public void onClientSelectedCheckBoxClicked() {
        JCheckBox clientSelectedCheckBox = budgetModifyView.getClientSelectedCheckBox();
        if (!clientSelectedCheckBox.isSelected()) {
            budgetModifyView.setSecondPanelsVisibility();
        } else {
            budgetModifyView.setInitialPanelsVisibility();
        }
    }


    // IF THE SEARCH CLIENT BUTTON IS CLICKED:
    public void OnSearchClientButtonClicked() {
        String city = "";
        String clientType = "";
        city = (String) budgetModifyView.getCitiesComboBox().getSelectedItem(); // GET CITY
        String name = budgetModifyView.getBudgetClientName(); // GET BUDGET CLIENT NAME
        int clientID = -1; // CLIENT ID VARIABLE

        // IF CITY IS "SELECCIONE UNA CIUDAD"
        if (city.equals("Seleccione una ciudad")) {
            city = ""; // SET CITY TO EMPTY STRING
        }


        ArrayList<Client> clients = budgetModel.getClients(name, city); // LOCAL VARIABLE -> GET CLIENTS BY NAME AND CITY
        budgetModifyView.clearClientTable(); // CLEAR CLIENT TABLE
        int rowCount = 0; // ROW COUNT VARIABLE

        // LOOP THROUGH CLIENTS
        for (Client client : clients) {
            clientType = "Cliente";

            if (!client.isClient()) {
                clientType = "Particular";
            }

            clientID = budgetModel.getClientID(client.getName(), clientType); // GET CLIENT ID

            // SET CLIENT TABLE VALUES
            budgetModifyView.setClientIntTableValueAt(rowCount, 0, clientID);
            budgetModifyView.setClientStringTableValueAt(rowCount, 1, client.getName());
            budgetModifyView.setClientStringTableValueAt(rowCount, 2, client.getAddress());
            budgetModifyView.setClientStringTableValueAt(rowCount, 3, client.getCity());
            budgetModifyView.setClientStringTableValueAt(rowCount, 4, client.getPhone());
            budgetModifyView.setClientStringTableValueAt(rowCount, 5, clientType);
            rowCount++; // INCREMENT ROW COUNT
        }
    }


    // IF THE ADD CLIENT BUTTON IS CLICKED:
    public void onAddClientButtonClicked() {
        int selectedRow = budgetModifyView.getClientTableSelectedRow(); // GET SELECTED ROW
        String clientName = ""; // CLIENT NAME STRING VARIABLE
        String clientType = ""; // CLIENT TYPE STRING VARIABLE
        JCheckBox clientSelectedCheckBox = budgetModifyView.getClientSelectedCheckBox(); // GET CLIENT SELECTED CHECK BOX
        DefaultTableModel clientTableModel = budgetModifyView.getClientResultTableModel(); // GET CLIENT RESULT TABLE MODEL

        // IF SELECTED ROW IS NOT -1 (NOT EMPTY)
        if (selectedRow != -1) {
            // IF CLIENT NAME IS NOT NULL AND NOT EMPTY
            if (clientTableModel.getValueAt(selectedRow, 1) != null && !clientTableModel.getValueAt(selectedRow, 1).toString().equals("")) {
                clientName = clientTableModel.getValueAt(selectedRow, 1).toString(); // GET CLIENT NAME FROM TABLE MODEL
                clientType = clientTableModel.getValueAt(selectedRow, 5).toString(); // GET CLIENT TYPE FROM TABLE MODEL
                budgetModifyView.setPreviewStringTableValueAt(0, 0, clientName); // SET PREVIEW STRING TABLE VALUE AT 0, 0, CLIENT NAME
                budgetModifyView.setPreviewStringTableValueAt(0, 6, clientType); // SET PREVIEW STRING TABLE VALUE AT 0, 6, CLIENT TYPE
                globalClientID = budgetModel.getClientID(clientName, clientType); // SET GLOBAL CLIENT ID TO CLIENT ID
                clientSelectedCheckBox.setSelected(true); // SET CLIENT SELECTED CHECK BOX TO SELECTED
                globalClientType = clientType; // SET GLOBAL CLIENT TYPE TO CLIENT TYPE
                updatePriceColumnByRecharge();
            } else {
                budgetModifyView.showMessage(MessageTypes.CLIENT_NOT_SELECTED); // SHOW MESSAGE CLIENT NOT SELECTED
            }
        } else {
            budgetModifyView.showMessage(MessageTypes.CLIENT_NOT_SELECTED); // SHOW MESSAGE CLIENT NOT SELECTED
        }
    }

    public void updatePriceColumnByRecharge(){
        double recharge = 1;
        String clientType = budgetModifyView.getPreviewStringTableValueAt(0, 6);
        updateTextArea(false, false, globalBudgetTotalPrice);
        globalBudgetTotalPrice = 0;
        if (clientType.equals("Particular")) {
            recharge = Double.parseDouble(settingsModel.getModularValue(GENERAL, "Recargo por particular"));
        }
        for (int i = 1; i <= productsRowCountOnPreviewTable; i++) {
            Product product = productModel.getOneProduct(productModel.getProductID(budgetModifyView.getPreviewStringTableValueAt(i, 1)));
            double productOriginalPrice = product.calculateRealTimePrice();
            double toAdd = productOriginalPrice * recharge;
            budgetModifyView.setPreviewStringTableValueAt(i, 5, String.valueOf(toAdd));
            double totalPrice = toAdd * Integer.parseInt(budgetModifyView.getPreviewStringTableValueAt(i, 2));
            updateTextArea(true, false, totalPrice);
        }
    }


    public Product GetSelectedProductFromProductsTable() {
        int selectedProductRow = budgetModifyView.getProductTableSelectedRow();
        Product product = null;

        if (selectedProductRow != -1) {
            String productName = budgetModifyView.getProductStringTableValueAt(selectedProductRow, 0);
            product = productModel.getOneProduct(productModel.getProductID(productName));
        }

        return product;
    }


    public void AddProductToPreviewTable(Product product, int row) {

        String productName = product.getName();
        String productAmountStr = budgetModifyView.getAmountTextField().getText();
        String productWidthMeasures = budgetModifyView.getWidthMeasureTextField().getText();
        String productHeightMeasures = budgetModifyView.getHeightMeasureTextField().getText();
        boolean unlockedMeasures = CheckMeasureFieldsAreEnabled();
        String productObservations = budgetModifyView.getObservationsTextField().getText();
        double oneItemProductPrice = product.calculateRealTimePrice();
        double totalItemsPrice = 0.0;
        double settingPrice = 0.0;
        double recharge = 1.0;
        String productMeasures = "";

        if (productAmountStr.isEmpty()) { // IF PRODUCT AMOUNT STRING IS EMPTY
            productAmountStr = "1";
        }

        if(globalClientType.equals("Particular")){
            recharge = Double.parseDouble(settingsModel.getModularValue(GENERAL, "Recargo por particular"));
        }

        if (unlockedMeasures) {
            if (productHeightMeasures.isEmpty()) {
                productHeightMeasures = "1";
            }
            if (productWidthMeasures.isEmpty()) {
                productWidthMeasures = "1";
            }
            int meters = Integer.parseInt(productWidthMeasures) * Integer.parseInt(productHeightMeasures);
            totalItemsPrice = oneItemProductPrice * Integer.parseInt(productAmountStr) * meters * recharge;
            settingPrice = oneItemProductPrice * meters * recharge;
            productMeasures = productWidthMeasures + " x " + productHeightMeasures;
        } else {
            totalItemsPrice = oneItemProductPrice * Integer.parseInt(productAmountStr) * recharge;
            settingPrice = oneItemProductPrice * recharge;
        }



        budgetModifyView.setPreviewStringTableValueAt(row, 1, productName); //INSERTA EN LA COLUMNA DE NOMBREPRODUCTO
        budgetModifyView.setPreviewStringTableValueAt(row, 2, productAmountStr); //INSERTA EN LA COLUMNA DE CANTIDAD
        budgetModifyView.setPreviewStringTableValueAt(row, 3, productMeasures); //INSERTA EN LA COLUMNA DE MEDIDAS
        budgetModifyView.setPreviewStringTableValueAt(row, 4, productObservations); //INSERTA EN LA COLUMNA DE OBSERVACIONES
        budgetModifyView.setPreviewStringTableValueAt(row, 5, String.valueOf(settingPrice)); //INSERTA EN LA COLUMNA DE PRECIO
        updateTextArea(true, false, totalItemsPrice);
    }


    public List<String> GetProductFromPreviewTable(int row) {
        List<String> productRowData = new ArrayList<>();
        String productName = budgetModifyView.getPreviewStringTableValueAt(row, 1);
        String productAmount = budgetModifyView.getPreviewStringTableValueAt(row, 2);
        String productMeasures = budgetModifyView.getPreviewStringTableValueAt(row, 3);
        String productObservations = budgetModifyView.getPreviewStringTableValueAt(row, 4);

        productRowData.add(productName);
        productRowData.add(productAmount);
        productRowData.add(productMeasures);
        productRowData.add(productObservations);

        for (String data : productRowData) {
            System.out.println("DATA: " + data);
        }

        return productRowData;
    }


    public void onAddProductButtonClicked() {
        Product product; // PRODUCT VARIABLE
        int selectedProductRow = budgetModifyView.getProductTableSelectedRow(); // SELECTED PRODUCT ROW
        int selectedPreviewRow = budgetModifyView.getPreviewTableSelectedRow(); // SELECTED PREVIEW ROW

        if (selectedProductRow != -1) {
            product = GetSelectedProductFromProductsTable();
            AddProductToPreviewTable(product, productsRowCountOnPreviewTable + 1);
            productsRowCountOnPreviewTable++;
        }

    }

    public void onDeleteProductButtonClicked() {
        budgetModifyView.getProductsResultTable().clearSelection();
        int selectedRow = budgetModifyView.getPreviewTable().getSelectedRow();

        double totalSelectedPrice = 0.0;

        if (selectedRow != -1) {
            if (productsRowCountOnPreviewTable >= 1) {
                totalSelectedPrice = GetSelectedTotalPrice(selectedRow);
                budgetModifyView.getPreviewTableModel().removeRow(selectedRow);
                productsRowCountOnPreviewTable--;
                updateTextArea(false, false, totalSelectedPrice);
            }
        }
    }

    public double GetSelectedTotalPrice(int selectedPreviewRow) {
        double totalPrice = 0.0;
        double oneProductPrice = Double.parseDouble(budgetModifyView.getPreviewStringTableValueAt(selectedPreviewRow, 5));
        int productAmount = Integer.parseInt(budgetModifyView.getPreviewStringTableValueAt(selectedPreviewRow, 2));
        if (selectedPreviewRow != -1) {
            totalPrice = oneProductPrice * productAmount;
        }
        return totalPrice;
    }

    public boolean CheckMeasureFieldsAreEnabled() {
        JTextField widthMeasureTextField = budgetModifyView.getWidthMeasureTextField();
        JTextField heightMeasureTextField = budgetModifyView.getHeightMeasureTextField();
        boolean areUnlocked = false;

        if (widthMeasureTextField.isEnabled() || heightMeasureTextField.isEnabled()) {
            areUnlocked = true;
        }

        return areUnlocked;
    }

    public boolean onEmptyFields(int clientNameColumn, int productColumn) {
        boolean anyEmpty = false;
        String clientName = budgetModifyView.getPreviewStringTableValueAt(0, clientNameColumn);
        String product = budgetModifyView.getPreviewStringTableValueAt(1, productColumn);

        if ((clientName == null || clientName.trim().isEmpty()) ||
                (product == null || product.trim().isEmpty())) {
            anyEmpty = true;
        }
        return anyEmpty;
    }

    public int GetGlobalBudgetNumer() {
        return globalBudgetNumber;
    }


    public void onModifySearchViewButtonClicked(boolean filledRow, int selectedRow, int budgetNumber) {
        if(selectedRow != -1){
            if(filledRow){
                budgetModifyView.setWorkingStatus();

                budgetModifyView.getWidthMeasureTextField().setEnabled(false);
                budgetModifyView.getHeightMeasureTextField().setEnabled(false);

                productsRowCountOnPreviewTable = budgetModifyView.getFilledRowsCount(budgetModifyView.getPreviewTable());
                globalBudgetNumber = budgetNumber;
                oldClientName = budgetModifyModel.getOldClientName(globalBudgetNumber);
                budgetModifyView.getClientSelectedCheckBox().setSelected(true); //MARCO EL CHECKBOX DE QUE YA HAY UN CLIENTE SELECCIONADO
                ArrayList<String> budgetClientData = budgetModifyModel.getSelectedBudgetData(budgetNumber);
                String budgetClientName = budgetClientData.get(1);
                double budgetTotalPrice = 0.0;
                globalClientType = "";

                ArrayList<String> productMeasures = getProductsMeasures(budgetNumber, budgetClientName);
                ArrayList<String> productObservations = getProductObservations(budgetNumber, budgetClientName);
                ArrayList<Double> productPrices = getProductPrices(budgetNumber, budgetClientName);
                ArrayList<String> productNames = budgetModifyModel.getSavedProductNames(budgetNumber, budgetClientName);
                ArrayList<Integer> productAmounts = budgetModifyModel.getSavedProductAmounts(budgetNumber, budgetClientName);

                globalBudgetTotalPrice = 0.0;
                setModifyView(productNames, productAmounts, productObservations, productMeasures, budgetNumber, productPrices);
                updateTextArea(true, true, globalBudgetTotalPrice);


                budgetModifyView.showView();
                budgetModifyView.setWaitingStatus();
            } else{budgetModifyView.showMessage(BUDGET_MODIFY_FAILURE);}
        } else{budgetModifyView.showMessage(BUDGET_MODIFY_FAILURE);}
    }

    public double GetBudgetTotalPrice(ArrayList<String> productNames, ArrayList<Integer> productAmounts) {
        ArrayList<Double> prices = getProductPrices(globalBudgetNumber, oldClientName);
        double totalPrice = 0.0;
        int productIndex = 0;

        for (int row = 1; row <= productNames.size(); row++) {
            String productAmount = String.valueOf(productAmounts.get(productIndex));
            double productPrice = prices.get(productIndex) * Integer.parseInt(productAmount);
            totalPrice += productPrice;
            productIndex++;
        }

        return totalPrice;
    }

    public ArrayList<Double> getProductPrices(int budgetNumber, String budgetName) {
        return budgetModifyModel.getProductPrices(budgetNumber, budgetName);
    }

    public ArrayList<String> getProductObservations(int budgetNumber, String budgetName) {
        return budgetModifyModel.getProductObservations(budgetNumber, budgetName);
    }

    public ArrayList<String> getProductsMeasures(int budgetNumber, String budgetName) {
        return budgetModifyModel.getProductMeasures(budgetNumber, budgetName);
    }

    public void SetProductsInPreviewTable(ArrayList<Double> prices, ArrayList<String> productNames, ArrayList<Integer> productAmounts, ArrayList<String> measures, ArrayList<String> observations) {

        String productName = "";
        String productMeasure = "";
        String productAmount = "";
        String productObservation = "";
        double productPrice = 0.0;
        int productIndex = 0;

        for (int row = 1; row <= productNames.size(); row++) {

            productName = productNames.get(productIndex);
            productAmount = String.valueOf(productAmounts.get(productIndex));
            productMeasure = measures.get(productIndex);
            productObservation = observations.get(productIndex);
            productPrice = prices.get(productIndex);

            budgetModifyView.setPreviewStringTableValueAt(row, 1, productName);
            budgetModifyView.setPreviewStringTableValueAt(row, 2, productAmount);
            budgetModifyView.setPreviewStringTableValueAt(row, 3, productMeasure);
            budgetModifyView.setPreviewStringTableValueAt(row, 4, productObservation);
            budgetModifyView.setPreviewStringTableValueAt(row, 5, Double.toString(productPrice));

            productIndex++;
        }
    }

    public void SetClientInPreviewTable(String clientName, String clientType) {
        budgetModifyView.setPreviewStringTableValueAt(0, 0, clientName);
        budgetModifyView.setPreviewStringTableValueAt(0, 6, clientType);
    }


    public void setModifyView(ArrayList<String> productNames, ArrayList<Integer> productAmounts, ArrayList<String> observations, ArrayList<String> measures, int budgetNumber, ArrayList<Double> prices) {
        ArrayList<String> budgetData = budgetModifyModel.getSelectedBudgetData(budgetNumber);
        int budgetID = Integer.parseInt(budgetData.get(0));
        String budgetclientName = budgetData.get(1);
        String budgetDate = budgetData.get(2);
        String budgetClientType = budgetData.get(3);
        SetClientInPreviewTable(budgetclientName, budgetClientType);
        SetProductsInPreviewTable(prices, productNames, productAmounts, measures, observations);
        globalBudgetTotalPrice = GetBudgetTotalPrice(productNames, productAmounts);
    }


    public void onSaveModificationsButtonClicked() {
        int newBudgetID = -1;
        int oldBudgetID = -1;

        String newClientName = budgetModifyView.getPreviewStringTableValueAt(0, 0);
        String newClientType = budgetModifyView.getPreviewStringTableValueAt(0, 6);
        String date = budgetModifyView.getBudgetDate();
        String productName = "";
        int productAmount = 0;
        String productObservation = "";
        String productMeasure = "";
        String measuresObservations = "";
        Double productPrice = 0.0;

        ArrayList<String> productNames = new ArrayList<>();
        ArrayList<Integer> productAmounts = new ArrayList<>();
        ArrayList<String> productObservations = new ArrayList<>();
        ArrayList<String> productMeasures = new ArrayList<>();
        ArrayList<Double> productPrices = new ArrayList<>();
        boolean anyEmpty = onEmptyFields(0, 1);

        if (anyEmpty) {
            budgetModifyView.showMessage(MessageTypes.BUDGET_CREATION_EMPTY_COLUMN);
        } else {
            for (int row = 1; row <= productsRowCountOnPreviewTable; row++) {
                productName = budgetModifyView.getPreviewStringTableValueAt(row, 1);
                productAmount = Integer.parseInt(budgetModifyView.getPreviewStringTableValueAt(row, 2));
                productMeasure = budgetModifyView.getPreviewStringTableValueAt(row, 3);
                productObservation = budgetModifyView.getPreviewStringTableValueAt(row, 4);
                productPrice = Double.parseDouble(budgetModifyView.getPreviewStringTableValueAt(row, 5));


                productNames.add(productName);
                productAmounts.add(productAmount);
                productObservations.add(productObservation);
                productMeasures.add(productMeasure);
                productPrices.add(productPrice);
            }

            oldBudgetID = budgetModel.getBudgetID(globalBudgetNumber, oldClientName);
            budgetModel.deleteOneBudget(oldBudgetID);

            if (newClientType.equals("Particular")) {
                globalBudgetTotalPrice *= 1.25;
            }
            budgetModel.createBudget(newClientName, date, newClientType, globalBudgetNumber, globalBudgetTotalPrice);

            newBudgetID = budgetModel.getMaxBudgetID();
            budgetModel.saveProducts(newBudgetID, productAmounts, productNames, productObservations, productMeasures, productPrices);
            budgetModel.deleteBudgetProducts(oldBudgetID);

            budgetModifyView.showMessage(MessageTypes.BUDGET_MODIFY_SUCCESS);

            budgetModifyView.hideView();
            budgetModifyView.getWindowFrame().dispose();
        }
    }

    public ArrayList<Object> GetTextAreaAndStringBuilder() {
        ArrayList<Object> textAreaAndStringBuilder = new ArrayList<>();
        textAreaAndStringBuilder.add(budgetModifyView.getPriceTextArea());
        textAreaAndStringBuilder.add(budgetModifyView.getStringBuilder());
        return textAreaAndStringBuilder;
    }

    public void updateTextArea(boolean adding, boolean start, double productPrice) {
        ArrayList<Object> textAreaAndStringBuilder = GetTextAreaAndStringBuilder();
        JTextArea textArea = (JTextArea) textAreaAndStringBuilder.get(0);
        StringBuilder stb = (StringBuilder) textAreaAndStringBuilder.get(1);

        if (!start) {
            if (adding) {
                globalBudgetTotalPrice += productPrice;
            } else {
                globalBudgetTotalPrice -= productPrice;
            }
        }


        stb.setLength(0);
        stb.append("Precio total: $").append(globalBudgetTotalPrice);
        textArea.setText(stb.toString());
    }


// ---------> METHODS AND FUNCTIONS END HERE <-------------
// ---------> METHODS AND FUNCTIONS END HERE <-------------
}
