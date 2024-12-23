package presenters.budget;

//IMPORTS FROM PRESENTERS PACKAGE

import PdfFormater.IPdfConverter;
import PdfFormater.PdfConverter;
import PdfFormater.Row;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import models.*;
import presenters.StandardPresenter;

//IMPORTS FROM UTILS PACKAGE
import utils.*;
import utils.Client;
import utils.Product;

//IMPORTS FROM VIEWS PACKAGE
import views.budget.IBudgetCreateView;

//IMPORTS FROM MODELS PACKAGE

//IMPORTS FROM JAVA
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.ArrayList;
import java.util.List;


//  --------> BUDGET CREATE PRESENTER CLASS STARTS HERE <-------------
//  --------> BUDGET CREATE PRESENTER CLASS STARTS HERE <-------------

public class BudgetCreatePresenter extends StandardPresenter {
    private final IBudgetCreateView budgetCreateView;
    private final IBudgetModel budgetModel;
    private final IProductModel productModel;
    private final ICategoryModel categoryModel;
    private static final IPdfConverter pdfConverter = new PdfConverter();

    double globalBudgetTotalPrice = 0.0;
    private ArrayList<Client> globalClientsList;
    private int globalClientID = -1;
    private int productsRowCountOnPreviewTable = 0; // ROW COUNT ON PREVIEW TABLE

    private boolean editingProduct = false;
    private Product editedProduct;

    public BudgetCreatePresenter(IBudgetCreateView budgetCreateView, IBudgetModel budgetModel, IProductModel productModel,
                                 ICategoryModel categoryModel) {
        this.budgetCreateView = budgetCreateView;
        view = budgetCreateView;
        this.budgetModel = budgetModel;
        this.productModel = productModel;
        this.categoryModel = categoryModel;

        cargarCategorias();
        cargarCiudades();
    }


    // ---------> METHODS AND FUNCTIONS START HERE <-------------
    // ---------> METHODS AND FUNCTIONS START HERE <-------------

    // LISTENERS:
    @Override
    protected void initListeners() {
        budgetModel.addBudgetCreationFailureListener(() -> budgetCreateView.showMessage(MessageTypes.BUDGET_CREATION_FAILURE));
        budgetModel.addBudgetCreationFailureListener(() -> budgetCreateView.showMessage(MessageTypes.BUDGET_CREATION_SUCCESS));
    }


    private void cargarCategorias() {
        List<String> categorias = categoryModel.getCategoriesName();
        budgetCreateView.setCategoriesComboBox(categorias);
    }

    private void cargarCiudades() {
        ArrayList<String> ciudades = budgetModel.getCitiesName();
        budgetCreateView.setCitiesComboBox(ciudades);
    }


    // START METHOD:
    public void onHomeCreateBudgetButtonClicked() {
        globalClientID = -1;
        productsRowCountOnPreviewTable = 0;
        globalBudgetTotalPrice = 0.0;
        editedProduct = null;
        editingProduct = false;
        budgetCreateView.getWidthMeasureTextField().setEnabled(false);
        budgetCreateView.getHeightMeasureTextField().setEnabled(false);
        budgetCreateView.showView();
    }


    // IF THE SEARCH PRODUCTS BUTTON IS CLICKED:
    public void OnSearchProductButtonClicked() {
        //FILTRAR
        String productName = budgetCreateView.getProductsTextField().getText(); // PRODUCT NAME SEARCHED
        List<String> categoriesName = categoryModel.getCategoriesName(); // GET CATEGORIES NAMES
        JComboBox categoryComboBox = budgetCreateView.getCategoriesComboBox(); // GET CATEGORIES COMBO BOX
        String selectedCategory = (String) categoryComboBox.getSelectedItem(); // GET SELECTED CATEGORY
        double productPrice = 0.0;

        //BUSCA SEGUN LOS FILTROS Y AGREGA LOS PRODUCTOS FILTRADOS A UN ARRAYLIST
        ArrayList<Product> products = budgetModel.getProducts(productName, selectedCategory); // GET PRODUCTS BY NAME AND CATEGORY
        String productCategoryName = ""; // PRODUCT CATEGORY NAME STRING VARIABLE
        budgetCreateView.clearProductTable(); // CLEAR PRODUCT TABLE
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

            productPrice = product.calculateRealTimePrice(); // CALCULATE REAL TIME PRICE
            budgetCreateView.setProductStringTableValueAt(rowCount, 0, product.getName()); // SET PRODUCT STRING TABLE VALUE AT ROW COUNT, 0, PRODUCT NAME
            budgetCreateView.setProductStringTableValueAt(rowCount, 1, productCategoryName); // SET PRODUCT STRING TABLE VALUE AT ROW COUNT, 2, PRODUCT CATEGORY NAME
            budgetCreateView.setProductStringTableValueAt(rowCount, 2, String.valueOf(productPrice)); // SET PRODUCT STRING TABLE VALUE AT ROW COUNT, 3, PRODUCT PRICE
            rowCount++; // INCREMENT ROW COUNT
        }
        categoryComboBox.setSelectedIndex(0);
    }


    public void onClientSelectedCheckBoxClicked() {
        JCheckBox clientSelectedCheckBox = budgetCreateView.getClientSelectedCheckBox();
        if (clientSelectedCheckBox.isSelected()) {
            budgetCreateView.setSecondPanelsVisibility();
        } else {
            budgetCreateView.setInitialPanelsVisibility();
        }
    }


    // IF THE SEARCH CLIENT BUTTON IS CLICKED:
    public void OnSearchClientButtonClicked() {
        String city = "";
        String clientType = "";
        city = (String) budgetCreateView.getCitiesComboBox().getSelectedItem(); // GET CITY
        String name = budgetCreateView.getBudgetClientName(); // GET BUDGET CLIENT NAME
        int clientID = -1; // CLIENT ID VARIABLE

        // IF CITY IS "SELECCIONE UNA CIUDAD"
        if (city.equals("Seleccione una ciudad")) {
            city = ""; // SET CITY TO EMPTY STRING
        }


        globalClientsList = budgetModel.getClients(name, city); // GLOBAL VARIABLE -> GET CLIENTS BY NAME AND CITY
        ArrayList<Client> clients = budgetModel.getClients(name, city); // LOCAL VARIABLE -> GET CLIENTS BY NAME AND CITY
        budgetCreateView.clearClientTable(); // CLEAR CLIENT TABLE
        int rowCount = 0; // ROW COUNT VARIABLE

        // LOOP THROUGH CLIENTS
        for (Client client : clients) {
            clientType = "Cliente";

            if (!client.isClient()) {
                clientType = "Particular";
            }

            clientID = budgetModel.getClientID(client.getName(), clientType); // GET CLIENT ID

            // SET CLIENT TABLE VALUES
            budgetCreateView.setClientIntTableValueAt(rowCount, 0, clientID);
            budgetCreateView.setClientStringTableValueAt(rowCount, 1, client.getName());
            budgetCreateView.setClientStringTableValueAt(rowCount, 2, client.getAddress());
            budgetCreateView.setClientStringTableValueAt(rowCount, 3, client.getCity());
            budgetCreateView.setClientStringTableValueAt(rowCount, 4, client.getPhone());
            budgetCreateView.setClientStringTableValueAt(rowCount, 5, client.isClient() ? "Cliente" : "Particular");
            rowCount++; // INCREMENT ROW COUNT
        }
    }


    // IF THE ADD CLIENT BUTTON IS CLICKED:
    public void onAddClientButtonClicked() {
        int selectedRow = budgetCreateView.getClientTableSelectedRow(); // GET SELECTED ROW
        String clientName = ""; // CLIENT NAME STRING VARIABLE
        String clientType = ""; // CLIENT TYPE STRING VARIABLE
        JCheckBox clientSelectedCheckBox = budgetCreateView.getClientSelectedCheckBox(); // GET CLIENT SELECTED CHECK BOX
        DefaultTableModel clientTableModel = budgetCreateView.getClientResultTableModel(); // GET CLIENT RESULT TABLE MODEL

        // IF SELECTED ROW IS NOT -1 (NOT EMPTY)
        if (selectedRow != -1) {
            // IF CLIENT NAME IS NOT NULL AND NOT EMPTY
            if (clientTableModel.getValueAt(selectedRow, 1) != null && !clientTableModel.getValueAt(selectedRow, 1).toString().equals("")) {
                clientName = clientTableModel.getValueAt(selectedRow, 1).toString(); // GET CLIENT NAME FROM TABLE MODEL
                clientType = clientTableModel.getValueAt(selectedRow, 5).toString(); // GET CLIENT TYPE FROM TABLE MODEL
                budgetCreateView.setPreviewStringTableValueAt(0, 0, clientName); // SET PREVIEW STRING TABLE VALUE AT 0, 0, CLIENT NAME
                budgetCreateView.setPreviewStringTableValueAt(0, 6, clientType); // SET PREVIEW STRING TABLE VALUE AT 0, 6, CLIENT TYPE
                globalClientID = budgetModel.getClientID(clientName, clientType); // SET GLOBAL CLIENT ID TO CLIENT ID
                clientSelectedCheckBox.setSelected(true); // SET CLIENT SELECTED CHECK BOX TO SELECTED
            } else {
                budgetCreateView.showMessage(MessageTypes.CLIENT_NOT_SELECTED); // SHOW MESSAGE CLIENT NOT SELECTED
            }
        } else {
            budgetCreateView.showMessage(MessageTypes.CLIENT_NOT_SELECTED); // SHOW MESSAGE CLIENT NOT SELECTED
        }
    }


    public void onCreateButtonClicked() {
        List<String[]> budgetData = budgetCreateView.getPreviewTableFilledRowsData();
        Client client;
        ArrayList<Row> tableContent = new ArrayList<>();

        // BUDGET DATA LIST
        String budgetClientName = ""; // BUDGET CLIENT NAME VARIABLE
        String budgetClientType = ""; // BUDGET CLIENT TYPE VARIABLE
        String budgetDate = budgetCreateView.getBudgetDate(); // BUDGET DATE
        int budgetNumber = budgetModel.getNextBudgetNumber(); // BUDGET NUMBER
        int budgetID = -1;

        List<String> productsName = new ArrayList<>(); // PRODUCTS NAME LIST
        ArrayList<Double> productsPrices = new ArrayList<>(); // PRODUCTS PRICE LIST
        List<Integer> productsAmount = new ArrayList<>(); // PRODUCTS AMOUNT LIST
        ArrayList<String> productsMeasures = new ArrayList<>(); // PRODUCTS MEASURE LIST
        ArrayList<String> productsObservations = new ArrayList<>(); // PRODUCTS OBSERVATIONS LIST
        Multimap<Integer, String> products = ArrayListMultimap.create(); // PRODUCTS MULTIMAP

        // LOOP THROUGH BUDGET DATA

        if (!budgetData.isEmpty() && !onEmptyFields(0, 1)) {

            for (int i = 0; i < budgetData.size(); i++) {
                if (i == 0) {
                    budgetClientName = budgetData.get(i)[0]; // SET BUDGET CLIENT NAME TO BUDGET DATA AT I, 0
                    budgetClientType = budgetData.get(i)[1]; // SET BUDGET CLIENT TYPE TO BUDGET DATA AT I, 6
                } else {
                    productsName.add(budgetData.get(i)[0]); // ADD BUDGET DATA AT I, 0 TO PRODUCTS NAME LIST
                    productsAmount.add(Integer.parseInt(budgetData.get(i)[1])); // ADD BUDGET DATA AT I, 2 TO PRODUCTS AMOUNT LIST
                    productsMeasures.add(budgetData.get(i)[2]); // ADD BUDGET DATA AT I, 3 TO PRODUCTS MEASURE LIST
                    productsObservations.add(budgetData.get(i)[3]); // ADD BUDGET DATA AT I, 4 TO PRODUCTS OBSERVATIONS LIST

                    if (budgetData.get(i)[4].isEmpty()) { // IF BUDGET DATA AT I, 5 IS EMPTY (PRICE)
                        productsPrices.add(0.0); // ADD 0.0 TO PRODUCTS PRICE LIST
                    } else {
                        productsPrices.add(Double.parseDouble(budgetData.get(i)[4])); // ADD BUDGET DATA AT I, 1 TO PRODUCTS PRICE LIST
                    }
                }
            }

            // LOOP THROUGH PRODUCTS NAME
            for (int i = 0; i < productsName.size(); i++) {
                products.put(productsAmount.get(i), productsName.get(i)); // PUT I, PRODUCTS NAME AT I
            }

            // CREATE BUDGET
            budgetModel.createBudget(budgetClientName, budgetDate, budgetClientType, budgetNumber);
            budgetCreateView.showMessage(MessageTypes.BUDGET_CREATION_SUCCESS);

            //INSERT THE PRODUCTS INTO THE BUDGET_PRODUCTS TABLE
            budgetID = budgetModel.getBudgetID(budgetNumber, budgetClientName);
            budgetModel.saveProducts(budgetID, products, productsObservations, productsMeasures, productsPrices);

            //PDF CREATION
            client = GetOneClientByID(budgetClientName, budgetClientType);
            tableContent = GetAllProductsFromPreviewTable();
            GeneratePDF(client, tableContent, budgetNumber);


            budgetCreateView.restartWindow(); // RESTART WINDOW
            budgetCreateView.getWindowFrame().dispose(); // CLOSE WINDOW

        } else{
            budgetCreateView.showMessage(MessageTypes.BUDGET_CREATION_FAILURE);
        }

        //SET WAITING STATUS
        budgetCreateView.setWaitingStatus();
    }

    public void GeneratePDF(Client client, ArrayList<Row> tableContent, int budgetNumber) {
        try {
            if(!client.isClient()){
                globalBudgetTotalPrice*=1.25;
            }
            pdfConverter.generateBill(false, client, budgetNumber, tableContent, globalBudgetTotalPrice);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public Product GetSelectedProductFromProductsTable() {
        int selectedProductRow = budgetCreateView.getProductTableSelectedRow();
        Product product = null;

        if (selectedProductRow != -1) {
            String productName = budgetCreateView.getProductStringTableValueAt(selectedProductRow, 0);
            product = productModel.getOneProduct(productModel.getProductID(productName));
        }

        return product;
    }


    public double GetSelectedTotalPrice(int selectedPreviewRow) {
        double totalPrice = 0.0;
        double oneProductPrice = Double.parseDouble(budgetCreateView.getPreviewStringTableValueAt(selectedPreviewRow, 5));
        int productAmount = Integer.parseInt(budgetCreateView.getPreviewStringTableValueAt(selectedPreviewRow, 2));
        if (selectedPreviewRow != -1) {
            totalPrice = oneProductPrice * productAmount;
        }
        return totalPrice;
    }

    public boolean CheckMeasureFieldsAreEnabled()
    {
        JTextField widthMeasureTextField = budgetCreateView.getWidthMeasureTextField();
        JTextField heightMeasureTextField = budgetCreateView.getHeightMeasureTextField();
        boolean areUnlocked = false;

        if(widthMeasureTextField.isEnabled() || heightMeasureTextField.isEnabled())
        {
            areUnlocked = true;
        }

        return areUnlocked;
    }


    //AGREGA EL PRODUCTO A LA PREVIEW TABLE CUANDO CLICKEA EL BOTON "AGREGAR PRODUCTO"
    public void AddProductToPreviewTable(Product product, int row) {

        String productName = product.getName();
        String productAmountStr = budgetCreateView.getAmountTextField().getText();
        String productWidthMeasures = budgetCreateView.getWidthMeasureTextField().getText();
        String productHeightMeasures = budgetCreateView.getHeightMeasureTextField().getText();
        String productObservations = budgetCreateView.getObservationsTextField().getText();
        boolean unlockedMeasures = CheckMeasureFieldsAreEnabled();
        double oneItemProductPrice = product.calculateRealTimePrice();
        double totalItemsPrice = 0.0;
        double settingPrice = 0.0;

        if (productAmountStr.isEmpty()) { // IF PRODUCT AMOUNT STRING IS EMPTY
            productAmountStr = "1";
        }

        if(unlockedMeasures){
            if(productHeightMeasures.isEmpty()){
                productHeightMeasures = "1";
            }
            if(productWidthMeasures.isEmpty()){
                productWidthMeasures = "1";
            }
            int meters = Integer.parseInt(productWidthMeasures) * Integer.parseInt(productHeightMeasures);
            totalItemsPrice = oneItemProductPrice * Integer.parseInt(productAmountStr) * meters;
            settingPrice = oneItemProductPrice * meters;
        } else {
            productWidthMeasures = "-";
            productHeightMeasures = "-";
            totalItemsPrice = oneItemProductPrice * Integer.parseInt(productAmountStr);
            settingPrice = oneItemProductPrice;
        }

        String productMeasures = productWidthMeasures + " x " + productHeightMeasures;


        budgetCreateView.setPreviewStringTableValueAt(row, 1, productName); //INSERTA EN LA COLUMNA DE NOMBREPRODUCTO
        budgetCreateView.setPreviewStringTableValueAt(row, 2, productAmountStr); //INSERTA EN LA COLUMNA DE CANTIDAD
        budgetCreateView.setPreviewStringTableValueAt(row, 3, productMeasures); //INSERTA EN LA COLUMNA DE MEDIDAS
        budgetCreateView.setPreviewStringTableValueAt(row, 4, productObservations); //INSERTA EN LA COLUMNA DE OBSERVACIONES
        budgetCreateView.setPreviewStringTableValueAt(row, 5, String.valueOf(settingPrice)); //INSERTA EN LA COLUMNA DE PRECIO

        if (!editingProduct) {
            updateTextArea(true, totalItemsPrice);
            System.out.println("ONEITEMPRODUCTPRICE: " + oneItemProductPrice +" TOTALITEMSPRICE: " + totalItemsPrice +
                   " PRODUCT AMOUNT: " + productAmountStr +  "GLOBAL PRICE: " + globalBudgetTotalPrice);
        }
    }


    //PARA CUANDO SE CREA EL PRESUPUESTO
    public List<String> GetOneProductFromPreviewTable(int row) {
        List<String> productRowData = new ArrayList<>();
        String productName = budgetCreateView.getPreviewStringTableValueAt(row, 1);
        String productAmount = budgetCreateView.getPreviewStringTableValueAt(row, 2);
        String productMeasures = budgetCreateView.getPreviewStringTableValueAt(row, 3);
        String productObservations = budgetCreateView.getPreviewStringTableValueAt(row, 4);

        productRowData.add(productName);
        productRowData.add(productAmount);
        productRowData.add(productMeasures);
        productRowData.add(productObservations);

        return productRowData;
    }

    public ArrayList<Row> GetAllProductsFromPreviewTable() {
        ArrayList<Row> productRowData = new ArrayList<>();
        List<String> oneProduct = new ArrayList<>();

        Product product;
        double productPrice = 0.0;
        double totalPrice = 0.0;
        Row row;

        for (int i = 1; i <= productsRowCountOnPreviewTable; i++) {
            oneProduct = GetOneProductFromPreviewTable(i);
            product = productModel.getOneProduct(productModel.getProductID(oneProduct.get(0)));
            productPrice = product.calculateRealTimePrice();
            totalPrice = productPrice * Integer.parseInt(oneProduct.get(1));

            row = new Row(product.getName(), Integer.parseInt(oneProduct.get(1)), oneProduct.get(2), oneProduct.get(3), productPrice, totalPrice);
            productRowData.add(row);
        }
        return productRowData;
    }

    public Client GetOneClientByID(String clientName, String clientType) {
        int clientID = budgetModel.getClientID(clientName, clientType);
        return budgetModel.GetOneClientByID(clientID);
    }

    public void onAddProductButtonClicked() {
        Product product; // PRODUCT VARIABLE
        int selectedProductRow = budgetCreateView.getProductTableSelectedRow(); // SELECTED PRODUCT ROW
        int selectedPreviewRow = budgetCreateView.getPreviewTableSelectedRow(); // SELECTED PREVIEW ROW

        if (selectedProductRow != -1) {
            if (!editingProduct) {
                product = GetSelectedProductFromProductsTable();
                AddProductToPreviewTable(product, productsRowCountOnPreviewTable + 1);
                productsRowCountOnPreviewTable++;
            }
        } else {
            EditProduct(editedProduct, selectedPreviewRow);
            FinishProductEditingOnPreviewTable();
        }

    }

    public void FinishProductEditingOnPreviewTable() {
        editingProduct = false;
        editedProduct = null;
        budgetCreateView.getPreviewTable().clearSelection();
        budgetCreateView.getPreviewTable().setEnabled(true);
    }

    private void EditProduct(Product product, int selectedRow) {
        System.out.println("GLOBAL PRICE: " + globalBudgetTotalPrice);

        int initialProductAmount = Integer.parseInt(budgetCreateView.getPreviewStringTableValueAt(selectedRow, 2));
        double initialProductPrice = Double.parseDouble(budgetCreateView.getPreviewStringTableValueAt(selectedRow, 5));
        double removePrice = initialProductAmount * initialProductPrice;
        System.out.println("REMOVE PRICE: " + removePrice);

        AddProductToPreviewTable(product, selectedRow);

        int finalProductAmount = Integer.parseInt(budgetCreateView.getPreviewStringTableValueAt(selectedRow, 2));
        double finalProductPrice = Double.parseDouble(budgetCreateView.getPreviewStringTableValueAt(selectedRow, 5));
        double addingPrice = finalProductAmount * finalProductPrice;
        System.out.println("ADDING PRICE: " + addingPrice);

        double finalPriceEdit = addingPrice - removePrice;
        System.out.println("FINAL PRICE EDIT: " + finalPriceEdit);

        if (finalPriceEdit != 0) {
            if (finalPriceEdit > 0) {
                updateTextArea(true, finalPriceEdit);
            } else {
                finalPriceEdit = finalPriceEdit * -1;
                System.out.println("FINAL FINAL PRICE EDIT: " + finalPriceEdit);
                updateTextArea(false, finalPriceEdit);
            }
        }
    }


    public void onPreviewTableDoubleClickedRow(int clickedRow) {
        String productWidthMeasures = "";
        String productHeightMeasures = "";
        String productObservations = "";
        String productName = "";
        int multiplierIndex = 0;
        int productAmount = 0;
        int productID = -1;
        Object productNameObject = budgetCreateView.getPreviewTable().getValueAt(clickedRow, 1);
        Object productObservationsObject = budgetCreateView.getPreviewTable().getValueAt(clickedRow, 4);
        Object productMeasuresObject = budgetCreateView.getPreviewTable().getValueAt(clickedRow, 3);
        Object productAmountObject = budgetCreateView.getPreviewTable().getValueAt(clickedRow, 2);

        if (clickedRow != -1 && clickedRow != 0) {
            System.out.println("SE ACTIVO EL BOOLEAN DE EDITANDO PRODUCTO.");
            editingProduct = true;
            JTable productTable = budgetCreateView.getProductsResultTable();
            productTable.clearSelection();
            budgetCreateView.getPreviewTable().setEnabled(false);

            multiplierIndex = ((String) productMeasuresObject).indexOf('x');
            productName = (String) productNameObject;
            productAmount = Integer.parseInt((String) productAmountObject);
            productWidthMeasures = ((String) productMeasuresObject).substring(0, multiplierIndex - 2);
            productHeightMeasures = ((String) productMeasuresObject).substring(multiplierIndex + 2);
            productObservations = (String) productObservationsObject;
            productID = productModel.getProductID(productName);

            editedProduct = productModel.getOneProduct(productID);
        }

        budgetCreateView.setAmountTextField(productAmount);
        budgetCreateView.setWidthMeasureTextField(productWidthMeasures);
        budgetCreateView.setHeightMeasureTextField(productHeightMeasures);
        budgetCreateView.setObservationsTextField(productObservations);
    }

    public void onDeleteProductButtonClicked() {
        budgetCreateView.getProductsResultTable().clearSelection();
        int selectedRow = budgetCreateView.getPreviewTableSelectedRow();

        double totalSelectedPrice = 0.0;


        if (selectedRow != -1) {
            if (productsRowCountOnPreviewTable >= 1) {
                totalSelectedPrice = GetSelectedTotalPrice(selectedRow);
                budgetCreateView.getPreviewTableModel().removeRow(selectedRow);
                productsRowCountOnPreviewTable--;
                updateTextArea(false, totalSelectedPrice);
                System.out.println("TOTAL SELECTED PRICE: " + totalSelectedPrice);
                System.out.println("GLOBAL PRICE: " + globalBudgetTotalPrice);
            }
        }
    }

    public boolean onEmptyFields(int clientNameColumn, int productColumn) {
        boolean anyEmpty = false;
        String clientName = budgetCreateView.getPreviewStringTableValueAt(0, clientNameColumn);
        String product = budgetCreateView.getPreviewStringTableValueAt(1, productColumn);

        if ((clientName == null || clientName.trim().isEmpty()) ||
                (product == null || product.trim().isEmpty())) {
            anyEmpty = true;
        }
        return anyEmpty;
    }

    public ArrayList<Object> GetTextAreaAndStringBuilder() {
        ArrayList<Object> textAreaAndStringBuilder = new ArrayList<>();
        textAreaAndStringBuilder.add(budgetCreateView.getPriceTextArea());
        textAreaAndStringBuilder.add(budgetCreateView.getStringBuilder());
        return textAreaAndStringBuilder;
    }

    public void updateTextArea(boolean adding, double productPrice) {
        ArrayList<Object> textAreaAndStringBuilder = GetTextAreaAndStringBuilder();
        JTextArea textArea = (JTextArea) textAreaAndStringBuilder.get(0);
        StringBuilder stb = (StringBuilder) textAreaAndStringBuilder.get(1);

        if (adding) {
            globalBudgetTotalPrice += productPrice;
        } else {
            globalBudgetTotalPrice -= productPrice;
        }

        stb.setLength(0);
        stb.append("Precio total: $").append(globalBudgetTotalPrice);
        textArea.setText(stb.toString());
    }


    // ---------> METHODS AND FUNCTIONS END HERE <-------------
    // ---------> METHODS AND FUNCTIONS END HERE <-------------
}








