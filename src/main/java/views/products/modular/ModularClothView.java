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

public class ModularClothView extends JPanel implements IModularCategoryView {
    private JPanel containerPanel;
    private JComboBox clothComboBox;
    private JPanel clothComboBoxContainer;
    private JPanel clothMetersContainer;
    private JPanel clothMetersPriceContainer;
    private JPanel profitContainer;
    private JPanel finalPriceContainer;
    private JLabel profitMultiplyLabel;
    private JLabel clothFinalPriceEqualsLabel;
    private JTextField clothMetersPriceTextField;
    private JTextField profitTextField;
    private JTextField clothFinalPriceTextField;
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
    @Getter
    private final ArrayList<String> radioValues = new ArrayList<>();
    @Getter
    private final Map<String, String> comboBoxValues = new HashMap<>();
    @Getter
    private final Map<String, String> textFieldValues = new HashMap<>();
    private double profit;
    private double clothMetersPrice;
    private boolean initialization;

    private final ProductCreatePresenter createPresenter;
    private final ProductSearchPresenter searchPresenter;
    private final ProductPresenter presenter;

    public ModularClothView(boolean isCreate, ProductPresenter presenter) {
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
        panels.add(clothMetersPriceContainer);
        panels.add(profitContainer);
        panels.add(finalPriceContainer);
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

        textFields.add(clothMetersPriceTextField);
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

        clothComboBox.addItemListener(e -> {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                SwingUtilities.invokeLater(this::calculateDependantPrices);
            }
        });

        IVAcombobox.addItemListener(e -> {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                calculateDependantPrices();
            }
        });
    }

    @Override
    public void calculateDependantPrices() {
        try {
            float clothMeters = clothMetersPriceTextField.getText().isEmpty() ? 0 : Float.parseFloat(clothMetersPriceTextField.getText());
            float profit = profitTextField.getText().isEmpty() ? 0 : Float.parseFloat(profitTextField.getText());
            float iva = String.valueOf(IVAcombobox.getSelectedItem()).isEmpty() ? 0 : Float.parseFloat(String.valueOf(IVAcombobox.getSelectedItem()));
            float recharge = particularAddTextField.getText().isEmpty() ? 0 : Float.parseFloat(particularAddTextField.getText());

            float priceWOiva = clothMeters * (profit / 100);
            float priceWiva = priceWOiva + (priceWOiva * iva / 100);

            clientFinalPriceTextField.setText(truncateAndRound(String.valueOf(priceWiva)));
            clothFinalPriceTextField.setText(truncateAndRound(String.valueOf( priceWiva + ( priceWiva * recharge / 100))));
        } catch (NumberFormatException | NullPointerException e) {
            showMessage(MessageTypes.FLOAT_PARSING_ERROR, containerPanel);
        }
    }

    private String getClothComboBoxSelection() {
        return (String) clothComboBox.getSelectedItem();
    }

    @Override
    public void loadComboBoxValues() {
        ArrayList<Pair<String, Double>> list = presenter.getTableAsArrayList(TELAS);
        for (Pair<String, Double> pair : list) {
            if (pair.getValue0().contains("LINEAL")) {
                clothComboBox.addItem(pair.getValue0());
            }
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
        profit = presenter.getIndividualPrice(GANANCIAS, "Telas");
        clothMetersPrice = presenter.getIndividualPrice(TELAS, getClothComboBoxSelection());

        profitTextField.setText(String.valueOf(profit));
        clothMetersPriceTextField.setText(String.valueOf(clothMetersPrice));

    }

    @Override
    public ArrayList<Attribute> getAttributes() {
        ArrayList<Attribute> attributes = new ArrayList<>();
        attributes.add(new Attribute("T1A", clothMetersPriceTextField.getText()));
        attributes.add(new Attribute("TELA", getClothComboBoxSelection()));
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
        Map<String, String> attributes = searchPresenter.getProductAttributes(product);
        clothComboBox.setSelectedItem(attributes.get("TELA"));
    }

}
