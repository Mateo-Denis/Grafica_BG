package views.budget.modify;

import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.uiDesigner.core.Spacer;
import lombok.Getter;
import presenters.StandardPresenter;
import presenters.budget.BudgetModifyPresenter;
import utils.CuttingService;
import utils.MultiLineHeaderRenderer;
import utils.NumberInputVerifier;
import views.ToggleableView;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.plaf.FontUIResource;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.AbstractDocument;
import javax.swing.text.StyleContext;
import java.awt.*;
import java.awt.event.ItemListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static utils.WindowFormatter.relativeSizeAndCenter;

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
    @Getter
    private JTextField heightMeasureTextField;
    private JTextField observationsTextField;
    private JLabel observationsLabel;
    private JScrollPane productTableScrollPanel;
    private JTable productTable;
    private JButton addProductButton;
    private JPanel previewTableContainer;
    private JScrollPane budgetPreviewScrollPanel;
    private JTable budgetPreviewTable;
    @Getter
    private JTextArea priceTextArea;
    private JButton budgetModifyButton;
    private JButton deleteProductButton;
    @Getter
    private JTextField widthMeasureTextField;
    private JLabel widthMeasureLabel;
    private JLabel previewTableLabe;
    private JButton cutServiceAddButton;
    private JLabel orLabel;
    private BudgetModifyPresenter budgetModifyPresenter;
    private DefaultTableModel clientsTableModel;
    private DefaultTableModel productsTableModel;
    @Getter
    private DefaultTableModel previewTableModel;
    private final StringBuilder sb = new StringBuilder();
    private static MultiLineHeaderRenderer multiLineRenderer;


    public BudgetModifyView() {
        windowFrame = new JFrame("Modificar Presupuesto");
        windowFrame.setContentPane(containerPanel);
        windowFrame.pack();
        windowFrame.setLocationRelativeTo(null);
        windowFrame.setIconImage(new ImageIcon("src/main/resources/BGLogo.png").getImage());

        SetDisableNumberInputFilter();

        budgetModifyButton.setVisible(true);

        SetPriceTextAreaInitialText();

        cambiarTamanioFuente(containerPanel, 14);

        relativeSizeAndCenter(windowFrame, 1, 0.90);
        windowFrame.setResizable(false);

        DisableMeasureTextFields();

        clientSearchingContainer.setVisible(false);
    }

    private void SetDisableNumberInputFilter() {
        ((AbstractDocument) amountTextField.getDocument()).setDocumentFilter(new NumberInputVerifier());
        ((AbstractDocument) heightMeasureTextField.getDocument()).setDocumentFilter(new NumberInputVerifier());
        ((AbstractDocument) widthMeasureTextField.getDocument()).setDocumentFilter(new NumberInputVerifier());
    }

    private void SetPriceTextAreaInitialText() {
        priceTextArea.setEditable(false);
        sb.append("Precio total: ");
        priceTextArea.setText(sb.toString());
    }

    private void DisableMeasureTextFields() {
        widthMeasureTextField.setEnabled(false);
        heightMeasureTextField.setEnabled(false);
    }

    @Override
    protected void wrapContainer() {
        containerPanelWrapper = containerPanel;
    }

    //    @Override
    protected void initListeners() {
        budgetModifyButton.addActionListener(e -> {
            budgetModifyPresenter.onSaveModificationsButtonClicked();
        });

        clientSearchButton.addActionListener(e -> budgetModifyPresenter.OnSearchClientButtonClicked());

        addProductButton.addActionListener(e -> budgetModifyPresenter.onAddProductButtonClicked());

        deleteProductButton.addActionListener(e -> budgetModifyPresenter.onDeleteProductButtonClicked());

        clientSelectedCheckBox.addItemListener(e -> budgetModifyPresenter.onClientSelectedCheckBoxClicked());

        clientAddButton.addActionListener(e -> budgetModifyPresenter.onAddClientButtonClicked());

        productSearchButton.addActionListener(e -> budgetModifyPresenter.OnSearchProductButtonClicked());

        productTextField.addActionListener(e -> budgetModifyPresenter.OnSearchProductButtonClicked());

        clientTextField.addActionListener(e -> budgetModifyPresenter.OnSearchClientButtonClicked());

        cutServiceAddButton.addActionListener(e -> budgetModifyPresenter.OnAddCuttingServiceButtonClicked());

        windowFrame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                restartWindow();
                windowFrame.dispose();
            }
        });

        SetProductTableListener();

    }

    public void addCuttingService(CuttingService cuttingService) {
        System.out.println("CUTTING SERVICE FROM MODIFY VIEW");
        int nextRow = getFilledRowsCount(budgetPreviewTable) + 1;

        setPreviewStringTableValueAt(nextRow, 1, "Servicio de corte");
        setPreviewStringTableValueAt(nextRow, 2, String.valueOf(cuttingService.getAmount()));
        setPreviewStringTableValueAt(nextRow, 3, String.valueOf(cuttingService.getLinealMeters()));
        setPreviewStringTableValueAt(nextRow, 4, cuttingService.getDescription());
        setPreviewStringTableValueAt(nextRow, 5, String.valueOf(cuttingService.getTotal()));

        budgetModifyPresenter.updateTextArea(true, false, cuttingService.getTotal());
        budgetModifyPresenter.increaseRowCountOnPreviewTable();
    }


    private void SetProductTableListener() {
        productTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    int selectedRow = productTable.getSelectedRow();
                    if ((selectedRow != -1) && (productTable.getValueAt(selectedRow, 0) != null)) {
                        String productCategory = (String) productTable.getValueAt(selectedRow, 1);
                        if (productCategory.equals("Servicio de corte") || productCategory.equals("Impresión en metro cuadrado")) {
                            widthMeasureTextField.setText("");
                            heightMeasureTextField.setText("");
                            widthMeasureTextField.setEnabled(true);
                            heightMeasureTextField.setEnabled(true);
                        } else if (productCategory.equals("Tela") || productCategory.equals("Impresión lineal")) {
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

    private void SetColumnTitlesFix() {
        MultiLineHeaderRenderer.applyToTable(productTable, false);
        MultiLineHeaderRenderer.applyToTable(budgetPreviewTable, true);
        MultiLineHeaderRenderer.applyToTable(clientResultTable, false);
    }

    @Override
    public void clearView() {
        clientTextField.setText("");
        cityComboBox.setSelectedIndex(0);
        observationsTextField.setText("");
        productTextField.setText("");
        amountTextField.setText("");
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
        relativeSizeAndCenter(windowFrame, 1, 0.90);
        sb.setLength(0);
        sb.append("Precio Total: ");
        priceTextArea.setEditable(false);
        priceTextArea.setText(sb.toString());
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
    public void comboBoxListenerSet(ItemListener listener) {
        productComboBox.addItemListener(listener);
    }

    @Override
    public void setProductStringTableValueAt(int row, int col, String value) {
        productTable.setValueAt(value, row, col);
    }

    @Override
    public void start() {
        super.start();
        SetClientsTableModel();
        SetProductsTableModel();
        SetBudgetTableModel();
        SetColumnTitlesFix();

        setTableVisibility(clientResultTable);
        setTableVisibility(productTable);
        setTableVisibility(budgetPreviewTable);

        clientSearchingContainer.setBorder(BorderFactory.createEmptyBorder(0, 0, 100, 0));
        addClientContainer.setBorder(BorderFactory.createEmptyBorder(15, 0, 0, 0));
    }

    private void SetClientsTableModel() {
        clientsTableModel = new DefaultTableModel(new Object[]{"ID", "Nombre", "Dirección", "Localidad", "Teléfono", "Cliente/Particular"}, 200) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Todas las celdas no serán editables
            }
        };
        clientResultTable.setModel(clientsTableModel);
        clientResultTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    }

    private void SetProductsTableModel() {
        productsTableModel = new DefaultTableModel(new Object[]{"Nombre", "Categoria", "Precio"}, 200) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Todas las celdas no serán editables
            }
        };
        productTable.setModel(productsTableModel);
        productTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    }

    private void SetBudgetTableModel() {
        previewTableModel = new DefaultTableModel(new Object[]{"Nombre del Cliente", "Nombre del producto", "Cantidad del producto", "Medidas", "Observaciones", "Precio", "Cliente / Particular"}, 200) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        budgetPreviewTable.setModel(previewTableModel);
        budgetPreviewTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    }

    @Override
    public void setTableVisibility(JTable table) {
        int rowCountToShow = 4;
        int rowHeight = table.getRowHeight(); // Altura de cada fila
        int tableHeight = rowCountToShow * rowHeight; // Altura total para x filas
        // Establecer la altura preferida del viewport dentro del JScrollPane
        table.setPreferredScrollableViewportSize(new Dimension(
                table.getPreferredSize().width, tableHeight));
    }

    public int getFilledRowsCount(JTable table) {
        int rowCount = 0;
        for (int row = 1; row < table.getRowCount(); row++) {
            if ((table.getValueAt(row, 1) != null) && (!table.getValueAt(row, 1).equals(""))) {
                rowCount++;
            }
        }
        return rowCount;
    }

    public void setHeightMeasureTextField(String productsHeightMeasure) {
        heightMeasureTextField.setText(productsHeightMeasure);
    }

    public void setClientStringTableValueAt(int row, int col, String value) {
        clientResultTable.setValueAt(value, row, col);
    }

    @Override
    public void setClientIntTableValueAt(int row, int col, int value) {
        clientResultTable.setValueAt(value, row, col);
    }

    @Override
    public void setPreviewStringTableValueAt(int row, int col, String value) {
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

    @Override
    public JTable getPreviewTable() {
        return budgetPreviewTable;
    }

    public String getProductStringTableValueAt(int row, int col) {
        return (String) productTable.getValueAt(row, col);
    }

    public String getPreviewStringTableValueAt(int row, int col) {
        return (String) budgetPreviewTable.getValueAt(row, col);
    }

    public JTextField getProductsTextField() {
        return productTextField;
    }

    @Override
    public JTextField getAmountTextField() {
        return amountTextField;
    }

    @Override
    public JTextField getObservationsTextField() {
        return observationsTextField;
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
        relativeSizeAndCenter(windowFrame, 1, 0.90);
        windowFrame.setResizable(false);
    }

    public void setSecondPanelsVisibility() {
        clientSearchingContainer.setVisible(true);
        budgetPreviewContainer.setVisible(false);
        productSearchingContainer.setVisible(false);
        budgetCreationButtonsContainer.setVisible(false);
        priceContainer.setVisible(false);
        relativeSizeAndCenter(windowFrame, 1, 0.90);
        windowFrame.setResizable(false);
    }

    @Override
    public JCheckBox getClientSelectedCheckBox() {
        return clientSelectedCheckBox;
    }

    @Override
    public JFrame getWindowFrame() {
        return windowFrame;
    }

}
