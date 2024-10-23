package views.products.modular;

import org.javatuples.Pair;
import org.javatuples.Triplet;
import presenters.product.ProductCreatePresenter;

import javax.swing.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static utils.databases.SettingsTableNames.MEDIDAS;

public class ModularPrintingView extends JPanel implements IModularCategoryView{
	private JPanel containerPanel;
	private JComboBox sizeComboBox;
	private Map<String,String> comboBoxValues = new HashMap<>();
	private JLabel sizeLabel;
	private ProductCreatePresenter presenter;
	public ModularPrintingView(ProductCreatePresenter presenter) {
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
		return comboBoxValues;
	}

	@Override
	public Map<String, String> getTextFieldValues() {
		return null;
	}

	@Override
	public ArrayList<String> getRadioValues() {
		return null;
	}

	@Override
	public Map<String, String> getModularAttributes() {
		Map<String,String> attributes = new HashMap<>();

		for(Map.Entry<String,String> entry : comboBoxValues.entrySet()){
			attributes.put(entry.getKey(), entry.getValue());
		}

		return attributes;
	}

	@Override
	public double getPrice() {
		return presenter.calculatePrice("printing");
	}

	@Override
	public ArrayList<String> getRelevantInformation() {
		ArrayList<String> relevantInformation = new ArrayList<>();
		relevantInformation.add(getPrintingComboBoxSelection());
		return relevantInformation;
	}

	@Override
	public void loadComboBoxValues() {
		ArrayList<Pair<String, Double>> list = presenter.getTableAsArrayList(MEDIDAS);
		for (Pair<String, Double> pair : list) {
			sizeComboBox.addItem(pair.getValue0());
		}
	}

	@Override
	public void loadTextFieldsValues() {

	}

	@Override
	public ArrayList<String> getExhaustiveInformation() {
		ArrayList<String> information = new ArrayList<>();

		information.add(getPrintingComboBoxSelection());

		return information;

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

	private String getPrintingComboBoxSelection() {
		return (String) sizeComboBox.getSelectedItem();
	}
}
