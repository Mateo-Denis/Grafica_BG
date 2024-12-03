package views.budget;

import presenters.StandardPresenter;
import presenters.budget.BudgetCreatePresenter;
import views.ToggleableView;
import utils.NumberInputVerifier;
import utils.Product;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.AbstractDocument;
import java.awt.event.*;
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
    private JTextField productTextField;
    private JPanel productTextFieldContainer;
    private JButton productSearchButton;
    private JPanel productSearchButtonContainer;
    private JButton deleteProductButton;
    private JPanel priceContainer;
    private JTextArea priceTextArea;
    private JButton productModifyButton;
    private JPanel clientSelectedCheckContainer;
    private JCheckBox clientSelectedCheckBox;
    private JPanel addClientContainer;
    private JButton saveModificationsButton;
    private BudgetCreatePresenter budgetCreatePresenter;
    private DefaultTableModel clientsTableModel;
    private DefaultTableModel productsTableModel;
    private DefaultTableModel previewTableModel;
    private StringBuilder sb = new StringBuilder();

    public BudgetCreateView() {
        windowFrame = new JFrame("Crear Presupuesto");
        windowFrame.setContentPane(containerPanel);
        windowFrame.pack();
        windowFrame.setLocationRelativeTo(null);
        windowFrame.setIconImage(new ImageIcon("src/main/resources/BGLogo.png").getImage());
        windowFrame.setSize(590,1010);
        windowFrame.setResizable(true);
        ((AbstractDocument) amountTextField.getDocument()).setDocumentFilter(new NumberInputVerifier());

        sb.append("Precio Total: ");
        priceTextArea.setEditable(false);
        priceTextArea.setText(sb.toString());

        productSearchingContainer.setVisible(false);
        productModifyButton.setVisible(false);
        budgetCreationButtonsContainer.setVisible(false);
        priceContainer.setVisible(false);

        cambiarTamanioFuente(containerPanel, 14);

        windowFrame.setSize(750,800);
        windowFrame.setResizable(false);

    }

    @Override
    protected void wrapContainer() {
        containerPanelWrapper = containerPanel;
    }

    @Override
    protected void initListeners() {
        budgetCreateButton.addActionListener(e -> {
            budgetCreatePresenter.onCreateButtonClicked();
            restartWindow();
        });
        productSearchButton.addActionListener(e -> budgetCreatePresenter.onSearchProductButtonClicked());
        addProductButton.addActionListener(e -> budgetCreatePresenter.onAddProductButtonClicked());
        deleteProductButton.addActionListener(e -> budgetCreatePresenter.onDeleteProductButtonClicked());
        addClientButton.addActionListener(e -> budgetCreatePresenter.onAddClientButtonClicked());
        clientSearchButton.addActionListener(e -> budgetCreatePresenter.onSearchClientButtonClicked());
        clientSelectedCheckBox.addItemListener(e -> budgetCreatePresenter.onClientSelectedCheckBoxClicked());
        budgetPreviewingTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                // Detectar doble clic
                if (e.getClickCount() == 2 && !e.isConsumed()) {
                    e.consume(); // Evitar más procesamiento de este evento
                    int clickedRow = budgetPreviewingTable.getSelectedRow();
                    budgetCreatePresenter.onPreviewTableDoubleClickedRow(clickedRow);
                }
            }
        });
        windowFrame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                restartWindow();
            }
        });
    }

    @Override
    public void clearView() {
        clientTextField.setText("");
        productTextField.setText("");
        cityComboBox.setSelectedIndex(0);
        amountTextField.setText("");
        observationsTextField.setText("");
        measuresTextField.setText("");
        clientSelectedCheckBox.setSelected(false);
        clearPreviewTable();
        clearClientTable();
        clearProductTable();
    }

    @Override
    public String getBudgetClientName() {
        return clientTextField.getText();
    }

    public void restartWindow() {
        windowFrame.setSize(590,1010);
        sb.setLength(0);
        sb.append("Precio Total: ");
        priceTextArea.setEditable(false);
        priceTextArea.setText(sb.toString());
        setInitialPanelsVisibility();
        clearView();
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
        clientsTableModel = new DefaultTableModel(new Object[]{"ID", "Nombre", "Dirección", "Localidad", "Teléfono", "Cliente/Particular"}, 200) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        clientResultTable.setModel(clientsTableModel);

        productsTableModel = new DefaultTableModel(new Object[]{"Nombre", "Precio", "Categoria"}, 200) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        productResultTable.setModel(productsTableModel);

        previewTableModel = new DefaultTableModel(new Object[]{"Nombre del Cliente", "Nombre del producto", "Cantidad del producto", "Medidas" , "Observaciones",  "Precio", "Cliente / Particular"}, 200) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        budgetPreviewingTable.setModel(previewTableModel);

        setTableVisibility(clientResultTable);
        setTableVisibility(productResultTable);
        setTableVisibility(budgetPreviewingTable);

        clientSearchingContainer.setBorder(BorderFactory.createEmptyBorder(0, 0, 100, 0));
        addClientContainer.setBorder(BorderFactory.createEmptyBorder(15, 0, 0, 0));
    }

    public JButton getAddProductButton() {
        return addProductButton;
    }

    public void addProductToPreviewTable(Product product, int row) {
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
        budgetPreviewingTable.setValueAt(clientType, 0, 6);
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
            if (cellValue != null && !cellValue.toString().trim().isEmpty()) {
                count++;
            }
        }

        return count;
    }

    public DefaultTableModel getPreviewTableModel() {
        return previewTableModel;
    }

    public BudgetCreatePresenter getBudgetCreatePresenter() {
        return budgetCreatePresenter;
    }

    public JTextArea getPriceTextArea() {
        return priceTextArea;
    }

    @Override
    public void setProductNameTextField(String productsName) {
        productTextField.setText(productsName);
    }

    @Override
    public void setObservationsTextField(String productsObservation) {
        observationsTextField.setText(productsObservation);
    }

    @Override
    public void setMeasuresTextField(String productsMeasure) {
        measuresTextField.setText(productsMeasure);
    }

    @Override
    public void setAmountTextField(int productsAmount) {
        amountTextField.setText(String.valueOf(productsAmount));
    }

    @Override
    public void setTableVisibility(JTable table) {
        int rowCountToShow = 4;
        int rowHeight = table.getRowHeight(); // Altura de cada fila
        int tableHeight = rowCountToShow * rowHeight; // Altura total para x filas
        // Establecer la altura preferida del viewport dentro del JScrollPane
        table.setPreferredScrollableViewportSize(new java.awt.Dimension(
                table.getPreferredSize().width, tableHeight));
    }

    @Override
    public JCheckBox getClientSelectedCheckBox() {
        return clientSelectedCheckBox;
    }

    public void setInitialPanelsVisibility() {
        productSearchingContainer.setVisible(false);
        budgetCreationButtonsContainer.setVisible(false);
        priceContainer.setVisible(false);
        clientSearchingContainer.setVisible(true);
        budgetPreviewContainer.setVisible(true);
        windowFrame.setSize(750,800);
        windowFrame.setResizable(false);
    }

    public void setSecondPanelsVisibility() {
        clientSearchingContainer.setVisible(false);
        budgetPreviewContainer.setVisible(true);
        productSearchingContainer.setVisible(true);
        budgetCreationButtonsContainer.setVisible(true);
        priceContainer.setVisible(true);
        windowFrame.setSize(750,800);
        windowFrame.setResizable(false);
    }

    @Override
    public JTable getProductsResultTable() {
        return productResultTable;
    }

    @Override
    public DefaultTableModel getClientResultTableModel() {
        return clientsTableModel;
    }

    @Override
    public StringBuilder getStringBuilder() {
        return sb;
    }

    public JTextArea getTextArea() {
        return priceTextArea;
    }

    @Override
    public JFrame getWindowFrame() {
        return windowFrame;
    }
}

