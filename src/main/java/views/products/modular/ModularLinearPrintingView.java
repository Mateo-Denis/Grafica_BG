package views.products.modular;

import org.javatuples.Triplet;
import presenters.product.ProductCreatePresenter;
import utils.databases.SettingsTableNames;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static utils.databases.SettingsTableNames.GANANCIAS;
import static utils.databases.SettingsTableNames.GENERAL;

public class ModularLinearPrintingView extends JPanel implements IModularCategoryView {
    private JPanel containerPanel;
    private JPanel leftSideComponentsContainer;
    private JPanel centerSideComponentsContainer;
    private JPanel rightSideComponentsContainer;
    private JPanel paperMeterPriceContainer;
    private JPanel inkByMeterPriceContainer;
    private JTextField paperMeterPriceTextField;
    private JTextField inkByMeterPriceTextField;
    private JLabel profitMultiplyLabel;
    private JLabel linearPrintingFinalPriceEqualsLabel;
    private JPanel profitContainer;
    private JPanel finalPriceContainer;
    private JTextField profitTextField;
    private JTextField finalPriceTextField;
    private ProductCreatePresenter presenter;
    private double paperMeterPrice;
    private double inkByMeterPrice;
    private double profit;

    public ModularLinearPrintingView(ProductCreatePresenter presenter) {
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
        paperMeterPrice = presenter.getIndividualPrice(GENERAL, "Costo de papel por metro");
        inkByMeterPrice = presenter.getIndividualPrice(GENERAL, "Costo de tinta por metro");
        profit = presenter.getIndividualPrice(GANANCIAS, "Impresión lineal");

        profitTextField.setText(String.valueOf(profit));
        paperMeterPriceTextField.setText(String.valueOf(paperMeterPrice));
        inkByMeterPriceTextField.setText(String.valueOf(inkByMeterPrice));
    }
}
