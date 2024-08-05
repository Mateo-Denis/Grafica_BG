package views.products;

import javax.swing.*;
import javax.swing.text.AbstractDocument;
import utils.ProductPriceInputVerifier;
import views.ToggleableView;
import presenters.StandardPresenter;
import presenters.product.ProductCreatePresenter;

import views.ToggleableView;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

public class ProductCreateView extends ToggleableView implements IProductCreateView {
    private JPanel containerPanel;
    private JPanel productSpecsContainer;
    private JTextField productNameField;
    private JLabel productLabel;
    private JComboBox categoryComboBox;
    private JComboBox subCategoryComboBox;
    private JLabel categoryLabel;
    private JLabel SubCategoryLabel;
    private JTextField productDescriptionField;
    private JLabel descriptionLabel;
    private JTextField productPriceField;
    private JLabel priceLabel;
    private JPanel createButtonContainer;
    private JButton createButton;
    private ProductCreatePresenter productCreatePresenter;

    public ProductCreateView() {
        windowFrame = new JFrame("Crear Producto");
        windowFrame.setContentPane(containerPanel);
        windowFrame.pack();
        windowFrame.setLocationRelativeTo(null);
        windowFrame.setIconImage(new ImageIcon("src/main/resources/BGLogo.png").getImage());

        //Aplica el filtro al documento asociado al JTextField
        ((AbstractDocument) productPriceField.getDocument()).setDocumentFilter(new ProductPriceInputVerifier());
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
    public String getProductSubCategory() {
        return (String) subCategoryComboBox.getSelectedItem();
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
        subCategoryComboBox.setSelectedIndex(-1);
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

    public String getSelectedCategory() {
        return (String) categoryComboBox.getSelectedItem();
    }

/*    public void updateSubCategoryComboBox(String category) {
        subCategoryComboBox.removeAllItems();
        
        if(category.equals("Remeras")) {
            subCategoryComboBox.addItem("Manga corta");
            subCategoryComboBox.addItem("Manga larga");
        } else if(category.equals("Pantalones")) {
            subCategoryComboBox.addItem("Cortos");
            subCategoryComboBox.addItem("Largos");
        } else if(category.equals("Zapatos")) {
            subCategoryComboBox.addItem("Deportivos");
            subCategoryComboBox.addItem("Casuales");
        }
    }*/

    public void addSubCategory(String subCategory) {
        subCategoryComboBox.addItem(subCategory);
    }

    public void clearSubCategories() {
        subCategoryComboBox.removeAllItems();
    }
}
