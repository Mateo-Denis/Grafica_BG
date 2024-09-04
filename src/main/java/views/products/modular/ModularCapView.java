package views.products.modular;

import presenters.product.ProductCreatePresenter;

import javax.swing.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ModularCapView extends JPanel implements IModularCategoryView {
    private JPanel containerPanel;
    private JRadioButton whiteFrontRadioButton;
    private JRadioButton sublimatedRadioButton;
    private JRadioButton visorStampRadioButton;
    private ArrayList<String> radioValues = new ArrayList<>();
    private Map<String,String> comboBoxValues = new HashMap<>();
    private Map<String,String> textFieldValues = new HashMap<>();
    private ProductCreatePresenter presenter;
    public ModularCapView(ProductCreatePresenter presenter) {
        this.presenter = presenter;
        sublimatedRadioButton.setSelected(true);
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
        double finalPrice = 0;
        if(sublimatedRadioButton.isSelected()){
            finalPrice += 10;
        }else {
            finalPrice += 5;
        }
        if(visorStampRadioButton.isSelected()){
            finalPrice += 7;
        }

        return finalPrice;
    }

    //@Override
    public void initListeners() {
        whiteFrontRadioButton.addActionListener(e -> {
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
        });
    }
}

