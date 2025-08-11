package views.products.modular;

import lombok.Getter;
import org.javatuples.Pair;
import org.javatuples.Triplet;
import presenters.product.ProductCreatePresenter;
import presenters.product.ProductPresenter;
import presenters.product.ProductSearchPresenter;
import utils.Attribute;
import utils.MessageTypes;
import utils.Product;
import utils.databases.SettingsDatabaseConnection;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static utils.TextUtils.truncateAndRound;
import static utils.databases.SettingsTableNames.*;

public class ModularCupView extends JPanel implements IModularCategoryView {
    private JPanel containerPanel;
    private JPanel leftSideComponentsContainer;
    private JPanel centerSideComponentsContainer;
    private JPanel rightSideComponentsContainer;
    private JPanel cupPriceContainer;
    private JPanel plankLoweringContainer;
    private JPanel printingContainer;
    private JTextField cupPriceTextField;
    private JPanel plankLoweringAmountContainer;
    private JPanel plankLoweringPriceContainer;
    private JPanel plankLoweringFinalPriceContainer;
    private JLabel plankLoweringMultiplyLabel;
    private JLabel plankLoweringEqualsLabel;
    private JPanel printingMetersAmountContainer;
    private JPanel printingMetersPriceContainer;
    private JPanel printingMetersFinalPriceContainer;
    private JPanel profitContainer;
    private JTextField profitTextField;
    private JLabel profitMultiplyLabel;
    private JLabel cupFinalPriceEqualsLabel;
    private JTextField cupFinalPriceTextField;
    private JPanel cupFinalPriceContainer;
    private JTextField plankLoweringAmountTextField;
    private JTextField plankLoweringPriceTextField;
    private JTextField plankLoweringFinalPriceTextField;
    private JTextField printingMetersAmountTextField;
    private JTextField printingMetersPriceTextField;
    private JTextField printingMetersFinalPriceTextField;
    private JLabel printingMultiplyLabel;
    private JLabel printingEqualsLabel;
    private JLabel profitLabel;
    private JPanel IVAContainer;
    private JLabel ivaSumLabel;
    private JLabel ivaPercentLabel;
    private JPanel IVAComboboxContainer;
    private JComboBox IVAcombobox;
    private JLabel particularAddSumLabel;
    private JPanel ParticularAddTextFieldContainer;
    private JTextField particularAddTextField;
    private JLabel particularAddPercentLabel;
    private JTextField particularFinalPriceTField;
    private JLabel particularPriceLabel;
    private JLabel clientPriceLabel;
    private JPanel dollarPriceContainer;
    private JComboBox dollarComboBox;
    private JTextField dollarValueTextField;
    private JRadioButton ceramicRadioButton;
    private JRadioButton plasticRadioButton;
    private JRadioButton whiteRadioButton;
    private JRadioButton sublimatedRadioButton;
    @Getter
    private final ArrayList<String> radioValues = new ArrayList<>();
    @Getter
    private final Map<String, String> comboBoxValues = new HashMap<>();
    @Getter
    private final Map<String, String> textFieldValues = new HashMap<>();
    private double cupPrice;
    private double plankLoweringPrice;
    private double printingMetersPrice;
    private double profit;
    private double particularRecharge;
    private final SettingsDatabaseConnection settingsDBConnection;

    private final ProductCreatePresenter createPresenter;
    private final ProductSearchPresenter searchPresenter;
    private final ProductPresenter presenter;

    public ModularCupView(boolean isCreate, ProductPresenter presenter) {
        if (isCreate) {
            this.createPresenter = (ProductCreatePresenter) presenter;
            this.searchPresenter = null;
        } else {
            this.createPresenter = null;
            this.searchPresenter = (ProductSearchPresenter) presenter;
        }

        settingsDBConnection = new SettingsDatabaseConnection();

        this.presenter = presenter;
        initListeners();
        adjustPanels();
    }

    private void adjustPanels() {

        ArrayList<JPanel> panels = new ArrayList<>();
        panels.add(printingMetersPriceContainer);
        panels.add(printingMetersAmountContainer);
        panels.add(printingMetersFinalPriceContainer);
        panels.add(plankLoweringAmountContainer);
        panels.add(plankLoweringPriceContainer);
        panels.add(plankLoweringFinalPriceContainer);
        panels.add(profitContainer);
        panels.add(cupFinalPriceContainer);
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

        textFields.add(cupPriceTextField);
        textFields.add(plankLoweringAmountTextField);
        textFields.add(plankLoweringPriceTextField);
        textFields.add(printingMetersAmountTextField);
        textFields.add(printingMetersPriceTextField);
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

        dollarComboBox.addItemListener(e -> {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                calculateDependantPrices();
            }
        });

    }

    @Override
    public void calculateDependantPrices() {
        try {
            int plankLoweringAmount = plankLoweringAmountTextField.getText().isEmpty() ? 0 : Integer.parseInt(plankLoweringAmountTextField.getText());
            try {
                float printingMetersAmount = printingMetersAmountTextField.getText().isEmpty() ? 0 : Float.parseFloat(printingMetersAmountTextField.getText());
                float profit = profitTextField.getText().isEmpty() ? 0 : Float.parseFloat(profitTextField.getText());
                float cupPrice = cupPriceTextField.getText().isEmpty() ? 0 : Float.parseFloat(cupPriceTextField.getText());
                float plankLoweringPrice = plankLoweringPriceTextField.getText().isEmpty() ? 0 : Float.parseFloat(plankLoweringPriceTextField.getText());
                float printingMetersPrice = printingMetersPriceTextField.getText().isEmpty() ? 0 : Float.parseFloat(printingMetersPriceTextField.getText());
                float dollarPrice = dollarComboBox.getSelectedItem() == null ? 0 : (float) presenter.getIndividualPrice(GENERAL, (String) dollarComboBox.getSelectedItem());

                float recharge = particularAddTextField.getText().isEmpty() ? 0 : Float.parseFloat(particularAddTextField.getText());
                float iva = IVAcombobox.getSelectedItem() == null ? 0 : Float.parseFloat(IVAcombobox.getSelectedItem().toString());

                float plankLoweringFinalPrice = plankLoweringAmount * plankLoweringPrice;
                float printingMetersFinalPrice = printingMetersAmount * printingMetersPrice;

                float priceWOiva = (cupPrice + plankLoweringFinalPrice + printingMetersFinalPrice) + ((cupPrice + plankLoweringFinalPrice + printingMetersFinalPrice) * (profit/100));
                float priceWiva = (priceWOiva + (priceWOiva * (iva / 100))) * dollarPrice;
                float cupParticularFinalPrice = priceWiva + (priceWiva * (recharge / 100));

                dollarValueTextField.setText(String.valueOf(dollarPrice));
                plankLoweringFinalPriceTextField.setText(String.valueOf(plankLoweringFinalPrice));
                printingMetersFinalPriceTextField.setText(String.valueOf(printingMetersFinalPrice));
                cupFinalPriceTextField.setText(truncateAndRound(String.valueOf(priceWiva)));
                particularFinalPriceTField.setText(truncateAndRound(String.valueOf(cupParticularFinalPrice)));

            } catch (NumberFormatException | NullPointerException e) {
                showMessage(MessageTypes.FLOAT_PARSING_ERROR, containerPanel);
            }
        } catch (NumberFormatException | NullPointerException e) {
            showMessage(MessageTypes.INT_PARSING_ERROR, containerPanel);
        }
    }

    @Override
    public void loadComboBoxValues() {
        ArrayList<Pair<String, Double>> dollarList = presenter.getGeneralTableAsArrayList(GENERAL);
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
        printingMetersPriceTextField.setText(String.valueOf(0));
        plankLoweringPriceTextField.setText(String.valueOf(0));
        cupPriceTextField.setText(String.valueOf(0));
        profitTextField.setText(String.valueOf(0));
        particularAddTextField.setText(String.valueOf(0));

    }

    @Override
    public ArrayList<Attribute> getAttributes() {
        ArrayList<Attribute> attributes = new ArrayList<>();
        attributes.add(new Attribute("CANTIDAD_BAJADA", plankLoweringAmountTextField.getText()));

        attributes.add(new Attribute("CANTIDAD_IMP", printingMetersAmountTextField.getText()));

        attributes.add(new Attribute("TAZA", cupPriceTextField.getText()));

        attributes.add(new Attribute("PRECIO_BAJADA", plankLoweringPriceTextField.getText()));

        attributes.add(new Attribute("PRECIO_IMP", printingMetersPriceTextField.getText()));
        attributes.add(new Attribute("VALOR_TIPO_CAMBIO", "###"));
        attributes.add(new Attribute("TIPO_CAMBIO", (String) dollarComboBox.getSelectedItem()));
        attributes.add(new Attribute("GANANCIA", profitTextField.getText()));
        attributes.add(new Attribute("IVA", String.valueOf(IVAcombobox.getSelectedItem())));
        attributes.add(new Attribute("RECARGO", particularAddTextField.getText()));
        attributes.add(new Attribute("VALOR_TIPO_CAMBIO", "###"));
        attributes.add(new Attribute("TIPO_CAMBIO", (String) dollarComboBox.getSelectedItem()));

        return attributes;
    }

    @Override
    public void comboBoxListenerSet(ItemListener listener) {

    }

    @Override
    public void setSearchTextFields(Product product) {
        if(searchPresenter==null) {
            return;
        }
        Map<String, String> attributes = searchPresenter.getProductAttributes(product);
        cupPriceTextField.setText(attributes.getOrDefault("TAZA", "0"));
        plankLoweringAmountTextField.setText(attributes.getOrDefault("CANTIDAD_BAJADA", "0"));
        plankLoweringPriceTextField.setText(attributes.getOrDefault("PRECIO_BAJADA", "0"));
        printingMetersAmountTextField.setText(attributes.getOrDefault("CANTIDAD_IMP", "0"));
        printingMetersPriceTextField.setText(attributes.getOrDefault("PRECIO_IMP", "0"));
        profitTextField.setText(attributes.getOrDefault("GANANCIA", "0"));
        particularAddTextField.setText(attributes.getOrDefault("RECARGO", "0"));
        IVAcombobox.setSelectedItem(attributes.getOrDefault("IVA", "0"));
        dollarValueTextField.setText(attributes.get("VALOR_TIPO_CAMBIO"));
        dollarComboBox.setSelectedItem(attributes.get("TIPO_CAMBIO"));

    }

}
