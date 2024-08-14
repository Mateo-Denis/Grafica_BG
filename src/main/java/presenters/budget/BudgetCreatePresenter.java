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

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static utils.MessageTypes.*;

public class BudgetCreatePresenter extends StandardPresenter {
    private final IBudgetCreateView budgetCreateView;
    private final IBudgetModel budgetModel;
    private final ICategoryModel categoryModel;
    //private ProductSearchView productSearchView;
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
                String selectedOption = (String) budgetCreateView.getCategoriesComboBox().getSelectedItem();
                cargarProductosSegunCategoria(selectedOption);
            }
        });

        budgetCreateView.getCategoriesComboBox().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String selectedOption = (String) budgetCreateView.getCategoriesComboBox().getSelectedItem();
                ArrayList<Product> products = budgetModel.getProducts();
                int rowCount = 0;
                for (Product product : products) {
                    if (product.getCategoryName().equals(selectedOption)) {
                        budgetCreateView.setProductStringTableValueAt(rowCount, 0, product.getName());
                        budgetCreateView.setProductStringTableValueAt(rowCount, 1, product.getDescription());
                        budgetCreateView.setProductDoubleTableValueAt(rowCount, 2, product.getPrice());
                        budgetCreateView.setProductStringTableValueAt(rowCount, 3, product.getCategoryName());
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

        budgetModel.createBudget(
                budgetCreateView.getPreviewStringTableValueAt(0,0),
                budgetCreateView.getPreviewStringTableValueAt(0, 2),
                budgetCreateView.getPreviewStringTableValueAt(0, 3),
                budgetCreateView.getPreviewIntTableValueAt(0, 4)
        );
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

    private void cargarProductosSegunCategoria(String categoria) {
        ArrayList<String> productNames = budgetModel.getProductNamesByCategory(categoria);
        budgetCreateView.setProductsComboBox(productNames);
    }

    public void onAddProductButtonClicked() {
        ArrayList<Product> products = budgetModel.getProducts();
        Product selectedProduct;
        for (Product product : products) {
            if (product.getName().equals(budgetCreateView.getProductsComboBox().getSelectedItem())) {
                selectedProduct = product;
                budgetCreateView.addProductToPreviewTable(selectedProduct, rowCountOnPreviewTable);
            }
        }
        rowCountOnPreviewTable++;
    }

    public void setearFecha() {
        String fecha = (String) budgetCreateView.getBudgetDate();
        budgetCreateView.setPreviewStringTableValueAt(0,2, fecha);
    }

    public void setearNumeroDePresupuesto(){
        budgetCreateView.setPreviewIntTableValueAt(0,4, new Random().nextInt(1000));
    }
}
