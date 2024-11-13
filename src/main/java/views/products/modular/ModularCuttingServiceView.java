package views.products.modular;

import org.javatuples.Triplet;
import presenters.product.ProductCreatePresenter;
import utils.MessageTypes;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static utils.databases.SettingsTableNames.VINILOS;

public class ModularCuttingServiceView extends JPanel implements IModularCategoryView {
    private JPanel containerPanel;
    private JPanel vinylsComboBoxContainer;
    private JPanel vinylMetersContainer;
    private JComboBox vinylsComboBox;
    private JPanel vinylMetersAmountContainer;
    private JPanel vinylMetersPriceContainer;
    private JLabel vinylMetersMultiplyLabel;
    private JLabel vinylProfitMultiplyLabel;
    private JPanel profitContainer;
    private JLabel cuttingServiceFinalPriceEqualsLabel;
    private JPanel cuttingServiceFinalPriceContainer;
    private JTextField vinylCostTextField;
    private JTextField profitTextField;
    private JTextField vinylMetersAmount;
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

    }

    @Override
    public void calculateDependantPrices() {
        try {
            float vinylMeters = vinylMetersAmount.getText().isEmpty() ? 0 : Float.parseFloat(vinylMetersAmount.getText());
            float vinylCost = vinylCostTextField.getText().isEmpty() ? 0 : Float.parseFloat(vinylCostTextField.getText());
            float profit = profitTextField.getText().isEmpty() ? 0 : Float.parseFloat(profitTextField.getText());

            cuttingServiceFinalPriceTextField.setText(String.valueOf(vinylMeters * vinylCost * profit));
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

    private String getVinylTypeSelected() {
        return (String) vinylsComboBox.getSelectedItem();
    }
}
