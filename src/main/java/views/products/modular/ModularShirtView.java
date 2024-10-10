package views.products.modular;

import presenters.product.ProductCreatePresenter;

import javax.swing.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ModularShirtView extends JPanel implements IModularCategoryView {
	private JRadioButton tshirtRadioButton;
	private JRadioButton chombaRadioButton;
	private JComboBox materialComboBox;
	private JLabel materialLabel;
	private JRadioButton shortSleeveRadioButton;
	private JRadioButton longSleeveRadioButton;
	private JPanel containerPanel;
	private JRadioButton tankTopRadioButton;
	private ArrayList<String> radioValues = new ArrayList<>();
	private Map<String,String> comboBoxValues = new HashMap<>();
	private Map<String,String> textFieldValues = new HashMap<>();
	private ProductCreatePresenter presenter;
	public ModularShirtView(ProductCreatePresenter presenter) {
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
		return presenter.calculatePrice("shirt");
	}


	public String getShirtMaterialSelected() {
		System.out.println(tshirtRadioButton.isSelected());



		if(chombaRadioButton.isSelected()) {
			return "Chomba";
		} else if (tshirtRadioButton.isSelected()) {
			return "Remera";
		}else {
			return (String) materialComboBox.getSelectedItem();
		}

	}

	public String getShirtTypeSelected() {
		String shirt;
		if(tshirtRadioButton.isSelected()) {
			shirt = "Tela para manga corta";
		} else if(longSleeveRadioButton.isSelected()) {
			shirt = "Tela para manga larga";
		}else {
			shirt = "Tela para musculosa";
		}
		return shirt;
	}

	public String getMaterialSelected() {
		return (String) materialComboBox.getSelectedItem();
	}
}
