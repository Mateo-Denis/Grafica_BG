package views.products.modular;

import presenters.product.ProductCreatePresenter;

import javax.swing.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ModularJacketView extends JPanel implements IModularCategoryView {
	private JPanel containerPanel;
	private JRadioButton adultRadioButton;
	private JRadioButton kidRadioButton;
	private JRadioButton sportRadioButton;
	private JRadioButton cottonRadioButton;
	private JRadioButton kettenRadioButton;
	private ProductCreatePresenter productCreatePresenter;
	private ArrayList<String> radioValues = new ArrayList<>();
	private Map<String,String> comboBoxValues = new HashMap<>();
	private Map<String,String> textFieldValues = new HashMap<>();
	private ProductCreatePresenter presenter;
	public ModularJacketView(ProductCreatePresenter presenter) {
		this.presenter = presenter;
		initListeners();
	}
	@Override
	public JPanel getContainerPanel() {
		return containerPanel;
	}

	public void initListeners() {
		adultRadioButton.addActionListener(e -> productCreatePresenter.onAdultRadioButtonClicked());
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
		return presenter.calculatePrice("jacket");
	}

	@Override
	public ArrayList<String> getRelevantInformation() {
		ArrayList<String> relevantInformation = new ArrayList<>();
		relevantInformation.add(getJacketMaterialSelected());
		relevantInformation.add(getJacketSizeSelected());
		return relevantInformation;
	}

	private String getJacketMaterialSelected() {
		String jacket;
		if (cottonRadioButton.isSelected()) {
			jacket = "Campera Algodon";
		} else if (kettenRadioButton.isSelected()) {
			jacket = "Campera Ketten";
		} else {
			jacket = "Deportivo Frisado Sublimado";
		}

		return jacket;
	}

	private String getJacketSizeSelected() {
		String size;
		if (adultRadioButton.isSelected()) {
			size = "Campera Adulto";
		} else {
			size = "Campera Niño";
		}
		return size;
	}
}
