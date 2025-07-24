package views.products.modular;

import lombok.Getter;
import net.miginfocom.swing.MigLayout;
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
    private JTextField particularFinalPriceTextField;
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
    private JTextField clientFinalPriceTextField;
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
        //adjustPanels();
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
        if (createPresenter != null) {
            profitTextField.setText(String.valueOf(0));
            printingMetersPriceTextField.setText(String.valueOf(0));
            plankLoweringPriceTextField.setText(String.valueOf(0));
            capCostTextField.setText(String.valueOf(0));
        }
    }

    @Override
    public ArrayList<Attribute> getAttributes() {

        ArrayList<Attribute> attributes = new ArrayList<>();

        attributes.add(new Attribute("PRECIO_BAJADA", plankLoweringPriceTextField.getText()));

        attributes.add(new Attribute("CANTIDAD_BAJADA", plankLoweringAmountTextField.getText()));

        attributes.add(new Attribute("PRECIO_IMP", printingMetersPriceTextField.getText()));

        attributes.add(new Attribute("CANTIDAD_IMP", printingMetersAmountTextField.getText()));

        attributes.add(new Attribute("GORRA", capCostTextField.getText()));

        attributes.add(new Attribute("GANANCIA", profitTextField.getText()));
        attributes.add(new Attribute("IVA", String.valueOf(IVAcombobox.getSelectedItem())));
        attributes.add(new Attribute("RECARGO", particularAddTextField.getText()));
        return attributes;
    }

    @Override
    public void comboBoxListenerSet(ItemListener listener) {
    }

    @Override
    public void setSearchTextFields(Product product) {
        if(searchPresenter == null) {
            return;
        }
        Map<String, String> attributes = searchPresenter.getProductAttributes(product);
        capCostTextField.setText(attributes.get("GORRA"));
        plankLoweringAmountTextField.setText(attributes.get("CANTIDAD_BAJADA"));
        plankLoweringPriceTextField.setText(attributes.get("PRECIO_BAJADA"));
        printingMetersAmountTextField.setText(attributes.get("CANTIDAD_IMP"));
        printingMetersPriceTextField.setText(attributes.get("PRECIO_IMP"));
        IVAcombobox.setSelectedItem(attributes.get("IVA"));
        particularAddTextField.setText(attributes.get("RECARGO"));
        profitTextField.setText(attributes.get("GANANCIA"));
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
            int plankLoweringAmount = plankLoweringAmountTextField.getText().isEmpty() ? 0 : Integer.parseInt(plankLoweringAmountTextField.getText());
            try {
                float printingMetersAmount = printingMetersAmountTextField.getText().isEmpty() ? 0 : Float.parseFloat(printingMetersAmountTextField.getText());
                float capCost = capCostTextField.getText().isEmpty() ? 0 : Float.parseFloat(capCostTextField.getText());
                float plankLoweringPrice = plankLoweringPriceTextField.getText().isEmpty() ? 0 : Float.parseFloat(plankLoweringPriceTextField.getText());
                float printingMetersPrice = printingMetersPriceTextField.getText().isEmpty() ? 0 : Float.parseFloat(printingMetersPriceTextField.getText());
                float profit = profitTextField.getText().isEmpty() ? 0 : Float.parseFloat(profitTextField.getText());
                float iva = IVAcombobox.getSelectedItem()==null ? 0 : Float.parseFloat(String.valueOf(IVAcombobox.getSelectedItem()));
                float recharge = particularAddTextField.getText().isEmpty() ? 0 : Float.parseFloat(particularAddTextField.getText());

                float plankLoweringFinalPrice = plankLoweringPrice * plankLoweringAmount;
                float printingMetersFinalPrice = printingMetersPrice * printingMetersAmount;

                float priceWithoutIVA = (capCost + plankLoweringFinalPrice + printingMetersFinalPrice) + ((capCost + plankLoweringFinalPrice + printingMetersFinalPrice) * (profit / 100));

                float priceWithIVA = priceWithoutIVA + (priceWithoutIVA * (iva / 100));
                float finalPrice = priceWithIVA + (priceWithIVA * (recharge / 100));
                clientFinalPriceTextField.setText(truncateAndRound(String.valueOf(priceWithIVA)));
                plankLoweringFinalPriceTextField.setText(String.valueOf(plankLoweringFinalPrice));
                printingMetersFinalPriceTextField.setText(String.valueOf(printingMetersFinalPrice));
                particularFinalPriceTextField.setText(truncateAndRound(String.valueOf(finalPrice)));

            } catch (NumberFormatException | NullPointerException e) {
                showMessage(MessageTypes.FLOAT_PARSING_ERROR, containerPanel);
            }
        } catch (NumberFormatException | NullPointerException e) {
            showMessage(MessageTypes.INT_PARSING_ERROR, containerPanel);
        }
    }

}

