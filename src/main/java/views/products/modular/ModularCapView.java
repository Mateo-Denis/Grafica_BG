package views.products.modular;

import org.javatuples.Triplet;
import presenters.product.ProductCreatePresenter;

import javax.swing.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static utils.databases.SettingsTableNames.*;
import static utils.databases.SettingsTableNames.GANANCIAS;

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
    private JTextField textField6;
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
    private ArrayList<String> radioValues = new ArrayList<>();
    private Map<String,String> comboBoxValues = new HashMap<>();
    private Map<String,String> textFieldValues = new HashMap<>();
    private double profit;
    private double capCost;
    private double plankLoweringPrice;
    private double printingMetersPrice;
    private ProductCreatePresenter presenter;
    public ModularCapView(ProductCreatePresenter presenter) {
        this.presenter = presenter;
        initListeners();
    }

    @Override
    public JPanel getContainerPanel() {
        return containerPanel;
    }

    public Map<String, String> getComboBoxValues() {
        return comboBoxValues;
    }

    public Map<String, String> getTextFieldValues() {
        return textFieldValues;
    }

    public ArrayList<String> getRadioValues() {
        return radioValues;
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
        printingMetersPrice = presenter.getIndividualPrice(IMPRESIONES, "Sublimación");
        profit = presenter.getIndividualPrice(GANANCIAS, "Impresión lineal");

        profitTextField.setText(String.valueOf(profit));
        printingMetersPriceTextField.setText(String.valueOf(printingMetersPrice));
        plankLoweringPriceTextField.setText(String.valueOf(plankLoweringPrice));
        capCostTextField.setText(String.valueOf(capCost));
    }

    public void initListeners() {
    }
}

