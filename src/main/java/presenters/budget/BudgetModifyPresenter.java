package presenters.budget;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import models.*;
import models.IBudgetModifyModel;
import presenters.StandardPresenter;
import utils.Client;
import utils.MessageTypes;
import utils.Product;
import views.budget.modify.IBudgetModifyView;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;

public class BudgetModifyPresenter extends StandardPresenter {
    private final IBudgetModifyView budgetModifyView;
    private final IBudgetModifyModel budgetModifyModel;
    private final IBudgetModel budgetModel;
    private final ICategoryModel categoryModel;
    private final IProductModel productModel;
    private Multimap<Integer, Product> productsList = ArrayListMultimap.create();
    private ArrayList<Client> globalClientsList;
    private int globalClientID = -1;
    private int budgetNumber = -1;
    private boolean editingProduct = false;
    private int rowCountOnPreviewTable = 0; 

    public BudgetModifyPresenter(IBudgetModifyView budgetModifyView, IBudgetModifyModel budgetModifyModel, ICategoryModel categoryModel, IBudgetModel budgetModel, IProductModel productModel) {
        this.budgetModifyView = budgetModifyView;
        view = budgetModifyView;
        this.budgetModifyModel = budgetModifyModel;
        this.categoryModel = categoryModel;
        this.budgetModel = budgetModel;
        this.productModel = productModel;

        budgetModifyView.getClientsSearchButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String city = (String) budgetModifyView.getCitiesComboBox().getSelectedItem();
                String name = budgetModifyView.getBudgetClientName();
                int clientID = -1;
                if (city.equals("Seleccione una ciudad")) {
                    city = "";
                }
                globalClientsList = budgetModel.getClients(name, city);
                ArrayList<Client> clients = budgetModel.getClients(name, city);
                budgetModifyView.clearClientTable();
                int rowCount = 0;
                for (Client client : clients) {
                    clientID = budgetModel.getClientID(client.getName());

                    budgetModifyView.setClientIntTableValueAt(rowCount, 0, clientID);
                    budgetModifyView.setClientStringTableValueAt(rowCount, 1, client.getName());
                    budgetModifyView.setClientStringTableValueAt(rowCount, 2, client.getAddress());
                    budgetModifyView.setClientStringTableValueAt(rowCount, 3, client.getCity());
                    budgetModifyView.setClientStringTableValueAt(rowCount, 4, client.getPhone());
                    budgetModifyView.setClientStringTableValueAt(rowCount, 5, client.isClient() ? "Cliente" : "Particular");
                    rowCount++;
                }
            }
        });

        budgetModifyView.getClientAddButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = budgetModifyView.getClientTableSelectedRow();
                String clientName = "";
                String clientType = "";
                JCheckBox clientSelectedCheckBox = budgetModifyView.getClientSelectedCheckBox();
                if (selectedRow != -1) {
                    clientName = budgetModifyView.getClientStringTableValueAt(selectedRow, 1);
                    clientType = budgetModifyView.getClientStringTableValueAt(selectedRow, 5);
                    budgetModifyView.setPreviewStringTableValueAt(0, 0, clientName);
                    budgetModifyView.setPreviewStringTableValueAt(0, 5, clientType);
                    globalClientID = budgetModel.getClientID(clientName);
                    clientSelectedCheckBox.setSelected(true);
                } else {
                    budgetModifyView.showMessage(MessageTypes.CLIENT_NOT_SELECTED);
                }
            }
        });

        cargarCategorias();
        cargarCiudades();
    }

    public void onSaveModificationsButtonClicked() {
        int newBudgetID = -1;
        int oldBudgetID = -1;
        String newClientName = budgetModifyView.getPreviewStringTableValueAt(0, 0);
        String newClientType = budgetModifyView.getPreviewStringTableValueAt(0, 5);
        String oldClientName = budgetModifyModel.getOldClientName(budgetNumber);
        String date = budgetModifyView.getBudgetDate();
        String productName = "";
        int productAmount = 0;
        String productObservation = "";
        String productMeasure = "";
        String measuresObservations = "";
        Multimap<Integer, String> products = ArrayListMultimap.create();
        ArrayList<String> productObservations = new ArrayList<>();
        ArrayList<String> productMeasures = new ArrayList<>();
        boolean anyEmpty = onEmptyFields(0, 1);

        if (anyEmpty) {
            budgetModifyView.showMessage(MessageTypes.BUDGET_CREATION_EMPTY_COLUMN);
        } else {
            for (int row = 1; row <= (rowCountOnPreviewTable - 1); row++) {
                System.out.println("FILA: " + row);
                System.out.println("CANTIDAD DE FILAS: " + rowCountOnPreviewTable);
                productName = budgetModifyView.getPreviewStringTableValueAt(row, 1);
                System.out.println("METODO DEL BUDGETMODIFYPRESENTER CLASS: PRODUCTO: " + productName);
                productAmount = budgetModifyView.getPreviewIntTableValueAt(row, 2);
                System.out.println("METODO DEL BUDGETMODIFYPRESENTER CLASS: CANTIDAD: " + productAmount);
                measuresObservations = budgetModifyView.getPreviewStringTableValueAt(row, 3);
                System.out.println("METODO DEL BUDGETMODIFYPRESENTER CLASS: MEDIDAS Y OBSERVACIONES: " + measuresObservations);

                if (!measuresObservations.equals("") && !(measuresObservations == null)) {
                    if (measuresObservations.contains("Medidas:") && measuresObservations.contains("Observaciones:")) {
                        int measuresIndexStart = measuresObservations.indexOf("Medidas:");
                        int measuresIndexEnd = measuresObservations.indexOf("Observaciones:");
                        productMeasure = measuresObservations.substring(measuresIndexStart + 8, measuresIndexEnd);
                        productObservation = measuresObservations.substring(measuresIndexEnd + 14);
                    } else if (measuresObservations.contains("Medidas:") && !measuresObservations.contains("Observaciones:")) {
                        productMeasure = measuresObservations;
                    } else if (measuresObservations.contains("Observaciones:") && !measuresObservations.contains("Medidas:")) {
                        productObservation = measuresObservations;
                    }
                }


                products.put(productAmount, productName);
                productObservations.add(productObservation);
                productMeasures.add(productMeasure);
            }

            System.out.println("PRODUCTOS A AGREGAR: (METODO DEL BUDGETMODIFYPRESENTER CLASS)");
            for(Map.Entry<Integer, String> entry : products.entries()) {
                System.out.println("CANTIDAD: " + entry.getKey() + " PRODUCTO: " + entry.getValue());
            }

            oldBudgetID = budgetModel.getBudgetID(budgetNumber, oldClientName);
            System.out.println("ID DEL VIEJO BUDGET: " + oldBudgetID);
            budgetModel.createBudget(newClientName, date, newClientType, budgetNumber);
            newBudgetID = budgetModel.getMaxBudgetID();
            System.out.println("ID DEL NUEVO BUDGET: " + newBudgetID);
            budgetModel.saveProducts(newBudgetID, products, productObservations, productMeasures);
            budgetModel.deleteBudgetProducts(oldClientName, oldBudgetID, budgetNumber, true);
            budgetModel.deleteOneBudget(oldBudgetID);
            budgetModifyView.hideView();
        }
    }

    public void setModifyView(int budgetNumber, int selectedBudgetRow, Multimap<Integer, String> products, ArrayList<String> productObservations, ArrayList<String> productMeasures) {
        budgetModifyView.showView();
        budgetModifyView.getClientSelectedCheckBox().setSelected(true);
        rowCountOnPreviewTable = budgetModifyView.countNonEmptyCells(budgetModifyView.getPreviewTable(), 1) + 1;
        JTextArea textArea = budgetModifyView.getPriceTextArea();
        StringBuilder sb = budgetModifyView.getStringBuilder();
        ArrayList<String> budgetData = budgetModifyModel.getSelectedBudgetData(budgetNumber);
        String clientName = budgetData.get(0);
        String date = budgetData.get(1);
        String clientType = budgetData.get(2);
        int productIndex = 1;
        String productObservation = "";
        String productMeasure = "";
        String productName = "";
        String productString = "";
        double productPrice = 0;
        int productAmount = 0;
        int observationsIndex = 0;
        int measuresIndex = 0;
        int productID = -1;
        int rowCount = budgetModifyView.countNonEmptyCells(budgetModifyView.getPreviewTable(), 1);
        int productAmountToSave = 0;
        Multimap<Integer, Product> productsMap = ArrayListMultimap.create();
        budgetModifyView.setPreviewStringTableValueAt(0, 0, clientName);
        budgetModifyView.setPreviewStringTableValueAt(0, 5, clientType);

        for (Map.Entry<Integer, String> entry : products.entries()) {

            productAmountToSave = entry.getKey();
            productAmount = entry.getKey();
            productName = entry.getValue();
            productObservation = productObservations.get(observationsIndex);
            productMeasure = productMeasures.get(measuresIndex);
            productID = productModel.getProductID(productName);
            Product product = productModel.getOneProduct(productID);
            productPrice = product.getPrice() * productAmount;
            budgetModifyView.setPreviewStringTableValueAt(productIndex, 1, productName);
            budgetModifyView.setPreviewIntTableValueAt(productIndex, 2, productAmount);
            if (!productMeasure.equals("") && !productObservation.equals("")) {
                productString = "Medidas: " + productMeasure + " || Observaciones: " + productObservation;
            } else if (!productMeasure.equals("")) {
                productString = "Medidas: " + productMeasure;
            } else if (!productObservation.equals("")) {
                productString = "Observaciones: " + productObservation;
            } else {
                productString = "-";
            }
            budgetModifyView.setPreviewStringTableValueAt(productIndex, 3, productString);
            budgetModifyView.setPreviewDoubleTableValueAt(productIndex, 4, productPrice);

            if (productsMap.isEmpty()) {
                productsMap.put(productAmount, product);
            } else {
                Iterator<Map.Entry<Integer, Product>> iterator = productsMap.entries().iterator();
                boolean productAlreadyOnMap = false;
                while (iterator.hasNext()) {
                    Map.Entry<Integer, Product> entry2 = iterator.next();
                    Product productValue = entry2.getValue();
                    int productKey = entry2.getKey();
                    if (productValue.getName().equals(productName)) {
                        productsMap.remove(productKey, productValue); //REMUEVO ESE PRODUCTO DEL MAPA
                        productKey += productAmount; //LE SUMO LA CANTIDAD DE PRODUCTOS QUE SE ESTÁN AGREGANDO
                        productsMap.put(productKey, productValue); //AGREGO EL PRODUCTO CON LA NUEVA CANTIDAD
                        productAlreadyOnMap = true;
                        break; //CORTO EL BUCLE
                    }
                }

                if (!productAlreadyOnMap) {
                    productsMap.put(productAmount, product);
                }
            }

            productIndex++;
            observationsIndex++;
            measuresIndex++;
        }

        budgetModifyView.setPreviewStringTableValueAt(0, 5, clientType);
        this.budgetNumber = budgetNumber;
        budgetModifyView.setWaitingStatus();
    }

    public void onModifySearchViewButtonClicked(JTable table, int selectedRow, int budgetNumber) {
        StringBuilder sb = budgetModifyView.getStringBuilder();
        JTextArea textArea = budgetModifyView.getPriceTextArea();
        Multimap<Integer, String> savedProducts = ArrayListMultimap.create();
        ArrayList<String> productObservations = new ArrayList<>();
        ArrayList<String> productMeasures = new ArrayList<>();

        if (selectedRow != -1) {
            String selectedBudgetName = (String) table.getValueAt(selectedRow, 0);
            savedProducts = budgetModifyModel.getSavedProducts(budgetNumber, selectedBudgetName);
            productObservations = budgetModifyModel.getProductObservations(budgetNumber, selectedBudgetName);
            productMeasures = budgetModifyModel.getProductMeasures(budgetNumber, selectedBudgetName);
            setModifyView(budgetNumber, selectedRow, savedProducts, productObservations, productMeasures);
            updateTextArea(sb, textArea, false);
        }
    }

    @Override
    protected void initListeners() {

    }

    public void onAddProductButtonClicked() {
        int selectedProductRow = budgetModifyView.getProductTableSelectedRow();
        String productName = "";
        String productMeasures = "";
        String productObservations = "";
        String textToPut = "";
        String productAmountStr = "";
        int productAmountInt = 1;
        int productID = -1;
        double productPrice = -1;
        JTextArea priceTextArea = budgetModifyView.getPriceTextArea();
        StringBuilder sb = budgetModifyView.getStringBuilder();
        JTable productsTable = budgetModifyView.getProductsResultTable();

        if (selectedProductRow != -1 || editingProduct) {

            if (!editingProduct) {
                productName = budgetModifyView.getProductStringTableValueAt(selectedProductRow, 0);
                productObservations = budgetModifyView.getObservationsTextField().getText();
                productMeasures = budgetModifyView.getMeasuresTextField().getText();
                productAmountStr = budgetModifyView.getAmountTextField().getText();

                if (productAmountStr.equals("")) { // NO INGRESÓ CANTIDAD
                    productAmountInt = 1;
                } else { // INGRESÓ CANTIDAD
                    productAmountInt = Integer.parseInt(productAmountStr);
                }

                if (productObservations.equals("")) { // NO INGRESÓ OBSERVACIONES
                    productObservations = "";
                } else { // INGRESÓ OBSERVACIONES
                    productObservations = productObservations.trim();
                }

                if (productMeasures.equals("")) { // NO INGRESÓ MEDIDAS
                    productMeasures = "";
                } else { // INGRESÓ MEDIDAS
                    productMeasures = productMeasures.trim();
                }

                if (!productMeasures.equals("") && !productObservations.equals("")) { // INGRESÓ MEDIDAS Y OBSERVACIONES
                    textToPut = "Medidas: " + productMeasures + " || Observaciones: " + productObservations;
                } else if (!productMeasures.equals("")) { // INGRESÓ MEDIDAS
                    textToPut = "Medidas: " + productMeasures;
                } else if (!productObservations.equals("")) { // INGRESÓ OBSERVACIONES
                    textToPut = "Observaciones: " + productObservations;
                } else { // NO INGRESÓ MEDIDAS NI OBSERVACIONES
                    textToPut = "-";
                }

                if (budgetModifyView.countNonEmptyCells(budgetModifyView.getPreviewTable(), 1) == 0) { //NO HAY CELDAS CON CONTENIDO EN LA TABLA DE PREVIEW

                    budgetModifyView.setPreviewStringTableValueAt(1, 1, productName); //INSERTA EN LA COLUMNA DE NOMBREPRODUCTO
                    budgetModifyView.setPreviewIntTableValueAt(1, 2, productAmountInt); //INSERTA EN LA COLUMNA DE CANTIDAD
                    budgetModifyView.setPreviewStringTableValueAt(1, 3, textToPut); //INSERTA EN LA COLUMNA DE PRODUCTO

                    productID = productModel.getProductID(productName);
                    Product product = productModel.getOneProduct(productID);
                    productPrice = (double) product.getPrice() * productAmountInt;
                    budgetModifyView.setPreviewDoubleTableValueAt(1, 4, productPrice);
                    rowCountOnPreviewTable = 2;
                    updateTextArea(sb, priceTextArea, true);

                } else { //HAY CELDAS CON CONTENIDO EN LA TABLA DE PREVIEW

                    budgetModifyView.setPreviewStringTableValueAt(rowCountOnPreviewTable, 1, productName);
                    budgetModifyView.setPreviewIntTableValueAt(rowCountOnPreviewTable, 2, productAmountInt);
                    budgetModifyView.setPreviewStringTableValueAt(rowCountOnPreviewTable, 3, textToPut);
                    productID = productModel.getProductID(productName);
                    Product product = productModel.getOneProduct(productID);
                    productPrice = product.getPrice() * productAmountInt;
                    budgetModifyView.setPreviewDoubleTableValueAt(rowCountOnPreviewTable, 4, productPrice);
                    updateTextArea(sb, priceTextArea, true);
                    rowCountOnPreviewTable++;
                }
            } else {
                int previewTableSelectedRow = budgetModifyView.getPreviewTable().getSelectedRow();
                int productTableSelectedRow = budgetModifyView.getProductsResultTable().getSelectedRow();
                String productNameToEdit = budgetModifyView.getPreviewStringTableValueAt(previewTableSelectedRow, 1);
                String productMeasuresToEdit = budgetModifyView.getMeasuresTextField().getText();
                String productObservationsToEdit = budgetModifyView.getObservationsTextField().getText();
                String textToPutToEdit = "";
                String productAmountStrToEdit = budgetModifyView.getAmountTextField().getText();
                int productAmountIntToEdit = 1;
                int productIDToEdit = -1;
                double productPriceToEdit = -1;

                if (productTableSelectedRow != -1) {
                    System.out.println("Hay un producto seleccionado en la tabla PRODUCTOS");
                    productNameToEdit = productsTable.getValueAt(productTableSelectedRow, 0).toString();
                }

                if (productAmountStrToEdit.equals("")) { // NO INGRESÓ CANTIDAD
                    productAmountIntToEdit = 1;
                } else { // INGRESÓ CANTIDAD
                    productAmountIntToEdit = Integer.parseInt(productAmountStrToEdit);
                }

                if (productObservationsToEdit.equals("")) { // NO INGRESÓ OBSERVACIONES
                    productObservationsToEdit = "";
                } else { // INGRESÓ OBSERVACIONES
                    productObservationsToEdit = productObservationsToEdit.trim();
                }

                if (productMeasuresToEdit.equals("")) { // NO INGRESÓ MEDIDAS
                    productMeasuresToEdit = "";
                } else { // INGRESÓ MEDIDAS
                    productMeasuresToEdit = productMeasuresToEdit.trim();
                }

                if (!productMeasuresToEdit.equals("") && !productObservationsToEdit.equals("")) { // INGRESÓ MEDIDAS Y OBSERVACIONES
                    textToPutToEdit = "Medidas: " + productMeasuresToEdit + " || Observaciones: " + productObservationsToEdit;
                } else if (!productMeasuresToEdit.equals("")) { // INGRESÓ MEDIDAS
                    textToPutToEdit = "Medidas: " + productMeasuresToEdit;
                } else if (!productObservationsToEdit.equals("")) { // INGRESÓ OBSERVACIONES
                    textToPutToEdit = "Observaciones: " + productObservationsToEdit;
                } else { // NO INGRESÓ MEDIDAS NI OBSERVACIONES
                    textToPutToEdit = "";
                }

                budgetModifyView.setPreviewStringTableValueAt(previewTableSelectedRow, 1, productNameToEdit);
                budgetModifyView.setPreviewIntTableValueAt(previewTableSelectedRow, 2, productAmountIntToEdit);
                budgetModifyView.setPreviewStringTableValueAt(previewTableSelectedRow, 3, textToPutToEdit);
                productIDToEdit = productModel.getProductID(productNameToEdit);
                Product productToEdit = productModel.getOneProduct(productIDToEdit);
                productPriceToEdit = productToEdit.getPrice() * productAmountIntToEdit;
                budgetModifyView.setPreviewDoubleTableValueAt(previewTableSelectedRow, 4, productPriceToEdit);
                budgetModifyView.getPreviewTable().setEnabled(true);
            }
        }
    }

    private void cargarCategorias() {
        List<String> categorias = categoryModel.getCategoriesName();
        budgetModifyView.setCategoriesComboBox(categorias);
    }

    private void cargarCiudades() {
        ArrayList<String> ciudades = budgetModel.getCitiesName();
        budgetModifyView.setCitiesComboBox(ciudades);
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

    public void onDeleteProductButtonClicked() {
        budgetModifyView.getProductsResultTable().clearSelection();
        int selectedRow = budgetModifyView.getPreviewTable().getSelectedRow();
        if (selectedRow != -1) {
            StringBuilder sb = budgetModifyView.getStringBuilder();
            JTextArea priceTextArea = budgetModifyView.getPriceTextArea();
            budgetModifyView.getPreviewTableModel().removeRow(selectedRow);
            updateTextArea(sb, priceTextArea, false);
            rowCountOnPreviewTable--;
        }
    }

    public void updateTextArea(StringBuilder stb, JTextArea textArea, boolean adding) {
        double totalPrice = 0;
        int productAmount = 0;
        int selectedProductRowIndex = budgetModifyView.getProductTableSelectedRow();
        double productPrice = 0;
        int filledCells = budgetModifyView.countNonEmptyCells(budgetModifyView.getPreviewTable(), 1);
        System.out.println("HAY " + filledCells + " CELDAS CON CONTENIDO.");
        stb.setLength(0);

        System.out.println("HAY: " + filledCells + " CELDAS LLENAS EN LA PREVIEW TABLE.");


        if (adding) { // SI SE ESTA HACIENDO UPDATE DEL PRECIO PORQUE SE AGREGÓ UN PRODUCTO:
            productPrice = (double) budgetModifyView.getProductsResultTable().getValueAt(selectedProductRowIndex, 2); // EL PRECIO DEL PRODUCTO ES EL PRECIO DE LA TABLA DE PRODUCTOS DE LA FILA SELECCIONADA
            if (filledCells == 0) { // SI NO HAY NINGUN PRODUCTO EN LA PREVIEW TABLE:
                if (selectedProductRowIndex != -1) { // SI SE SELECCIONÓ UN PRODUCTO DE LA TABLA DE PRODUCTOS:
                    if (budgetModifyView.getAmountTextField().getText().equals("")) { // SI NO SE ESPECIFICÓ LA CANTIDAD DE PRODUCTOS A AGREGAR:
                        productAmount = 1; // SE PONE CANTIDAD 1 POR DEFECTO
                    } else {// SI SE ESPECIFICÓ LA CANTIDAD DE PRODUCTOS A AGREGAR:
                        productAmount = Integer.parseInt(budgetModifyView.getAmountTextField().getText()); // SE CAPTURA LA CANTIDAD DE PRODUCTOS A AGREGAR
                    }
                    totalPrice += productPrice * productAmount; // SE AGREGA LA CANTIDAD * PRECIO AL PRECIO TOTAL
                } else { // SI NO SE SELECCIONÓ NINGUN PRODUCTO DE LA TABLA DE PRODUCTOS:
                    totalPrice = 0; // EL PRECIO TOTAL ES 0
                }
            } else { // SI YA HAY PRODUCTOS EN LA PREVIEW TABLE:
                for (int i = 1; i <= filledCells; i++) { // SE RECORREN LAS FILAS DE LA PREVIEW TABLE
                    productPrice = (double) budgetModifyView.getPreviewTable().getValueAt(i, 4);
                    totalPrice += productPrice;
                }
            }
        } else { // SI SE ESTA HACIENDO UPDATE DEL PRECIO PORQUE SE ELIMINÓ UN PRODUCTO:
            if (filledCells == 0) {
                totalPrice = 0;
            } else {
                for (int i = 1; i <= filledCells; i++) {
                    productPrice = (double) budgetModifyView.getPreviewTable().getValueAt(i, 4);
                    totalPrice += productPrice;
                }
            }
        }

        stb.append("\nPrecio Total: $").append(String.format("%.2f", totalPrice));
        textArea.setText(stb.toString());
    }

    public void onClientSelectedCheckBoxClicked() {
        JCheckBox clientSelectedCheckBox = budgetModifyView.getClientSelectedCheckBox();
        if (!clientSelectedCheckBox.isSelected()) {
            budgetModifyView.setSecondPanelsVisibility();
        } else {
            budgetModifyView.setInitialPanelsVisibility();
        }
    }

    public void onAddClientButtonClicked() {
        int selectedRow = budgetModifyView.getClientTableSelectedRow();
        String clientName = "";
        String clientType = "";
        DefaultTableModel clientTableModel = budgetModifyView.getClientResultTableModel();
        JCheckBox clientSelectedCheckBox = budgetModifyView.getClientSelectedCheckBox();

        if (selectedRow != -1) {
            if (clientTableModel.getValueAt(selectedRow, 1) != null && !clientTableModel.getValueAt(selectedRow, 1).toString().equals("")) {
                clientName = clientTableModel.getValueAt(selectedRow, 1).toString();
                clientType = clientTableModel.getValueAt(selectedRow, 5).toString();
                budgetModifyView.setPreviewStringTableValueAt(0, 0, clientName);
                budgetModifyView.setPreviewStringTableValueAt(0, 5, clientType);
                clientSelectedCheckBox.setSelected(true);
            } else {
                budgetModifyView.showMessage(MessageTypes.CLIENT_NOT_SELECTED);
            }
        } else {
            budgetModifyView.showMessage(MessageTypes.CLIENT_NOT_SELECTED);
        }
    }

    public void onSearchProductButtonClicked() {
        String productName = budgetModifyView.getProductsTextField().getText();
        List<String> categoriesName = categoryModel.getCategoriesName();
        JComboBox categoryComboBox = budgetModifyView.getCategoriesComboBox();
        String selectedCategory = (String) categoryComboBox.getSelectedItem();
        ArrayList<Product> products = budgetModel.getProducts(productName, selectedCategory);
        String productCategoryName = "";
        budgetModifyView.clearProductTable();
        int rowCount = 0;
        for (Product product : products) {
            int categoryID = product.getCategoryID();
            for (String categoryName : categoriesName) {
                if (categoryModel.getCategoryID(categoryName) == categoryID) {
                    productCategoryName = categoryName;
                }
            }
            budgetModifyView.setProductStringTableValueAt(rowCount, 0, product.getName());
            budgetModifyView.setProductStringTableValueAt(rowCount, 1, product.getDescription());
            budgetModifyView.setProductDoubleTableValueAt(rowCount, 2, product.getPrice());
            budgetModifyView.setProductStringTableValueAt(rowCount, 3, productCategoryName);
            rowCount++;
        }
        categoryComboBox.setSelectedIndex(0);
    }

    public void onPreviewTableDoubleClickedRow(int clickedRow) {
        String productName = "";
        String productMeasures = "";
        String productObservations = "";
        int productAmount = 0;
        int productID = -1;
        Object observationsMeasures = "";

        if (clickedRow != -1 && clickedRow != 0) {
            budgetModifyView.getProductsResultTable().clearSelection();
            editingProduct = true;
            budgetModifyView.getPreviewTable().setEnabled(false);
            productName = (String) budgetModifyView.getPreviewTable().getValueAt(clickedRow, 1);
            productAmount = (int) budgetModifyView.getPreviewTable().getValueAt(clickedRow, 2);
            observationsMeasures = budgetModifyView.getPreviewTable().getValueAt(clickedRow, 3);

            if (observationsMeasures != null && !observationsMeasures.equals("")) {
                if (observationsMeasures.toString().contains("Medidas:") && observationsMeasures.toString().contains("Observaciones:")) {
                    productMeasures = observationsMeasures.toString().substring(observationsMeasures.toString().indexOf("Medidas: ") + 9, observationsMeasures.toString().indexOf(" || Observaciones: "));
                    productObservations = observationsMeasures.toString().substring(observationsMeasures.toString().indexOf("Observaciones: ") + 14);
                } else if (observationsMeasures.toString().contains("Medidas:")) {
                    productMeasures = observationsMeasures.toString().substring(observationsMeasures.toString().indexOf("Medidas: ") + 9);
                    productObservations = "";
                } else if (observationsMeasures.toString().contains("Observaciones:")) {
                    productObservations = observationsMeasures.toString().substring(observationsMeasures.toString().indexOf("Observaciones: ") + 14);
                    productMeasures = "";
                } else {
                    productMeasures = "";
                    productObservations = "";
                }
            }

            budgetModifyView.setAmountTextField(productAmount);
            budgetModifyView.setMeasuresTextField(productMeasures);
            budgetModifyView.setObservationsTextField(productObservations);
        }
    }

}
