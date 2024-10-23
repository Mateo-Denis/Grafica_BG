package views.products.modular;

import org.javatuples.Triplet;
import presenters.product.ProductCreatePresenter;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;
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

	@Override
	public ArrayList<String> getExhaustiveInformation() {
		return new ArrayList<>();
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
}
