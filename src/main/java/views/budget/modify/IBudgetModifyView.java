package views.budget.modify;

import presenters.budget.BudgetCreatePresenter;
import utils.Product;
import views.IToggleableView;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.List;

public interface IBudgetModifyView extends IToggleableView {
    String getBudgetClientName();
    String getBudgetDate();
    String getBudgetClientType();
    int getBudgetNumber();
    void clearPreviewTable();
    void clearClientTable();
    void clearProductTable();
    void setCategoriesComboBox(List<String> categorias);
    JComboBox<String> getCategoriesComboBox();
    void setProductsComboBox(ArrayList<String> productsName);
    JComboBox<String> getProductsComboBox();
    String getSelectedCategory();
    void comboBoxListenerSet(ItemListener listener);
    void setProductStringTableValueAt(int row, int col, String value);
    void setProductDoubleTableValueAt(int row, int col, Double value);
    void setProductIntTableValueAt(int row, int col, int value);
    JButton getAddProductButton();
    void addProductToPreviewTable(Product product, int row);
    void setCitiesComboBox(ArrayList<String> cities);
    JComboBox<String> getCitiesComboBox();
    JButton getClientsSearchButton();
    void setClientStringTableValueAt(int row, int col, String value);
    void setClientIntTableValueAt(int row, int col, int value);
    void setClientDoubleTableValueAt(int row, int col, Double value);
    int getClientTableSelectedRow();
    JTable getClientResultTable();
    JTable getPreviewTable();
    String getClientStringTableValueAt(int row, int col);
    int getClientIntTableValueAt(int row, int col);
    String getProductStringTableValueAt(int row, int col);
    Double getClientDoubleTableValueAt(int row, int col);
    JButton getClientAddButton();
    void setPreviewStringTableValueAt(int row, int col, String value);
    void setPreviewDoubleTableValueAt(int row, int col, Double value);
    void setPreviewIntTableValueAt(int row, int col, int value);
    int getPreviewIntTableValueAt(int row, int col);
    String getPreviewStringTableValueAt(int row, int col);
    int getProductTableSelectedRow();
    JTextField getProductsTextField();
    JButton getProductSearchButton();
    void setClientOnPreviewTable(String clientName, String clientType);
    JTextField getAmountTextField();
    JTextField getMeasuresTextField();
    JTextField getObservationsTextField();
    int countNonEmptyCells(JTable table, int columnIndex);
    DefaultTableModel getPreviewTableModel();
    JButton getSaveModificationsButton();
    JTextArea getPriceTextArea();
    StringBuilder getStringBuilder();
    JTable getProductsResultTable();
    void setInitialPanelsVisibility();
    void setSecondPanelsVisibility();
    JCheckBox getClientSelectedCheckBox();
    void setTableVisibility(JTable table);
    void setObservationsTextField(String productsObservation);
    void setMeasuresTextField(String productsMeasure);
    void setAmountTextField(int productsAmount);
}
