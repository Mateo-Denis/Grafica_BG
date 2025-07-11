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

public class ModularClothesView extends JPanel implements IModularCategoryView {
    private JRadioButton tshirtRadioButton;
    private JRadioButton chombaRadioButton;
    private JComboBox materialComboBox;
    private JRadioButton shortSleeveRadioButton;
    private JRadioButton longSleeveRadioButton;
    private JPanel containerPanel;
    private JRadioButton tankTopRadioButton;
    private JTextField printingMetersPriceTextField;
    private JTextField clothMetersAmountTextField;
    private JTextField profitTextField;
    private JTextField printingMetersAmountTextField;
    private JPanel materialContainerPanel;
    private JPanel printingMetersContainer;
    private JPanel printingMetersAmountContainer;
    private JPanel printingMetersPriceContainer;
    private JLabel printingMultiplyLabel;
    private JLabel printingEqualsLabel;
    private JPanel clothMetersContainer;
    private JPanel printingMetersFinalPriceContainer;
    private JTextField printingMetersFinalPriceTextField;
    private JPanel clothMetersAmountContainer;
    private JPanel clothMetersPriceContainer;
    private JTextField clothMetersPriceTextField;
    private JLabel clothMultiplyLabel;
    private JLabel clothEqualsLabel;
    private JPanel clothMetersFinalPriceContainer;
    private JTextField clothMetersFinalPriceTextField;
    private JPanel plankLoweringContainer;
    private JPanel plankLoweringAmountContainer;
    private JPanel plankLoweringPriceContainer;
    private JPanel plankLoweringFinalPriceContainer;
    private JLabel plankLoweringMultiplyLabel;
    private JLabel plankLoweringEqualsLabel;
    private JPanel otherFieldsContainer;
    private JPanel seamstressPriceContainer;
    private JTextField seamstressPriceTextField;
    private JPanel seamstressTypeContainer;
    private JTextField plankLoweringAmountTextField;
    private JTextField plankLoweringPriceTextField;
    private JTextField plankLoweringFinalPriceTextField;
    private JPanel profitContainer;
    private JLabel profitMultiplyLabel;
    private JPanel finalPriceContainer;
    private JLabel finalPriceEqualsLabel;
    private JTextField finalPriceTextField;
    private JComboBox seamstressTypeComboBox;
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
    private JLabel shirt;
    private JCheckBox editPriceCheckBox;
    @Getter
    private final ArrayList<String> radioValues = new ArrayList<>();
    @Getter
    private final Map<String, String> comboBoxValues = new HashMap<>();
    @Getter
    private final Map<String, String> textFieldValues = new HashMap<>();
    private boolean isCalculating = false;

    private double profit;
    private double printingMetersPrice;
    private double plankLoweringPrice;
    private double clothMetersPrice;
    private double seamstressPrice;

    private final ProductCreatePresenter createPresenter;
    private final ProductSearchPresenter searchPresenter;
    private final ProductPresenter presenter;

    public ModularClothesView(boolean isCreate, ProductPresenter presenter) {
        this.presenter = presenter;
        if (isCreate && presenter instanceof ProductCreatePresenter) {
            this.createPresenter = (ProductCreatePresenter) presenter;
            this.searchPresenter = null;
        } else if (!isCreate && presenter instanceof ProductSearchPresenter) {
            this.createPresenter = null;
            this.searchPresenter = (ProductSearchPresenter) presenter;
        } else {
            this.createPresenter = null;
            this.searchPresenter = null;
            // Optionally show an error or throw an exception
        }
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
        panels.add(clothMetersAmountContainer);
        panels.add(clothMetersPriceContainer);
        panels.add(clothMetersFinalPriceContainer);
        panels.add(seamstressPriceContainer);
        panels.add(seamstressTypeContainer);
        panels.add(profitContainer);
        panels.add(finalPriceContainer);
        for (JPanel panel : panels) {

            TitledBorder border = (TitledBorder) panel.getBorder();

            FontMetrics fm = panel.getFontMetrics(border.getTitleFont());
            int titleWidth = fm.stringWidth(border.getTitle());

            panel.setPreferredSize(new Dimension(titleWidth + 20, 50));
        }
    }

    private void setLabelPosition(JPanel leftPanel, JPanel rightPanel, JLabel label) {

        int leftPosition = leftPanel.getX();
        leftPosition += leftPanel.getWidth();
        int rightPosition = rightPanel.getX();

        int average = (rightPosition - leftPosition) / 2;

        label.setBounds(Math.max(0, average), label.getY(), label.getWidth(), label.getHeight());

    }

    @Override
    public void loadComboBoxValues() {
        ArrayList<Pair<String, Double>> materialsList = presenter.getTableAsArrayList(TELAS);
        ArrayList<Pair<String, Double>> seamstressList = presenter.getTableAsArrayList(SERVICIOS);

        for (Pair<String, Double> pair : materialsList) {
            if (pair.getValue0().contains("LINEAL")) materialComboBox.addItem(pair.getValue0());
        }
        for (Pair<String, Double> pair : seamstressList) {
            if (pair.getValue0().contains("Costurera")) seamstressTypeComboBox.addItem(pair.getValue0());
        }
    }

    @Override
    public JPanel getContainerPanel() {
        return containerPanel;
    }

    @Override
    public void initListeners() {
        ArrayList<JTextField> textFields = new ArrayList<>();

        textFields.add(clothMetersAmountTextField);
        textFields.add(clothMetersPriceTextField);
        textFields.add(plankLoweringAmountTextField);
        textFields.add(plankLoweringPriceTextField);
        textFields.add(printingMetersAmountTextField);
        textFields.add(printingMetersPriceTextField);
        textFields.add(seamstressPriceTextField);
        textFields.add(profitTextField);

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

        materialComboBox.addItemListener(e -> {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                SwingUtilities.invokeLater(this::calculateDependantPrices);
            }
        });

        seamstressTypeComboBox.addItemListener(e -> {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                SwingUtilities.invokeLater(this::calculateDependantPrices);
            }
        });
    }

    @Override
    public void calculateDependantPrices() {
        try {
            if (isCalculating) return;
            isCalculating = true;
            float clothMetersAmount = clothMetersAmountTextField.getText().isEmpty() ? 0 : Float.parseFloat(clothMetersAmountTextField.getText());
            float printingMetersAmount = printingMetersAmountTextField.getText().isEmpty() ? 0 : Float.parseFloat(printingMetersAmountTextField.getText());
            float plankLoweringAmount = plankLoweringAmountTextField.getText().isEmpty() ? 0 : Float.parseFloat(plankLoweringAmountTextField.getText());
            float clothMetersPrice = clothMetersPriceTextField.getText().isEmpty() ? 0 : Float.parseFloat(clothMetersPriceTextField.getText());
            float printingMetersPrice = printingMetersPriceTextField.getText().isEmpty() ? 0 : Float.parseFloat(printingMetersPriceTextField.getText());
            float plankLoweringPrice = plankLoweringPriceTextField.getText().isEmpty() ? 0 : Float.parseFloat(plankLoweringPriceTextField.getText());

            float clothMetersFinalPrice = clothMetersAmount * clothMetersPrice;
            float printingMetersFinalPrice = printingMetersAmount * printingMetersPrice;
            float plankLoweringFinalPrice = plankLoweringAmount * plankLoweringPrice;
            float profit = profitTextField.getText().isEmpty() ? 0 : Float.parseFloat(profitTextField.getText());

            SwingUtilities.invokeLater(() -> {
                clothMetersFinalPriceTextField.setText(String.valueOf(clothMetersFinalPrice));
                printingMetersFinalPriceTextField.setText(String.valueOf(printingMetersFinalPrice));
                plankLoweringFinalPriceTextField.setText(String.valueOf(plankLoweringFinalPrice));
                float seamstressPrice = seamstressPriceTextField.getText().isEmpty() ? 0 : Float.parseFloat(seamstressPriceTextField.getText());
                finalPriceTextField.setText(String.valueOf((clothMetersFinalPrice + printingMetersFinalPrice + plankLoweringFinalPrice + seamstressPrice) * (profit / 100)));
                isCalculating = false;
            });

        } catch (NumberFormatException | NullPointerException e) {
            showMessage(MessageTypes.FLOAT_PARSING_ERROR, containerPanel);
            isCalculating = false;
        }
    }

    private String getMaterialSelected() {
        return (String) materialComboBox.getSelectedItem();
    }

    @Override
    public void setPriceTextFields() {
        profit = presenter.getIndividualPrice(GANANCIAS, "Prendas");
        printingMetersPrice = presenter.getIndividualPrice(IMPRESIONES, "Metro de Sublimación");
        plankLoweringPrice = presenter.getIndividualPrice(BAJADA_PLANCHA, "En prenda");
        clothMetersPrice = presenter.getIndividualPrice(TELAS, getMaterialSelected());
        String seamstressType = (String) seamstressTypeComboBox.getSelectedItem();
        seamstressPrice = presenter.getIndividualPrice(SERVICIOS, seamstressType);
        String profitText = String.valueOf(profit);

        profitTextField.setText(profitText);
        printingMetersPriceTextField.setText(String.valueOf(printingMetersPrice));
        clothMetersPriceTextField.setText(String.valueOf(clothMetersPrice));
        plankLoweringPriceTextField.setText(String.valueOf(plankLoweringPrice));
        seamstressPriceTextField.setText(String.valueOf(seamstressPrice));
    }

    @Override
    public ArrayList<Attribute> getAttributes() {
        ArrayList<Attribute> attributes = new ArrayList<>();
        attributes.add(new Attribute("T1A", printingMetersAmountTextField.getText()));
        attributes.add(new Attribute("T1B", printingMetersPriceTextField.getText()));
        attributes.add(new Attribute("T2A", clothMetersAmountTextField.getText()));
        attributes.add(new Attribute("T2B", clothMetersPriceTextField.getText()));
        attributes.add(new Attribute("T3A", plankLoweringAmountTextField.getText()));
        attributes.add(new Attribute("T3B", plankLoweringPriceTextField.getText()));
        attributes.add(new Attribute("COSTURERA", seamstressPriceTextField.getText()));
        attributes.add(new Attribute("TELA", (String) materialComboBox.getSelectedItem()));
        attributes.add(new Attribute("TIPO_COSTURERA", (String) seamstressTypeComboBox.getSelectedItem()));
        attributes.add(new Attribute("GANANCIA", profitTextField.getText()));
        return attributes;
    }

    @Override
    public void comboBoxListenerSet(ItemListener listener) {
        materialComboBox.addItemListener(listener);
        seamstressTypeComboBox.addItemListener(listener);
    }

    @Override
    public void setSearchTextFields(Product product) {
        Map<String, String> attributes = searchPresenter.getProductAttributes(product);
        printingMetersAmountTextField.setText(attributes.get("T1A"));
        clothMetersAmountTextField.setText(attributes.get("T2A"));
        plankLoweringAmountTextField.setText(attributes.get("T3A"));
        materialComboBox.setSelectedItem(attributes.get("TELA"));
        seamstressTypeComboBox.setSelectedItem(attributes.get("TIPO_COSTURERA"));
    }

    @Override
    public void blockTextFields() {
        clothMetersAmountTextField.setEnabled(false);
        printingMetersPriceTextField.setEnabled(false);
        profitTextField.setEnabled(false);
        plankLoweringPriceTextField.setEnabled(false);
        seamstressPriceTextField.setEnabled(false);
    }

    @Override
    public void unlockTextFields() {
        clothMetersAmountTextField.setEnabled(true);
        printingMetersPriceTextField.setEnabled(true);
        profitTextField.setEnabled(true);
        plankLoweringPriceTextField.setEnabled(true);
        seamstressPriceTextField.setEnabled(true);
    }

    @Override
    public List<Triplet<String, String, Double>> getModularPrices() {
        double actualMetersPrice = clothMetersPriceTextField.getText().isEmpty() ? 0 : Double.parseDouble(clothMetersAmountTextField.getText());
        double actualPrintingPrice = printingMetersPriceTextField.getText().isEmpty() ? 0 : Double.parseDouble(printingMetersPriceTextField.getText());
        double actualProfit = profitTextField.getText().isEmpty() ? 0 : Double.parseDouble(profitTextField.getText());
        double actualSeamstressPrice = seamstressPriceTextField.getText().isEmpty() ? 0 : Double.parseDouble(seamstressPriceTextField.getText());
        double actualPlankLoweringPrice = plankLoweringPriceTextField.getText().isEmpty() ? 0 : Double.parseDouble(plankLoweringPriceTextField.getText());
        String actualClothSelected = (String) materialComboBox.getSelectedItem();
        String actualSeamstressType = (String) seamstressTypeComboBox.getSelectedItem();
        List<Triplet<String, String, Double>> modularPrices = new ArrayList<>();

        modularPrices.add(new Triplet<>("TELAS", actualClothSelected, actualMetersPrice));
        modularPrices.add(new Triplet<>("IMPRESIONES", "En sublimación", actualPrintingPrice));
        modularPrices.add(new Triplet<>("GANANCIAS", "Prendas", actualProfit));
        modularPrices.add(new Triplet<>("SERVICIOS", actualSeamstressType, actualSeamstressPrice));
        modularPrices.add(new Triplet<>("SERVICIOS", "Bajada de plancha", actualPlankLoweringPrice));

        return modularPrices;
    }

}
