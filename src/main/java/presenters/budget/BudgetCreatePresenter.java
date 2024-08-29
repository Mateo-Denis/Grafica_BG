package presenters.budget;


import presenters.StandardPresenter;
import models.IBudgetModel;
import models.ICategoryModel;
import utils.Budget;
import utils.Client;
import utils.Product;
import utils.databases.BudgetsDatabaseConnection;
import views.budget.*;
import views.products.IProductSearchView;
import views.products.ProductSearchView;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.lang.reflect.Array;
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


        if (onEmptyFields(0, 1,3))
        {
            budgetCreateView.showMessage(BUDGET_CREATION_EMPTY_COLUMN);
        }
        else
        {
            budgetModel.createBudget(
                    budgetCreateView.getPreviewStringTableValueAt(0, 0),
                    budgetCreateView.getPreviewStringTableValueAt(0, 2),
                    budgetCreateView.getPreviewStringTableValueAt(0, 3),
                    budgetCreateView.getPreviewIntTableValueAt(0, 4)
            );
            budgetCreateView.showMessage(BUDGET_CREATION_SUCCESS);

            Map<Integer,String> products = new HashMap<>();
            ArrayList<String> productsName = getBudgetProductsName(budgetCreateView.getPreviewStringTableValueAt(0, 1));

            for(String productName : productsName) {
               //DEBUGGING
                System.out.println(productName);
                //DEBUGGING

                Object value = "";
                int productCount = 0;
                int columnIndex = 1;
                for(int i = 0; i < budgetCreateView.getPreviewTable().getRowCount(); i++){
                    value = budgetCreateView.getPreviewTable().getValueAt(i, columnIndex);
                    if(value.equals(productName)) {
                        productCount++;
                        //DEBUGGING
                        System.out.println(productCount);
                        //DEBUGGING
                    }
                }
                products.put(productCount, productName);
            }

            budgetModel.saveProducts(budgetCreateView.getPreviewStringTableValueAt(0,0), products);
        }


        budgetCreateView.setWaitingStatus();
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
        if (selectedRow != -1) {
            productName = budgetCreateView.getProductStringTableValueAt(selectedRow, 0);
            budgetCreateView.setPreviewStringTableValueAt(rowCountOnPreviewTable, 1, productName);
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

    public ArrayList<String> getBudgetProductsName(String budgetName) {
        Map<Integer,String> products = budgetModel.getSavedProducts(budgetName);
        ArrayList<String> productsName = new ArrayList<>();

        for (int i = 0; i < products.size(); i++) {
            String productName = products.get(i);
            productsName.add(productName);
        }
        return productsName;
    }
}
