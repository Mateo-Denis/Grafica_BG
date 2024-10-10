package views.products.modular;

import lombok.Getter;
import org.javatuples.Pair;
import presenters.product.ProductCreatePresenter;

import javax.swing.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static utils.databases.SettingsTableNames.MEDIDAS;
import static utils.databases.SettingsTableNames.TELAS;

public class ModularFlagView extends JPanel implements IModularCategoryView {
	private JPanel containerPanel;
	private JComboBox clothComboBox;
	private JLabel clothLabel;
	private JComboBox sizeComboBox;
	private JLabel sizeLabel;
	@Getter
	private ArrayList<String> radioValues = new ArrayList<>();
	@Getter
	private Map<String,String> comboBoxValues = new HashMap<>();
	@Getter
	private Map<String,String> textFieldValues = new HashMap<>();
	private ProductCreatePresenter presenter;
	public ModularFlagView(ProductCreatePresenter presenter) {
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
		return presenter.calculatePrice("flag");
	}

	@Override
	public ArrayList<String> getRelevantInformation() {
		ArrayList<String> relevantInformation = new ArrayList<>();
		relevantInformation.add(getFlagComboBoxSelection());
		relevantInformation.add(getSizeComboBoxSelection());
		return relevantInformation;
	}

	@Override
	public void loadComboBoxValues() {
		ArrayList<Pair<String, Double>> clothList = presenter.getTableAsArrayList(TELAS);
		for (Pair<String, Double> pair : clothList) {
			clothComboBox.addItem(pair.getValue0());
		}

		ArrayList<Pair<String, Double>> sizeList = presenter.getTableAsArrayList(MEDIDAS);
		for (Pair<String, Double> pair : sizeList) {
			sizeComboBox.addItem(pair.getValue0());
		}
	}

	@Override
	public ArrayList<String> getExhaustiveInformation() {
		ArrayList<String> information = new ArrayList<>();

		information.add(getFlagComboBoxSelection());
		information.add(getSizeComboBoxSelection());

		return information;
	}

	private String getFlagComboBoxSelection() {
		return (String) clothComboBox.getSelectedItem();
	}

	private String getSizeComboBoxSelection() {
		return (String) sizeComboBox.getSelectedItem();
	}
}
