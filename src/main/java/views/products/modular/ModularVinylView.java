package views.products.modular;

import presenters.product.ProductCreatePresenter;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Map;

public class ModularVinylView extends JPanel implements IModularCategoryView {
	private JComboBox frostedComboBox;
	private JPanel containerPanel;
	private JLabel frostedLabel;
	private JRadioButton impresiónUVRadioButton;
	private JRadioButton impresiónEcosolventeRadioButton;
	private JComboBox baseComboBox;
	private JLabel baseLabel;
	private ProductCreatePresenter presenter;
	public ModularVinylView(ProductCreatePresenter presenter) {
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
		return null;
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
		return null;
	}

	@Override
	public double getPrice() {
		return 99999;
	}

	@Override
	public ArrayList<String> getRelevantInformation() {
		return new ArrayList<>();
	}

	@Override
	public void loadComboBoxValues() {

	}
}
