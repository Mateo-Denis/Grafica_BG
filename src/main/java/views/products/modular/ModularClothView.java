package views.products.modular;

import org.javatuples.Pair;
import org.javatuples.Triplet;
import presenters.product.ProductCreatePresenter;
import utils.MessageTypes;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static utils.databases.SettingsTableNames.*;

public class ModularClothView extends JPanel implements IModularCategoryView {
	private JPanel containerPanel;
	private JComboBox clothComboBox;
	private JPanel clothComboBoxContainer;
	private JPanel clothMetersContainer;
	private JPanel clothMetersPriceContainer;
	private JPanel profitContainer;
	private JPanel finalPriceContainer;
	private JLabel profitMultiplyLabel;
	private JLabel clothFinalPriceEqualsLabel;
	private JTextField clothMetersPriceTextField;
	private JTextField profitTextField;
	private JTextField clothFinalPriceTextField;
	private ArrayList<String> radioValues = new ArrayList<>();
	private Map<String,String> comboBoxValues = new HashMap<>();
	private Map<String,String> textFieldValues = new HashMap<>();
	private ProductCreatePresenter presenter;
	private double profit;
	private double clothMetersPrice;
	private boolean initialization;

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
		ArrayList<JTextField> textFields = new ArrayList<>();

		textFields.add(clothMetersPriceTextField);
		textFields.add(profitTextField);


		for (JTextField textField : textFields) {
			textField.getDocument().addDocumentListener(new DocumentListener() {
				@Override
				public void insertUpdate(DocumentEvent e) {
					calculateDependantPrices();
				}
				@Override
				public void removeUpdate(DocumentEvent e) {
					calculateDependantPrices();
				}
				@Override
				public void changedUpdate(DocumentEvent e) {
					calculateDependantPrices();
				}
			});
		}
	}

	@Override
	public void calculateDependantPrices() {
		try {
			float clothMeters = clothMetersPriceTextField.getText().isEmpty() ? 0 : Float.parseFloat(clothMetersPriceTextField.getText());
			float profit = profitTextField.getText().isEmpty() ? 0 : Float.parseFloat(profitTextField.getText());

			clothFinalPriceTextField.setText(String.valueOf(clothMeters * profit));
		} catch (NumberFormatException | NullPointerException e) {
			showMessage(MessageTypes.FLOAT_PARSING_ERROR, containerPanel);
		}
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

	private String getClothComboBoxSelection() {
		return (String) clothComboBox.getSelectedItem();
	}

	@Override
	public void loadComboBoxValues() {
		ArrayList<Pair<String, Double>> list = presenter.getTableAsArrayList(TELAS);
		for (Pair<String, Double> pair : list) {
			clothComboBox.addItem(pair.getValue0());
		}
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
		profit = presenter.getIndividualPrice(GANANCIAS, "Telas");
		clothMetersPrice = presenter.getIndividualPrice(TELAS, getClothComboBoxSelection());

		profitTextField.setText(String.valueOf(profit));
		clothMetersPriceTextField.setText(String.valueOf(clothMetersPrice));

	}

	@Override
	public ArrayList<String> getAttributes() {
		ArrayList<String> attributes = new ArrayList<>();
		attributes.add("T1A");
		attributes.add("GANANCIA");
		return attributes;
	}

}
