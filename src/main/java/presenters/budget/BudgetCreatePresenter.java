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
        int indiceAsterisco = -1;
        int endOfAmountValueIndex = -1;
        int productID = -1;
        JTextArea priceTextArea = budgetCreateView.getPriceTextArea();
        StringBuilder sb = budgetCreateView.getStringBuilder();

        if (selectedProductRow != -1) {

            productName = budgetCreateView.getProductStringTableValueAt(selectedProductRow, 0);
            productObservations = budgetCreateView.getObservationsTextField().getText();
            productMeasures = budgetCreateView.getMeasuresTextField().getText();
            productAmountStr = budgetCreateView.getAmountTextField().getText();

            if (productAmountStr.equals("")) {
                productAmountInt = 1;
            } else {
                productAmountInt = Integer.parseInt(productAmountStr);
            }

            if (productObservations.equals("")) {
                productObservations = "Oservaciones no especificadas / no necesarias";
            } else {
                productObservations = productObservations.trim();
            }

            if (productMeasures.equals("")) {
                productMeasures = "Medidas no especificadas / no necesarias";
            } else {
                productMeasures = productMeasures.trim();
            }

            if (budgetCreateView.countNonEmptyCells(budgetCreateView.getPreviewTable(), 1) == 0) { //NO HAY CELDAS CON CONTENIDO EN LA TABLA DE PREVIEW
                productID = productModel.getProductID(productName);
                Product product = productModel.getOneProduct(productID);
                productsList.put(productAmountInt, product);
                textToPut = productName + " * (" + productAmountInt + ".00) Unidades " + "\t" + "Medidas: " + productMeasures + "\t" + " Observaciones: " + productObservations;
                budgetCreateView.setPreviewStringTableValueAt(1, 1, textToPut);
                updateTextArea(sb, priceTextArea, productsList);
                rowCountOnPreviewTable = 2;
            } else { //HAY CELDAS CON CONTENIDO EN LA TABLA DE PREVIEW
                productID = productModel.getProductID(productName);
                Product product = productModel.getOneProduct(productID);
                productsList.put(productAmountInt, product);
                updateTextArea(sb, priceTextArea, productsList);
                textToPut = productName + " * (" + productAmountInt + ".00) Unidades " + "\t" + "Medidas: " + productMeasures + "\t" + " Observaciones: " + productObservations;
                budgetCreateView.setPreviewStringTableValueAt(rowCountOnPreviewTable, 1, textToPut);
                rowCountOnPreviewTable++;
            }
        }
    }

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
        JTextArea priceTextArea = budgetCreateView.getPriceTextArea();
        StringBuilder sb = budgetCreateView.getStringBuilder();

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
                for (Map.Entry<Integer, Product> entry : productsList.entries()) {
                    System.out.println("CANTIDAD: " + entry.getKey() + " PRODUCTO: " + entry.getValue().getName());
                }

                productsList.remove(productAmount, product);

                System.out.println("PRODUCTSLISTS LUEGO DE ELIMINAR EL PRODUCTO CON EL .remove(): ");
                for (Map.Entry<Integer, Product> entry : productsList.entries()) {
                    System.out.println("CANTIDAD: " + entry.getKey() + " PRODUCTO: " + entry.getValue().getName());
                }
                updateTextArea(sb, priceTextArea, productsList);
            }
        }
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

    public void updateTextArea(StringBuilder stb, JTextArea textArea, Multimap<Integer, Product> products) {
        double totalPrice = 0;
        stb.setLength(0);
        for (Map.Entry<Integer, Product> entry : products.entries()) {
            int productAmount = entry.getKey();
            Product product = entry.getValue();
            totalPrice += product.getPrice() * productAmount;
        }
        stb.append("\nPrecio Total: $").append(String.format("%.2f", totalPrice));
        textArea.setText(stb.toString());
    }
}
