package views.products;

import javax.swing.*;
import javax.swing.text.AbstractDocument;

import utils.ProductPriceInputVerifier;
import views.ToggleableView;
import views.products.modular.*;
import presenters.StandardPresenter;
import presenters.product.ProductCreatePresenter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


//TESTEO PRODUCT MODULAR:
import models.CategoryModel;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.Map;
import utils.TextUtils;

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



    //TESTEO PRODUCT MODULAR:
    private JPanel modularView = new JPanel();

    public ProductCreateView() {
        windowFrame = new JFrame("Crear Producto");
        windowFrame.setContentPane(containerPanel);
        windowFrame.pack();
        windowFrame.setLocationRelativeTo(null);
        windowFrame.setIconImage(new ImageIcon("src/main/resources/BGLogo.png").getImage());
        //Aplica el filtro al documento asociado al JTextField
        ((AbstractDocument) productPriceField.getDocument()).setDocumentFilter(new ProductPriceInputVerifier());


        //TESTEO PRODUCT MODULAR:
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
        modularContainer.setVisible(true);
    }

    //TESTEO MODULAR VIEW:
    public JPanel getCorrespondingModularView(String category) {
        JPanel correspondingModularView = new JPanel();
        Map<String, JPanel> panelesCategorias = getCategoryPanelsMap();
        for (String categoria : panelesCategorias.keySet()) {
            if (categoria.equals(category)) {
                correspondingModularView = panelesCategorias.get(categoria);
            }
        }
        return correspondingModularView;
    }

    //TESTEO PRODUCT MODULAR:

    public Map<String, JPanel> getCategoryPanelsMap() {

        String directoryPath = "src\\main\\java\\views\\products\\modulars";
        List<String> nombresDeModulars = textUtils.getFileNamesInDirectory(directoryPath);
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
        categoryComboBox.addItemListener(e -> productCreatePresenter.onCategoryItemSelected());
    }

    @Override
    public void setPresenter(StandardPresenter productCreatePresenter) {
        this.productCreatePresenter = (ProductCreatePresenter) productCreatePresenter;
    }

    public void setCategorias(List<String> categorias) {
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
}
