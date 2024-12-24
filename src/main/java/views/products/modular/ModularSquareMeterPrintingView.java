package views.products.modular;

import org.javatuples.Pair;
import org.javatuples.Triplet;
import presenters.product.ProductCreatePresenter;
import utils.Attribute;
import utils.MessageTypes;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static utils.databases.SettingsTableNames.*;

public class ModularSquareMeterPrintingView extends JPanel implements IModularCategoryView {
    private JPanel containerPanel;
    private JPanel leftSideComponentsContainer;
    private JPanel centerSideComponentsContainer;
    private JPanel materialPrincipalContainer;
    private JPanel materialSquareMetersContainer;
    private JPanel inkBySquareMeterContainer;
    private JComboBox materialComboBox;
    private JPanel materialSquareMetersPriceContainer;
    private JTextField materialSquareMetersPriceTextField;
    private JPanel inkBySquareMeterPriceContainer;
    private JTextField inkBySquareMeterPriceTextField;
    private JPanel dollarPriceContainer;
    private JTextField profitTextField;
    private JPanel profitContainer;
    private JLabel dollarPriceMultiplyLabel;
    private JLabel profitMultiplyLabel;
    private JLabel finalPriceEqualsLabel;
    private JPanel squareMeterPrintingFinalPriceContainer;
    private JTextField squareMeterPrintingFinalPriceTextField;
    private JRadioButton UVRadioButton;
    private JRadioButton ecosolventeRadioButton;
    private JComboBox dollarComboBox;
    private JTextField dollarValueTextField;
    private ProductCreatePresenter presenter;
    private double materialMeterSqrPrice;
    private double inkByMeterPrice;
    private double profit;


    public ModularSquareMeterPrintingView(ProductCreatePresenter presenter) {
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

        textFields.add(materialSquareMetersPriceTextField);
        textFields.add(inkBySquareMeterPriceTextField);
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

        UVRadioButton.addActionListener(e -> swapInk(true));
        ecosolventeRadioButton.addActionListener(e -> swapInk(false));
    }
    private void swapInk(boolean isUV){
        if(isUV){
            System.out.println("UV");
            inkByMeterPrice = presenter.getIndividualPrice(IMPRESIONES, "Metro2 de tinta UV");
        } else {
            System.out.println("Eco");
            inkByMeterPrice = presenter.getIndividualPrice(IMPRESIONES, "Metro2 de tinta ECO");
        }
        inkBySquareMeterPriceTextField.setText(String.valueOf(inkByMeterPrice));
    }

    @Override
    public void calculateDependantPrices() {
        try {

            float materialSquareMetersPrice = materialSquareMetersPriceTextField.getText().isEmpty() ? 0 : Float.parseFloat(materialSquareMetersPriceTextField.getText());
            float inkBySquareMeterPrice = inkBySquareMeterPriceTextField.getText().isEmpty() ? 0 : Float.parseFloat(inkBySquareMeterPriceTextField.getText());
            float dollarPrice = dollarComboBox.getSelectedItem() == null ? 0 : (float) presenter.getIndividualPrice(GENERAL, (String) dollarComboBox.getSelectedItem());
            float profit = profitTextField.getText().isEmpty() ? 0 : Float.parseFloat(profitTextField.getText());

            float finalPrice = (materialSquareMetersPrice + inkBySquareMeterPrice) * dollarPrice * profit;


            dollarValueTextField.setText(String.valueOf(dollarPrice));
            squareMeterPrintingFinalPriceTextField.setText(String.valueOf(finalPrice));

        } catch (NumberFormatException e) {
            showMessage(MessageTypes.ERROR_DEBUG, containerPanel);
        } catch (NullPointerException e) {
            showMessage(MessageTypes.DEBUG, containerPanel);
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
        ArrayList<Pair<String, Double>> materialLlist = presenter.getTableAsArrayList(MATERIALES);
        ArrayList<Pair<String, Double>> dollarList = presenter.getTableAsArrayList(GENERAL);
        for (Pair<String, Double> pair : materialLlist) {
            materialComboBox.addItem(pair.getValue0());
        }
        for (Pair<String, Double> pair : dollarList) {
            String s = pair.getValue0();
            if(s.contains("Dólar") || s.contains("Dolar")){
                dollarComboBox.addItem(pair.getValue0());
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

        squareMeterPrintingFinalPriceTextField.setText(String.valueOf(0));

        materialMeterSqrPrice = presenter.getIndividualPrice(MATERIALES, getMaterialComboBoxSelection());
        if(UVRadioButton.isSelected()){
            inkByMeterPrice = presenter.getIndividualPrice(IMPRESIONES, "Metro2 de tinta UV");
        } else {
            inkByMeterPrice = presenter.getIndividualPrice(IMPRESIONES, "Metro2 de tinta ECO");
        }
        profit = presenter.getIndividualPrice(GANANCIAS, "Impresión metro cuadrado");

        profitTextField.setText(String.valueOf(profit));
        materialSquareMetersPriceTextField.setText(String.valueOf(materialMeterSqrPrice));
        inkBySquareMeterPriceTextField.setText(String.valueOf(inkByMeterPrice));
    }

    @Override
    public ArrayList<Attribute> getAttributes() {
        ArrayList<Attribute> attributes = new ArrayList<>();
        attributes.add(new Attribute("T1A", materialSquareMetersPriceTextField.getText()));
        attributes.add(new Attribute("MATERIAL", getMaterialComboBoxSelection()));
        attributes.add(new Attribute("T2A", inkBySquareMeterPriceTextField.getText()));
        attributes.add(new Attribute("UV", UVRadioButton.isSelected() ? "SI" : "NO"));
        attributes.add(new Attribute("DOLAR", dollarValueTextField.getText()));
        attributes.add(new Attribute("TIPO_DOLAR", (String) dollarComboBox.getSelectedItem()));
        attributes.add(new Attribute("GANANCIA", profitTextField.getText()));
        return attributes;
    }

    @Override
    public void comboBoxListenerSet(ItemListener listener) {
        materialComboBox.addItemListener(listener);
        dollarComboBox.addItemListener(listener);
    }

    private String getMaterialComboBoxSelection() {
        return (String) materialComboBox.getSelectedItem();
    }

}
