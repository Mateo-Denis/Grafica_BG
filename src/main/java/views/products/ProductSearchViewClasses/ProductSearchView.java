package views.products.ProductSearchViewClasses;

import javax.swing.*;
import java.awt.event.ActionListener;
import java.util.List;
import models.ProductSearchModel;
import views.products.ProductSearchViewClasses.ProductTableModel;

public class ProductSearchView extends JFrame {
    private JPanel containerPanel;
    private JPanel productSearchContainer;
    private JPanel searchFieldContainer;
    private JTextField searchField;
    private JPanel searchButtonsContainer;
    private JButton searchButton;
    private JPanel productListButtonsContainer;
    private JButton productListOpenButton;
    private JPanel productResultContainer;
    private JTable productResultTable;
    private JScrollPane productResultScrollPanel;
    private ProductTableModel tableModel;

    public ProductSearchView() {
        JFrame windowFrame = new JFrame("Buscar Productos");
        windowFrame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        windowFrame.setContentPane(containerPanel);
        windowFrame.pack();
        windowFrame.setLocationRelativeTo(null);
        windowFrame.setVisible(true);
        tableModel = new ProductTableModel();
        productResultTable.setModel(tableModel);
    }

    public String getSearchText() {
        return searchField.getText();
    }

    public void setSearchListener(ActionListener listener) {
        searchButton.addActionListener(listener);
    }

    public void updateTable(List<ProductSearchModel> products) {
        tableModel.setProducts(products);
    }

    public JPanel getContainerPanel() {
        return containerPanel;
    }
}
