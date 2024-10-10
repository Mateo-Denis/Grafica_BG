package views.products.modular;

import org.javatuples.Pair;
import presenters.product.ProductCreatePresenter;

import javax.swing.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static utils.databases.SettingsTableNames.MEDIDAS;
import static utils.databases.SettingsTableNames.TELAS;

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

	private String getPrintingComboBoxSelection() {
		return (String) sizeComboBox.getSelectedItem();
	}
}
