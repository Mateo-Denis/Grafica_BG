package views.products.modular;

import org.javatuples.Triplet;
import presenters.product.ProductCreatePresenter;

import javax.swing.*;
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
    private JPanel materialSquareMetersTextFieldContainer;
    private JPanel materialSquareMetersPriceContainer;
    private JPanel materialSquareMetersFinalPriceContainer;
    private JLabel materialSquareMetersMultiplyLabel;
    private JLabel materialSquareMeters;
    private JTextField materialSquareMetersAmountTextField;
    private JTextField materialSquareMetersPriceTextField;
    private JTextField materialSquareMetersFinalPriceTextField;
    private JPanel inkBySquareMeterPriceContainer;
    private JPanel inkBySquareMeterFinalPriceContainer;
    private JLabel materialSquareMetersAmountLabel;
    private JLabel materialSquareMeterInkAmountLabel;
    private JLabel inkBySquareMeterMultiplyLabel;
    private JLabel inkBySquareMeterEqualsLabel;
    private JTextField inkBySquareMeterPriceTextField;
    private JTextField inkBySquareMeterFinalPriceTextField;
    private JPanel dolarPriceContainer;
    private JTextField profitTextField;
    private JPanel profitContainer;
    private JLabel dolarPriceMultiplyLabel;
    private JLabel profitMultiplyLabel;
    private JLabel finalPriceEqualsLabel;
    private JPanel squareMeterPrintingFinalPriceContainer;
    private JTextField squareMeterPrintingFinalPriceTextField;
    private JRadioButton UVRadioButton;
    private JRadioButton ecosolventeRadioButton;
    private JComboBox dollarComboBox;
    private JTextField textField1;
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
        materialMeterSqrPrice = presenter.getIndividualPrice(VINILOS, getMaterialComboBoxSelection());
        if(UVRadioButton.isSelected()){
            inkByMeterPrice = presenter.getIndividualPrice(GENERAL, "Tinta UV por metro2");
        } else {
            inkByMeterPrice = presenter.getIndividualPrice(GENERAL, "Tinta ECO por metro2");
        }
        profit = presenter.getIndividualPrice(GANANCIAS, "Impresión metro cuadrado");

        profitTextField.setText(String.valueOf(profit));
        materialSquareMetersPriceTextField.setText(String.valueOf(materialMeterSqrPrice));
        inkBySquareMeterPriceTextField.setText(String.valueOf(inkByMeterPrice));
    }
    private String getMaterialComboBoxSelection() {
        return (String) materialComboBox.getSelectedItem();
    }

}
