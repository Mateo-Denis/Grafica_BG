package presenters.budget;


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
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;

import static utils.MessageTypes.*;

public class BudgetCreatePresenter extends StandardPresenter {
    private final IBudgetCreateView budgetCreateView;
    private final IBudgetModel budgetModel;
    private final ICategoryModel categoryModel;
    @Setter
    private int rowCountOnPreviewTable = 1;
    private final IProductModel productModel;
    private Multimap<Integer, Product> productsList = ArrayListMultimap.create();

    public BudgetCreatePresenter(IBudgetCreateView budgetCreateView, IBudgetModel budgetModel, ICategoryModel categoryModel, IProductModel productModel) {
        this.budgetCreateView = budgetCreateView;
        view = budgetCreateView;
        this.budgetModel = budgetModel;
        this.categoryModel = categoryModel;
        this.productModel = productModel;
        cargarCategorias();
        cargarCiudades();
    }

    @Override
    protected void initListeners() {
        budgetModel.addBudgetCreationFailureListener(() -> budgetCreateView.showMessage(BUDGET_CREATION_SUCCESS));
        budgetModel.addBudgetCreationFailureListener(() -> budgetCreateView.showMessage(BUDGET_CREATION_FAILURE));
    }

    public void onHomeCreateBudgetButtonClicked() {
        budgetCreateView.showView();
        setearFecha();
        setearNumeroDePresupuesto();
    }

    public void onCategorySelected() {
        String selectedOption = budgetCreateView.getSelectedCategory();
        ArrayList<Product> products = budgetModel.getProducts(selectedOption);
        int categoryID = 0;
        String selectedCategoryName = "";
        List<String> categoriesName = categoryModel.getCategoriesName();

        int rowCount = 0;
        for (Product product : products) {
            categoryID = product.getCategoryID();
            for (String categoryName : categoriesName) {
                if (categoryModel.getCategoryID(categoryName) == categoryID) {
                    selectedCategoryName = categoryName;
                }
            }
            if (selectedCategoryName.equals(selectedOption)) {
                budgetCreateView.setProductStringTableValueAt(rowCount, 0, product.getName());
                budgetCreateView.setProductStringTableValueAt(rowCount, 1, product.getDescription());
                budgetCreateView.setProductDoubleTableValueAt(rowCount, 2, product.getPrice());
                budgetCreateView.setProductStringTableValueAt(rowCount, 3, selectedCategoryName);
                rowCount++;
            }
        }
    }

    public void onSearchClientButtonClicked() {
        String city = (String) budgetCreateView.getCitiesComboBox().getSelectedItem();
        String name = budgetCreateView.getBudgetClientName();
        if (city.equals("Seleccione una ciudad")) {
            city = "";
        }
        ArrayList<Client> clients = budgetModel.getClients(name, city);
        budgetCreateView.clearClientTable();
        int rowCount = 0;
        for (Client client : clients) {
            budgetCreateView.setClientStringTableValueAt(rowCount, 0, client.getName());
            budgetCreateView.setClientStringTableValueAt(rowCount, 1, client.getAddress());
            budgetCreateView.setClientStringTableValueAt(rowCount, 2, client.getCity());
            budgetCreateView.setClientStringTableValueAt(rowCount, 3, client.getPhone());
            budgetCreateView.setClientStringTableValueAt(rowCount, 4, client.isClient() ? "Cliente" : "Particular");
            rowCount++;
        }
    }

    public void onAddClientButtonClicked() {
        int selectedRow = budgetCreateView.getClientTableSelectedRow();
        String clientName = "";
        String clientType = "";
        JCheckBox clientSelectedCheckBox = budgetCreateView.getClientSelectedCheckBox();
        if (selectedRow != -1) {
            clientName = budgetCreateView.getClientStringTableValueAt(selectedRow, 0);
            clientType = budgetCreateView.getClientStringTableValueAt(selectedRow, 4);
            budgetCreateView.setPreviewStringTableValueAt(0, 0, clientName);
            budgetCreateView.setPreviewStringTableValueAt(0, 3, clientType);
            clientSelectedCheckBox.setSelected(true);
        } else {
            budgetCreateView.showMessage(MessageTypes.CLIENT_NOT_SELECTED);
        }
    }

    public void onSearchProductButtonClicked() {
        String productName = budgetCreateView.getProductsTextField().getText();
        ArrayList<Product> products = budgetModel.getProducts(productName);
        List<String> categoriesName = categoryModel.getCategoriesName();
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
    }

    public void onCreateButtonClicked() {
        budgetCreateView.setWorkingStatus();

        if (onEmptyFields(0, 1, 3)) {
            budgetCreateView.showMessage(BUDGET_CREATION_EMPTY_COLUMN);
        } else {
            budgetModel.createBudget(
                    budgetCreateView.getPreviewStringTableValueAt(0, 0),
                    budgetCreateView.getPreviewStringTableValueAt(0, 2),
                    budgetCreateView.getPreviewStringTableValueAt(0, 3),
                    budgetCreateView.getPreviewIntTableValueAt(0, 4)
            );
            budgetCreateView.showMessage(BUDGET_CREATION_SUCCESS);

            Multimap<Integer, String> products = ArrayListMultimap.create();
            ArrayList<String> productObservations = new ArrayList<>();
            ArrayList<String> productMeasures = new ArrayList<>();

            int budgetNumber = budgetCreateView.getPreviewIntTableValueAt(0, 4);
            String productCellValue = "";
            int amount = 0;
            String measures = "";
            String observation = "";
            String budgetName = budgetCreateView.getPreviewStringTableValueAt(0, 0);
            int columnIndex = 1;

            for (int i = 1; i < budgetCreateView.getPreviewTable().getRowCount(); i++) {

                productCellValue = budgetCreateView.getPreviewStringTableValueAt(i, columnIndex);

                if (productCellValue != null) {
                    int indiceX = productCellValue.indexOf('*');
                    int endOfAmountValueIndex = productCellValue.indexOf(".00)");
                    String productName = productCellValue.substring(0, indiceX - 1).trim();
                    String productObservation = productCellValue.substring(productCellValue.indexOf("Observaciones: ") + 14).trim();
                    measures = productCellValue.substring(productCellValue.indexOf("Medidas: ") + 9, productCellValue.indexOf("Observaciones: ")).trim();
                    amount = Integer.parseInt(productCellValue.substring(indiceX + 3, productCellValue.indexOf(".")));

                    products.put(amount, productName);
                    productObservations.add(productObservation);
                    productMeasures.add(measures);
                }
            }

            for (Map.Entry<Integer, String> entry : products.entries()) {
                System.out.println("CANTIDAD: " + entry.getKey() + " PRODUCTO:  " + entry.getValue());
            }
            for (String obs : productObservations) {
                System.out.println("OBSERVACIONES: " + obs);
            }
            for (String meas : productMeasures) {
                System.out.println("MEDIDAS: " + meas);
            }

            budgetModel.saveProducts(budgetNumber, budgetName, products, productObservations, productMeasures);
            budgetCreateView.setWaitingStatus();
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
        Object productCellValue = "";
        String productNameOnPreview = "";
        String productAmountOnPreview = "";
        String productMeasuresOnPreview = "";
        String productObservationOnPreview = "";
        boolean coincidenTodosLosCampos = false;
        int coincidentIndex = -1;
        int indiceX = -1;
        int endOfAmountValueIndex = -1;
        int productID = -1;
        JTextArea priceTextArea = budgetCreateView.getPriceTextArea();
        StringBuilder sb = budgetCreateView.getStringBuilder();

        if (selectedProductRow != -1) {

            productName = budgetCreateView.getProductStringTableValueAt(selectedProductRow, 0);
            System.out.println("Product Name: " + productName);
            productObservations = budgetCreateView.getObservationsTextField().getText();
            System.out.println("Product Observations: " + productObservations);
            productMeasures = budgetCreateView.getMeasuresTextField().getText();
            System.out.println("Product Measures: " + productMeasures);
            productAmountStr = budgetCreateView.getAmountTextField().getText();

            if (!productAmountStr.isEmpty()) {
                productAmountInt = Integer.parseInt(productAmountStr);
            }

            if (productObservations.isEmpty()) {
                productObservations = "Oservaciones no especificadas / no necesarias";
            }

            if (productMeasures.isEmpty()) {
                productMeasures = "Medidas no especificadas / no necesarias";
            }

            productID = productModel.getProductID(productName);
            Product product = productModel.getOneProduct(productID);
            productsList.put(productAmountInt, product);
            updateTotalPriceTextArea(priceTextArea, sb, productsList);

            if (budgetCreateView.countNonEmptyCells(budgetCreateView.getPreviewTable(), 1) == 0) {

                textToPut = productName + " * (" + productAmountInt + ".00) Unidades " + "\t" + "Medidas: " + productMeasures + "\t" + " Observaciones: " + productObservations;
                budgetCreateView.setPreviewStringTableValueAt(1, 1, textToPut);
                rowCountOnPreviewTable = 2;

            } else {
                rowCountOnPreviewTable = budgetCreateView.countNonEmptyCells(budgetCreateView.getPreviewTable(), 1) + 1;
                for (int i = 0; i < budgetCreateView.getPreviewTable().getRowCount() && !coincidenTodosLosCampos; i++) {
                    productCellValue = budgetCreateView.getPreviewTable().getValueAt(i, 1);

                    if (productCellValue != null && productCellValue != "") {
                        indiceX = ((String) productCellValue).indexOf("*");
                        endOfAmountValueIndex = ((String) productCellValue).indexOf(".00)");

                        productNameOnPreview = ((String) productCellValue).substring(0, ((String) productCellValue).indexOf("*") - 1).trim();
                        productMeasuresOnPreview = ((String) productCellValue).substring(((String) productCellValue).indexOf("Medidas: ") + 9, ((String) productCellValue).indexOf("Observaciones: ")).trim();
                        productObservationOnPreview = ((String) productCellValue).substring(((String) productCellValue).indexOf("Observaciones: ") + 14).trim();

                        if (productName.equals(productNameOnPreview) && productMeasures.equals(productMeasuresOnPreview) && productObservations.equals(productObservationOnPreview)) {
                            coincidenTodosLosCampos = true;
                            coincidentIndex = i;
                        }
                    }
                }

                if (coincidenTodosLosCampos) {
                    int productCount = Integer.parseInt(((String) productCellValue).substring(indiceX + 3, endOfAmountValueIndex).trim());
                    productAmountInt += productCount;
                    textToPut = productName + " * (" + productAmountInt + ".00) Unidades " + "\t" + "Medidas: " + productMeasures + "\t" + " Observaciones: " + productObservations;
                    budgetCreateView.setPreviewStringTableValueAt(coincidentIndex, 1, textToPut);
                    updateTotalPriceTextArea(priceTextArea, sb, productsList);
                } else {
                    assert productCellValue != null; //SIRVE PARA QUE LA LINEA DE ABAJO NO DE NULL EXCEPTION
                    int productCount = Integer.parseInt(((String) productCellValue).substring(indiceX + 3, endOfAmountValueIndex).trim());
                    updateTotalPriceTextArea(priceTextArea, sb, productsList);
                    StringBuilder textAreaSB = getTextAreaContent(priceTextArea);
                    textToPut = productName + " * (" + productAmountInt + ".00) Unidades " + "\t" + "Medidas: " + productMeasures + "\t" + " Observaciones: " + productObservations;
                    budgetCreateView.setPreviewStringTableValueAt(rowCountOnPreviewTable, 1, textToPut);
                    rowCountOnPreviewTable++;
                }
                budgetCreateView.setWaitingStatus();
            }
        }
    }

/*    public void onAddProductButtonClicked() {
        int selectedProductRow = budgetCreateView.getProductTableSelectedRow();
        String productName = "";
        String productMeasures = "";
        String productObservations = "";
        String textToPut = "";
        int productAmount = 0;
        int productID = -1;
        Object productCellValue = "";
        JTextArea priceTextArea = budgetCreateView.getPriceTextArea();
        StringBuilder sb = budgetCreateView.getStringBuilder();

        if (selectedProductRow != -1) {

            productName = budgetCreateView.getProductStringTableValueAt(selectedProductRow, 0);
            productObservations = budgetCreateView.getObservationsTextField().getText();
            productMeasures = budgetCreateView.getMeasuresTextField().getText();

            if (budgetCreateView.getAmountTextField().getText().isEmpty()) {
                productAmount = 1;
            } else {
                productAmount = Integer.parseInt(budgetCreateView.getAmountTextField().getText());
            }

            if (productObservations.isEmpty()) {
                productObservations = "Observaciones no especificadas / no necesarias.";
            }

            if (productMeasures.isEmpty()) {
                productMeasures = "Medidas no especificadas / no necesarias.";
            }

            productID = productModel.getProductID(productName);
            Product product = productModel.getOneProduct(productID);
            productsList.put(productAmount, product);
            updateTotalPriceTextArea(priceTextArea, sb, productsList);

            System.out.println("Producto a ingresar: " + productName + " Cantidad: " + productAmount + " Medidas: " + productMeasures + " Observaciones: " + productObservations);

            if (budgetCreateView.countNonEmptyCells(budgetCreateView.getPreviewTable(), 1) == 0) {
                textToPut = productName + " * (" + productAmount + ".00) Unidades" + "\t" + " Medidas: " + productMeasures + "\t" + " Observaciones: " + productObservations;
                budgetCreateView.setPreviewStringTableValueAt(rowCountOnPreviewTable, 1, textToPut);
                rowCountOnPreviewTable++;
                System.out.println("PRODUCTO INGRESADO CON LA OPCION DONDE NO HAY PRODUCTOS EN NINGUNA CELDA.");
            } else {
                System.out.println("HAY CELDAS CON CONTENIDO DE PRODUCTOS.");
                for (int i = 0; i < budgetCreateView.getPreviewTable().getRowCount(); i++) {

                    productCellValue = budgetCreateView.getPreviewTable().getValueAt(i, 1);
                    System.out.println("Row: " + i + " Cell Value: " + productCellValue);

                    if (productCellValue != null && !productCellValue.equals("")) {
                        System.out.println("SE OBTUVO EL VALOR DE LA FILA: " +i+ " Y NO ESTÁ VACÍA.");
                        int indiceX = ((String) productCellValue).indexOf('*');
                        int endOfAmountValueIndex = ((String) productCellValue).indexOf(".00)");
                        String productNameOnPreview = ((String) productCellValue).substring(0, ((String) productCellValue).indexOf("*") - 1).trim();
                        String productObservationOnPreview = ((String) productCellValue).substring(((String) productCellValue).indexOf("Observaciones: ") + 14).trim();
                        String productMeasuresOnPreview = ((String) productCellValue).substring(((String) productCellValue).indexOf("Medidas: ") + 9, ((String) productCellValue).indexOf("Observaciones: ")).trim();
                        System.out.println("LA CELDA TIENE, NOMBRE DE PRODUCTO: " + productNameOnPreview + " OBSERVACIONES: " + productObservationOnPreview + " MEDIDAS: " + productMeasuresOnPreview);
                        if (productNameOnPreview.equals(productName) && productObservationOnPreview.equals(productObservations) && productMeasuresOnPreview.equals(productMeasures)) {
                            System.out.println("LOS 3 CAMPOS COINCIDEN");
                            int productAmountOnPreview = Integer.parseInt(((String) productCellValue).substring(indiceX + 3, endOfAmountValueIndex).trim());
                            System.out.println("CANTIDAD DE PRODUCTO EN LA CELDA: " + productAmountOnPreview);
                            productAmount += productAmountOnPreview;
                            System.out.println("CANTIDAD DE PRODUCTO NUEVA: " + productAmount);
                            textToPut = productName + " * (" + productAmount + ".00) Unidades " + "\t" + "Medidas: " + productMeasures + "\t" + " Observaciones: " + productObservations;
                            System.out.println("SE INGRESA EN LA CELDA: " + textToPut);
                            budgetCreateView.setPreviewStringTableValueAt(i, 1, textToPut);
                            System.out.println("PRODUCTO INGRESADO CON LA OPCION DONDE HAY PRODUCTOS EN ALGUNA CELDA Y LOS 3 CAMPOS COINCIDEN.");
                            break;
                        } else {
                            System.out.println("ALGUNO/S DE LOS CAMPOS NO COINCIDE");
                            textToPut = productName + " * (" + productAmount + ".00) Unidades " + "\t" + "Medidas: " + productMeasures + "\t" + " Observaciones: " + productObservations;
                            budgetCreateView.setPreviewStringTableValueAt(rowCountOnPreviewTable, 1, textToPut);
                            rowCountOnPreviewTable++;
                            System.out.println("PRODUCTO INGRESADO CON LA OPCION DONDE HAY PRODUCTOS EN ALGUNA CELDA Y ALGUNO/S DE LOS 3 CAMPOS NO COINCIDE..");
                            break;
                        }
                    }
                }
            }
        }
        System.out.println("INDICE DE FILA DONDE COLOCAR EL SIGUIENTE PRODUCTO: " + rowCountOnPreviewTable);
    }*/


    public void setearFecha() {
        String fecha = (String) budgetCreateView.getBudgetDate();
        budgetCreateView.setPreviewStringTableValueAt(0, 2, fecha);
    }

    public void setearNumeroDePresupuesto() {
        budgetCreateView.setPreviewIntTableValueAt(0, 4, new Random().nextInt(1000));
    }

    public boolean onEmptyFields(int clientNameColumn, int productColumn, int clientTypeColumn) {
        boolean anyEmpty = false;
        String clientName = budgetCreateView.getPreviewStringTableValueAt(0, clientNameColumn);
        String clientType = budgetCreateView.getPreviewStringTableValueAt(0, clientTypeColumn);
        String product = budgetCreateView.getPreviewStringTableValueAt(1, productColumn);

        if ((clientName == null || clientName.trim().isEmpty()) ||
                (clientType == null || clientType.trim().isEmpty()) ||
                (product == null || product.trim().isEmpty())) {
            anyEmpty = true;
        }
        return anyEmpty;
    }

    public void onDeleteProductButtonClicked() {
        int selectedRow = budgetCreateView.getPreviewTable().getSelectedRow();
        String productName;
        Object productCellValue = "";
        int productID = -1;
        int productAmount = -1;
        int indiceX = -1;
        int endOfAmountValueIndex = -1;

        if (selectedRow != -1 && selectedRow != 0) {
            productCellValue = budgetCreateView.getPreviewTable().getValueAt(selectedRow, 1);
            System.out.println("VALOR DEL PRODUCTCELLVALUE: " + productCellValue);

            if (productCellValue != null && !productCellValue.equals("")) {
                System.out.println("EL PRODUCTCELLVALUE NO ESTÁ VACÍO Y ENTRO AL IF");
                indiceX = ((String) productCellValue).indexOf('*');
                endOfAmountValueIndex = ((String) productCellValue).indexOf(".00)");
                productName = ((String) productCellValue).substring(0, ((String) productCellValue).indexOf("*") - 1).trim();
                productAmount = Integer.parseInt(((String) productCellValue).substring(indiceX + 3, endOfAmountValueIndex).trim());
                productID = productModel.getProductID(productName);
                Product product = productModel.getOneProduct(productID);
                System.out.println("NOMBRE DEL PRODUCTO EN EL CELLVALUE: " + productName + "\n" + "NOMBRE DEL PRODUCTO CREADO: " + product.getName() + "\n"
                + "CANTIDAD DEL PRODUCTO EN EL CELL VALUE: " + productAmount);

                budgetCreateView.getPreviewTableModel().removeRow(selectedRow);
                rowCountOnPreviewTable = budgetCreateView.countNonEmptyCells(budgetCreateView.getPreviewTable(), 1) + 1;

                System.out.println("PRODUCTSLISTS ANTES DE ELIMINAR EL PRODUCTO CON EL .remove(): ");
                for(Map.Entry<Integer, Product> entry : productsList.entries()) {
                    System.out.println("CANTIDAD: " + entry.getKey() + " PRODUCTO: " + entry.getValue().getName());
                }

                productsList.remove(productAmount, product);

                System.out.println("PRODUCTSLISTS LUEGO DE ELIMINAR EL PRODUCTO CON EL .remove(): ");
                for(Map.Entry<Integer, Product> entry : productsList.entries()) {
                    System.out.println("CANTIDAD: " + entry.getKey() + " PRODUCTO: " + entry.getValue().getName());
                }

                updateTotalPriceTextArea(budgetCreateView.getPriceTextArea(), budgetCreateView.getStringBuilder(), productsList);
            }
        }
    }

    public void updateTotalPriceTextArea(JTextArea textArea, StringBuilder sb, Multimap<Integer, Product> products) {
        double totalPrice = 0;
        sb.setLength(0);
        sb.append("Productos:\n");
        for (Map.Entry<Integer, Product> entry : products.entries()) {
            int productAmount = entry.getKey();
            Product product = entry.getValue();
            String productAmountStr = " x " + productAmount + ".00 unidades ";
            double unitPrice = product.getPrice() * productAmount;
            sb.append(product.getName()).append(productAmountStr).append(": $").append(unitPrice).append("\n");
            totalPrice += product.getPrice() * productAmount;
        }
        sb.append("\nTotal: $").append(String.format("%.2f", totalPrice));
        textArea.setText(sb.toString());
    }

    public StringBuilder getTextAreaContent(JTextArea textArea) {
        StringBuilder stringBuilder = new StringBuilder();
        String textAreaContent = textArea.getText();
        for(String line : textAreaContent.split("\n")) {
            stringBuilder.append(line).append("\n");
        }
        return stringBuilder;
    }

    public void onPreviewTableDoubleClickedRow(int clickedRow) {
        String productName = "";
        String productMeasures = "";
        String productObservations = "";
        int productAmount = 0;
        int productID = -1;
        int indiceX = -1;
        int endOfAmountValueIndex = -1;
        Object productCellValue = "";

        if (clickedRow != -1 && clickedRow != 0) {
            productCellValue = budgetCreateView.getPreviewTable().getValueAt(clickedRow, 1);
            if (productCellValue != null && !productCellValue.equals("")) {
                indiceX = ((String) productCellValue).indexOf('*');
                endOfAmountValueIndex = ((String) productCellValue).indexOf(".00)");
                productName = ((String) productCellValue).substring(0, ((String) productCellValue).indexOf("*") - 1).trim();
                productMeasures = ((String) productCellValue).substring(((String) productCellValue).indexOf("Medidas: ") + 9, ((String) productCellValue).indexOf("Observaciones: ")).trim();
                productObservations = ((String) productCellValue).substring(((String) productCellValue).indexOf("Observaciones: ") + 14).trim();
                productAmount = Integer.parseInt(((String) productCellValue).substring(indiceX + 3, endOfAmountValueIndex).trim());
                productID = productModel.getProductID(productName);
                budgetCreateView.setProductNameTextField(productName);
                budgetCreateView.setMeasuresTextField(productMeasures);
                budgetCreateView.setObservationsTextField(productObservations);
                budgetCreateView.setAmountTextField(productAmount);
            }
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
}
