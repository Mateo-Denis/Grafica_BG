package views.products.modular;

import org.javatuples.Triplet;
import presenters.product.ProductCreatePresenter;

import javax.swing.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ModularCapView extends JPanel implements IModularCategoryView {
    private JPanel containerPanel;
    private JPanel leftSideComponentsContainer;
    private JPanel capPriceContainer;
    private JTextField capPriceTextField;
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
    private double plankLoweringPrice;
    private double printingMetersPrice;
    private ProductCreatePresenter presenter;
    public ModularCapView(ProductCreatePresenter presenter) {
        this.presenter = presenter;
        initListeners();
    }

    public void loadTextFieldsValues() {
        plankLoweringPrice = presenter.getPlankLoweringPrice("Gorra");
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
    public Map<String,String> getModularAttributes() {
        Map<String,String> attributes = new HashMap<>();

        for(Map.Entry<String,String> entry : comboBoxValues.entrySet()){
            attributes.put(entry.getKey(), entry.getValue());
        }

        for(Map.Entry<String,String> entry : textFieldValues.entrySet()){
            attributes.put(entry.getKey(), entry.getValue());
        }

        for(String value : radioValues){
            attributes.put(" ", value);
        }

        return attributes;
    }

    @Override
    public double getPrice() {
        /*return presenter.calculatePrice("cap");*/
        return 0.0;
    }

    @Override
    public ArrayList<String> getRelevantInformation() {
        ArrayList<String> relevantInformation = new ArrayList<>();
        relevantInformation.add(getSelectedRadioButton());
        relevantInformation.add(hasVisorStamp());
        return relevantInformation;
    }

    @Override
    public void loadComboBoxValues() {

    }

    @Override
    public ArrayList<String> getExhaustiveInformation() {
/*        ArrayList<String> information = new ArrayList<>();

        information.add(whiteFrontRadioButton.isSelected() ? "Si" : "No");
        information.add(sublimatedRadioButton.isSelected() ? "Si" : "No");
        information.add(visorStampRadioButton.isSelected() ? "Si" : "No");

        return information;*/
        return new ArrayList<String>();
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

    private String hasVisorStamp(){
/*        if(visorStampRadioButton.isSelected()){
            return "Gorra con estampa en el visor";
        } else {
            return "";
        }*/
        return "";
    }

    private String getSelectedRadioButton(){
/*        if(whiteFrontRadioButton.isSelected()){
            return "Gorra Blanca";
        } else if(sublimatedRadioButton.isSelected()){
            return "Gorra Sublimada";
        } else {
            return "";
        }*/
        return "";

    }

    //@Override
    public void initListeners() {
/*        whiteFrontRadioButton.addActionListener(e -> {
            System.out.println(whiteFrontRadioButton.isSelected());
            presenter.onModularOptionsClicked(getPrice());
        });

        sublimatedRadioButton.addActionListener(e -> {
            System.out.println(sublimatedRadioButton.isSelected());
            presenter.onModularOptionsClicked(getPrice());
        });

        visorStampRadioButton.addActionListener(e -> {
            System.out.println(visorStampRadioButton.isSelected());
            presenter.onModularOptionsClicked(getPrice());
        });*/
    }
}

