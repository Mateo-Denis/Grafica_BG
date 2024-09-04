package views.products.modular;

import presenters.product.ProductCreatePresenter;

import javax.swing.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ModularSweaterView extends JPanel implements IModularCategoryView {
	private JPanel containerPanel;
	private JRadioButton roundNeckRadioButton;
	private JRadioButton hoodieRadioButton;
	private JRadioButton adultRadioButton;
	private JRadioButton kidRadioButton;
	private JRadioButton sportRadioButton;
	private JRadioButton kettenRadioButton;
	private JRadioButton cottonRadioButton;
	private ArrayList<String> radioValues = new ArrayList<>();
	private Map<String,String> comboBoxValues = new HashMap<>();
	private Map<String,String> textFieldValues = new HashMap<>();
	private ProductCreatePresenter presenter;
	public ModularSweaterView(ProductCreatePresenter presenter) {
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
		return 0;
	}
}
