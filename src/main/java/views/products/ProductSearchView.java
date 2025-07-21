
package views.products;

import lombok.Getter;
import presenters.StandardPresenter;
import presenters.product.ProductListPresenter;
import presenters.product.ProductPresenter;
import presenters.product.ProductSearchPresenter;
import utils.CategoryParser;
import utils.Product;
import utils.TextUtils;
import views.ToggleableView;
import views.products.modular.IModularCategoryView;

import javax.swing.*;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static utils.CategoryParser.parseCategory;
import static utils.WindowFormatter.relativeSizeAndCenter;

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
    @Getter
    private JTable productResultTable;
    private JButton productListOpenButton;
    private JButton deleteOneProductButton;
    private JPanel deleteButtonsContainer;
    private JPanel bottomButtonsContainer;
    private JComboBox categoryComboBox;
    private JLabel categoryLabel;
    private final TextUtils textUtils = new TextUtils();
    private JPanel modularContainer;
    private JButton modifyProductButton;
    private JPanel modifyProductButtonContainer;
    private JButton deleteAllProductsButton;
    private ProductSearchPresenter productSearchPresenter;
    private ProductPresenter productPresenter;
    private DefaultTableModel tableModel;
    private final ProductListPresenter productListPresenter;
    @Getter
    private IModularCategoryView modularView;

    public ProductSearchView(ProductListPresenter productListPresenter) {
        windowFrame = new JFrame("Buscar Productos");
        windowFrame.setContentPane(containerPanel);
        windowFrame.pack();
        windowFrame.setLocationRelativeTo(null);
        windowFrame.setIconImage(new ImageIcon("src/main/resources/BGLogo.png").getImage());

        this.productListPresenter = productListPresenter;
        cambiarTamanioFuente(containerPanel, 14);
        relativeSizeAndCenter(windowFrame, 0.8, 0.9);
        windowFrame.setResizable(false);
    }

    @Override
    public void start() {
        super.start();
        tableModel = new DefaultTableModel(new Object[] { "Nombre", "Categoría", "Precio Cliente", "Precio Particular" }, 200) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
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
        modifyProductButton.addActionListener(e -> productSearchPresenter.onModifyButtonPressed());
    }

    public void setTableListener(ListSelectionListener listener) {
        productResultTable.getSelectionModel().addListSelectionListener(listener);
        productResultTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    }

    private void setModularPrices() {
        if (modularView != null) {
            modularView.setPriceTextFields();
        }
    }

    @Override
    public void showSelectedView(String category, Product product) {
        // Limpiar el panel del contenedor
        modularContainer.removeAll();
        appearModularView();

        // Mostrar la vista correspondiente
        // JPanel selectedView = getCorrespondingModularView(category);
        IModularCategoryView selectedView = getCorrespondingModularView(category);
        if (selectedView != null && selectedView.getContainerPanel() != null) {
            modularView = selectedView;
            modularContainer.setLayout(new BorderLayout());
            modularContainer.add(selectedView.getContainerPanel(), BorderLayout.CENTER);
            selectedView.getContainerPanel().setSize(1000, 200);
            selectedView.getContainerPanel().setVisible(true);
            modularView.loadComboBoxValues();
            modularView.setSearchTextFields(product);
            modularView.calculateDependantPrices();

        }

        // Actualizar el layout del panel
        modularContainer.revalidate();
        modularContainer.repaint();
    }

    public IModularCategoryView getCorrespondingModularView(String category) {
        IModularCategoryView correspondingModularView = null;
        Map<String, IModularCategoryView> panelesCategorias = getCategoryPanelsMap();

        for (String categoria : panelesCategorias.keySet()) {

            if (parseCategory(categoria).equals(category)) {
                correspondingModularView = panelesCategorias.get(categoria);
                break;
            }
        }
        return correspondingModularView;
    }

    public Map<String, IModularCategoryView> getCategoryPanelsMap() {
        String directoryPath = "src/main/java/views/products/modular";
        List<String> nombresDeModulars = textUtils.getFileNamesInDirectory(directoryPath);

        nombresDeModulars.removeIf(nombreCompleto -> nombreCompleto.startsWith("I"));

        List<String> subStringModulars = new ArrayList<>();
        List<IModularCategoryView> categoryViews = TextUtils.loadAllViewPanels("views.products.modular",
                productSearchPresenter, false);
        Map<String, IModularCategoryView> categoryPanelsMap = new HashMap<>();

        // Se extraen los substrings de los nombres de los modulars. EJ: ModularCapView
        // -> Cap
        for (String stringModular : nombresDeModulars) {
            String subString = textUtils.extractor(stringModular);
            subStringModulars.add(subString);
        }
        for (int i = 0; i < subStringModulars.size(); i++) {
            categoryPanelsMap.put(subStringModulars.get(i), categoryViews.get(i));
        }
        return categoryPanelsMap;
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

        return null;
    }

    public void deselectAllRows() {
        productResultTable.clearSelection();
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

    @Override
    public void setCategoriesComboBox(List<String> categorias) {
        categoryComboBox.addItem("Seleccione una categoría");
        for (String categoria : categorias) {
            categoryComboBox.addItem(CategoryParser.parseCategory(categoria));
        }
    }

    @Override
    public JComboBox getCategoriesComboBox() {
        return categoryComboBox;
    }

    public void hideModularView() {
        modularContainer.setVisible(false);
    }

    public void appearModularView() {
        modularContainer.setVisible(true);
    }
}
