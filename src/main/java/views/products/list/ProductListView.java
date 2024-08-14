package views.products.list;

import presenters.StandardPresenter;
import views.ToggleableView;
import presenters.product.ProductListPresenter;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class ProductListView extends ToggleableView implements IProductListView {

    private JPanel containerPanel;
    private JPanel buttonsContainer;
    private JTable productTable;
    private JScrollPane productScrollPanel;
    private JPanel titleContainer;
    private JLabel titleLabel;
    private JButton deleteOneProductButton;
    private ProductListPresenter productListPresenter;
    private DefaultTableModel tableModel;

    public ProductListView() {
        windowFrame = new JFrame("Lista de Productos");
        windowFrame.setContentPane(containerPanel);
        windowFrame.pack();
        windowFrame.setLocationRelativeTo(null);
        windowFrame.setIconImage(new ImageIcon("src/main/resources/BGLogo.png").getImage());
    }

    @Override
    public void start() {
        super.start();
        setProductTableModel();
    }

    public void setProductTableModel() {
        tableModel = new DefaultTableModel(new Object[]{"ID", "Nombre", "Descripción", "Precio", "Categoria"}, 200);
        productTable.setModel(tableModel);
    }

    @Override
    protected void wrapContainer() {
        containerPanelWrapper = containerPanel;
    }

    @Override
    protected void initListeners() {
        deleteOneProductButton.addActionListener(e -> productListPresenter.onDeleteOneProductButtonClicked());
    }

    @Override
    public void clearView() {
        for (int row = 0; row < productTable.getRowCount(); row++) {
            for (int col = 0; col < productTable.getColumnCount(); col++) {
                productTable.setValueAt("", row, col);
            }
        }
    }

    @Override
    public void setPresenter(StandardPresenter productListPresenter) {
        this.productListPresenter = (ProductListPresenter) productListPresenter;
    }

    @Override
    public void setStringTableValueAt(int row, int col, String value) {
        productTable.setValueAt(value, row, col);
    }

    @Override
    public void setDoubleTableValueAt(int row, int col, double value) {
        productTable.setValueAt(value, row, col);
    }

    @Override
    public void setIntTableValueAt(int row, int col, int value) {
        productTable.setValueAt(value, row, col);
    }

    public int getSelectedTableRow() {
        return productTable.getSelectedRow();
    }

    public String getSelectedProductName() {
        String productName = "";
        try {
            productName = (String) productTable.getValueAt(getSelectedTableRow(), 1);
            return productName;
        } catch (ArrayIndexOutOfBoundsException e) {
            JOptionPane.showMessageDialog(null, "No product selected", "Error", JOptionPane.ERROR_MESSAGE);
        }
        return null;
    }

    public void deselectAllRows() {
        productTable.clearSelection();
    }

    public JFrame getJFrame() {
        return windowFrame;
    }
}
