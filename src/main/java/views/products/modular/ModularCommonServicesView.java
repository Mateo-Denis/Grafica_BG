package views.products.modular;

import org.javatuples.Pair;
import org.javatuples.Triplet;
import presenters.product.ProductCreatePresenter;
import presenters.product.ProductPresenter;
import presenters.product.ProductSearchPresenter;
import utils.Attribute;
import utils.Product;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static utils.databases.SettingsTableNames.GENERAL;
import static utils.databases.SettingsTableNames.SERVICIOS;

public class ModularCommonServicesView extends JPanel implements IModularCategoryView {
    private JPanel containerPanel;
    private JPanel servicesComboBoxContainer;
    private JPanel serviceCostContainer;
    private JComboBox serviceTypeComboBox;
    private JTextField serviceCostTextField;
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
    private JPanel cupFinalPriceContainer;
    private JTextField cupFinalPriceTextField;
    private JTextField particularFinalPriceTField;
    private JLabel particularPriceLabel;
    private JLabel clientPriceLabel;
    private JPanel dollarPriceContainer;
    private JComboBox dollarComboBox;
    private JTextField dollarValueTextField;
    private double servicePrice;
    private boolean initialization;

    private final ProductCreatePresenter createPresenter;
    private final ProductSearchPresenter searchPresenter;
    private final ProductPresenter presenter;

    public ModularCommonServicesView(boolean isCreate, ProductPresenter presenter) {
        if (isCreate) {
            this.createPresenter = (ProductCreatePresenter) presenter;
            this.searchPresenter = null;
        } else {
            this.createPresenter = null;
            this.searchPresenter = (ProductSearchPresenter) presenter;
        }

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

            IVAcombobox.addItemListener(e -> {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    calculateDependantPrices();
                }
            });

            serviceTypeComboBox.addItemListener(e -> {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    SwingUtilities.invokeLater(this::calculateDependantPrices);
                }
            });

            dollarComboBox.addItemListener(e -> {
                if (e.getStateChange() == ItemEvent.SELECTED) {
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
        ArrayList<String> serviceList = presenter.getOtherTablesAsArrayList(SERVICIOS);

        for (String service : serviceList) {
            serviceTypeComboBox.addItem(service);
        }

        ArrayList<Pair<String, Double>> dollarList = presenter.getGeneralTableAsArrayList(GENERAL);
        for (Pair<String, Double> pair : dollarList) {
            String s = pair.getValue0();
            dollarComboBox.addItem(pair.getValue0());
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
        if (initialization) {
            return;
        }

        serviceCostTextField.setText(String.valueOf(servicePrice));
    }

    @Override
    public ArrayList<Attribute> getAttributes() {
        ArrayList<Attribute> attributes = new ArrayList<>();
        attributes.add(new Attribute("SERVICIO", serviceCostTextField.getText()));
        attributes.add(new Attribute("TIPO_SERVICIO", getServiceTypeSelected()));
        attributes.add(new Attribute("IVA", String.valueOf(IVAcombobox.getSelectedItem())));
        attributes.add(new Attribute("RECARGO", particularAddTextField.getText()));
        attributes.add(new Attribute("VALOR_TIPO_CAMBIO", "###"));
        attributes.add(new Attribute("TIPO_CAMBIO", (String) dollarComboBox.getSelectedItem()));
        return attributes;
    }

    @Override
    public void comboBoxListenerSet(ItemListener listener) {
        serviceTypeComboBox.addItemListener(listener);
    }

    @Override
    public void setSearchTextFields(Product product) {
        if (searchPresenter == null) {
            return;
        }
        Map<String, String> attributes = searchPresenter.getProductAttributes(product);
        serviceCostTextField.setText(attributes.get("SERVICIO"));
        serviceTypeComboBox.setSelectedItem(attributes.get("TIPO_SERVICIO"));
        IVAcombobox.setSelectedItem(attributes.get("IVA"));
        particularAddTextField.setText(attributes.get("RECARGO"));
        dollarValueTextField.setText(attributes.get("VALOR_TIPO_CAMBIO"));
        dollarComboBox.setSelectedItem(attributes.get("TIPO_CAMBIO"));


    }

    private String getServiceTypeSelected() {
        return (String) serviceTypeComboBox.getSelectedItem();
    }

}
