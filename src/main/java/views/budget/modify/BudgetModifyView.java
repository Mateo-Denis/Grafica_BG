package views.budget.modify;

import presenters.StandardPresenter;
import presenters.budget.BudgetModifyPresenter;
import utils.NumberInputVerifier;
import utils.Product;
import views.ToggleableView;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.AbstractDocument;
import java.awt.event.ItemListener;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class BudgetModifyView extends ToggleableView implements IBudgetModifyView {
    private JPanel containerPanel;
    private JPanel clientSearchingContainer;
    private JPanel productSearchingContainer;
    private JPanel budgetPreviewContainer;
    private JPanel budgetCreationButtonsContainer;
    private JPanel priceContainer;
    private JPanel clientResultContainer;
    private JLabel budgetNumberLabel;
    private JScrollPane clientResultScrollPanel;
    private JPanel addClientContainer;
    private JPanel nameAndCityContainer;
    private JTable clientResultTable;
    private JButton clientAddButton;
    private JLabel clientLabel;
    private JTextField clientTextField;
    private JLabel cityLabel;
    private JComboBox cityComboBox;
    private JButton clientSearchButton;
    private JPanel clientSelectedCheckContainer;
    private JPanel categoriesContainer;
    private JPanel especificationsContainer;
    private JPanel productTableContainer;
    private JCheckBox clientSelectedCheckBox;
    private JLabel productLabel;
    private JPanel productTextFieldContainer;
    private JTextField productTextField;
    private JComboBox productComboBox;
    private JPanel productSearchButtonContainer;
    private JButton productSearchButton;
    private JPanel amountContainer;
    private JPanel measuresContainer;
    private JPanel observationsContainer;
    private JLabel amountLabel;
    private JTextField amountTextField;
    private JLabel measuresLabel;
    private JTextField measuresTextField;
    private JTextField observationsTextField;
    private JLabel observationsLabel;
    private JScrollPane productTableScrollPanel;
    private JTable productTable;
    private JButton addProductButton;
    private JLabel previewTableLabel;
    private JPanel previewTableContainer;
    private JScrollPane budgetPreviewScrollPanel;
    private JTable budgetPreviewTable;
    private JTextArea priceTextArea;
    private JButton budgetPreviewButton;
    private JButton budgetModifyButton;
    private JButton deleteProductButton;
    private BudgetModifyPresenter budgetModifyPresenter;
    private DefaultTableModel clientsTableModel;
    private DefaultTableModel productsTableModel;
    private DefaultTableModel previewTableModel;
    private StringBuilder sb = new StringBuilder();


    public BudgetModifyView(){
        windowFrame = new JFrame("Modificar Presupuesto");
        windowFrame.setContentPane(containerPanel);
        windowFrame.pack();
        windowFrame.setLocationRelativeTo(null);
        windowFrame.setIconImage(new ImageIcon("src/main/resources/BGLogo.png").getImage());
        ((AbstractDocument) amountTextField.getDocument()).setDocumentFilter(new NumberInputVerifier());
        budgetModifyButton.setVisible(true);
        priceTextArea.setEditable(false);
        sb.append("Productos: ");
        sb.append("\n");
        sb.append("Precio total: ");
        priceTextArea.setText(sb.toString());

        clientSearchingContainer.setVisible(false);
    }

    @Override
    protected void wrapContainer() {
        containerPanelWrapper = containerPanel;
    }

    @Override
    protected void initListeners() {
        budgetModifyButton.addActionListener(e -> budgetModifyPresenter.onSaveModificationsButtonClicked());
        addProductButton.addActionListener(e -> budgetModifyPresenter.onAddProductButtonClicked());
        deleteProductButton.addActionListener(e -> budgetModifyPresenter.onDeleteProductButtonClicked());
        clientSelectedCheckBox.addItemListener(e -> budgetModifyPresenter.onClientSelectedCheckBoxClicked());
        clientAddButton.addActionListener(e -> budgetModifyPresenter.onAddClientButtonClicked());
        productSearchButton.addActionListener(e -> budgetModifyPresenter.onSearchProductButtonClicked());
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
        for (int row = 0; row < budgetPreviewTable.getRowCount(); row++) {
            for (int col = 0; col < budgetPreviewTable.getColumnCount(); col++) {
                budgetPreviewTable.setValueAt("", row, col);
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
        for (int row = 0; row < productTable.getRowCount(); row++) {
            for (int col = 0; col < productTable.getColumnCount(); col++) {
                productTable.setValueAt("", row, col);
            }
        }
    }

    @Override
    public void setPresenter(StandardPresenter budgetModifyPresenter) {
        this.budgetModifyPresenter = (BudgetModifyPresenter) budgetModifyPresenter;
    }

    @Override
    public void setCategoriesComboBox(List<String> categorias) {
        productComboBox.addItem("Seleccione una categoría");
        for (String categoria : categorias) {
            productComboBox.addItem(categoria);
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
        return productComboBox;
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
        return (String) productComboBox.getSelectedItem();
    }

    @Override
    public void comboBoxListenerSet(ItemListener listener) {
        productComboBox.addItemListener(listener);
    }

    @Override
    public void setProductStringTableValueAt(int row, int col, String value) {
        productTable.setValueAt(value, row, col);
    }

    @Override
    public void setProductDoubleTableValueAt(int row, int col, Double value) {
        productTable.setValueAt(value, row, col);
    }

    @Override
    public void setProductIntTableValueAt(int row, int col, int value) {
        productTable.setValueAt(value, row, col);
    }


    @Override
    public void start() {
        super.start();
        clientsTableModel = new DefaultTableModel(new Object[]{"Nombre", "Dirección", "Localidad", "Teléfono", "Cliente/Particular"}, 200);
        clientResultTable.setModel(clientsTableModel);
        productsTableModel = new DefaultTableModel(new Object[]{"Nombre", "Descripción", "Precio", "Categoria"}, 200);
        productTable.setModel(productsTableModel);
        previewTableModel = new DefaultTableModel(new Object[]{"Nombre del Cliente", "Productos", "Precio", "Fecha del presupuesto", "Cliente / Particular", "Numero de Presupuesto"}, 200);
        budgetPreviewTable.setModel(previewTableModel);
        budgetPreviewTable.getColumnModel().getColumn(1).setPreferredWidth(400);

    }

    public JButton getAddProductButton() {
        return addProductButton;
    }

    public void addProductToPreviewTable(Product product, int row) {
        budgetPreviewTable.setValueAt(product.getName(), row, 1);
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

    public JButton getClientsSearchButton() {
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
        return clientAddButton;
    }

    @Override
    public void setPreviewStringTableValueAt(int row, int col, String value) {
        budgetPreviewTable.setValueAt(value, row, col);
    }

    @Override
    public void setPreviewDoubleTableValueAt(int row, int col, Double value) {
        budgetPreviewTable.setValueAt(value, row, col);
    }

    @Override
    public void setPreviewIntTableValueAt(int row, int col, int value) {
        budgetPreviewTable.setValueAt(value, row, col);
    }

    public int getClientTableSelectedRow() {
        return clientResultTable.getSelectedRow();
    }

    public int getProductTableSelectedRow() {
        return productTable.getSelectedRow();
    }

    public JTable getClientResultTable() {
        return clientResultTable;
    }

    @Override
    public JTable getPreviewTable() {
        return budgetPreviewTable;
    }

    public String getProductStringTableValueAt(int row, int col) {
        return (String) productTable.getValueAt(row, col);
    }

    public String getClientStringTableValueAt(int row, int col) {
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

    public String getPreviewStringTableValueAt(int row, int col) {
        return (String) budgetPreviewTable.getValueAt(row, col);
    }

    @Override
    public int getPreviewIntTableValueAt(int row, int col) {
        return (int) budgetPreviewTable.getValueAt(row, col);
    }

    public JTextField getProductsTextField() {
        return productTextField;
    }

    public JButton getProductSearchButton() {
        return productSearchButton;
    }

    @Override
    public void setClientOnPreviewTable(String clientName, String clientType) {
        budgetPreviewTable.setValueAt(clientName, 0, 0);
        budgetPreviewTable.setValueAt(clientType, 0, 3);
    }

    @Override
    public JTextField getAmountTextField() {
        return amountTextField;
    }

    @Override
    public JTextField getMeasuresTextField() {
        return measuresTextField;
    }

    @Override
    public JTextField getObservationsTextField() {
        return observationsTextField;
    }

    public int countNonEmptyCells(JTable table, int columnIndex) {
        int count = 0;

        // Iterar a través de todas las filas de la tabla
        for (int rowIndex = 0; rowIndex < table.getRowCount(); rowIndex++) {
            Object cellValue = table.getValueAt(rowIndex, columnIndex);

            // Comprobar si la celda no está vacía (ni null ni cadena vacía)
            if (cellValue != null && !cellValue.equals("")) {
                count++;
            }
        }

        return count;
    }

    public DefaultTableModel getPreviewTableModel() {
        return previewTableModel;
    }

    @Override
    public JButton getSaveModificationsButton() {
        return null;
    }

    public JTextArea getPriceTextArea() {
        return priceTextArea;
    }

    public StringBuilder getStringBuilder() {
        return sb;
    }

    public JTable getProductsResultTable() {
        return productTable;
    }

    public void setInitialPanelsVisibility() {
        productSearchingContainer.setVisible(false);
        budgetCreationButtonsContainer.setVisible(false);
        priceContainer.setVisible(false);
        clientSearchingContainer.setVisible(true);
        budgetPreviewContainer.setVisible(false);
    }

    public void setSecondPanelsVisibility() {
        clientSearchingContainer.setVisible(false);
        budgetPreviewContainer.setVisible(true);
        productSearchingContainer.setVisible(true);
        budgetCreationButtonsContainer.setVisible(true);
        priceContainer.setVisible(true);
    }

    @Override
    public JCheckBox getClientSelectedCheckBox() {
        return clientSelectedCheckBox;
    }
}
