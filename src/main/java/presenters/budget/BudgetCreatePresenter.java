package presenters.budget;

//IMPORTS FROM PRESENTERS PACKAGE

import PdfFormater.SamplePDFCreation;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import presenters.StandardPresenter;

//IMPORTS FROM UTILS PACKAGE
import utils.*;
import utils.Client;
import utils.Product;
import utils.MessageTypes.*;

//IMPORTS FROM VIEWS PACKAGE
import views.budget.BudgetCreateView;
import views.budget.IBudgetCreateView;

//IMPORTS FROM MODELS PACKAGE
import models.ICategoryModel;
import models.IBudgetModel;
import models.IProductModel;

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

    private ArrayList<Client> globalClientsList;
    private int globalClientID = -1;
    private int productsRowCountOnPreviewTable = 1; // ROW COUNT ON PREVIEW TABLE

    private boolean editingProduct = false;

    public BudgetCreatePresenter(IBudgetCreateView budgetCreateView, IBudgetModel budgetModel, IProductModel productModel,
                                 ICategoryModel categoryModel)
    {
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
        productsRowCountOnPreviewTable = 1;
        editingProduct = false;
        budgetCreateView.showView();
    }


    // IF THE SEARCH PRODUCTS BUTTON IS CLICKED:
    public void OnSearchProductButtonClicked() {
        String productName = budgetCreateView.getProductsTextField().getText(); // PRODUCT NAME SEARCHED
        List<String> categoriesName = categoryModel.getCategoriesName(); // GET CATEGORIES NAMES
        JComboBox categoryComboBox = budgetCreateView.getCategoriesComboBox(); // GET CATEGORIES COMBO BOX
        String selectedCategory = (String) categoryComboBox.getSelectedItem(); // GET SELECTED CATEGORY
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

            budgetCreateView.setProductStringTableValueAt(rowCount, 0, product.getName()); // SET PRODUCT STRING TABLE VALUE AT ROW COUNT, 0, PRODUCT NAME
            budgetCreateView.setProductStringTableValueAt(rowCount, 1, productCategoryName); // SET PRODUCT STRING TABLE VALUE AT ROW COUNT, 2, PRODUCT CATEGORY NAME
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
            clientID = budgetModel.getClientID(client.getName()); // GET CLIENT ID

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
                globalClientID = budgetModel.getClientID(clientName); // SET GLOBAL CLIENT ID TO CLIENT ID
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

        // BUDGET DATA LIST
        String budgetClientName = ""; // BUDGET CLIENT NAME VARIABLE
        String budgetClientType = ""; // BUDGET CLIENT TYPE VARIABLE
        String budgetDate = budgetCreateView.getBudgetDate(); // BUDGET DATE
        int budgetNumber = budgetModel.getNextBudgetNumber(); // BUDGET NUMBER
        int budgetID = -1;

        List<String> productsName = new ArrayList<>(); // PRODUCTS NAME LIST
        List<Double> productsPrice = new ArrayList<>(); // PRODUCTS PRICE LIST
        List<Integer> productsAmount = new ArrayList<>(); // PRODUCTS AMOUNT LIST
        ArrayList<String> productsMeasure = new ArrayList<>(); // PRODUCTS MEASURE LIST
        ArrayList<String> productsObservations = new ArrayList<>(); // PRODUCTS OBSERVATIONS LIST
        Multimap<Integer, String> products = ArrayListMultimap.create(); // PRODUCTS MULTIMAP

        // LOOP THROUGH BUDGET DATA
        for (int i = 0; i < budgetData.size(); i++) {


            if (i == 0) {
                System.out.println("ROW: " + i);

                budgetClientName = budgetData.get(i)[0]; // SET BUDGET CLIENT NAME TO BUDGET DATA AT I, 0
                System.out.println("BUDGET CLIENT NAME: " + budgetClientName);
                budgetClientType = budgetData.get(i)[1]; // SET BUDGET CLIENT TYPE TO BUDGET DATA AT I, 6
                System.out.println("BUDGET CLIENT TYPE: " + budgetClientType);


            } else {
                System.out.println("ROW: " + i);

                productsName.add(budgetData.get(i)[0]); // ADD BUDGET DATA AT I, 0 TO PRODUCTS NAME LIST
                System.out.println("PRODUCT NAME: " + budgetData.get(i)[0]);

                System.out.println("PRODUCT AMOUNT: " + budgetData.get(i)[1]);
                productsAmount.add(Integer.parseInt(budgetData.get(i)[1])); // ADD BUDGET DATA AT I, 2 TO PRODUCTS AMOUNT LIST

                productsMeasure.add(budgetData.get(i)[2]); // ADD BUDGET DATA AT I, 3 TO PRODUCTS MEASURE LIST
                System.out.println("PRODUCT MEASURE: " + budgetData.get(i)[3]);

                productsObservations.add(budgetData.get(i)[3]); // ADD BUDGET DATA AT I, 4 TO PRODUCTS OBSERVATIONS LIST
                System.out.println("PRODUCT OBSERVATIONS: " + budgetData.get(i)[4]);

                if(budgetData.get(i)[4].isEmpty()) { // IF BUDGET DATA AT I, 5 IS EMPTY (PRICE)
                    System.out.println("PRODUCT PRICE: 0.0");
                    productsPrice.add(0.0); // ADD 0.0 TO PRODUCTS PRICE LIST
                } else{
                    System.out.println("PRODUCT PRICE: " + Double.parseDouble(budgetData.get(i)[4]));
                    productsPrice.add(Double.parseDouble(budgetData.get(i)[4])); // ADD BUDGET DATA AT I, 1 TO PRODUCTS PRICE LIST
                }
            }
        }

        // LOOP THROUGH PRODUCTS NAME
        for (int i = 0; i < productsName.size(); i++) {
            products.put(productsAmount.get(i), productsName.get(i)); // PUT I, PRODUCTS NAME AT I
        }


        //TRY TO INSERT THE BUDGET INTO THE BUDGETS TABLE
        if(onEmptyFields(0, 1)) {
            budgetCreateView.showMessage(MessageTypes.BUDGET_CREATION_FAILURE);
        } else {
            // CREATE BUDGET
            budgetModel.createBudget(budgetClientName, budgetDate, budgetClientType, budgetNumber);
            budgetCreateView.showMessage(MessageTypes.BUDGET_CREATION_SUCCESS);

            //INSERT THE PRODUCTS INTO THE BUDGET_PRODUCTS TABLE
            budgetID = budgetModel.getBudgetID(budgetNumber, budgetClientName);
            budgetModel.saveProducts(budgetID, products, productsObservations, productsMeasure);

            budgetCreateView.restartWindow(); // RESTART WINDOW
            budgetCreateView.getWindowFrame().dispose(); // CLOSE WINDOW
        }

        //PDF CREATION
        //SamplePDFCreation.createPDF(false, oneClientList.get(0), budgetNumber, rows, finalPrice);

        //SET WAITING STATUS
        budgetCreateView.setWaitingStatus();
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




    public void EditProductOnPreviewTable(List<String> productRowData, int row) {
        String productName = productRowData.get(0);
        String productAmountStr = productRowData.get(1);
        String productMeasures = productRowData.get(2);
        String productObservations = productRowData.get(3);

        budgetCreateView.setPreviewStringTableValueAt(row, 1, productName);
        budgetCreateView.setPreviewStringTableValueAt(row, 2, productAmountStr);
        budgetCreateView.setPreviewStringTableValueAt(row, 3, productMeasures);
        budgetCreateView.setPreviewStringTableValueAt(row, 4, productObservations);
    }




    public void AddProductToPreviewTable(Product product, int row) {

        String productName = product.getName();
        String productAmountStr = budgetCreateView.getAmountTextField().getText();
        String productMeasures = budgetCreateView.getMeasuresTextField().getText();
        String productObservations = budgetCreateView.getObservationsTextField().getText();

        if(productAmountStr.isEmpty()) { // IF PRODUCT AMOUNT STRING IS EMPTY
            productAmountStr = "1";
        }

        budgetCreateView.setPreviewStringTableValueAt(row, 1, productName); //INSERTA EN LA COLUMNA DE NOMBREPRODUCTO
        budgetCreateView.setPreviewStringTableValueAt(row, 2, productAmountStr); //INSERTA EN LA COLUMNA DE CANTIDAD
        budgetCreateView.setPreviewStringTableValueAt(row, 3, productMeasures); //INSERTA EN LA COLUMNA DE MEDIDAS
        budgetCreateView.setPreviewStringTableValueAt(row, 4, productObservations); //INSERTA EN LA COLUMNA DE OBSERVACIONES
    }




    public List<String> GetProductFromPreviewTable(int row) {
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




    public void onAddProductButtonClicked() {
        Product product; // PRODUCT VARIABLE
        int selectedProductRow = budgetCreateView.getProductTableSelectedRow(); // SELECTED PRODUCT ROW
        int productAmountInt = 1; // PRODUCT AMOUNT INT
        int productID = -1; // PRODUCT ID
        double productPrice = -1; // PRODUCT PRICE
        JTextArea priceTextArea = budgetCreateView.getPriceTextArea(); // PRICE TEXT AREA
        StringBuilder sb = budgetCreateView.getStringBuilder(); // STRING BUILDER
        JTable productsTable = budgetCreateView.getProductsResultTable(); // PRODUCTS TABLE

        // IF SELECTED PRODUCT ROW IS NOT -1 (NOT EMPTY)
        if (selectedProductRow != -1) {
            product = GetSelectedProductFromProductsTable(); // GET SELECTED PRODUCT FROM PRODUCTS TABLE

            // IF BOOLEAN GLOBAL VARIABLE "editingProduct" IS FALSE
            if (!editingProduct) {
                AddProductToPreviewTable(product, productsRowCountOnPreviewTable);
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
            JTable productTable = budgetCreateView.getProductsResultTable();
            productTable.clearSelection();
            budgetCreateView.getPreviewTable().setEnabled(false);
            productAmount = (int) budgetCreateView.getPreviewTable().getValueAt(clickedRow, 2);
            measures = budgetCreateView.getPreviewTable().getValueAt(clickedRow, 3);
            observations = budgetCreateView.getPreviewTable().getValueAt(clickedRow, 4);
        }

        budgetCreateView.setAmountTextField(productAmount);
        budgetCreateView.setMeasuresTextField(productMeasures);
        budgetCreateView.setObservationsTextField(productObservations);
    }


    public void onDeleteProductButtonClicked() {
        budgetCreateView.getProductsResultTable().clearSelection();
        int selectedRow = budgetCreateView.getPreviewTable().getSelectedRow();
        if (selectedRow != -1) {
            StringBuilder sb = budgetCreateView.getStringBuilder();
            JTextArea priceTextArea = budgetCreateView.getPriceTextArea();
            budgetCreateView.getPreviewTableModel().removeRow(selectedRow);

            if(productsRowCountOnPreviewTable > 1)
            {
                productsRowCountOnPreviewTable--;
            }
            //updateTextArea(sb, priceTextArea, false);
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




    // ---------> METHODS AND FUNCTIONS END HERE <-------------
    // ---------> METHODS AND FUNCTIONS END HERE <-------------
}




