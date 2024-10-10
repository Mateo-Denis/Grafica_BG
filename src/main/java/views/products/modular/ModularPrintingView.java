package views.products.modular;

import presenters.product.ProductCreatePresenter;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Map;

public class ModularPrintingView extends JPanel implements IModularCategoryView{
	private JPanel containerPanel;
	private JComboBox sizeComboBox;
	private JLabel sizeLabel;
	private ProductCreatePresenter presenter;
	public ModularPrintingView(ProductCreatePresenter presenter) {
		this.presenter = presenter;
		initListeners();
	}
	@Override
	public JPanel getContainerPanel() {
		return null;
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
		return presenter.calculatePrice("printing");
	}

	public String getPrintingComboBoxSelection() {
		return (String) sizeComboBox.getSelectedItem();
	}
}
