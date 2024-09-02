package presenters.budget;


import presenters.StandardPresenter;
import models.IBudgetModel;
import models.ICategoryModel;
import utils.Client;
import utils.Product;
import views.budget.*;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;

import static utils.MessageTypes.*;

public class BudgetCreatePresenter extends StandardPresenter {
    private final IBudgetCreateView budgetCreateView;
    private final IBudgetModel budgetModel;
    private final ICategoryModel categoryModel;
    private int rowCountOnPreviewTable = 0;

    public BudgetCreatePresenter(IBudgetCreateView budgetCreateView, IBudgetModel budgetModel, ICategoryModel categoryModel) {
        this.budgetCreateView = budgetCreateView;
        view = budgetCreateView;
        this.budgetModel = budgetModel;
        this.categoryModel = categoryModel;
        cargarCategorias();
        cargarCiudades();

        budgetCreateView.getCategoriesComboBox().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
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
        });

        budgetCreateView.getClientsSearchButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
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
        });

        budgetCreateView.getClientAddButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = budgetCreateView.getClientTableSelectedRow();
                String clientName = "";
                String clientType = "";
                if (selectedRow != -1) {
                    clientName = budgetCreateView.getClientStringTableValueAt(selectedRow, 0);
                    clientType = budgetCreateView.getClientStringTableValueAt(selectedRow, 4);
                    budgetCreateView.setPreviewStringTableValueAt(0, 0, clientName);
                    budgetCreateView.setPreviewStringTableValueAt(0, 3, clientType);
                }
            }
        });

        budgetCreateView.getProductSearchButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
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
        });
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

            Map<Integer, String> products = new HashMap<>();

            ArrayList<String> productsName = new ArrayList<>();
            Object value = "";
            int productCount = 0;
            int amount = 0;
            String measures = "";
            String observations = "";
            int columnIndex = 1;
            for (int i = 0; i < budgetCreateView.getPreviewTable().getRowCount(); i++) {
                value = budgetCreateView.getPreviewTable().getValueAt(i, columnIndex);
                int indiceX = ((String) value).indexOf('*');
                String productName = ((String) value).substring(0, indiceX).trim();

                if (!productsName.contains(productName)) {
                    productsName.add((String) value);
                }

                budgetModel.saveProducts(budgetCreateView.getPreviewIntTableValueAt(0, 4), budgetCreateView.getPreviewStringTableValueAt(0, 0), products);
            }


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
        int selectedRow = budgetCreateView.getProductTableSelectedRow();
        String productName = "";
        String productMeasures = "";
        String productObservations = "";
        String textToPut = "";
        int productAmount = 0;

        if (selectedRow != -1) {
            productName = budgetCreateView.getProductStringTableValueAt(selectedRow, 0);
            productObservations = budgetCreateView.getObservationsTextField().getText();
            productMeasures = budgetCreateView.getMeasuresTextField().getText();
            if(budgetCreateView.getAmountTextField().getText().isEmpty()){
                productAmount = 1;
            } else {
                productAmount = Integer.parseInt(budgetCreateView.getAmountTextField().getText());
            }

            textToPut = productName + " * " + productAmount + ".00 Unidades " + "\t" + "Medidas: " + productMeasures + "\t" + " Observaciones: " + productObservations;

            budgetCreateView.setPreviewStringTableValueAt(rowCountOnPreviewTable, 1, textToPut);
        }
        rowCountOnPreviewTable++;
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
        String product = budgetCreateView.getPreviewStringTableValueAt(0, productColumn);

        if ((clientName == null || clientName.trim().isEmpty()) ||
                (clientType == null || clientType.trim().isEmpty()) ||
                (product == null || product.trim().isEmpty())) {
            anyEmpty = true;
        }
        return anyEmpty;
    }

    public ArrayList<String> getBudgetProductsName(String budgetName, int budgetNumber) {
        Map<Integer, String> products = budgetModel.getSavedProducts(budgetNumber, budgetName);
        ArrayList<String> productsName = new ArrayList<>();

        for (Map.Entry<Integer, String> entry : products.entrySet()) {
            String actualProductName = entry.getValue();
            if (!productsName.contains(actualProductName)) {
                productsName.add(actualProductName);
            }
        }

        return productsName;
    }
}
