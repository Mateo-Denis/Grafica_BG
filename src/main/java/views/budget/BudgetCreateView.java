package views.budget;

import presenters.StandardPresenter;
import presenters.budget.BudgetCreatePresenter;
import utils.NumberInputVerifier;
import utils.Product;
import views.ToggleableView;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.AbstractDocument;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.List;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class BudgetCreateView extends ToggleableView implements IBudgetCreateView {
    private JPanel containerPanel;
    private JPanel clientSearchingContainer;
    private JPanel productSearchingContainer;
    private JPanel budgetPreviewContainer;
    private JLabel budgetNumberLabel;
    private JPanel nameAndCityContainer;
    private JTextField clientTextField;
    private JComboBox cityComboBox;
    private JLabel cityLabel;
    private JLabel clientLabel;
    private JButton clientSearchButton;
    private JPanel clientResultContainer;
    private JScrollPane clientResultScrollPanel;
    private JTable clientResultTable;
    private JPanel categoriesContainer;
    private JLabel productLabel;
    private JComboBox productCategoryComboBox;
    private JPanel especificationsContainer;
    private JTextField amountTextField;
    private JPanel amountContainer;
    private JLabel amountLabel;
    private JPanel observationsContainer;
    private JTextField observationsTextField;
    private JLabel observationsLabel;
    private JComboBox<String> productComboBox;
    private JButton addProductButton;
    private JScrollPane budgetPreviewScrollPanel;
    private JTable budgetPreviewingTable;
    private JPanel budgetCreationButtonsContainer;
    private JButton budgetPreviewButton;
    private JButton budgetCreateButton;
    private JPanel tableContainer;
    private JLabel previewTableLabe;
    private JTable productResultTable;
    private JPanel productTableContainer;
    private JScrollPane productTableScrollPanel;
    private JPanel measuresContainer;
    private JTextField measuresTextField;
    private JLabel measuresLabel;
    private JButton addClientButton;
    private JPanel selectClientButtonContainer;
    private JTextField productTextField;
    private JPanel productTextFieldContainer;
    private JButton productSearchButton;
    private JPanel productSearchButtonContainer;
    private BudgetCreatePresenter budgetCreatePresenter;
    private DefaultTableModel clientsTableModel;
    private DefaultTableModel productsTableModel;
    private DefaultTableModel previewTableModel;

    public BudgetCreateView(){
        windowFrame = new JFrame("Crear Presupuesto");
        windowFrame.setContentPane(containerPanel);
        windowFrame.pack();
        windowFrame.setLocationRelativeTo(null);
        windowFrame.setIconImage(new ImageIcon("src/main/resources/BGLogo.png").getImage());
        ((AbstractDocument) measuresTextField.getDocument()).setDocumentFilter(new NumberInputVerifier());
    }

    @Override
    protected void wrapContainer() {
        containerPanelWrapper = containerPanel;
    }

    @Override
    protected void initListeners() {
        budgetCreateButton.addActionListener(e -> budgetCreatePresenter.onCreateButtonClicked());
        addProductButton.addActionListener(e -> budgetCreatePresenter.onAddProductButtonClicked());
        budgetCreateButton.addActionListener(e -> budgetCreatePresenter.onCreateButtonClicked());
    }

    @Override
    public void clearView() {
        clientTextField.setText("");
        cityComboBox.setSelectedIndex(-1);
        amountTextField.setText("");
        observationsTextField.setText("");
        this.clearPreviewTable();
    }

    @Override
    public String getBudgetClientName() {
        return clientTextField.getText();
    }

    @Override
    public String getBudgetDate() {
        LocalDate fechaActual = LocalDate.now();
        DateTimeFormatter formato = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return fechaActual.format(formato);
    }

    @Override
    public String getBudgetClientType() {
        return "Particular";
    }

    @Override
    public int getBudgetNumber() {
        return 8888;
    }

    @Override
    public void clearPreviewTable() {
        for (int row = 0; row < budgetPreviewingTable.getRowCount(); row++) {
            for (int col = 0; col < budgetPreviewingTable.getColumnCount(); col++) {
                budgetPreviewingTable.setValueAt("", row, col);
            }
        }
    }

    @Override
    public void clearClientTable() {
        for (int row = 0; row < clientResultTable.getRowCount(); row++) {
            for (int col = 0; col < clientResultTable.getColumnCount(); col++) {
                clientResultTable.setValueAt("", row, col);
            }
        }
    }

    @Override
    public void clearProductTable() {
        for (int row = 0; row < productResultTable.getRowCount(); row++) {
            for (int col = 0; col < productResultTable.getColumnCount(); col++) {
                productResultTable.setValueAt("", row, col);
            }
        }
    }

    @Override
    public void setPresenter(StandardPresenter budgetCreatePresenter) {
        this.budgetCreatePresenter = (BudgetCreatePresenter) budgetCreatePresenter;
    }

    @Override
    public void setCategoriesComboBox(List<String> categorias) {
        productCategoryComboBox.addItem("Seleccione una categoría");
        for (String categoria : categorias) {
            productCategoryComboBox.addItem(categoria);
        }
    }

    @Override
    public void setCitiesComboBox(ArrayList<String> cities) {
        cityComboBox.addItem("Seleccione una ciudad");
        for (String city : cities) {
            cityComboBox.addItem(city);
        }
    }

    @Override
    public JComboBox<String> getCitiesComboBox() {
        return cityComboBox;
    }

    @Override
    public JComboBox<String> getCategoriesComboBox() {
        return productCategoryComboBox;
    }

    @Override
    public void setProductsComboBox(ArrayList<String> productsName) {
        productComboBox.addItem("Seleccione un producto:");
        for (String productName : productsName) {
            productComboBox.addItem(productName);
        }
    }

    @Override
    public JComboBox<String> getProductsComboBox() {
        return productComboBox;
    }

    public String getSelectedCategory() {
        return (String) productCategoryComboBox.getSelectedItem();
    }

    @Override
    public void comboBoxListenerSet(ItemListener listener) {
        productComboBox.addItemListener(listener);
    }

    @Override
    public void setProductStringTableValueAt(int row, int col, String value) {
        productResultTable.setValueAt(value, row, col);
    }

    @Override
    public void setProductDoubleTableValueAt(int row, int col, Double value) {
        productResultTable.setValueAt(value, row, col);
    }

    @Override
    public void setProductIntTableValueAt(int row, int col, int value) {
        productResultTable.setValueAt(value, row, col);
    }


    @Override
    public void start() {
        super.start();
        clientsTableModel = new DefaultTableModel(new Object[]{"Nombre", "Dirección", "Localidad", "Teléfono", "Cliente/Particular"}, 200);
        clientResultTable.setModel(clientsTableModel);
        productsTableModel = new DefaultTableModel(new Object[]{"Nombre", "Descripción", "Precio", "Categoria"}, 200);
        productResultTable.setModel(productsTableModel);
        previewTableModel = new DefaultTableModel(new Object[]{"Nombre del Cliente", "Productos", "Fecha del presupuesto", "Cliente / Particular", "Numero de Presupuesto"}, 200);
        budgetPreviewingTable.setModel(previewTableModel);
    }

    public JButton getAddProductButton() {
        return addProductButton;
    }

    public void addProductToPreviewTable(Product product, int row){
        budgetPreviewingTable.setValueAt(product.getName(), row, 1);
    }

    public int getFilledRowsCount(JTable table) {
        int rowCount = 0;
        for (int row = 0; row < table.getRowCount(); row++) {
            if (table.getValueAt(row, 2) != null) {
                rowCount++;
            }
        }
        return rowCount;
    }

    public JButton getClientsSearchButton(){
        return clientSearchButton;
    }

    public void setClientStringTableValueAt(int row, int col, String value) {
        clientResultTable.setValueAt(value, row, col);
    }

    @Override
    public void setClientDoubleTableValueAt(int row, int col, Double value) {
        clientResultTable.setValueAt(value, row, col);
    }

    @Override
    public void setClientIntTableValueAt(int row, int col, int value) {
        clientResultTable.setValueAt(value, row, col);
    }

    public JButton getClientAddButton() {
        return addClientButton;
    }

    @Override
    public void setPreviewStringTableValueAt(int row, int col, String value) {
        budgetPreviewingTable.setValueAt(value, row, col);
    }

    @Override
    public void setPreviewDoubleTableValueAt(int row, int col, Double value) {
        budgetPreviewingTable.setValueAt(value, row, col);
    }

    @Override
    public void setPreviewIntTableValueAt(int row, int col, int value) {
        budgetPreviewingTable.setValueAt(value, row, col);
    }

    public int getClientTableSelectedRow() {
        return clientResultTable.getSelectedRow();
    }

    public int getProductTableSelectedRow() {
        return productResultTable.getSelectedRow();
    }

    public JTable getClientResultTable() {
        return clientResultTable;
    }

    @Override
    public JTable getPreviewTable() {
        return budgetPreviewingTable;
    }

    public String getProductStringTableValueAt(int row, int col) {
        return (String) productResultTable.getValueAt(row, col);
    }

    public String getClientStringTableValueAt(int row, int col){
        return (String) clientResultTable.getValueAt(row, col);
    }

    @Override
    public int getClientIntTableValueAt(int row, int col) {
        return (int) clientResultTable.getValueAt(row, col);
    }

    @Override
    public Double getClientDoubleTableValueAt(int row, int col) {
        return (Double) clientResultTable.getValueAt(row, col);
    }

    public int getColumnCount() {
        return previewTableModel.getColumnCount();
    }

    public String getPreviewStringTableValueAt(int row, int col){
        return (String) budgetPreviewingTable.getValueAt(row, col);
    }

    @Override
    public int getPreviewIntTableValueAt(int row, int col) {
        return (int) budgetPreviewingTable.getValueAt(row, col);
    }

    public JTextField getProductsTextField() {
        return productTextField;
    }

    public JButton getProductSearchButton() {
        return productSearchButton;
    }

    @Override
    public void setClientOnPreviewTable(String clientName, String clientType) {
        budgetPreviewingTable.setValueAt(clientName, 0, 0);
        budgetPreviewingTable.setValueAt(clientType, 0, 3);
    }
}
