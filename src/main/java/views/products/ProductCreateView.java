package views.products;

import javax.swing.*;
import javax.swing.text.AbstractDocument;

import views.ToggleableView;
import views.products.modular.IModularCategoryView;
import utils.NumberInputVerifier;
import utils.databases.CategoriesDatabaseConnection;
import utils.databases.ProductsDatabaseConnection;
import presenters.StandardPresenter;
import presenters.product.ProductCreatePresenter;
import presenters.categories.ModularCategoriesPresenter;

import java.awt.*;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


//TEST
import models.CategoryModel;
import java.util.Map;
import utils.TextUtils;

import static utils.CategoryParser.parseCategory;

public class ProductCreateView extends ToggleableView implements IProductCreateView {
    private JPanel containerPanel;
    private JPanel productSpecsContainer;
    private JTextField productNameField;
    private JLabel productLabel;
    private JComboBox<String> categoryComboBox;
    private JLabel categoryLabel;
    private JTextField productPriceField;
    private JLabel priceLabel;
    private JPanel createButtonContainer;
    private JButton createButton;
    private JPanel modularContainer;
    private JButton updatePriceButton;
    private JCheckBox priceEditCheckBox;
    private JButton savePricesButton;
    private JComboBox<String> subCategoryComboBox;
    private JPanel comboBoxOriginalPanel;
    private ProductCreatePresenter productCreatePresenter;
    private CategoryModel categoryModel;
    private TextUtils textUtils = new TextUtils();
    private IModularCategoryView modularView;
    private CategoriesDatabaseConnection categoriesDatabaseConnection = new CategoriesDatabaseConnection();
    private ProductsDatabaseConnection productDatabaseConnection = new ProductsDatabaseConnection();
    private Map<String, JPanel> viewMap;
    private Map<String, IModularCategoryView> modularMap;
    private ModularCategoriesPresenter modularCategoriesPresenter;

    public ProductCreateView() {
        windowFrame = new JFrame("Crear Producto");
        windowFrame.setContentPane(containerPanel);
        windowFrame.pack();
        windowFrame.setLocationRelativeTo(null);
        windowFrame.setIconImage(new ImageIcon("src/main/resources/BGLogo.png").getImage());
        //Aplica el filtro al documento asociado al JTextField
        windowFrame.setSize(1300,530);
        cambiarTamanioFuente(containerPanel, 14);
        windowFrame.setResizable(false);
        ((AbstractDocument) productPriceField.getDocument()).setDocumentFilter(new NumberInputVerifier());
        modularContainer.setLayout(new BorderLayout());
        modularMap = getCategoryPanelsMap();
    }


    public void showSelectedView(String category) {
        // Limpiar el panel del contenedor
        modularContainer.removeAll();

        // Mostrar la vista correspondiente
        //JPanel selectedView = getCorrespondingModularView(category);
        IModularCategoryView selectedView = getCorrespondingModularView(category);
        if (selectedView != null && selectedView.getContainerPanel() != null) {
            System.out.println("selectedViewselectedViewselectedViewselectedViewselectedViewselectedViewselectedViewselectedViewselectedViewselectedViewselectedViewselectedView");
            modularView = selectedView;
            modularContainer.add(selectedView.getContainerPanel(), BorderLayout.CENTER);
            selectedView.getContainerPanel().setVisible(true);
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
                System.out.println(panelesCategorias.get(categoria));
                correspondingModularView = panelesCategorias.get(categoria);
                break;
            }
        }
        System.out.println(correspondingModularView);
        return correspondingModularView;
    }

    public Map<String, IModularCategoryView> getCategoryPanelsMap() {
        String directoryPath = "src/main/java/views/products/modular";
        List<String> nombresDeModulars = textUtils.getFileNamesInDirectory(directoryPath);

        nombresDeModulars.removeIf(nombreCompleto -> nombreCompleto.startsWith("I"));

        List<String> subStringModulars = new ArrayList<>();
        List<IModularCategoryView> categoryViews = TextUtils.loadAllViewPanels("views.products.modular", productCreatePresenter);
        Map<String, IModularCategoryView> categoryPanelsMap = new HashMap<>();

        //Se extraen los substrings de los nombres de los modulars. EJ: ModularCapView -> Cap
        for (String stringModular : nombresDeModulars) {
            String subString = textUtils.extractor(stringModular);
            subStringModulars.add(subString);
        }

        for (int i = 0; i < subStringModulars.size(); i++) {
            categoryPanelsMap.put(subStringModulars.get(i), categoryViews.get(i));
        }

        //PRINTING TEST
        for(String str : subStringModulars) {
            System.out.println(str);
        }
        for(IModularCategoryView imod : categoryViews) {
            System.out.println(imod);
        }
        //PRINTING TEST

        return categoryPanelsMap;
    }

    @Override
    public String getProductName() {
        return productNameField.getText();
    }

    @Override
    public String getProductCategory() {
        System.out.println((String) categoryComboBox.getSelectedItem());
        String s = (String) categoryComboBox.getSelectedItem();
        return s;
    }

    @Override
    public String getProductCategoryEnglish(){
        String s = (String) categoryComboBox.getSelectedItem();
        switch (s) {
            case "Taza":
                return "Cup";
            case "Gorra":
                return "Cap";
            case "Buzo":
                return "Sweater";
            case "Remera":
                return "Shirt";
            case "Tela":
                return "Cloth";
            case "Campera":
                return "Jacket";
            case "Bandera":
                return "Flag";
            case "Impresion":
                return "Printing";
            case "Vinilo":
                return "Vinyl";
            case "Lona":
                return "Canvas";
            default:
                return s;
        }
    }

    @Override
    public double getProductPrice() {
        return Double.parseDouble(productPriceField.getText());
    }
    @Override
    public void setProductPriceField(String productPrice) {
        productPriceField.setText(productPrice);
    }

    @Override
    public void clearView() {
        productNameField.setText("");
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
        updatePriceButton.addActionListener(e -> productCreatePresenter.onUpdatePriceButtonClicked());
        priceEditCheckBox.addActionListener(e -> productCreatePresenter.onEditCheckBoxClicked());
        savePricesButton.addActionListener(e -> productCreatePresenter.onSavePricesButtonClicked());
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

    public IModularCategoryView getModularView() {
        return modularView;
    }

    public JPanel getContainerPanel() {
        return containerPanel;
    }

    @Override
    public void comboBoxListenerSet(ItemListener listener) {
        categoryComboBox.addItemListener(listener);
    }

    @Override
    public void componentsListenerSet(ItemListener listener) {
        IModularCategoryView modularView = getCorrespondingModularView((String) categoryComboBox.getSelectedItem());
        modularCategoriesPresenter.initListeners();
    }

    public JCheckBox getEditPriceCheckBox() {
        return priceEditCheckBox;
    }
}