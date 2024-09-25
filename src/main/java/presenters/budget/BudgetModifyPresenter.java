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
                if (city.equals("Seleccione una ciudad")) {
                    city = "";
                }
                ArrayList<Client> clients = budgetModel.getClients(name, city);
                budgetModifyView.clearClientTable();
                int rowCount = 0;
                for (Client client : clients) {
                    budgetModifyView.setClientStringTableValueAt(rowCount, 0, client.getName());
                    budgetModifyView.setClientStringTableValueAt(rowCount, 1, client.getAddress());
                    budgetModifyView.setClientStringTableValueAt(rowCount, 2, client.getCity());
                    budgetModifyView.setClientStringTableValueAt(rowCount, 3, client.getPhone());
                    budgetModifyView.setClientStringTableValueAt(rowCount, 4, client.isClient() ? "Cliente" : "Particular");
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
                if (selectedRow != -1) {
                    clientName = budgetModifyView.getClientStringTableValueAt(selectedRow, 0);
                    clientType = budgetModifyView.getClientStringTableValueAt(selectedRow, 4);
                    budgetModifyView.setPreviewStringTableValueAt(0, 0, clientName);
                    budgetModifyView.setPreviewStringTableValueAt(0, 4, clientType);
                }
            }
        });

        cargarCategorias();
        cargarCiudades();
    }

    public void onSaveModificationsButtonClicked() {
        String newClientName = budgetModifyView.getPreviewStringTableValueAt(0, 0);
        String date = budgetModifyView.getPreviewStringTableValueAt(0, 3);
        String clientType = budgetModifyView.getPreviewStringTableValueAt(0, 4);
        int budgetNumber = (int) budgetModifyView.getPreviewIntTableValueAt(0, 5);
        String oldClientName = budgetModifyModel.getOldClientName(budgetNumber);
        int productIndex = 0;
        String productName = "";
        int productAmount = 0;
        String productString = "";
        String productObservation = "";
        String productMeasure = "";
        Multimap<Integer, String> products = ArrayListMultimap.create();
        ArrayList<String> productObservations = new ArrayList<>();
        ArrayList<String> productMeasures = new ArrayList<>();
        int observationsIndex = 0;
        int measuresIndex = 0;
        boolean anyEmpty = onEmptyFields(0, 1, 3);

        if (anyEmpty) {
            budgetModifyView.showMessage(MessageTypes.BUDGET_CREATION_EMPTY_COLUMN);
        } else {
            for (int row = 0; row < budgetModifyView.getPreviewTable().getRowCount(); row++) {
                productString = budgetModifyView.getPreviewStringTableValueAt(row, 1);
                if (productString != null) {
                    productName = productString.substring(0, productString.indexOf("*") - 1).trim();
                    productAmount = Integer.parseInt(productString.substring(productString.indexOf("*") + 3, productString.indexOf(".")));
                    productObservation = productString.substring(productString.indexOf("Observaciones: ") + 15);
                    productMeasure = productString.substring(productString.indexOf("Medidas: ") + 9, productString.indexOf("Observaciones: "));
                    products.put(productAmount, productName);
                    productObservations.add(productObservation);
                    productMeasures.add(productMeasure);
                }
            }

            for (Map.Entry<Integer, String> entry : products.entries()) {
                System.out.println("Product Amount: " + entry.getKey());
                System.out.println("Product Name: " + entry.getValue());
            }

            budgetModifyModel.updateBudget(oldClientName, newClientName, date, clientType, budgetNumber, products, productObservations, productMeasures);
            budgetModifyView.hideView();
        }
    }

    public void setModifyView(int budgetNumber, int selectedBudgetRow, Multimap<Integer, String> products, ArrayList<String> productObservations, ArrayList<String> productMeasures) {
        budgetModifyView.showView();
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
        budgetModifyView.setPreviewStringTableValueAt(0, 3, date);

        for (Map.Entry<Integer, String> entry : products.entries()) {

            productAmountToSave = entry.getKey();
            productAmount = entry.getKey();
            productName = entry.getValue();
            productObservation = productObservations.get(observationsIndex);
            productMeasure = productMeasures.get(measuresIndex);
            productID = productModel.getProductID(productName);
            Product product = productModel.getOneProduct(productID);
            productPrice = product.getPrice() * productAmount;
            productString = productName + " * (" + productAmount + ".00) Unidades" + "\t" + " Medidas: " + productMeasure + "\t" + " Observaciones: " + productObservation;
            budgetModifyView.setPreviewStringTableValueAt(productIndex, 1, productString);
            budgetModifyView.setPreviewDoubleTableValueAt(productIndex, 2, productPrice);


            if (productsMap.isEmpty()) {
                System.out.println("MAPA VACIO XDDD");
                productsMap.put(productAmount, product);
            } else {
                Iterator<Map.Entry<Integer, Product>> iterator = productsMap.entries().iterator();
                boolean productAlreadyOnMap = false;
                System.out.println("MAPA NO VACIO XDDD");
                while (iterator.hasNext()) {
                    Map.Entry<Integer, Product> entry2 = iterator.next();
                    Product productValue = entry2.getValue();
                    int productKey = entry2.getKey();
                    System.out.println("PRODUCTO ITERADO DEL MAPA: " + productValue + " CANTIDAD: " + productKey);
                    System.out.println("ESTÁ EN EL MAPA? " + productValue.getName().equals(productName));
                    if (productValue.getName().equals(productName)) { //EL PRODUCT DE LA FILA DE LA PREVIEW TABLE, YA ESTÁ EN EL MAPA:
                        System.out.println("EL PRODUCTO YA ESTÁ EN EL MAPA");
                        productsMap.remove(productKey, productValue); //REMUEVO ESE PRODUCTO DEL MAPA
                        productKey += productAmount; //LE SUMO LA CANTIDAD DE PRODUCTOS QUE SE ESTÁN AGREGANDO
                        productsMap.put(productKey, productValue); //AGREGO EL PRODUCTO CON LA NUEVA CANTIDAD
                        productAlreadyOnMap = true;
                        break; //CORTO EL BUCLE
                    }
                }

                if (!productAlreadyOnMap) {
                    System.out.println("EL PRODUCTO NO ESTÁ EN EL MAPA");
                    productsMap.put(productAmount, product);
                }
            }

            productIndex++;
            observationsIndex++;
            measuresIndex++;
        }

        budgetModifyView.setPreviewStringTableValueAt(0, 4, clientType);
        budgetModifyView.setPreviewIntTableValueAt(0, 5, budgetNumber);

        budgetModifyView.setWaitingStatus();
    }

    public void onModifySearchViewButtonClicked(JTable table, int selectedRow) {
        StringBuilder sb = budgetModifyView.getStringBuilder();
        JTextArea textArea = budgetModifyView.getPriceTextArea();
        Multimap<Integer, String> savedProducts = ArrayListMultimap.create();
        ArrayList<String> productNames = new ArrayList<>();
        ArrayList<String> productObservations = new ArrayList<>();
        ArrayList<String> productMeasures = new ArrayList<>();
        int budgetNumber = Integer.parseInt((String) table.getValueAt(selectedRow, 3));
        ArrayList<String> budgetData = budgetModifyModel.getSelectedBudgetData(budgetNumber);

        if (selectedRow != -1) {
            String selectedBudgetName = (String) table.getValueAt(selectedRow, 0);
            int selectedBudgetNumber = Integer.parseInt((String) table.getValueAt(selectedRow, 3));
            savedProducts = budgetModifyModel.getSavedProducts(selectedBudgetNumber, selectedBudgetName);
            productObservations = budgetModifyModel.getProductObservations(selectedBudgetNumber, selectedBudgetName);
            productMeasures = budgetModifyModel.getProductMeasures(selectedBudgetNumber, selectedBudgetName);
            setModifyView(budgetNumber, selectedRow, savedProducts, productObservations, productMeasures);
            updateTextArea(sb, textArea, false);
        }
    }

    @Override
    protected void initListeners() {

    }

    public void onAddProductButtonClicked() {
        int selectedProductRow = budgetModifyView.getProductTableSelectedRow();
        int filledRows = budgetModifyView.countNonEmptyCells(budgetModifyView.getPreviewTable(), 1);
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

        if (selectedProductRow != -1) {

            productName = budgetModifyView.getProductStringTableValueAt(selectedProductRow, 0);
            productObservations = budgetModifyView.getObservationsTextField().getText();
            productMeasures = budgetModifyView.getMeasuresTextField().getText();
            productAmountStr = budgetModifyView.getAmountTextField().getText();

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

            textToPut = productName + " * (" + productAmountInt + ".00) Unidades " + "\t" + "Medidas: " + productMeasures + "\t" + " Observaciones: " + productObservations;
            budgetModifyView.setPreviewStringTableValueAt(filledRows + 1, 1, textToPut);
            productID = productModel.getProductID(productName);
            Product product = productModel.getOneProduct(productID);
            productPrice = product.getPrice() * productAmountInt;
            budgetModifyView.setPreviewDoubleTableValueAt(filledRows + 1, 2, productPrice);
            updateTextArea(sb, priceTextArea, true);
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

    public boolean onEmptyFields(int clientNameColumn, int productColumn, int clientTypeColumn) {
        boolean anyEmpty = false;
        String clientName = budgetModifyView.getPreviewStringTableValueAt(0, clientNameColumn);
        String clientType = budgetModifyView.getPreviewStringTableValueAt(0, clientTypeColumn);
        String product = budgetModifyView.getPreviewStringTableValueAt(1, productColumn);

        if ((clientName == null || clientName.trim().isEmpty()) ||
                (clientType == null || clientType.trim().isEmpty()) ||
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
        }
    }

    public void updateTextArea(StringBuilder stb, JTextArea textArea, boolean adding) {
        double totalPrice = 0;
        int productAmount = 0;
        int indiceAst = -1;
        int endOfAmountValueIndex = -1;
        int selectedProductRowIndex = budgetModifyView.getProductTableSelectedRow();
        double productPrice = 0;
        int filledCells = budgetModifyView.countNonEmptyCells(budgetModifyView.getPreviewTable(), 1);
        System.out.println("HAY " + filledCells + " CELDAS CON CONTENIDO.");
        stb.setLength(0);

        System.out.println("HAY: " + filledCells + " CELDAS LLENAS EN LA PREVIEW TABLE.");


        if(adding){ // SI SE ESTA HACIENDO UPDATE DEL PRECIO PORQUE SE AGREGÓ UN PRODUCTO:
            productPrice = (double) budgetModifyView.getProductsResultTable().getValueAt(selectedProductRowIndex, 2); // EL PRECIO DEL PRODUCTO ES EL PRECIO DE LA TABLA DE PRODUCTOS DE LA FILA SELECCIONADA
            if (filledCells == 0) { // SI NO HAY NINGUN PRODUCTO EN LA PREVIEW TABLE:
                if (selectedProductRowIndex != -1) { // SI SE SELECCIONÓ UN PRODUCTO DE LA TABLA DE PRODUCTOS:
                    if (budgetModifyView.getAmountTextField().getText().equals("")) { // SI NO SE ESPECIFICÓ LA CANTIDAD DE PRODUCTOS A AGREGAR:
                        productAmount = 1; // SE PONE CANTIDAD 1 POR DEFECTO
                    } else { // SI SE ESPECIFICÓ LA CANTIDAD DE PRODUCTOS A AGREGAR:
                        productAmount = Integer.parseInt(budgetModifyView.getAmountTextField().getText());  // SE CAPTURA LA CANTIDAD DE PRODUCTOS A AGREGAR DESDE EL TEXTFIELD
                    }
                    totalPrice += productPrice * productAmount; // SE AGREGA LA CANTIDAD * PRECIO AL PRECIO TOTAL
                } else { // SI NO SE SELECCIONÓ NINGUN PRODUCTO DE LA TABLA DE PRODUCTOS:
                    totalPrice = 0; // EL PRECIO TOTAL ES 0
                }
            } else { // SI YA HAY PRODUCTOS EN LA PREVIEW TABLE:
                for (int i = 1; i <= filledCells; i++) { // SE RECORREN LAS FILAS DE LA PREVIEW TABLE
                    indiceAst = ((String) budgetModifyView.getPreviewTable().getValueAt(i, 1)).indexOf('*');
                    endOfAmountValueIndex = ((String) budgetModifyView.getPreviewTable().getValueAt(i, 1)).indexOf(".00)");
                    productAmount = Integer.parseInt(((String) budgetModifyView.getPreviewTable().getValueAt(i, 1)).substring(indiceAst + 3, endOfAmountValueIndex));
                    productPrice = (double) budgetModifyView.getPreviewTable().getValueAt(i, 2);
                    totalPrice += productPrice;
                }
            }
        } else { // SI SE ESTA HACIENDO UPDATE DEL PRECIO PORQUE SE ELIMINÓ UN PRODUCTO:
            if(filledCells == 0) {
                totalPrice = 0;
            } else {
                for (int i = 1; i <= filledCells; i++) {
                    indiceAst = ((String) budgetModifyView.getPreviewTable().getValueAt(i, 1)).indexOf('*');
                    endOfAmountValueIndex = ((String) budgetModifyView.getPreviewTable().getValueAt(i, 1)).indexOf(".00)");
                    productAmount = Integer.parseInt(((String) budgetModifyView.getPreviewTable().getValueAt(i, 1)).substring(indiceAst + 3, endOfAmountValueIndex));

                    //TESTING:
                    System.out.println("INTENTANDO CAPTURAR EL PRECIO DE LA FILA: " + i + " DE LA PREVIEW TABLE. OBTUVE: " + budgetModifyView.getProductsResultTable().getValueAt(i, 2));
                    productPrice = (double) budgetModifyView.getPreviewTable().getValueAt(i, 2);


                    totalPrice += productPrice;
                }
            }
        }

        stb.append("\nPrecio Total: $").append(String.format("%.2f", totalPrice));
        textArea.setText(stb.toString());
    }

    public void onClientSelectedCheckBoxClicked() {
        JCheckBox clientSelectedCheckBox = budgetModifyView.getClientSelectedCheckBox();
        if (clientSelectedCheckBox.isSelected()) {
            budgetModifyView.setSecondPanelsVisibility();
        } else {
            budgetModifyView.setInitialPanelsVisibility();
        }
    }

    public void onAddClientButtonClicked() {
        int selectedRow = budgetModifyView.getClientTableSelectedRow();
        String clientName = "";
        String clientType = "";
        JCheckBox clientSelectedCheckBox = budgetModifyView.getClientSelectedCheckBox();
        if (selectedRow != -1) {
            clientName = budgetModifyView.getClientStringTableValueAt(selectedRow, 0);
            clientType = budgetModifyView.getClientStringTableValueAt(selectedRow, 4);
            budgetModifyView.setPreviewStringTableValueAt(0, 0, clientName);
            budgetModifyView.setPreviewStringTableValueAt(0, 4, clientType);
            clientSelectedCheckBox.setSelected(true);
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

}
