package presenters.budget;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
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
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import views.budget.modify.IBudgetModifyView;


public class BudgetModifyPresenter extends StandardPresenter {
    private final IBudgetModifyView budgetModifyView;
    private final IBudgetModel budgetModel;
    private final IProductModel productModel;
    private final ICategoryModel categoryModel;
    private final IBudgetModifyModel budgetModifyModel;

    private int productsRowCountOnPreviewTable = -1;
    private boolean editingProduct = false;
    private int globalClientID = -1;
    private int globalBudgetNumber = -1;
    private String oldClientName = "";

    public BudgetModifyPresenter(IBudgetModifyView budgetModifyView, IBudgetModel budgetModel, IProductModel productModel,
                                 ICategoryModel categoryModel, IBudgetModifyModel budgetModifyModel) {
        this.budgetModifyView = budgetModifyView;
        view = budgetModifyView;
        this.budgetModel = budgetModel;
        this.productModel = productModel;
        this.categoryModel = categoryModel;
        this.budgetModifyModel = budgetModifyModel;

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

        // SUPER LOOP THROUGH PRODUCTS
        for (Product product : products) {
            int categoryID = product.getCategoryID(); // GET CATEGORY ID

            // INNER LOOP THROUGH CATEGORIES NAMES
            for (String categoryName : categoriesName) {
                if (categoryModel.getCategoryID(categoryName) == categoryID) { // IF CATEGORY ID MATCHES
                    productCategoryName = categoryName; // SET PRODUCT CATEGORY NAME TO CATEGORY NAME
                }
            }

            budgetModifyView.setProductStringTableValueAt(rowCount, 0, product.getName()); // SET PRODUCT STRING TABLE VALUE AT ROW COUNT, 0, PRODUCT NAME
            budgetModifyView.setProductStringTableValueAt(rowCount, 1, productCategoryName); // SET PRODUCT STRING TABLE VALUE AT ROW COUNT, 2, PRODUCT CATEGORY NAME
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
            clientID = budgetModel.getClientID(client.getName()); // GET CLIENT ID

            // SET CLIENT TABLE VALUES
            budgetModifyView.setClientIntTableValueAt(rowCount, 0, clientID);
            budgetModifyView.setClientStringTableValueAt(rowCount, 1, client.getName());
            budgetModifyView.setClientStringTableValueAt(rowCount, 2, client.getAddress());
            budgetModifyView.setClientStringTableValueAt(rowCount, 3, client.getCity());
            budgetModifyView.setClientStringTableValueAt(rowCount, 4, client.getPhone());
            budgetModifyView.setClientStringTableValueAt(rowCount, 5, client.isClient() ? "Cliente" : "Particular");
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
                globalClientID = budgetModel.getClientID(clientName); // SET GLOBAL CLIENT ID TO CLIENT ID
                clientSelectedCheckBox.setSelected(true); // SET CLIENT SELECTED CHECK BOX TO SELECTED
            } else {
                budgetModifyView.showMessage(MessageTypes.CLIENT_NOT_SELECTED); // SHOW MESSAGE CLIENT NOT SELECTED
            }
        } else {
            budgetModifyView.showMessage(MessageTypes.CLIENT_NOT_SELECTED); // SHOW MESSAGE CLIENT NOT SELECTED
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


    public void EditProductOnPreviewTable(List<String> productRowData, int row) {
        String productName = productRowData.get(0);
        String productAmountStr = productRowData.get(1);
        String productMeasures = productRowData.get(2);
        String productObservations = productRowData.get(3);

        budgetModifyView.setPreviewStringTableValueAt(row, 1, productName);
        budgetModifyView.setPreviewStringTableValueAt(row, 2, productAmountStr);
        budgetModifyView.setPreviewStringTableValueAt(row, 3, productMeasures);
        budgetModifyView.setPreviewStringTableValueAt(row, 4, productObservations);
    }


    public void AddProductToPreviewTable(Product product, int row) {

        String productName = product.getName();
        String productAmountStr = budgetModifyView.getAmountTextField().getText();
        String productMeasures = budgetModifyView.getMeasuresTextField().getText();
        String productObservations = budgetModifyView.getObservationsTextField().getText();
        Double productPrice = 0.0;

        if (productAmountStr.isEmpty()) { // IF PRODUCT AMOUNT STRING IS EMPTY
            productAmountStr = "1";
        }

        budgetModifyView.setPreviewStringTableValueAt(row, 1, productName); //INSERTA EN LA COLUMNA DE NOMBREPRODUCTO
        budgetModifyView.setPreviewStringTableValueAt(row, 2, productAmountStr); //INSERTA EN LA COLUMNA DE CANTIDAD
        budgetModifyView.setPreviewStringTableValueAt(row, 3, productMeasures); //INSERTA EN LA COLUMNA DE MEDIDAS
        budgetModifyView.setPreviewStringTableValueAt(row, 4, productObservations); //INSERTA EN LA COLUMNA DE OBSERVACIONES
        budgetModifyView.setPreviewStringTableValueAt(row, 5, productPrice.toString()); //INSERTA EN LA COLUMNA DE PRECIO
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

        return productRowData;
    }


    public void onAddProductButtonClicked() {
        Product product; // PRODUCT VARIABLE // ROW COUNT
        int selectedProductRow = budgetModifyView.getProductTableSelectedRow(); // SELECTED PRODUCT ROW
        int productAmountInt = 1; // PRODUCT AMOUNT INT
        int productID = -1; // PRODUCT ID
        double productPrice = -1; // PRODUCT PRICE
        JTextArea priceTextArea = budgetModifyView.getPriceTextArea(); // PRICE TEXT AREA
        StringBuilder sb = budgetModifyView.getStringBuilder(); // STRING BUILDER
        JTable productsTable = budgetModifyView.getProductsResultTable(); // PRODUCTS TABLE

        // IF SELECTED PRODUCT ROW IS NOT -1 (NOT EMPTY)
        if (selectedProductRow != -1) {
            product = GetSelectedProductFromProductsTable(); // GET SELECTED PRODUCT FROM PRODUCTS TABLE

            // IF BOOLEAN GLOBAL VARIABLE "editingProduct" IS FALSE
            if (!editingProduct) {
                AddProductToPreviewTable(product, productsRowCountOnPreviewTable + 1);
                productsRowCountOnPreviewTable++;
            } else {
                EditProductOnPreviewTable(GetProductFromPreviewTable(selectedProductRow), selectedProductRow);
                editingProduct = false;
            }
        }
    }


    public void onPreviewTableDoubleClickedRow(int clickedRow) {
        String productName = "";
        String productMeasures = "";
        String productObservations = "";
        int productAmount = 0;
        int productID = -1;
        Object observations = "";
        Object measures = "";

        if (clickedRow != -1 && clickedRow != 0) {
            System.out.println("SE ACTIVO EL BOOLEAN DE EDITANDO PRODUCTO.");
            editingProduct = true;
            JTable productTable = budgetModifyView.getProductsResultTable();
            productTable.clearSelection();
            budgetModifyView.getPreviewTable().setEnabled(false);
            productAmount = (int) budgetModifyView.getPreviewTable().getValueAt(clickedRow, 2);
            measures = budgetModifyView.getPreviewTable().getValueAt(clickedRow, 3);
            observations = budgetModifyView.getPreviewTable().getValueAt(clickedRow, 4);
        }

        budgetModifyView.setAmountTextField(productAmount);
        budgetModifyView.setMeasuresTextField(productMeasures);
        budgetModifyView.setObservationsTextField(productObservations);
    }


    public void onDeleteProductButtonClicked() {
        budgetModifyView.getProductsResultTable().clearSelection();
        int selectedRow = budgetModifyView.getPreviewTable().getSelectedRow();
        if (selectedRow != -1) {
            StringBuilder sb = budgetModifyView.getStringBuilder();
            JTextArea priceTextArea = budgetModifyView.getPriceTextArea();

            if (productsRowCountOnPreviewTable >= 1) {
                System.out.println("ROW COUNT ON PREVIEW TABLE PREVIOUS DELETE: " + productsRowCountOnPreviewTable);
                budgetModifyView.getPreviewTableModel().removeRow(selectedRow);
                productsRowCountOnPreviewTable--;
                System.out.println("ROW COUNT ON PREVIEW TABLE AFTER DELETE: " + productsRowCountOnPreviewTable);
            }
            //updateTextArea(sb, priceTextArea, false);
        }
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


    public void onModifySearchViewButtonClicked(JTable table, int selectedRow, int budgetNumber)
    {
        budgetModifyView.setWorkingStatus();

        productsRowCountOnPreviewTable = budgetModifyView.getFilledRowsCount(budgetModifyView.getPreviewTable());
        editingProduct = false;
        globalBudgetNumber = budgetNumber;
        oldClientName = budgetModifyModel.getOldClientName(globalBudgetNumber);
        budgetModifyView.getClientSelectedCheckBox().setSelected(true); //MARCO EL CHECKBOX DE QUE YA HAY UN CLIENTE SELECCIONADO
        ArrayList<String> budgetClientData = budgetModifyModel.getSelectedBudgetData(budgetNumber);
        String budgetClientName = budgetClientData.get(1);


        ArrayList<String> productMeasures = getProductsMeasures(budgetNumber, budgetClientName);
        ArrayList<String> productObservations = getProductObservations(budgetNumber, budgetClientName);
        ArrayList<Double> productPrices = getProductPrices(budgetNumber, budgetClientName);
        Multimap<Integer, String> products = getSavedProduct(budgetNumber, budgetClientName);

        setModifyView(products, productObservations, productMeasures, budgetNumber, productPrices);

        budgetModifyView.showView();
        budgetModifyView.setWaitingStatus();
    }

    public ArrayList<Double> getProductPrices(int budgetNumber, String budgetName)
    {
        return budgetModifyModel.getProductPrices(budgetNumber, budgetName);
    }

    public ArrayList<String> getProductObservations(int budgetNumber, String budgetName)
    {
        return budgetModifyModel.getProductObservations(budgetNumber, budgetName);
    }

    public ArrayList<String> getProductsMeasures(int budgetNumber, String budgetName)
    {
        return budgetModifyModel.getProductMeasures(budgetNumber, budgetName);
    }

    public Multimap<Integer,String> getSavedProduct(int budgetNumber, String budgetName) {
        return budgetModifyModel.getSavedProducts(budgetNumber, budgetName);
    }

    public void SetProductsInPreviewTable(ArrayList<Double> prices, Multimap<Integer, String> products, ArrayList<String> measures, ArrayList<String> observations) {
        ArrayList<String> productNames = new ArrayList<>();
        ArrayList<String> productAmounts = new ArrayList<>();

        String productName = "";
        String productMeasure = "";
        String productAmount = "";
        String productObservation = "";
        double productPrice = 0.0;
        int productIndex = 0;

        for(Map.Entry<Integer, String> entry : products.entries())
        {
            productNames.add(entry.getValue());
            productAmounts.add(Integer.toString(entry.getKey()));
        }

        for (int row = 1; row <= products.size(); row++) {
            productName = productNames.get(productIndex);
            productAmount = productAmounts.get(productIndex);
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


    public void setModifyView(Multimap<Integer, String> products, ArrayList<String> observations, ArrayList<String> measures, int budgetNumber, ArrayList<Double> prices) {
        ArrayList<String> budgetData = budgetModifyModel.getSelectedBudgetData(budgetNumber);
        int budgetID = Integer.parseInt(budgetData.get(0));
        String budgetclientName = budgetData.get(1);
        String budgetDate = budgetData.get(2);
        String budgetClientType = budgetData.get(3);
        SetClientInPreviewTable(budgetclientName, budgetClientType);
        SetProductsInPreviewTable(prices, products, measures, observations);
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

        Multimap<Integer, String> products = ArrayListMultimap.create();
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


                products.put(productAmount, productName);
                productObservations.add(productObservation);
                productMeasures.add(productMeasure);
                productPrices.add(productPrice);
            }

            oldBudgetID = budgetModel.getBudgetID(globalBudgetNumber, oldClientName);
            budgetModel.deleteOneBudget(oldBudgetID);
            budgetModel.createBudget(newClientName, date, newClientType, globalBudgetNumber);

            newBudgetID = budgetModel.getMaxBudgetID();
            budgetModel.saveProducts(newBudgetID, products, productObservations, productMeasures, productPrices);
            budgetModel.deleteBudgetProducts(oldClientName, oldBudgetID, globalBudgetNumber);


            budgetModifyView.hideView();
        }
    }


    // ---------> METHODS AND FUNCTIONS END HERE <-------------
    // ---------> METHODS AND FUNCTIONS END HERE <-------------
}
