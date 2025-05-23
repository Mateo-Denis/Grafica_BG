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
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static utils.databases.SettingsTableNames.GANANCIAS;
import static utils.databases.SettingsTableNames.MATERIALES;

public class ModularCuttingServiceView extends JPanel implements IModularCategoryView {
    private JPanel containerPanel;
    private JPanel vinylsComboBoxContainer;
    private JPanel vinylMetersContainer;
    private JComboBox vinylsComboBox;
    private JPanel vinylMetersPriceContainer;
    private JLabel vinylProfitMultiplyLabel;
    private JPanel profitContainer;
    private JLabel cuttingServiceFinalPriceEqualsLabel;
    private JPanel cuttingServiceFinalPriceContainer;
    private JTextField vinylCostTextField;
    private JTextField profitTextField;
    private JTextField cuttingServiceFinalPriceTextField;
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
    private double vinylPrice;
    private double profit;
    private boolean initialization;

    private final ProductCreatePresenter createPresenter;
    private final ProductSearchPresenter searchPresenter;
    private final ProductPresenter presenter;

    public ModularCuttingServiceView(boolean isCreate, ProductPresenter presenter) {
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
        panels.add(vinylMetersPriceContainer);
        panels.add(profitContainer);
        panels.add(cuttingServiceFinalPriceContainer);
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
    public void initListeners() {
        ArrayList<JTextField> textFields = new ArrayList<>();

        textFields.add(vinylCostTextField);
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
    }

    @Override
    public void calculateDependantPrices() {
        try {

            float vinylCost = vinylCostTextField.getText().isEmpty() ? 0 : Float.parseFloat(vinylCostTextField.getText());
            float profit = profitTextField.getText().isEmpty() ? 0 : Float.parseFloat(profitTextField.getText());

            cuttingServiceFinalPriceTextField.setText(String.valueOf(vinylCost * (profit/100)));
        } catch (NumberFormatException | NullPointerException e) {
            showMessage(MessageTypes.FLOAT_PARSING_ERROR, containerPanel);
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
        ArrayList<Pair<String, Double>> list = presenter.getTableAsArrayList(MATERIALES);
        for (Pair<String, Double> pair : list) {
            vinylsComboBox.addItem(pair.getValue0());
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
        profit = presenter.getIndividualPrice(GANANCIAS, "Servicio de corte");
        vinylPrice = presenter.getIndividualPrice(MATERIALES, getVinylTypeSelected());

        vinylCostTextField.setText(String.valueOf(vinylPrice));
        profitTextField.setText(String.valueOf(profit));
    }

    @Override
    public ArrayList<Attribute> getAttributes() {
        ArrayList<Attribute> attributes = new ArrayList<>();
        attributes.add(new Attribute("T1A", vinylCostTextField.getText()));
        attributes.add(new Attribute("VINILO", getVinylTypeSelected()));
        attributes.add(new Attribute("GANANCIA", profitTextField.getText()));
        return attributes;
    }

    @Override
    public void comboBoxListenerSet(ItemListener listener) {
        vinylsComboBox.addItemListener(listener);
    }

    @Override
    public void setSearchTextFields(Product product) {
        Map<String, String> attributes = searchPresenter.getProductAttributes(product);
        vinylsComboBox.setSelectedItem(attributes.get("VINILO"));
    }

    private String getVinylTypeSelected() {
        return (String) vinylsComboBox.getSelectedItem();
    }

}
