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
    private int rowCountOnPreviewTable = 1;
    private Map<Integer, Product> productsList = new HashMap<>();

    public BudgetModifyPresenter(IBudgetModifyView budgetModifyView, IBudgetModifyModel budgetModifyModel, ICategoryModel categoryModel, IBudgetModel budgetModel, IProductModel productModel) {
        this.budgetModifyView = budgetModifyView;
        view = budgetModifyView;
        this.budgetModifyModel = budgetModifyModel;
        this.categoryModel = categoryModel;
        this.budgetModel = budgetModel;
        this.productModel = productModel;


        budgetModifyView.getCategoriesComboBox().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String selectedOption = budgetModifyView.getSelectedCategory();
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
                        budgetModifyView.setProductStringTableValueAt(rowCount, 0, product.getName());
                        budgetModifyView.setProductStringTableValueAt(rowCount, 1, product.getDescription());
                        budgetModifyView.setProductDoubleTableValueAt(rowCount, 2, product.getPrice());
                        budgetModifyView.setProductStringTableValueAt(rowCount, 3, selectedCategoryName);
                        rowCount++;
                    }
                }
            }
        });

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
                    budgetModifyView.setPreviewStringTableValueAt(0, 3, clientType);
                }
            }
        });

        budgetModifyView.getProductSearchButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String productName = budgetModifyView.getProductsTextField().getText();
                ArrayList<Product> products = budgetModel.getProducts(productName);
                List<String> categoriesName = categoryModel.getCategoriesName();
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
            }
        });

        cargarCategorias();
        cargarCiudades();

    }

    public void onSaveModificationsButtonClicked() {
        String newClientName = budgetModifyView.getPreviewStringTableValueAt(0, 0);
        String date = budgetModifyView.getPreviewStringTableValueAt(0, 2);
        String clientType = budgetModifyView.getPreviewStringTableValueAt(0, 3);
        int budgetNumber = (int) budgetModifyView.getPreviewIntTableValueAt(0, 4);
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
        int productAmount = 0;
        int observationsIndex = 0;
        int measuresIndex = 0;
        int productID = -1;
        int rowCount = budgetModifyView.countNonEmptyCells(budgetModifyView.getPreviewTable(), 1);
        int productAmountToSave = 0;
        Map<Integer, Product> productsMap = new HashMap<>();
        budgetModifyView.setPreviewStringTableValueAt(0, 0, clientName);
        budgetModifyView.setPreviewStringTableValueAt(0, 2, date);

        for (Map.Entry<Integer, String> entry : products.entries()) {

            productAmountToSave = entry.getKey();
            productAmount = entry.getKey();
            productName = entry.getValue();
            productObservation = productObservations.get(observationsIndex);
            productMeasure = productMeasures.get(measuresIndex);
            productString = productName + " * (" + productAmount + ".00) Unidades" + "\t" + " Medidas: " + productMeasure + "\t" + " Observaciones: " + productObservation;
            budgetModifyView.setPreviewStringTableValueAt(productIndex, 1, productString);
            productID = productModel.getProductID(productName);
            Product product = productModel.getOneProduct(productID);

            if (productsMap.isEmpty()) {
                System.out.println("MAPA VACIO XDDD");
                productsMap.put(productAmount, product);
            } else {
                Iterator<Map.Entry<Integer, Product>> iterator = productsMap.entrySet().iterator();
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

        budgetModifyView.setPreviewStringTableValueAt(0, 3, clientType);
        budgetModifyView.setPreviewIntTableValueAt(0, 4, budgetNumber);

        updateTotalPriceTextArea(textArea, sb, productsMap);
        budgetModifyView.setWaitingStatus();
    }

    public void onModifySearchViewButtonClicked(JTable table, int selectedRow) {
        rowCountOnPreviewTable = 1;
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
        JTextArea priceTextArea = budgetModifyView.getPriceTextArea();
        StringBuilder sb = budgetModifyView.getStringBuilder();

        if (selectedProductRow != -1) {

            productName = budgetModifyView.getProductStringTableValueAt(selectedProductRow, 0);
            System.out.println("Product Name: " + productName);
            productObservations = budgetModifyView.getObservationsTextField().getText();
            System.out.println("Product Observations: " + productObservations);
            productMeasures = budgetModifyView.getMeasuresTextField().getText();
            System.out.println("Product Measures: " + productMeasures);
            productAmountStr = budgetModifyView.getAmountTextField().getText();

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

            if (budgetModifyView.countNonEmptyCells(budgetModifyView.getPreviewTable(), 1) == 0) {

                textToPut = productName + " * (" + productAmountInt + ".00) Unidades " + "\t" + "Medidas: " + productMeasures + "\t" + " Observaciones: " + productObservations;
                budgetModifyView.setPreviewStringTableValueAt(1, 1, textToPut);
                rowCountOnPreviewTable = 2;

            } else {
                rowCountOnPreviewTable = budgetModifyView.countNonEmptyCells(budgetModifyView.getPreviewTable(), 1) + 1;
                for (int i = 0; i < budgetModifyView.getPreviewTable().getRowCount() && !coincidenTodosLosCampos; i++) {
                    productCellValue = budgetModifyView.getPreviewTable().getValueAt(i, 1);

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
                    budgetModifyView.setPreviewStringTableValueAt(coincidentIndex, 1, textToPut);
                    System.out.println("Text to put: " + textToPut);
                } else {
                    textToPut = productName + " * (" + productAmountInt + ".00) Unidades " + "\t" + "Medidas: " + productMeasures + "\t" + " Observaciones: " + productObservations;
                    budgetModifyView.setPreviewStringTableValueAt(rowCountOnPreviewTable, 1, textToPut);
                    rowCountOnPreviewTable++;
                }
                budgetModifyView.setWaitingStatus();
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
        int selectedRow = budgetModifyView.getPreviewTable().getSelectedRow();
        String productName;
        Object productCellValue = "";
        int productID = -1;
        int productAmount = -1;
        int indiceX = -1;
        int endOfAmountValueIndex = -1;

        if (selectedRow != -1 && selectedRow != 0) {
            productCellValue = budgetModifyView.getPreviewTable().getValueAt(selectedRow, 1);
            if (productCellValue != null && !productCellValue.equals("")) {
                indiceX = ((String) productCellValue).indexOf('*');
                endOfAmountValueIndex = ((String) productCellValue).indexOf(".00)");
                productName = ((String) productCellValue).substring(0, ((String) productCellValue).indexOf("*") - 1).trim();
                productAmount = Integer.parseInt(((String) productCellValue).substring(indiceX + 3, endOfAmountValueIndex).trim());
                productID = productModel.getProductID(productName);
                Product product = productModel.getOneProduct(productID);

                budgetModifyView.getPreviewTableModel().removeRow(selectedRow);
                rowCountOnPreviewTable = budgetModifyView.countNonEmptyCells(budgetModifyView.getPreviewTable(), 1) + 1;

                productsList.remove(productAmount, product);
                updateTotalPriceTextArea(budgetModifyView.getPriceTextArea(), budgetModifyView.getStringBuilder(), productsList);
            }
        }
    }

    public void updateTotalPriceTextArea(JTextArea textArea, StringBuilder sb, Map<Integer, Product> products) {
        double totalPrice = 0;
        sb.setLength(0);
        sb.append("Productos:\n");
        for (Map.Entry<Integer, Product> entry : products.entrySet()) {
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

}
