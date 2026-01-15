package views.products.modular;

import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
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

import static utils.TextUtils.truncateAndRound;
import static utils.databases.SettingsTableNames.GENERAL;
import static utils.databases.SettingsTableNames.TELAS;

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
    private JPanel ParticularAddTextFieldContainer;
    private JTextField particularAddTextField;
    private JTextField particularFinalPriceTextField;
    private JPanel dollarPriceContainer;
    private JComboBox dollarComboBox;
    private JTextField dollarValueTextField;
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
        ArrayList<String> materialsList = presenter.getOtherTablesAsArrayList(TELAS);

        // Limpia los items actuales
        materialComboBox.removeAllItems();
        seamstressTypeComboBox.removeAllItems();

        // Agrega los materiales al comboBox
        for (String material : materialsList) {
            materialComboBox.addItem(material);
        }
        // Agrega los tipos de costurera al comboBox
        seamstressTypeComboBox.addItem("Remera");
        seamstressTypeComboBox.addItem("Buzo");


        ArrayList<Pair<String, Double>> dollarList = presenter.getGeneralTableAsArrayList(GENERAL);
        for (Pair<String, Double> pair : dollarList) {
            String s = pair.getValue0();
            dollarComboBox.addItem(pair.getValue0());
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
        textFields.add(particularAddTextField);

        for (JTextField textField : textFields) {
            if (presenter instanceof ProductCreatePresenter) {
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

        materialComboBox.addItemListener(e -> {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                SwingUtilities.invokeLater(this::calculateDependantPrices);
            }
        });

        IVAcombobox.addItemListener(e -> {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                calculateDependantPrices();
            }
        });

        seamstressTypeComboBox.addItemListener(e -> {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                SwingUtilities.invokeLater(this::calculateDependantPrices);
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
            if (isCalculating) return;
            isCalculating = true;
            float clothMetersAmount = clothMetersAmountTextField.getText().isEmpty() ? 0 : Float.parseFloat(clothMetersAmountTextField.getText());
            float printingMetersAmount = printingMetersAmountTextField.getText().isEmpty() ? 0 : Float.parseFloat(printingMetersAmountTextField.getText());
            float plankLoweringAmount = plankLoweringAmountTextField.getText().isEmpty() ? 0 : Float.parseFloat(plankLoweringAmountTextField.getText());
            float clothMetersPrice = clothMetersPriceTextField.getText().isEmpty() ? 0 : Float.parseFloat(clothMetersPriceTextField.getText());
            float printingMetersPrice = printingMetersPriceTextField.getText().isEmpty() ? 0 : Float.parseFloat(printingMetersPriceTextField.getText());
            float plankLoweringPrice = plankLoweringPriceTextField.getText().isEmpty() ? 0 : Float.parseFloat(plankLoweringPriceTextField.getText());
            float dollarPrice = dollarComboBox.getSelectedItem() == null ? 0 : (float) presenter.getIndividualPrice(GENERAL, (String) dollarComboBox.getSelectedItem());

            float clothMetersFinalPrice = clothMetersAmount * clothMetersPrice;
            float printingMetersFinalPrice = printingMetersAmount * printingMetersPrice;
            float plankLoweringFinalPrice = plankLoweringAmount * plankLoweringPrice;
            float profit = profitTextField.getText().isEmpty() ? 0 : Float.parseFloat(profitTextField.getText());
            float IVA = IVAcombobox.getSelectedItem() == null ? 0 : Float.parseFloat(IVAcombobox.getSelectedItem().toString());
            float particularAdd = particularAddTextField.getText().isEmpty() ? 0 : Float.parseFloat(particularAddTextField.getText());


            clothMetersFinalPriceTextField.setText(String.valueOf(clothMetersFinalPrice));
            printingMetersFinalPriceTextField.setText(String.valueOf(printingMetersFinalPrice));
            plankLoweringFinalPriceTextField.setText(String.valueOf(plankLoweringFinalPrice));
            float seamstressPrice = seamstressPriceTextField.getText().isEmpty() ? 0 : Float.parseFloat(seamstressPriceTextField.getText());
            float priceWOIva = (clothMetersFinalPrice + printingMetersFinalPrice + plankLoweringFinalPrice + seamstressPrice) + ((clothMetersFinalPrice + printingMetersFinalPrice + plankLoweringFinalPrice + seamstressPrice) * (profit / 100));
            float priceWIva = (priceWOIva + (priceWOIva * (IVA / 100))) * dollarPrice;

            dollarValueTextField.setText(String.valueOf(dollarPrice));
            finalPriceTextField.setText(truncateAndRound(String.valueOf(priceWIva)));
            particularFinalPriceTextField.setText(truncateAndRound(String.valueOf(priceWIva + (priceWIva * (particularAdd / 100)))));
            isCalculating = false;


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
        profitTextField.setText(String.valueOf(0));
        printingMetersPriceTextField.setText(String.valueOf(0));
        printingMetersAmountTextField.setText(String.valueOf(0));
        clothMetersPriceTextField.setText(String.valueOf(0));
        clothMetersAmountTextField.setText(String.valueOf(0));
        plankLoweringPriceTextField.setText(String.valueOf(0));
        plankLoweringAmountTextField.setText(String.valueOf(0));
        seamstressPriceTextField.setText(String.valueOf(0));
        particularAddTextField.setText(String.valueOf(0));

    }

    @Override
    public ArrayList<Attribute> getAttributes() {
        ArrayList<Attribute> attributes = new ArrayList<>();
        attributes.add(new Attribute("CANTIDAD_IMP", printingMetersAmountTextField.getText()));
        attributes.add(new Attribute("PRECIO_IMP", printingMetersPriceTextField.getText()));
        attributes.add(new Attribute("CANTIDAD_TELA", clothMetersAmountTextField.getText()));
        attributes.add(new Attribute("PRECIO_TELA", clothMetersPriceTextField.getText()));
        attributes.add(new Attribute("CANTIDAD_BAJADA", plankLoweringAmountTextField.getText()));
        attributes.add(new Attribute("PRECIO_BAJADA", plankLoweringPriceTextField.getText()));
        attributes.add(new Attribute("COSTURERA", seamstressPriceTextField.getText()));
        attributes.add(new Attribute("TELA", (String) materialComboBox.getSelectedItem()));
        attributes.add(new Attribute("TIPO_COSTURERA", (String) seamstressTypeComboBox.getSelectedItem()));
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
        seamstressTypeComboBox.addItemListener(listener);
    }

    @Override
    public void setSearchTextFields(Product product) {
        if (searchPresenter == null) {
            return;
        }
        Map<String, String> attributes = searchPresenter.getProductAttributes(product);
        printingMetersAmountTextField.setText(attributes.get("CANTIDAD_IMP"));
        printingMetersPriceTextField.setText(attributes.get("PRECIO_IMP"));
        clothMetersAmountTextField.setText(attributes.get("CANTIDAD_TELA"));
        clothMetersPriceTextField.setText(attributes.get("PRECIO_TELA"));
        plankLoweringAmountTextField.setText(attributes.get("CANTIDAD_BAJADA"));
        plankLoweringPriceTextField.setText(attributes.get("PRECIO_BAJADA"));
        materialComboBox.setSelectedItem(attributes.get("TELA"));
        seamstressPriceTextField.setText(attributes.get("COSTURERA"));
        seamstressTypeComboBox.setSelectedItem(attributes.get("TIPO_COSTURERA"));
        profitTextField.setText(attributes.get("GANANCIA"));
        IVAcombobox.setSelectedItem(attributes.get("IVA"));
        particularAddTextField.setText(attributes.get("RECARGO"));
        dollarValueTextField.setText(attributes.get("VALOR_TIPO_CAMBIO"));
        dollarComboBox.setSelectedItem(attributes.get("TIPO_CAMBIO"));

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
