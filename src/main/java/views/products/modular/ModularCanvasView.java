package views.products.modular;

import presenters.product.ProductCreatePresenter;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Map;

public class ModularCanvasView extends JPanel implements IModularCategoryView{
	private ProductCreatePresenter presenter;
	private JPanel panel1;
	private JComboBox canvasComboBox;

	public ModularCanvasView(ProductCreatePresenter presenter) {
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
		return presenter.calculatePrice("canvas");
	}

	public String getCanvasComboBoxSelection() {
		return (String) canvasComboBox.getSelectedItem();
	}
}
