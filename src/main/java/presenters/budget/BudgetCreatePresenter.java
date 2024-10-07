package presenters.budget;


import PdfFormater.Row;
import PdfFormater.SamplePDFCreation;
import lombok.Setter;
import models.IProductModel;
import presenters.StandardPresenter;
import models.IBudgetModel;
import models.ICategoryModel;
import utils.Client;
import utils.MessageTypes;
import utils.Product;
import views.budget.*;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;

import javax.swing.*;
import java.util.*;

import static utils.MessageTypes.*;

public class BudgetCreatePresenter extends StandardPresenter {
    private final IBudgetCreateView budgetCreateView;
    private final IBudgetModel budgetModel;
    private final ICategoryModel categoryModel;
    @Setter
    private int rowCountOnPreviewTable = 1;
    private final IProductModel productModel;
    private ArrayList<Client> globalClientsList;
    private int globalClientID = -1;
    private int budgetNumber = -1;
    private boolean editingProduct = false;

    public BudgetCreatePresenter(IBudgetCreateView budgetCreateView, IBudgetModel budgetModel, ICategoryModel categoryModel, IProductModel productModel) {
        this.budgetCreateView = budgetCreateView;
        view = budgetCreateView;
        this.budgetModel = budgetModel;
        this.categoryModel = categoryModel;
        this.productModel = productModel;
        cargarCategorias();
        cargarCiudades();
        budgetNumber = budgetModel.getNextBudgetNumber();
    }

    @Override
    protected void initListeners() {
        budgetModel.addBudgetCreationFailureListener(() -> budgetCreateView.showMessage(BUDGET_CREATION_SUCCESS));
        budgetModel.addBudgetCreationFailureListener(() -> budgetCreateView.showMessage(BUDGET_CREATION_FAILURE));
    }

    public void onHomeCreateBudgetButtonClicked() {

        budgetCreateView.showView();
    }

    public void onSearchClientButtonClicked() {
        String city = (String) budgetCreateView.getCitiesComboBox().getSelectedItem();
        String name = budgetCreateView.getBudgetClientName();
        int clientID = -1;
        if (city.equals("Seleccione una ciudad")) {
            city = "";
        }
        globalClientsList = budgetModel.getClients(name, city);
        ArrayList<Client> clients = budgetModel.getClients(name, city);
        budgetCreateView.clearClientTable();
        int rowCount = 0;
        for (Client client : clients) {
            clientID = budgetModel.getClientID(client.getName());

            budgetCreateView.setClientIntTableValueAt(rowCount, 0, clientID);
            budgetCreateView.setClientStringTableValueAt(rowCount, 1, client.getName());
            budgetCreateView.setClientStringTableValueAt(rowCount, 2, client.getAddress());
            budgetCreateView.setClientStringTableValueAt(rowCount, 3, client.getCity());
            budgetCreateView.setClientStringTableValueAt(rowCount, 4, client.getPhone());
            budgetCreateView.setClientStringTableValueAt(rowCount, 5, client.isClient() ? "Cliente" : "Particular");
            rowCount++;
        }
    }

    public void onAddClientButtonClicked() {
        int selectedRow = budgetCreateView.getClientTableSelectedRow();
        String clientName = "";
        String clientType = "";
        JCheckBox clientSelectedCheckBox = budgetCreateView.getClientSelectedCheckBox();
        if (selectedRow != -1) {
            clientName = budgetCreateView.getClientStringTableValueAt(selectedRow, 1);
            clientType = budgetCreateView.getClientStringTableValueAt(selectedRow, 5);
            budgetCreateView.setPreviewStringTableValueAt(0, 0, clientName);
            budgetCreateView.setPreviewStringTableValueAt(0, 5, clientType);
            globalClientID = budgetModel.getClientID(clientName);
            clientSelectedCheckBox.setSelected(true);
        } else {
            budgetCreateView.showMessage(MessageTypes.CLIENT_NOT_SELECTED);
        }
    }

    public void onSearchProductButtonClicked() {
        String productName = budgetCreateView.getProductsTextField().getText();
        List<String> categoriesName = categoryModel.getCategoriesName();
        JComboBox categoryComboBox = budgetCreateView.getCategoriesComboBox();
        String selectedCategory = (String) categoryComboBox.getSelectedItem();
        ArrayList<Product> products = budgetModel.getProducts(productName, selectedCategory);
        String productCategoryName = "";
        budgetCreateView.clearProductTable();
        int rowCount = 0;
        for (Product product : products) {
            int categoryID = product.getCategoryID();
            for (String categoryName : categoriesName) {
                if (categoryModel.getCategoryID(categoryName) == categoryID) {
                    productCategoryName = categoryName;
                }
            }
            budgetCreateView.setProductStringTableValueAt(rowCount, 0, product.getName());
            budgetCreateView.setProductStringTableValueAt(rowCount, 1, product.getDescription());
            budgetCreateView.setProductDoubleTableValueAt(rowCount, 2, product.getPrice());
            budgetCreateView.setProductStringTableValueAt(rowCount, 3, productCategoryName);
            rowCount++;
        }
        categoryComboBox.setSelectedIndex(0);
    }

    public void onCreateButtonClicked() {
        int budgetID = -1;
        budgetNumber = budgetModel.getNextBudgetNumber();
        budgetCreateView.setWorkingStatus();
        ArrayList<Client> oneClientList = budgetModel.getOneClient(globalClientID);
        ArrayList<Row> rows = new ArrayList<>();

        if (onEmptyFields(0, 1)) {
            budgetCreateView.showMessage(BUDGET_CREATION_EMPTY_COLUMN);
        } else {
            budgetModel.createBudget(
                    budgetCreateView.getPreviewStringTableValueAt(0, 0),
                    budgetCreateView.getBudgetDate(),
                    oneClientList.get(0).isClient() ? "Cliente" : "Particular",
                    budgetNumber
            );
            budgetCreateView.showMessage(BUDGET_CREATION_SUCCESS);

            Multimap<Integer, String> products = ArrayListMultimap.create();
            ArrayList<String> productObservations = new ArrayList<>();
            ArrayList<String> productMeasures = new ArrayList<>();

            String productCellValue = "";
            String productName = "";
            int amount = 0;
            String measures = "";
            String observation = "";
            String budgetName = budgetCreateView.getPreviewStringTableValueAt(0, 0);
            double finalPrice = 0;

            for (int i = 1; i < rowCountOnPreviewTable; i++) {
                productCellValue = budgetCreateView.getPreviewStringTableValueAt(i, 3); // VALOR DE LA COLUMNA DE MEDIDAS/OBSERVACIONES
                productName = budgetCreateView.getPreviewStringTableValueAt(i, 1);

                if (!productCellValue.equals("") && productCellValue != null) { // SI LA COLUMNA NO ESTÁ VACIA
                    if (productCellValue.contains("Medidas:") && productCellValue.contains("Observaciones:")) { // SI CONTIENE MEDIDAS Y OBSERVACIONES
                        //TESTING:
                        System.out.println("INGRESÓ A MEDIDAS Y OBSERVACIONES");
                        //TESTING:
                        measures = productCellValue.substring(productCellValue.indexOf("Medidas: ") + 9, productCellValue.indexOf(" || Observaciones: "));
                        observation = productCellValue.substring(productCellValue.indexOf("Observaciones: ") + 14);
                    } else if (productCellValue.contains("Medidas:") && !productCellValue.contains("Observaciones:")) {
                        //TESTING:
                        System.out.println("INGRESÓ A MEDIDAS PERO NO OBSERVACIONES");
                        //TESTING:
                        measures = productCellValue.substring(productCellValue.indexOf("Medidas: ") + 9);
                        observation = "";
                    } else if (productCellValue.contains("Observaciones:") && !productCellValue.contains("Medidas:")) {
                        //TESTING:
                        System.out.println("INGRESÓ A OBSERVACIONES PERO NO MEDIDAS");
                        //TESTING:
                        observation = productCellValue.substring(productCellValue.indexOf("Observaciones: ") + 14);
                        measures = "";
                    } else {
                        //TESTING:
                        System.out.println("NO HAY MEDIDAS NI OBSERVACIONES");
                        //TESTING:
                        measures = "";
                        observation = "";
                    }
                }

                amount = (int) budgetCreateView.getPreviewTable().getValueAt(i, 2);
                Product product = productModel.getOneProduct(productModel.getProductID(productName));
                products.put(amount, productName);
                productMeasures.add(measures);
                productObservations.add(observation);
                double productPrice = product.getPrice();
                double totalPrice = productPrice * amount;
                rows.add(new Row(productName, amount, measures, productPrice, totalPrice));

                finalPrice += totalPrice;
            }

            budgetID = budgetModel.getBudgetID(budgetNumber, budgetName);
            budgetModel.saveProducts(budgetID, products, productObservations, productMeasures);
            SamplePDFCreation.createPDF(false, oneClientList.get(0), budgetNumber, rows, finalPrice);
            budgetCreateView.setWaitingStatus();
            budgetCreateView.getWindowFrame().dispose();
        }
    }

    private void cargarCategorias() {
        List<String> categorias = categoryModel.getCategoriesName();
        budgetCreateView.setCategoriesComboBox(categorias);
    }

    private void cargarCiudades() {
        ArrayList<String> ciudades = budgetModel.getCitiesName();
        budgetCreateView.setCitiesComboBox(ciudades);
    }


    public void onAddProductButtonClicked() {
        int selectedProductRow = budgetCreateView.getProductTableSelectedRow();
        String productName = "";
        String productMeasures = "";
        String productObservations = "";
        String textToPut = "";
        String productAmountStr = "";
        int productAmountInt = 1;
        int productID = -1;
        double productPrice = -1;
        JTextArea priceTextArea = budgetCreateView.getPriceTextArea();
        StringBuilder sb = budgetCreateView.getStringBuilder();
        JTable productsTable = budgetCreateView.getProductsResultTable();

        if (selectedProductRow != -1 || editingProduct) {

            if (!editingProduct) {
                productName = budgetCreateView.getProductStringTableValueAt(selectedProductRow, 0);
                productObservations = budgetCreateView.getObservationsTextField().getText();
                productMeasures = budgetCreateView.getMeasuresTextField().getText();
                productAmountStr = budgetCreateView.getAmountTextField().getText();

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

                if (budgetCreateView.countNonEmptyCells(budgetCreateView.getPreviewTable(), 1) == 0) { //NO HAY CELDAS CON CONTENIDO EN LA TABLA DE PREVIEW

                    budgetCreateView.setPreviewStringTableValueAt(1, 1, productName); //INSERTA EN LA COLUMNA DE NOMBREPRODUCTO
                    budgetCreateView.setPreviewIntTableValueAt(1, 2, productAmountInt); //INSERTA EN LA COLUMNA DE CANTIDAD
                    budgetCreateView.setPreviewStringTableValueAt(1, 3, textToPut); //INSERTA EN LA COLUMNA DE PRODUCTO

                    productID = productModel.getProductID(productName);
                    Product product = productModel.getOneProduct(productID);
                    productPrice = (double) product.getPrice() * productAmountInt;
                    budgetCreateView.setPreviewDoubleTableValueAt(1, 4, productPrice);
                    rowCountOnPreviewTable = 2;
                    updateTextArea(sb, priceTextArea, true);

                } else { //HAY CELDAS CON CONTENIDO EN LA TABLA DE PREVIEW

                    budgetCreateView.setPreviewStringTableValueAt(rowCountOnPreviewTable, 1, productName);
                    budgetCreateView.setPreviewIntTableValueAt(rowCountOnPreviewTable, 2, productAmountInt);
                    budgetCreateView.setPreviewStringTableValueAt(rowCountOnPreviewTable, 3, textToPut);
                    productID = productModel.getProductID(productName);
                    Product product = productModel.getOneProduct(productID);
                    productPrice = product.getPrice() * productAmountInt;
                    budgetCreateView.setPreviewDoubleTableValueAt(rowCountOnPreviewTable, 4, productPrice);
                    updateTextArea(sb, priceTextArea, true);
                    rowCountOnPreviewTable++;

                }
            } else {
                editingProduct = false;
                System.out.println("SE DESACTIVÓ EL BOOLEAN DE EDITANDO PRODUCTO.");
                int previewTableSelectedRow = budgetCreateView.getPreviewTable().getSelectedRow();
                int productTableSelectedRow = productsTable.getSelectedRow();
                String productNameToEdit = budgetCreateView.getPreviewStringTableValueAt(previewTableSelectedRow, 1);
                String productMeasuresToEdit = budgetCreateView.getMeasuresTextField().getText();
                String productObservationsToEdit = budgetCreateView.getObservationsTextField().getText();
                String textToPutToEdit = "";
                String productAmountStrToEdit = budgetCreateView.getAmountTextField().getText();
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

                budgetCreateView.setPreviewStringTableValueAt(previewTableSelectedRow, 1, productNameToEdit);
                budgetCreateView.setPreviewIntTableValueAt(previewTableSelectedRow, 2, productAmountIntToEdit);
                budgetCreateView.setPreviewStringTableValueAt(previewTableSelectedRow, 3, textToPutToEdit);
                productIDToEdit = productModel.getProductID(productNameToEdit);
                Product productToEdit = productModel.getOneProduct(productIDToEdit);
                productPriceToEdit = productToEdit.getPrice() * productAmountIntToEdit;
                budgetCreateView.setPreviewDoubleTableValueAt(previewTableSelectedRow, 4, productPriceToEdit);
                updateTextArea(sb, priceTextArea, false);
                budgetCreateView.getPreviewTable().setEnabled(true);
            }
        }
    }

    public void setearFecha() {
        String fecha = (String) budgetCreateView.getBudgetDate();
        //budgetCreateView.setPreviewStringTableValueAt(0, 4, fecha);
    }

    public void setearNumeroDePresupuesto() {
        //budgetCreateView.setPreviewIntTableValueAt(0, 6, new Random().nextInt(1000));
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

    public void onDeleteProductButtonClicked() {
        budgetCreateView.getProductsResultTable().clearSelection();
        int selectedRow = budgetCreateView.getPreviewTable().getSelectedRow();
        if (selectedRow != -1) {
            StringBuilder sb = budgetCreateView.getStringBuilder();
            JTextArea priceTextArea = budgetCreateView.getPriceTextArea();
            budgetCreateView.getPreviewTableModel().removeRow(selectedRow);
            rowCountOnPreviewTable--;
            updateTextArea(sb, priceTextArea, false);
        }
    }

    public void onPreviewTableDoubleClickedRow(int clickedRow) {
        String productName = "";
        String productMeasures = "";
        String productObservations = "";
        int productAmount = 0;
        int productID = -1;
        Object observationsMeasures = "";

        if (clickedRow != -1 && clickedRow != 0) {
            System.out.println("SE ACTIVO EL BOOLEAN DE EDITANDO PRODUCTO.");
            editingProduct = true;
            JTable productTable = budgetCreateView.getProductsResultTable();
            productTable.clearSelection();
            budgetCreateView.getPreviewTable().setEnabled(false);
            productAmount = (int) budgetCreateView.getPreviewTable().getValueAt(clickedRow, 2);
            observationsMeasures = budgetCreateView.getPreviewTable().getValueAt(clickedRow, 3);

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

            budgetCreateView.setAmountTextField(productAmount);
            budgetCreateView.setMeasuresTextField(productMeasures);
            budgetCreateView.setObservationsTextField(productObservations);
        }
    }

    public void onClientSelectedCheckBoxClicked() {
        JCheckBox clientSelectedCheckBox = budgetCreateView.getClientSelectedCheckBox();
        if (clientSelectedCheckBox.isSelected()) {
            budgetCreateView.setSecondPanelsVisibility();
        } else {
            budgetCreateView.setInitialPanelsVisibility();
        }
    }

    public void updateTextArea(StringBuilder stb, JTextArea textArea, boolean adding) {
        double totalPrice = 0;
        int productAmount = 0;
        int selectedProductRowIndex = budgetCreateView.getProductTableSelectedRow();
        double productPrice = 0;
        int filledCells = budgetCreateView.countNonEmptyCells(budgetCreateView.getPreviewTable(), 1);
        System.out.println("HAY " + filledCells + " CELDAS CON CONTENIDO.");
        stb.setLength(0);

        System.out.println("HAY: " + filledCells + " CELDAS LLENAS EN LA PREVIEW TABLE.");


        if (adding) { // SI SE ESTA HACIENDO UPDATE DEL PRECIO PORQUE SE AGREGÓ UN PRODUCTO:
            productPrice = (double) budgetCreateView.getProductsResultTable().getValueAt(selectedProductRowIndex, 2); // EL PRECIO DEL PRODUCTO ES EL PRECIO DE LA TABLA DE PRODUCTOS DE LA FILA SELECCIONADA
            if (filledCells == 0) { // SI NO HAY NINGUN PRODUCTO EN LA PREVIEW TABLE:
                if (selectedProductRowIndex != -1) { // SI SE SELECCIONÓ UN PRODUCTO DE LA TABLA DE PRODUCTOS:
                    if (budgetCreateView.getAmountTextField().getText().equals("")) { // SI NO SE ESPECIFICÓ LA CANTIDAD DE PRODUCTOS A AGREGAR:
                        productAmount = 1; // SE PONE CANTIDAD 1 POR DEFECTO
                    } else {// SI SE ESPECIFICÓ LA CANTIDAD DE PRODUCTOS A AGREGAR:
                        productAmount = Integer.parseInt(budgetCreateView.getAmountTextField().getText()); // SE CAPTURA LA CANTIDAD DE PRODUCTOS A AGREGAR
                    }
                    totalPrice += productPrice * productAmount; // SE AGREGA LA CANTIDAD * PRECIO AL PRECIO TOTAL
                } else { // SI NO SE SELECCIONÓ NINGUN PRODUCTO DE LA TABLA DE PRODUCTOS:
                    totalPrice = 0; // EL PRECIO TOTAL ES 0
                }
            } else { // SI YA HAY PRODUCTOS EN LA PREVIEW TABLE:
                for (int i = 1; i <= filledCells; i++) { // SE RECORREN LAS FILAS DE LA PREVIEW TABLE
                    productPrice = (double) budgetCreateView.getPreviewTable().getValueAt(i, 4);
                    totalPrice += productPrice;
                }
            }
        } else { // SI SE ESTA HACIENDO UPDATE DEL PRECIO PORQUE SE ELIMINÓ UN PRODUCTO:
            if (filledCells == 0) {
                totalPrice = 0;
            } else {
                for (int i = 1; i <= filledCells; i++) {
                    productPrice = (double) budgetCreateView.getPreviewTable().getValueAt(i, 4);
                    totalPrice += productPrice;
                }
            }
        }

        stb.append("\nPrecio Total: $").append(String.format("%.2f", totalPrice));
        textArea.setText(stb.toString());
    }
}
