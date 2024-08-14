package views.products;

import javax.swing.*;
import javax.swing.text.AbstractDocument;

import utils.ProductPriceInputVerifier;
import utils.databases.CategoriesDatabaseConnection;
import utils.databases.ProductsDatabaseConnection;
import views.ToggleableView;
import presenters.StandardPresenter;
import presenters.product.ProductCreatePresenter;

import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;



import models.CategoryModel;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.Map;
import utils.TextUtils;
import views.products.modular.IModularCategoryView;

public class ProductCreateView extends ToggleableView implements IProductCreateView {
    private JPanel containerPanel;
    private JPanel productSpecsContainer;
    private JTextField productNameField;
    private JLabel productLabel;
    private JComboBox<String> categoryComboBox;
    private JLabel categoryLabel;
    private JTextField productDescriptionField;
    private JLabel descriptionLabel;
    private JTextField productPriceField;
    private JLabel priceLabel;
    private JPanel createButtonContainer;
    private JButton createButton;
    private JPanel modularContainer;
    private JComboBox<String> subCategoryComboBox;
    private JPanel comboBoxOriginalPanel;
    private ProductCreatePresenter productCreatePresenter;
    private CategoryModel categoryModel;
    private TextUtils textUtils = new TextUtils();
    private JPanel modularView = new JPanel();
    private CategoriesDatabaseConnection categoriesDatabaseConnection = new CategoriesDatabaseConnection();
    private ProductsDatabaseConnection productDatabaseConnection = new ProductsDatabaseConnection();

    public ProductCreateView() {
        windowFrame = new JFrame("Crear Producto");
        windowFrame.setContentPane(containerPanel);
        windowFrame.pack();
        windowFrame.setLocationRelativeTo(null);
        windowFrame.setIconImage(new ImageIcon("src/main/resources/BGLogo.png").getImage());
        //Aplica el filtro al documento asociado al JTextField
        ((AbstractDocument) productPriceField.getDocument()).setDocumentFilter(new ProductPriceInputVerifier());


        modularContainer.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        modularView.setVisible(false);
        modularContainer.add(modularView);
    }

    //TESTEO MODULAR VIEW:
    public void setCorrespondingModularView(String category) {
        JPanel correspondingModularView = getCorrespondingModularView(category);
        modularContainer.removeAll();
        modularContainer.add(correspondingModularView);
        modularContainer.revalidate();
        modularContainer.repaint();
    }

    //TESTEO MODULAR VIEW:
    public JPanel getCorrespondingModularView(String category) {
        JPanel correspondingModularView = new JPanel();
        Map<String, JPanel> panelesCategorias = getCategoryPanelsMap();
        for (String categoria : panelesCategorias.keySet()) {
            if (categoria.equals(category)) {
                correspondingModularView = panelesCategorias.get(categoria);
                break;
            }
        }
        return correspondingModularView;
    }

    //TESTEO PRODUCT MODULAR:

    public Map<String, JPanel> getCategoryPanelsMap() {

        String directoryPath = "src/main/java/views/products/modular";
        List<String> nombresDeModulars = textUtils.getFileNamesInDirectory(directoryPath);
        nombresDeModulars.removeIf(nombreCompleto -> nombreCompleto.startsWith("I"));
        List<String> subStringModulars = new ArrayList<>();
        List<JPanel> categoryJPanels = TextUtils.loadAllViewPanels("views.products.modular");

        // Mapa que asocia los nombres de categorías con sus JPanels
        Map<String, JPanel> categoryPanelsMap = new HashMap<>();

        //Se extraen los substrings de los nombres de los modulars. EJ: ModularCapView -> Cap
        for (String stringModular : nombresDeModulars) {
            String subString = textUtils.extractor(stringModular);
            subStringModulars.add(subString);
        }

        for (int i = 0; i < subStringModulars.size(); i++) {
            categoryPanelsMap.put(subStringModulars.get(i), categoryJPanels.get(i));
        }

        return categoryPanelsMap;
    }

    @Override
    public String getProductName() {
        return productNameField.getText();
    }

    @Override
    public String getProductDescription() {
        return productDescriptionField.getText();
    }

    @Override
    public String getProductCategory() {
        System.out.println((String) categoryComboBox.getSelectedItem());
        return (String) categoryComboBox.getSelectedItem();
    }

    @Override
    public double getProductPrice() {
        return Double.parseDouble(productPriceField.getText());
    }

    @Override
    public void clearView() {
        productNameField.setText("");
        productDescriptionField.setText("");
        productPriceField.setText("");
        categoryComboBox.setSelectedIndex(-1);
        modularContainer.removeAll();
        //subCategoryComboBox.setSelectedIndex(-1);
    }

    @Override
    protected void wrapContainer() {
        containerPanelWrapper = containerPanel;
    }

    @Override
    protected void initListeners() {
        createButton.addActionListener(e -> productCreatePresenter.onCreateButtonClicked());
    }

    @Override
    public void setPresenter(StandardPresenter productCreatePresenter) {
        this.productCreatePresenter = (ProductCreatePresenter) productCreatePresenter;
    }

    public void setCategorias(List<String> categorias) {
        categoryComboBox.addItem("Seleccione una categoría");
        for (String categoria : categorias) {
            categoryComboBox.addItem(categoria);
        }
    }

    public JComboBox<String> getCategoryComboBox() {
        return categoryComboBox;
    }

    public JPanel getModularView() {
        return modularView;
    }

    public JPanel getContainerPanel(){
        return containerPanel;
    }

    @Override
    public void comboBoxListenerSet(ItemListener listener) {
        categoryComboBox.addItemListener(listener);
    }


}
