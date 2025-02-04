package views.budget.modify;

import presenters.StandardPresenter;
import presenters.budget.BudgetModifyPresenter;
import utils.NumberInputVerifier;
import utils.Product;
import views.ToggleableView;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.AbstractDocument;
import java.awt.event.*;
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
    private JLabel heightMeasureLabel;
    private JTextField heightMeasureTextField;
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
    private JTextField widthMeasureTextField;
    private JLabel widthMeasureLabel;
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
        ((AbstractDocument) heightMeasureTextField.getDocument()).setDocumentFilter(new NumberInputVerifier());
        ((AbstractDocument) widthMeasureTextField.getDocument()).setDocumentFilter(new NumberInputVerifier());
        budgetModifyButton.setVisible(true);
        priceTextArea.setEditable(false);
        sb.append("Precio total: ");
        priceTextArea.setText(sb.toString());
        cambiarTamanioFuente(containerPanel, 14);
        windowFrame.setSize(750,800);
        windowFrame.setResizable(false);

        widthMeasureTextField.setEnabled(false);
        heightMeasureTextField.setEnabled(false);

        clientSearchingContainer.setVisible(false);
    }

    @Override
    protected void wrapContainer() {
        containerPanelWrapper = containerPanel;
    }

//    @Override
    protected void initListeners() {
        budgetModifyButton.addActionListener(e -> {
            budgetModifyPresenter.onSaveModificationsButtonClicked();
            restartWindow();
        });
        clientSearchButton.addActionListener(e -> budgetModifyPresenter.OnSearchClientButtonClicked());
        addProductButton.addActionListener(e -> budgetModifyPresenter.onAddProductButtonClicked());
        deleteProductButton.addActionListener(e -> budgetModifyPresenter.onDeleteProductButtonClicked());
        clientSelectedCheckBox.addItemListener(e -> budgetModifyPresenter.onClientSelectedCheckBoxClicked());
        clientAddButton.addActionListener(e -> budgetModifyPresenter.onAddClientButtonClicked());
        productSearchButton.addActionListener(e -> budgetModifyPresenter.OnSearchProductButtonClicked());
        windowFrame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                restartWindow();
                windowFrame.dispose();
                //budgetModifyPresenter.onModifySearchViewButtonClicked(budgetModifyPresenter.GetGlobalBudgetNumer());
            }
        });

        productTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    int selectedRow = productTable.getSelectedRow();
                    if((selectedRow != -1) && (productTable.getValueAt(selectedRow, 0) != null)) {
                        String productCategory = (String) productTable.getValueAt(selectedRow, 1);
                        if (productCategory.equals("Cloth") || productCategory.equals("CuttingService") || productCategory.equals("SquareMeterPrinting")) {
                            widthMeasureTextField.setText("");
                            heightMeasureTextField.setText("");
                            widthMeasureTextField.setEnabled(true);
                            heightMeasureTextField.setEnabled(true);
                        } else if(productCategory.equals("LinearPrinting")) {
                            widthMeasureTextField.setText("");
                            heightMeasureTextField.setText("");
                            widthMeasureTextField.setEnabled(false);
                            heightMeasureTextField.setEnabled(true);
                        } else {
                            widthMeasureTextField.setText("");
                            heightMeasureTextField.setText("");
                            widthMeasureTextField.setEnabled(false);
                            heightMeasureTextField.setEnabled(false);
                        }
                    }
                }
            }
        });
    }

//    public List<String[]> getPreviewTableFilledRowsData() {
//        List<String[]> filledRowsData = new ArrayList<>();
//        String[] dataArray;
//
//        String budgetClientName = "";
//        String budgetProductName = "";
//        int budgetProductAmount = 1;
//        String budgetProductMeasures = "";
//        String budgetProductObservations = "";
//        double budgetProductPrice = -1.0;
//        String budgetClientType = "";
//
//        int filledRows = getFilledRowsCount(budgetPreviewTable);
//
//
//
//        for (int row = 0; row <= filledRows; row++) {
//            if(row == 0){
//                budgetClientName = (String) budgetPreviewTable.getValueAt(row, 0);
//                budgetClientType = (String) budgetPreviewTable.getValueAt(row, 6);
//                dataArray = new String[]{budgetClientName, budgetClientType};
//                filledRowsData.add(dataArray);
//            } else {
//                Object selectedProductName = budgetPreviewTable.getValueAt(row, 1);
//                Object selectedProductAmount = budgetPreviewTable.getValueAt(row, 2);
//                Object selectedProductMeasures = budgetPreviewTable.getValueAt(row, 3);
//                Object selectedProductObservations = budgetPreviewTable.getValueAt(row, 4);
//                Object selectedProductPrice = budgetPreviewTable.getValueAt(row, 5);
//
//                if(selectedProductName != null && !selectedProductName.equals("")) {
//                    budgetProductName = (String) selectedProductName;
//                }
//                if(selectedProductAmount != null && !selectedProductAmount.equals("")) {
//                    budgetProductAmount = Integer.parseInt((String) selectedProductAmount);
//                }
//                if(selectedProductMeasures != null && !selectedProductMeasures.equals("")) {
//                    budgetProductMeasures = (String) selectedProductMeasures;
//                }
//                if(selectedProductObservations != null && !selectedProductObservations.equals("")) {
//                    budgetProductObservations = (String) selectedProductObservations;
//                }
//                if(selectedProductPrice != null && !selectedProductPrice.equals("")) {
//                    budgetProductPrice = (double) selectedProductPrice;
//                }
//
//                dataArray = new String[]{budgetProductName, String.valueOf(budgetProductAmount), budgetProductMeasures, budgetProductObservations, String.valueOf(budgetProductPrice)};
//                filledRowsData.add(dataArray);
//
//            }
//        }
//        return filledRowsData;
//    }

    @Override
    public void clearView() {
        clientTextField.setText("");
        cityComboBox.setSelectedIndex(0);
        amountTextField.setText("");
        observationsTextField.setText("");
        clientTextField.setText("");
        productTextField.setText("");
        cityComboBox.setSelectedIndex(0);
        amountTextField.setText("");
        observationsTextField.setText("");
        heightMeasureTextField.setText("");
        clientSelectedCheckBox.setSelected(false);
        widthMeasureTextField.setEnabled(false);
        heightMeasureTextField.setEnabled(false);
        widthMeasureTextField.setText("");
        heightMeasureTextField.setText("");
        clearPreviewTable();
        clearClientTable();
        clearProductTable();
    }

    public void restartWindow() {
        windowFrame.setSize(590,1010);
        sb.setLength(0);
        sb.append("Precio Total: ");
        priceTextArea.setEditable(false);
        widthMeasureTextField.setText("");
        heightMeasureTextField.setText("");
        priceTextArea.setText(sb.toString());
        widthMeasureTextField.setEnabled(false);
        heightMeasureTextField.setEnabled(false);
        setInitialPanelsVisibility();
        clearView();
    }

    @Override
    public DefaultTableModel getClientResultTableModel() {
        return clientsTableModel;
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
        clientsTableModel = new DefaultTableModel(new Object[]{"ID", "Nombre", "Dirección", "Localidad", "Teléfono", "Cliente/Particular"}, 200);
        clientResultTable.setModel(clientsTableModel);
        clientResultTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        productsTableModel = new DefaultTableModel(new Object[]{"Nombre", "Categoria", "Precio"}, 200);
        productTable.setModel(productsTableModel);
        productTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        previewTableModel = new DefaultTableModel(new Object[]{"Nombre del Cliente", "Nombre del producto", "Cantidad del producto", "Medidas" , "Observaciones",  "Precio Unitario", "Cliente / Particular"}, 200) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        budgetPreviewTable.setModel(previewTableModel);
        budgetPreviewTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        setTableVisibility(clientResultTable);
        setTableVisibility(productTable);
        setTableVisibility(budgetPreviewTable);

        clientSearchingContainer.setBorder(BorderFactory.createEmptyBorder(0, 0, 100, 0));
        addClientContainer.setBorder(BorderFactory.createEmptyBorder(15, 0, 0, 0));
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

    public JButton getAddProductButton() {
        return addProductButton;
    }

    public void addProductToPreviewTable(Product product, int row) {
        budgetPreviewTable.setValueAt(product.getName(), row, 1);
    }

    public int getFilledRowsCount(JTable table) {
        int rowCount = 0;
        for (int row = 1; row < table.getRowCount(); row++) {
            if ((table.getValueAt(row, 1) != null) && (!table.getValueAt(row, 1).equals(""))) {
                rowCount++;
                System.out.println("FILAS LLENAS: " + rowCount);
            }
        }
        return rowCount;
    }

    public JTextField getWidthMeasureTextField() {
        return widthMeasureTextField;
    }
    public JTextField getHeightMeasureTextField() {
        return heightMeasureTextField;
    }

    @Override
    public void setWidthMeasureTextField(String productsWidthMeasure) {
        widthMeasureTextField.setText(productsWidthMeasure);
    }

    public void setHeightMeasureTextField(String productsHeightMeasure) {
        heightMeasureTextField.setText(productsHeightMeasure);
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

    public int getPreviewTableSelectedRow() {
        return budgetPreviewTable.getSelectedRow();
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
        return heightMeasureTextField;
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
        clientSearchingContainer.setVisible(false);
        budgetPreviewContainer.setVisible(true);
        productSearchingContainer.setVisible(true);
        budgetCreationButtonsContainer.setVisible(true);
        priceContainer.setVisible(true);
        windowFrame.setSize(750,800);
        windowFrame.setResizable(false);
    }

    public void setSecondPanelsVisibility() {
        clientSearchingContainer.setVisible(true);
        budgetPreviewContainer.setVisible(false);
        productSearchingContainer.setVisible(false);
        budgetCreationButtonsContainer.setVisible(false);
        priceContainer.setVisible(false);
        windowFrame.setSize(750,800);
        windowFrame.setResizable(false);
    }

    @Override
    public JCheckBox getClientSelectedCheckBox() {
        return clientSelectedCheckBox;
    }

    @Override
    public void setObservationsTextField(String productsObservation) {
        observationsTextField.setText(productsObservation);
    }

    @Override
    public void setMeasuresTextField(String productsMeasure) {
        heightMeasureTextField.setText(productsMeasure);
    }

    @Override
    public void setAmountTextField(int productsAmount) {
        amountTextField.setText(String.valueOf(productsAmount));
    }

    @Override
    public JFrame getWindowFrame() {
        return windowFrame;
    }
}
