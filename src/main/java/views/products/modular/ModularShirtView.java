package views.products.modular;

import org.javatuples.Pair;
import presenters.product.ProductCreatePresenter;

import javax.swing.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static utils.databases.SettingsTableNames.TELAS;

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
		ButtonGroup shirtButtonGroup = new ButtonGroup();
		shirtButtonGroup.add(tshirtRadioButton);
		shirtButtonGroup.add(chombaRadioButton);
		initListeners();
	}
	@Override
	public void loadComboBoxValues() {
		ArrayList<Pair<String, Double>> list = presenter.getTableAsArrayList(TELAS);
		for (Pair<String, Double> pair : list) {
			materialComboBox.addItem(pair.getValue0());
		}
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


	private String getShirtMaterialSelected() {
		System.out.println(tshirtRadioButton.getModel().isSelected());

		if (tshirtRadioButton.isSelected())
			return "Remera";
		else if (chombaRadioButton.isSelected())
			return "Chomba";

		return null;
	}

	private String getShirtTypeSelected() {
		String shirt;
		if(tshirtRadioButton.isSelected()) {
			shirt = "Tela para remera manga corta";
		} else if(longSleeveRadioButton.isSelected()) {
			shirt = "Tela para remera manga larga";
		}else {
			shirt = "Tela para remera musculosa";
		}
		return shirt;
	}

	private String getMaterialSelected() {
		return (String) materialComboBox.getSelectedItem();
	}

	public ArrayList<String> getRelevantInformation() {
		ArrayList<String> relevantInformation = new ArrayList<>();
		relevantInformation.add(getShirtMaterialSelected());
		relevantInformation.add(getShirtTypeSelected());
		relevantInformation.add(getMaterialSelected());
		return relevantInformation;
	}

	public ArrayList<String> getExhaustiveInformation() {
		ArrayList<String> information = new ArrayList<>();

		information.add(tshirtRadioButton.isSelected() ? "Si" : "No");
		information.add(chombaRadioButton.isSelected() ? "Si" : "No");
		information.add(tankTopRadioButton.isSelected() ? "Si" : "No");
		information.add(shortSleeveRadioButton.isSelected() ? "Si" : "No");
		information.add(longSleeveRadioButton.isSelected() ? "Si" : "No");
		information.add(getShirtMaterialSelected());

		return information;
	}
}
