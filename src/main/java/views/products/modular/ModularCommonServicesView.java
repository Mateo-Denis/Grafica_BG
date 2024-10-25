package views.products.modular;

import org.javatuples.Triplet;
import presenters.product.ProductCreatePresenter;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static utils.databases.SettingsTableNames.*;
import static utils.databases.SettingsTableNames.SERVICIOS;

public class ModularCommonServicesView extends JPanel implements IModularCategoryView {
    private JPanel containerPanel;
    private JPanel servicesComboBoxContainer;
    private JPanel serviceCostContainer;
    private JComboBox serviceTypeComboBox;
    private JTextField serviceCostTextField;
    private ProductCreatePresenter presenter;
    private double servicePrice;

    public ModularCommonServicesView(ProductCreatePresenter presenter) {
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
        servicePrice = presenter.getIndividualPrice(SERVICIOS, getServiceTypeSelected());

        serviceCostTextField.setText(String.valueOf(servicePrice));
    }

    private String getServiceTypeSelected() {
        return (String) serviceTypeComboBox.getSelectedItem();
    }
}
