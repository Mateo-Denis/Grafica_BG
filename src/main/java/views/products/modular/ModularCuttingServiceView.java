package views.products.modular;

import org.javatuples.Triplet;
import presenters.product.ProductCreatePresenter;
import utils.Attribute;
import utils.MessageTypes;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static utils.databases.SettingsTableNames.VINILOS;

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
    private ProductCreatePresenter presenter;
    private double vinylPrice;
    private double profit;
    private boolean initialization;

    public ModularCuttingServiceView(ProductCreatePresenter presenter) {
        this.presenter = presenter;
        initListeners();
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

            cuttingServiceFinalPriceTextField.setText(String.valueOf(vinylCost * profit));
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
        profit = 2;
        vinylPrice = presenter.getIndividualPrice(VINILOS, getVinylTypeSelected());

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

    private String getVinylTypeSelected() {
        return (String) vinylsComboBox.getSelectedItem();
    }
}
