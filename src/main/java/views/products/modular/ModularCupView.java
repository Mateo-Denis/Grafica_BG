package views.products.modular;

import lombok.Getter;
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
    private JPanel particularAddContainer;
    private JLabel particularAddSumLabel;
    private JPanel ParticularAddTextFieldContainer;
    private JTextField particularAddTextField;
    private JLabel particularAddPercentLabel;
    private JTextField particularFinalPriceTField;
    private JLabel particularPriceLabel;
    private JLabel clientPriceLabel;
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
            int plankLoweringAmount = plankLoweringAmountTextField.getText().isEmpty() ? 0 : Integer.parseInt(plankLoweringAmountTextField.getText());
            try {
                float printingMetersAmount = printingMetersAmountTextField.getText().isEmpty() ? 0 : Float.parseFloat(printingMetersAmountTextField.getText());
                float profit = profitTextField.getText().isEmpty() ? 0 : Float.parseFloat(profitTextField.getText());
                float cupPrice = cupPriceTextField.getText().isEmpty() ? 0 : Float.parseFloat(cupPriceTextField.getText());
                float plankLoweringPrice = plankLoweringPriceTextField.getText().isEmpty() ? 0 : Float.parseFloat(plankLoweringPriceTextField.getText());
                float printingMetersPrice = printingMetersPriceTextField.getText().isEmpty() ? 0 : Float.parseFloat(printingMetersPriceTextField.getText());

                float recharge = particularAddTextField.getText().isEmpty() ? 0 : Float.parseFloat(particularAddTextField.getText());
                float iva = IVAcombobox.getSelectedItem() == null ? 0 : Float.parseFloat(IVAcombobox.getSelectedItem().toString());

                float plankLoweringFinalPrice = plankLoweringAmount * plankLoweringPrice;
                float printingMetersFinalPrice = printingMetersAmount * printingMetersPrice;

                float priceWOiva = (cupPrice + plankLoweringFinalPrice + printingMetersFinalPrice) * (profit/100);
                float priceWiva = priceWOiva + (priceWOiva * (iva / 100));
                float cupParticularFinalPrice = priceWiva + (priceWiva * (recharge / 100));

                plankLoweringFinalPriceTextField.setText(String.valueOf(plankLoweringFinalPrice));
                printingMetersFinalPriceTextField.setText(String.valueOf(printingMetersFinalPrice));
                cupFinalPriceTextField.setText(String.valueOf(priceWiva));
                particularFinalPriceTField.setText(String.valueOf(cupParticularFinalPrice));

            } catch (NumberFormatException | NullPointerException e) {
                showMessage(MessageTypes.FLOAT_PARSING_ERROR, containerPanel);
            }
        } catch (NumberFormatException | NullPointerException e) {
            showMessage(MessageTypes.INT_PARSING_ERROR, containerPanel);
        }
    }

    @Override
    public void loadComboBoxValues() {

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
        profit = presenter.getIndividualPrice(GANANCIAS, "Tazas");
        cupPrice = presenter.getIndividualPrice(GENERAL, "Taza");
        plankLoweringPrice = presenter.getIndividualPrice(BAJADA_PLANCHA, "En taza");
        printingMetersPrice = presenter.getIndividualPrice(IMPRESIONES, "Metro de Sublimación");
        particularRecharge = 0;

        printingMetersPriceTextField.setText(String.valueOf(printingMetersPrice));
        plankLoweringPriceTextField.setText(String.valueOf(plankLoweringPrice));
        cupPriceTextField.setText(String.valueOf(cupPrice));
        profitTextField.setText(String.valueOf(profit));
        particularAddTextField.setText(String.valueOf(particularRecharge));

    }

    @Override
    public ArrayList<Attribute> getAttributes() {
        ArrayList<Attribute> attributes = new ArrayList<>();
        attributes.add(new Attribute("T1A", plankLoweringAmountTextField.getText()));

        String plankLoweringPrice = plankLoweringPriceTextField.getText();
        String settingsPLP = settingsDBConnection.getModularValue(BAJADA_PLANCHA, "En taza");
        String finalPLP = plankLoweringPrice.equals(settingsPLP) ? "###" : plankLoweringPrice;
        attributes.add(new Attribute("T1B", finalPLP));

        attributes.add(new Attribute("T2A", printingMetersAmountTextField.getText()));

        String printingMetersPrice = printingMetersPriceTextField.getText();
        String settingsPMP = settingsDBConnection.getModularValue(IMPRESIONES, "Metro de Sublimación");
        String finalPMP = printingMetersPrice.equals(settingsPMP) ? "###" : printingMetersPrice;
        attributes.add(new Attribute("T2B", finalPMP));

        String capCost = cupPriceTextField.getText();
        String settingsCC = settingsDBConnection.getModularValue(GENERAL, "Taza");
        String finalCC = capCost.equals(settingsCC) ? "###" : capCost;
        attributes.add(new Attribute("TAZA", finalCC));

        String profit = profitTextField.getText();
        String settingsProfit = settingsDBConnection.getModularValue(GANANCIAS, "Taza");
        String finalProfit = profit.equals(settingsProfit) ? "###" : profit;
        attributes.add(new Attribute("GANANCIA", finalProfit));

        return attributes;
    }

    @Override
    public void comboBoxListenerSet(ItemListener listener) {

    }

    @Override
    public void setSearchTextFields(Product product) {
        Map<String, String> attributes = searchPresenter.getProductAttributes(product);
        plankLoweringAmountTextField.setText(attributes.get("T1A"));
    }

}
