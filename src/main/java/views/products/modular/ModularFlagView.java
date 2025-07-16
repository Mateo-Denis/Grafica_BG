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

import static utils.TextUtils.truncateAndRound;
import static utils.databases.SettingsTableNames.*;

public class ModularFlagView extends JPanel implements IModularCategoryView {
    private JPanel containerPanel;
    private JComboBox clothComboBox;
    private JPanel clothSelectionContainer;
    private JPanel clothMeasuresContainer;
    private JPanel widthContainer;
    private JTextField widthTextField;
    private JLabel multiplyMeasuresAndPriceLabel;
    private JPanel metersPriceContainer;
    private JTextField metersPriceTextField;
    private JLabel measuresPriceEqualsLabel;
    private JPanel clothFinalPriceContainer;
    private JTextField clothFinalPriceTextField;
    private JPanel plankLoweringContainer;
    private JPanel plankLoweringAmountContainer;
    private JPanel plankLoweringPriceContainer;
    private JPanel plankLoweringFinalPriceContainer;
    private JLabel plankLoweringMultiplyLabel;
    private JLabel plankLoweringEqualsLabel;
    private JPanel seamstressContainer;
    private JTextField seamstressPriceTextField;
    private JPanel printingContainer;
    private JPanel printingMetersAmountContainer;
    private JPanel printingMetersPriceContainer;
    private JPanel printingFinalPriceContainer;
    private JTextField printingMetersAmountTextField;
    private JTextField printingMetersPriceTextField;
    private JTextField printingMetersFinalPriceTextField;
    private JPanel centerSideComponentsContainer;
    private JPanel rightSideComponentsContainer;
    private JPanel leftSideComponentsContainer;
    private JLabel profitMultiplyLabel;
    private JPanel profitContainer;
    private JTextField profitTextField;
    private JLabel flagFinalPriceEqualsLabel;
    private JPanel finalFlagPriceContainer;
    private JTextField flagFinalPriceTextField;
    private JTextField plankLoweringAmountTextField;
    private JTextField plankLoweringPriceTextField;
    private JTextField plankLoweringFinalPriceTextField;
    private JLabel printingMetersMultiplyLabel;
    private JLabel printingMetersEqualsLabel;
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
    private JTextField particularFinalPriceTextField;
    private JTextField heightTextField;
    private JComboBox measuresComboBox;
    private JComboBox sizeComboBox;
    @Getter
    private final ArrayList<String> radioValues = new ArrayList<>();
    @Getter
    private final Map<String, String> comboBoxValues = new HashMap<>();
    @Getter
    private final Map<String, String> textFieldValues = new HashMap<>();

    private final ProductCreatePresenter createPresenter;
    private final ProductSearchPresenter searchPresenter;
    private final ProductPresenter presenter;

    private double profit;
    private double printingMetersPrice;
    private double plankLoweringPrice;
    private double clothSqrMetersPrice;
    private double seamstressPrice;
    private boolean initialization;

//    private enum FlagSizes {
//
//        // After
//        SMALL(100, 60),
//        MEDIUM(100, 90),
//        LARGE(200, 9);
//
//        private final int width;
//        private final int height;
//
//        FlagSizes(int width, int height) {
//            this.width = width;
//            this.height = height;
//        }
//
//        public int getWidth() {
//            return width;
//        }
//
//        public int getHeight() {
//            return height;
//        }
//
//    }


    public ModularFlagView(boolean isCreate, ProductPresenter presenter) {
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
        panels.add(printingMetersPriceContainer);
        panels.add(printingMetersAmountContainer);
        panels.add(printingFinalPriceContainer);
        panels.add(plankLoweringAmountContainer);
        panels.add(plankLoweringPriceContainer);
        panels.add(plankLoweringFinalPriceContainer);
        panels.add(widthContainer);
        panels.add(metersPriceContainer);
        panels.add(clothFinalPriceContainer);
        panels.add(profitContainer);
        panels.add(finalFlagPriceContainer);
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


        textFields.add(widthTextField);
        textFields.add(metersPriceTextField);
        textFields.add(seamstressPriceTextField);
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
                float width = widthTextField.getText().isEmpty() ? 0 : Float.parseFloat(widthTextField.getText());
                float metersPrice = metersPriceTextField.getText().isEmpty() ? 0 : Float.parseFloat(metersPriceTextField.getText());
                float plankLoweringPrice = plankLoweringPriceTextField.getText().isEmpty() ? 0 : Float.parseFloat(plankLoweringPriceTextField.getText());
                float seamstressPrice = seamstressPriceTextField.getText().isEmpty() ? 0 : Float.parseFloat(seamstressPriceTextField.getText());
                float printingMetersAmount = printingMetersAmountTextField.getText().isEmpty() ? 0 : Float.parseFloat(printingMetersAmountTextField.getText());
                float printingMetersPrice = printingMetersPriceTextField.getText().isEmpty() ? 0 : Float.parseFloat(printingMetersPriceTextField.getText());
                float profit = profitTextField.getText().isEmpty() ? 0 : Float.parseFloat(profitTextField.getText());

                float clothPrice = width * metersPrice;
                float plankLoweringPriceTotal = plankLoweringAmount * plankLoweringPrice;
                float printingMetersPriceTotal = printingMetersAmount * printingMetersPrice;

                float iva = IVAcombobox.getSelectedItem() == null ? 0 : Float.parseFloat((String) IVAcombobox.getSelectedItem());
                float recharge = particularAddTextField.getText().isEmpty() ? 0 : Float.parseFloat(particularAddTextField.getText());

                float priceWOIva = (clothPrice + plankLoweringPriceTotal + seamstressPrice + printingMetersPriceTotal) * (profit/100);
                float priceWIva = priceWOIva + (priceWOIva * iva / 100);
                float particularFinalPrice = priceWIva + (priceWIva * recharge / 100);


                plankLoweringFinalPriceTextField.setText(String.valueOf(plankLoweringPriceTotal));
                printingMetersFinalPriceTextField.setText(String.valueOf(printingMetersPriceTotal));
                clothFinalPriceTextField.setText(String.valueOf(clothPrice));

                flagFinalPriceTextField.setText(truncateAndRound(String.valueOf(priceWIva)));
                particularFinalPriceTextField.setText(truncateAndRound(String.valueOf(particularFinalPrice)));

            } catch (NumberFormatException | NullPointerException e) {
                showMessage(MessageTypes.FLOAT_PARSING_ERROR, containerPanel);
            }
        } catch (NumberFormatException | NullPointerException e) {
            showMessage(MessageTypes.INT_PARSING_ERROR, containerPanel);
        }
    }

    @Override
    public void loadComboBoxValues() {
        ArrayList<Pair<String, Double>> clothList = presenter.getTableAsArrayList(TELAS);
        for (Pair<String, Double> pair : clothList) {
            clothComboBox.addItem(pair.getValue0());
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
        profitTextField.setText(String.valueOf(0));
        printingMetersPriceTextField.setText(String.valueOf(0));
        metersPriceTextField.setText(String.valueOf(0));
        plankLoweringPriceTextField.setText(String.valueOf(0));
        seamstressPriceTextField.setText(String.valueOf(0));

    }

    @Override
    public ArrayList<Attribute> getAttributes() {
        ArrayList<Attribute> attributes = new ArrayList<>();
        attributes.add(new Attribute("ANCHO_TELA", widthTextField.getText()));
        attributes.add(new Attribute("PRECIO_TELA", metersPriceTextField.getText()));
        attributes.add(new Attribute("CANTIDAD_BAJADA", plankLoweringAmountTextField.getText()));
        attributes.add(new Attribute("PRECIO_BAJADA", plankLoweringPriceTextField.getText()));
        attributes.add(new Attribute("CANTIDAD_IMP", printingMetersAmountTextField.getText()));
        attributes.add(new Attribute("PRECIO_IMP", printingMetersPriceTextField.getText()));
        attributes.add(new Attribute("TELA", getFlagComboBoxSelection()));
        attributes.add(new Attribute("COSTURERA", seamstressPriceTextField.getText()));
        attributes.add(new Attribute("GANANCIA", profitTextField.getText()));
        attributes.add(new Attribute("IVA", String.valueOf(IVAcombobox.getSelectedItem())));
        attributes.add(new Attribute("RECARGO", particularAddTextField.getText()));
        return attributes;
    }

    @Override
    public void comboBoxListenerSet(ItemListener listener) {
        clothComboBox.addItemListener(listener);
    }

    @Override
    public void setSearchTextFields(Product product) {
        if(searchPresenter == null) {
            return;
        }
        Map<String, String> attributes = searchPresenter.getProductAttributes(product);
        clothComboBox.setSelectedItem(attributes.get("TELA"));
        metersPriceTextField.setText(attributes.get("PRECIO_TELA"));
        profitTextField.setText(attributes.get("GANANCIA"));
        IVAcombobox.setSelectedItem(attributes.get("IVA"));
        particularAddTextField.setText(attributes.get("RECARGO"));
        plankLoweringAmountTextField.setText(attributes.get("CANTIDAD_BAJADA"));
        plankLoweringPriceTextField.setText(attributes.get("PRECIO_BAJADA"));
        printingMetersAmountTextField.setText(attributes.get("CANTIDAD_IMP"));
        printingMetersPriceTextField.setText(attributes.get("PRECIO_IMP"));
        seamstressPriceTextField.setText(attributes.get("COSTURERA"));
        widthTextField.setText(attributes.get("ANCHO_TELA"));
    }

    private String getFlagComboBoxSelection() {
        return (String) clothComboBox.getSelectedItem();
    }

    private String getSizeComboBoxSelection() {
        return (String) sizeComboBox.getSelectedItem();
    }

}
