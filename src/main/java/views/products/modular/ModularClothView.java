package views.products.modular;

import org.javatuples.Pair;
import presenters.product.ProductCreatePresenter;

import javax.swing.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static utils.databases.SettingsTableNames.TELAS;

public class ModularClothView extends JPanel implements IModularCategoryView {
	private JPanel containerPanel;
	private JComboBox clothComboBox;
	private JLabel clothLabel;
	private ArrayList<String> radioValues = new ArrayList<>();
	private Map<String,String> comboBoxValues = new HashMap<>();
	private Map<String,String> textFieldValues = new HashMap<>();
	private ProductCreatePresenter presenter;
	public ModularClothView(ProductCreatePresenter presenter) {
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

	private String getClothComboBoxSelection() {
		return (String) clothComboBox.getSelectedItem();
	}

	@Override
	public double getPrice() {
		return presenter.calculatePrice("cloth");
	}

	@Override
	public ArrayList<String> getRelevantInformation() {
		ArrayList<String> relevantInformation = new ArrayList<>();
		relevantInformation.add(getClothComboBoxSelection());
		return relevantInformation;
	}

	@Override
	public void loadComboBoxValues() {
		ArrayList<Pair<String, Double>> list = presenter.getTableAsArrayList(TELAS);
		for (Pair<String, Double> pair : list) {
			clothComboBox.addItem(pair.getValue0());
		}
	}

	@Override
	public ArrayList<String> getExhaustiveInformation() {
		ArrayList<String> information = new ArrayList<>();

		information.add(getClothComboBoxSelection());

		return information;
	}
}
