package views.products.modular;

import org.javatuples.Triplet;
import presenters.product.ProductCreatePresenter;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
    private ProductCreatePresenter presenter;

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
    public Map<String, String> getModularAttributes() {
        return Map.of();
    }

    @Override
    public double getPrice() {
        return 0;
    }

    @Override
    public ArrayList<String> getRelevantInformation() {
        return null;
    }

    @Override
    public void loadComboBoxValues() {

    }

    @Override
    public ArrayList<String> getExhaustiveInformation() {
        return null;
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

    }
}
