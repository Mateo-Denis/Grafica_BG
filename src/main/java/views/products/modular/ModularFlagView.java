package views.products.modular;

import lombok.Getter;
import presenters.product.ProductCreatePresenter;

import javax.swing.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

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

	public String getFlagComboBoxSelection() {
		return (String) clothComboBox.getSelectedItem();
	}

	public String getSizeComboBoxSelection() {
		return (String) sizeComboBox.getSelectedItem();
	}
}
