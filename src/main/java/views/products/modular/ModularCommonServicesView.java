package views.products.modular;

import org.javatuples.Pair;
import org.javatuples.Triplet;
import presenters.product.ProductCreatePresenter;
import utils.Attribute;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static utils.databases.SettingsTableNames.SERVICIOS;
import static utils.databases.SettingsTableNames.TELAS;

public class ModularCommonServicesView extends JPanel implements IModularCategoryView {
    private JPanel containerPanel;
    private JPanel servicesComboBoxContainer;
    private JPanel serviceCostContainer;
    private JComboBox serviceTypeComboBox;
    private JTextField serviceCostTextField;
    private ProductCreatePresenter presenter;
    private double servicePrice;
    private boolean initialization;

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
        ArrayList<JTextField> textFields = new ArrayList<>();

        textFields.add(serviceCostTextField);


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
        ArrayList<Pair<String, Double>> list = presenter.getTableAsArrayList(SERVICIOS);
        for (Pair<String, Double> pair : list) {
            serviceTypeComboBox.addItem(pair.getValue0());
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
        servicePrice = presenter.getIndividualPrice(SERVICIOS, getServiceTypeSelected());

        serviceCostTextField.setText(String.valueOf(servicePrice));
    }

    @Override
    public ArrayList<Attribute> getAttributes() {
        ArrayList<Attribute> attributes = new ArrayList<>();
        attributes.add(new Attribute("SERVICIO", serviceCostTextField.getText()));
        attributes.add(new Attribute("TIPO_SERVICIO", getServiceTypeSelected()));
        return attributes;
    }

    @Override
    public void comboBoxListenerSet(ItemListener listener) {
        serviceTypeComboBox.addItemListener(listener);
    }

    private String getServiceTypeSelected() {
        return (String) serviceTypeComboBox.getSelectedItem();
    }
}
