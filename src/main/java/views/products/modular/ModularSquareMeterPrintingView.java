package views.products.modular;

import org.javatuples.Pair;
import org.javatuples.Triplet;
import presenters.product.ProductCreatePresenter;
import presenters.product.ProductPresenter;
import presenters.product.ProductSearchPresenter;
import utils.Attribute;
import utils.MessageTypes;
import utils.Product;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static utils.TextUtils.truncateAndRound;
import static utils.databases.SettingsTableNames.*;

public class ModularSquareMeterPrintingView extends JPanel implements IModularCategoryView {
    private JPanel containerPanel;
    private JPanel leftSideComponentsContainer;
    private JPanel centerSideComponentsContainer;
    private JPanel materialPrincipalContainer;
    private JPanel materialSquareMetersContainer;
    private JPanel inkBySquareMeterContainer;
    private JComboBox materialComboBox;
    private JPanel materialSquareMetersPriceContainer;
    private JTextField materialSquareMetersPriceTextField;
    private JPanel inkBySquareMeterPriceContainer;
    private JTextField inkBySquareMeterPriceTextField;
    private JPanel dollarPriceContainer;
    private JTextField profitTextField;
    private JPanel profitContainer;
    private JLabel dollarPriceMultiplyLabel;
    private JLabel profitMultiplyLabel;
    private JLabel finalPriceEqualsLabel;
    private JPanel squareMeterPrintingFinalPriceContainer;
    private JTextField squareMeterPrintingFinalPriceTextField;
    private JRadioButton UVRadioButton;
    private JRadioButton ecosolventeRadioButton;
    private JComboBox dollarComboBox;
    private JTextField dollarValueTextField;
    private JLabel profitLabel;
    private JPanel IVAContainer;
    private JLabel ivaSumLabel;
    private JLabel ivaPercentLabel;
    private JPanel IVAComboboxContainer;
    private JComboBox IVAcombobox;
    private JPanel particularAddContainer;
    private JLabel particularAddSumLabel;
    private JPanel ParticularAddTextFieldContainer;
    private JTextField particularAddTextField;
    private JLabel particularAddPercentLabel;
    private JTextField clientFinalPriceTextField;
    private double materialMeterSqrPrice;
    private double inkByMeterPrice;
    private double profit;

    private final ProductCreatePresenter createPresenter;
    private final ProductSearchPresenter searchPresenter;
    private final ProductPresenter presenter;


    public ModularSquareMeterPrintingView(boolean isCreate, ProductPresenter presenter) {
        if (isCreate) {
            this.createPresenter = (ProductCreatePresenter) presenter;
            this.searchPresenter = null;
        } else {
            this.createPresenter = null;
            this.searchPresenter = (ProductSearchPresenter) presenter;
        }

        this.presenter = presenter;
        initListeners();
        adjustPanels();
    }

    private void adjustPanels() {

        ArrayList<JPanel> panels = new ArrayList<>();
        panels.add(materialSquareMetersPriceContainer);
        panels.add(inkBySquareMeterPriceContainer);
        panels.add(dollarPriceContainer);
        panels.add(profitContainer);
        panels.add(squareMeterPrintingFinalPriceContainer);
        for (JPanel panel : panels) {

            TitledBorder border = (TitledBorder) panel.getBorder();

            FontMetrics fm = panel.getFontMetrics(border.getTitleFont());
            int titleWidth = fm.stringWidth(border.getTitle());

            panel.setPreferredSize(new Dimension(titleWidth + 20, 50));
        }
    }

    @Override
    public JPanel getContainerPanel() {
        return containerPanel;
    }

    @Override
    public void initListeners() {
        ArrayList<JTextField> textFields = new ArrayList<>();

        textFields.add(materialSquareMetersPriceTextField);
        textFields.add(inkBySquareMeterPriceTextField);
        textFields.add(profitTextField);
        textFields.add(particularAddTextField);


        for (JTextField textField : textFields) {
            if(presenter instanceof ProductCreatePresenter){
                textField.addActionListener(e -> {
                    int lastProductCreatedID = ((ProductCreatePresenter) presenter).onCreateButtonClicked();
                    if (lastProductCreatedID != -1) {
                        ((ProductCreatePresenter) presenter).clearView();
                    }
                });
            }
            textField.getDocument().addDocumentListener(new DocumentListener() {
                @Override
                public void insertUpdate(DocumentEvent e) {
                    calculateDependantPrices();
                }

                @Override
                public void removeUpdate(DocumentEvent e) {
                    calculateDependantPrices();
                }

                @Override
                public void changedUpdate(DocumentEvent e) {
                    calculateDependantPrices();
                }
            });
        }

        IVAcombobox.addItemListener(e -> {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                calculateDependantPrices();
            }
        });

    }

    @Override
    public void calculateDependantPrices() {
        try {

            float materialSquareMetersPrice = materialSquareMetersPriceTextField.getText().isEmpty() ? 0 : Float.parseFloat(materialSquareMetersPriceTextField.getText());
            float inkBySquareMeterPrice = inkBySquareMeterPriceTextField.getText().isEmpty() ? 0 : Float.parseFloat(inkBySquareMeterPriceTextField.getText());
            float dollarPrice = dollarComboBox.getSelectedItem() == null ? 0 : (float) presenter.getIndividualPrice(GENERAL, (String) dollarComboBox.getSelectedItem());
            float profit = profitTextField.getText().isEmpty() ? 0 : Float.parseFloat(profitTextField.getText());
            float iva = String.valueOf(IVAcombobox.getSelectedItem()).isEmpty() ? 0 : Float.parseFloat(String.valueOf(IVAcombobox.getSelectedItem()));
            float recharge = particularAddTextField.getText().isEmpty() ? 0 : Float.parseFloat(particularAddTextField.getText());
            float finalPrice = (materialSquareMetersPrice + inkBySquareMeterPrice) * dollarPrice;

            float priceWOiva = finalPrice + (finalPrice * (profit / 100));
            float priceWiva = priceWOiva + (priceWOiva * (iva / 100));

            dollarValueTextField.setText(String.valueOf(dollarPrice));
            clientFinalPriceTextField.setText(truncateAndRound(String.valueOf(priceWiva)));
            squareMeterPrintingFinalPriceTextField.setText(truncateAndRound(String.valueOf(priceWiva + (priceWiva * (recharge / 100)))));

        } catch (NumberFormatException e) {
            showMessage(MessageTypes.ERROR_DEBUG, containerPanel);
        } catch (NullPointerException e) {
            showMessage(MessageTypes.DEBUG, containerPanel);
        }
    }

    @Override
    public Map<String, String> getComboBoxValues() {
        return Map.of();
    }

    @Override
    public Map<String, String> getTextFieldValues() {
        return Map.of();
    }

    @Override
    public ArrayList<String> getRadioValues() {
        return null;
    }

    @Override
    public void loadComboBoxValues() {
        ArrayList<String> materialLlist = presenter.getOtherTablesAsArrayList(MATERIALES);
        ArrayList<Pair<String, Double>> dollarList = presenter.getGeneralTableAsArrayList(GENERAL);
        for (String material : materialLlist) {
            materialComboBox.addItem(material);
        }
        for (Pair<String, Double> pair : dollarList) {
            String s = pair.getValue0();
                dollarComboBox.addItem(pair.getValue0());
        }
    }

    @Override
    public List<Triplet<String, String, Double>> getModularPrices() {
        return List.of();
    }

    @Override
    public void unlockTextFields() {

    }

    @Override
    public void blockTextFields() {

    }

    @Override
    public void setPriceTextFields() {

        squareMeterPrintingFinalPriceTextField.setText(String.valueOf(0));
        profitTextField.setText(String.valueOf(0));
        materialSquareMetersPriceTextField.setText(String.valueOf(0));
        inkBySquareMeterPriceTextField.setText(String.valueOf(0));
    }

    @Override
    public ArrayList<Attribute> getAttributes() {
        ArrayList<Attribute> attributes = new ArrayList<>();
        attributes.add(new Attribute("PRECIO_VINILO", materialSquareMetersPriceTextField.getText()));
        attributes.add(new Attribute("MATERIAL", getMaterialComboBoxSelection()));
        attributes.add(new Attribute("PRECIO_MATERIAL", materialSquareMetersPriceTextField.getText()));
        attributes.add(new Attribute("PRECIO_TINTA", inkBySquareMeterPriceTextField.getText()));
        attributes.add(new Attribute("UV", UVRadioButton.isSelected() ? "SI" : "NO"));
        attributes.add(new Attribute("VALOR_TIPO_CAMBIO", "###"));
        attributes.add(new Attribute("TIPO_CAMBIO", (String) dollarComboBox.getSelectedItem()));
        attributes.add(new Attribute("GANANCIA", profitTextField.getText()));
        attributes.add(new Attribute("IVA", String.valueOf(IVAcombobox.getSelectedItem())));
        attributes.add(new Attribute("RECARGO", particularAddTextField.getText()));
        return attributes;
    }

    @Override
    public void comboBoxListenerSet(ItemListener listener) {
        materialComboBox.addItemListener(listener);
        dollarComboBox.addItemListener(listener);
    }

    @Override
    public void setSearchTextFields(Product product) {
        if (searchPresenter == null) {
            return;
        }
        Map<String, String> attributes = searchPresenter.getProductAttributes(product);
        materialComboBox.setSelectedItem(attributes.get("MATERIAL"));
        UVRadioButton.setSelected(Boolean.parseBoolean(attributes.get("UV")));
        dollarComboBox.setSelectedItem(attributes.get("TIPO_CAMBIO"));
        materialSquareMetersPriceTextField.setText(attributes.get("PRECIO_VINILO"));
        inkBySquareMeterPriceTextField.setText(attributes.get("PRECIO_TINTA"));
        dollarValueTextField.setText(attributes.get("VALOR_TIPO_CAMBIO"));
        profitTextField.setText(attributes.get("GANANCIA"));
        IVAcombobox.setSelectedItem(attributes.get("IVA"));
        particularAddTextField.setText(attributes.get("RECARGO"));
    }

    private String getMaterialComboBoxSelection() {
        return (String) materialComboBox.getSelectedItem();
    }

}
