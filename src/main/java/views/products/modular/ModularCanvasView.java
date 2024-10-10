package views.products.modular;

import org.javatuples.Pair;
import presenters.product.ProductCreatePresenter;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Map;

import static utils.databases.SettingsTableNames.LONAS;

public class ModularCanvasView extends JPanel implements IModularCategoryView{
	private ProductCreatePresenter presenter;
	private JPanel containerPanel;
	private JComboBox canvasComboBox;

	public ModularCanvasView(ProductCreatePresenter presenter) {
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
		return presenter.calculatePrice("canvas");
	}

	@Override
	public ArrayList<String> getRelevantInformation() {
		ArrayList<String> relevantInformation = new ArrayList<>();
		relevantInformation.add(getCanvasComboBoxSelection());
		return relevantInformation;
	}

	@Override
	public void loadComboBoxValues() {
		ArrayList<Pair<String, Double>> list = presenter.getTableAsArrayList(LONAS);
		for (Pair<String, Double> pair : list) {
			canvasComboBox.addItem(pair.getValue0());
		}
	}

	private String getCanvasComboBoxSelection() {
		return (String) canvasComboBox.getSelectedItem();
	}
}
