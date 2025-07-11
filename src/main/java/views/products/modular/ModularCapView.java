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

public class ModularCapView extends JPanel implements IModularCategoryView {
    private JPanel containerPanel;
    private JPanel leftSideComponentsContainer;
    private JPanel capPriceContainer;
    private JTextField capCostTextField;
    private JPanel plankLoweringContainer;
    private JPanel plankLoweringAmountContainer;
    private JTextField plankLoweringAmountTextField;
    private JPanel plankLoweringPriceContainer;
    private JTextField plankLoweringPriceTextField;
    private JPanel plankLoweringFinalPriceContainer;
    private JTextField plankLoweringFinalPriceTextField;
    private JLabel plankLoweringMultiplyLabel;
    private JLabel plankLoweringEqualsLabel;
    private JPanel printingContainer;
    private JPanel printingMetersAmountContainer;
    private JTextField printingMetersAmountTextField;
    private JPanel printingMetersPriceContainer;
    private JTextField printingMetersPriceTextField;
    private JPanel printingMetersFinalPriceContainer;
    private JTextField printingMetersFinalPriceTextField;
    private JLabel printingMultiplyLabel;
    private JLabel printingEqualsLabel;
    private JPanel centerSideComponentsContainer;
    private JPanel profitContainer;
    private JTextField profitTextField;
    private JPanel rightSideComponentsContainer;
    private JPanel capFinalPriceContainer;
    private JTextField capFinalPriceTextField;
    private JLabel profitMultiplyLabel;
    private JLabel capFinalPriceEqualsLabel;
    private JLabel profitLabel;
    private JPanel IVAContainer;
    private JLabel ivaSumLabel;
    private JComboBox IVAcombobox;
    private JLabel ivaPercentLabel;
    private JPanel IVAComboboxContainer;
    private JPanel particularAddContainer;
    private JLabel particularAddSumLabel;
    private JTextField particularAddTextField;
    private JPanel ParticularAddTextFieldContainer;
    private JLabel particularAddPercentLabel;
    @Getter
    private final ArrayList<String> radioValues = new ArrayList<>();
    @Getter
    private final Map<String, String> comboBoxValues = new HashMap<>();
    @Getter
    private final Map<String, String> textFieldValues = new HashMap<>();
    private double profit;
    private double particularAdd;
    private double capCost;
    private double plankLoweringPrice;
    private double printingMetersPrice;
    private boolean initialization;
    private final SettingsDatabaseConnection settingsDBConnection;

    private final ProductCreatePresenter createPresenter;
    private final ProductSearchPresenter searchPresenter;
    private final ProductPresenter presenter;

    public ModularCapView(boolean isCreate, ProductPresenter presenter) {
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
        panels.add(capFinalPriceContainer);
        for (JPanel panel : panels) {

            TitledBorder border = (TitledBorder) panel.getBorder();

            FontMetrics fm = panel.getFontMetrics(border.getTitleFont());
            int titleWidth = fm.stringWidth(border.getTitle());
            System.out.println(border.getTitle() + " " + titleWidth);

            panel.setPreferredSize(new Dimension(titleWidth + 20, 50));
        }
    }


    @Override
    public JPanel getContainerPanel() {
        return containerPanel;
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
        capCost = presenter.getIndividualPrice(GENERAL, "Gorra");
        plankLoweringPrice = presenter.getIndividualPrice(BAJADA_PLANCHA, "En gorra");
        printingMetersPrice = presenter.getIndividualPrice(IMPRESIONES, "Metro de Sublimación");
        profit = presenter.getIndividualPrice(GANANCIAS, "Impresión lineal");
        particularAdd = 5;

        profitTextField.setText(String.valueOf(profit));
        printingMetersPriceTextField.setText(String.valueOf(printingMetersPrice));
        plankLoweringPriceTextField.setText(String.valueOf(plankLoweringPrice));
        capCostTextField.setText(String.valueOf(capCost));
        particularAddTextField.setText(String.valueOf(particularAdd));
    }

    @Override
    public ArrayList<Attribute> getAttributes() {

        ArrayList<Attribute> attributes = new ArrayList<>();
        attributes.add(new Attribute("T1A", plankLoweringAmountTextField.getText()));

        String plankLoweringPrice = plankLoweringPriceTextField.getText();
        String settingsPLP = settingsDBConnection.getModularValue(BAJADA_PLANCHA, "En gorra");
        String finalPLP = plankLoweringPrice.equals(settingsPLP) ? "###" : plankLoweringPrice;
        attributes.add(new Attribute("T1B", finalPLP));

        attributes.add(new Attribute("T2A", printingMetersAmountTextField.getText()));

        String printingMetersPrice = printingMetersPriceTextField.getText();
        String settingsPMP = settingsDBConnection.getModularValue(IMPRESIONES, "Metro de Sublimación");
        String finalPMP = printingMetersPrice.equals(settingsPMP) ? "###" : printingMetersPrice;
        attributes.add(new Attribute("T2B", finalPMP));

        String capCost = capCostTextField.getText();
        String settingsCC = settingsDBConnection.getModularValue(GENERAL, "Gorra");
        String finalCC = capCost.equals(settingsCC) ? "###" : capCost;
        attributes.add(new Attribute("GORRA", finalCC));

        String profit = profitTextField.getText();
        String settingsProfit = settingsDBConnection.getModularValue(GANANCIAS, "Gorras");
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
        printingMetersAmountTextField.setText(attributes.get("T2A"));
    }

    public void initListeners() {

        ArrayList<JTextField> textFields = new ArrayList<>();

        textFields.add(capCostTextField);
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
                float capCost = capCostTextField.getText().isEmpty() ? 0 : Float.parseFloat(capCostTextField.getText());
                float plankLoweringPrice = plankLoweringPriceTextField.getText().isEmpty() ? 0 : Float.parseFloat(plankLoweringPriceTextField.getText());
                float printingMetersPrice = printingMetersPriceTextField.getText().isEmpty() ? 0 : Float.parseFloat(printingMetersPriceTextField.getText());
                float profit = profitTextField.getText().isEmpty() ? 0 : Float.parseFloat(profitTextField.getText());

                float plankLoweringFinalPrice = plankLoweringPrice * plankLoweringAmount;
                float printingMetersFinalPrice = printingMetersPrice * printingMetersAmount;
                float ivaPriceValue = Float.parseFloat(String.valueOf(IVAcombobox.getSelectedItem()));
                float priceWithoutIVA = (capCost + plankLoweringFinalPrice + printingMetersFinalPrice) * (profit / 100);
                float priceWithIVA = priceWithoutIVA + (priceWithoutIVA * ivaPriceValue / 100);
                float particularAdd = Float.parseFloat(particularAddTextField.getText());
                float finalPrice = priceWithIVA + (priceWithIVA * particularAdd / 100);

                plankLoweringFinalPriceTextField.setText(String.valueOf(plankLoweringFinalPrice));
                printingMetersFinalPriceTextField.setText(String.valueOf(printingMetersFinalPrice));
                capFinalPriceTextField.setText(String.valueOf(finalPrice));

            } catch (NumberFormatException | NullPointerException e) {
                showMessage(MessageTypes.FLOAT_PARSING_ERROR, containerPanel);
            }
        } catch (NumberFormatException | NullPointerException e) {
            showMessage(MessageTypes.INT_PARSING_ERROR, containerPanel);
        }
    }

}

