
package views.products;


import presenters.StandardPresenter;
import presenters.product.ProductListPresenter;
import presenters.product.ProductSearchPresenter;
import views.ToggleableView;


import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.ArrayList;

public class ProductSearchView extends ToggleableView implements IProductSearchView {

    private JPanel containerPanel;
    private JPanel productSearchContainer;
    private JPanel searchFieldContainer;
    private JPanel searchButtonsContainer;
    private JPanel searchResultContainer;
    private JPanel productListButtonsContainer;
    private JTextField searchField;
    private JButton searchButton;
    private JScrollPane productResultScrollPanel;
    private JTable productResultTable;
    private JButton productListOpenButton;
    private JButton deleteOneProductButton;
    private JPanel deleteButtonsContainer;
    private JPanel bottomButtonsContainer;
    private JButton deleteAllProductsButton;
    private ProductSearchPresenter productSearchPresenter;
    private DefaultTableModel tableModel;
    private final ProductListPresenter productListPresenter;

    public ProductSearchView(ProductListPresenter productListPresenter) {
        windowFrame = new JFrame("Buscar Productos");
        windowFrame.setContentPane(containerPanel);
        windowFrame.pack();
        windowFrame.setLocationRelativeTo(null);
        windowFrame.setIconImage(new ImageIcon("src/main/resources/BGLogo.png").getImage());

        this.productListPresenter = productListPresenter;

        initListeners();
    }

    @Override
    public void start() {
        super.start();
        tableModel = new DefaultTableModel(new Object[]{"Nombre", "Descripción", "Precio", "Categoria"}, 200);
        productResultTable.setModel(tableModel);
    }

    @Override
    public void setPresenter(StandardPresenter productSearchPresenter) {
        this.productSearchPresenter = (ProductSearchPresenter) productSearchPresenter;
    }

    @Override
    protected void wrapContainer() {
        containerPanelWrapper = containerPanel;
    }

    @Override
    protected void initListeners() {
        searchButton.addActionListener(e -> productSearchPresenter.onSearchButtonClicked());
        deleteOneProductButton.addActionListener(e -> productSearchPresenter.onDeleteProductButtonClicked());
        productListOpenButton.addActionListener(e -> productListPresenter.onSearchViewOpenListButtonClicked());
    }

    @Override
    public void clearView() {
        for (int row = 0; row < productResultTable.getRowCount(); row++) {
            for (int col = 0; col < productResultTable.getColumnCount(); col++) {
                productResultTable.setValueAt("", row, col);
            }
        }
    }

    @Override
    public String getNameSearchText() {
        return searchField.getText();
    }

    public void setStringTableValueAt(int row, int col, String value) {
        productResultTable.setValueAt(value, row, col);
    }

    public void setDoubleTableValueAt(int row, int col, double value) {
        productResultTable.setValueAt(value, row, col);
    }

    @Override
    public void setIntTableValueAt(int row, int col, int value) {
        productResultTable.setValueAt(value, row, col);
    }

    public int getSelectedTableRow() {
        return productResultTable.getSelectedRow();
    }

    public String getSelectedProductName() {
        String productName = "";
        int row = getSelectedTableRow();
        try {
            productName = (String) productResultTable.getValueAt(getSelectedTableRow(), 0);
            return productName;
        } catch (ArrayIndexOutOfBoundsException e) {
            JOptionPane.showMessageDialog(null, "No product selected", "Error", JOptionPane.ERROR_MESSAGE);
        }

        deselectAllRows();
        System.out.println("LLAMADA A METODO getSelectedProductName");

        return null;
    }

    public void deselectAllRows() {
        productResultTable.clearSelection();
    }

    public JTable getProductResultTable() {
        return productResultTable;
    }

    public ArrayList<String> getMultipleSelectedProductNames() {
        ArrayList<String> productNames = new ArrayList<>();
        int[] selectedRows = productResultTable.getSelectedRows();
        for (int selectedRow : selectedRows) {
            String productName = (String) productResultTable.getValueAt(selectedRow, 0);
            productNames.add(productName);
        }
        return productNames;
    }
}
