package views.products.modular;

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

public class ModularCupView extends JPanel implements IModularCategoryView  {
	private JPanel containerPanel;
	private JPanel leftSideComponentsContainer;
	private JPanel centerSideComponentsContainer;
	private JPanel rightSideComponentsContainer;
	private JPanel cupPriceContainer;
	private JPanel plankLoweringContainer;
	private JPanel printingContainer;
	private JTextField cupPriceTextField;
	private JPanel plankLoweringAmountContainer;
	private JPanel plankLoweringPriceContainer;
	private JPanel plankLoweringFinalPriceContainer;
	private JLabel plankLoweringMultiplyLabel;
	private JLabel plankLoweringEqualsLabel;
	private JPanel printingMetersAmountContainer;
	private JPanel printingMetersPriceContainer;
	private JPanel printingMetersFinalPriceContainer;
	private JPanel profitContainer;
	private JTextField profitTextField;
	private JLabel profitMultiplyLabel;
	private JLabel cupFinalPriceEqualsLabel;
	private JTextField cupFinalPriceTextField;
	private JPanel cupFinalPriceContainer;
	private JTextField plankLoweringAmountTextField;
	private JTextField plankLoweringPriceTextField;
	private JTextField plankLoweringFinalPriceTextField;
	private JTextField printingMetersAmountTextField;
	private JTextField printingMetersPriceTextField;
	private JTextField printingMetersFinalPriceTextField;
	private JLabel printingMultiplyLabel;
	private JLabel printingEqualsLabel;
	private JRadioButton ceramicRadioButton;
	private JRadioButton plasticRadioButton;
	private JRadioButton whiteRadioButton;
	private JRadioButton sublimatedRadioButton;
	private ArrayList<String> radioValues = new ArrayList<>();
	private Map<String,String> comboBoxValues = new HashMap<>();
	private Map<String,String> textFieldValues = new HashMap<>();
	private ProductCreatePresenter presenter;
	private double cupPrice;
	private double plankLoweringPrice;
	private double printingMetersPrice;
	private double profit;
	private boolean initialization;

	public ModularCupView(ProductCreatePresenter presenter) {
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

		textFields.add(cupPriceTextField);
		textFields.add(plankLoweringAmountTextField);
		textFields.add(plankLoweringPriceTextField);
		textFields.add(printingMetersAmountTextField);
		textFields.add(printingMetersPriceTextField);
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
			int plankLoweringAmount = plankLoweringAmountTextField.getText().isEmpty() ? 0 : Integer.parseInt(plankLoweringAmountTextField.getText());
			try {
				float printingMetersAmount = printingMetersAmountTextField.getText().isEmpty() ? 0 : Float.parseFloat(printingMetersAmountTextField.getText());
				float profit = profitTextField.getText().isEmpty() ? 0 : Float.parseFloat(profitTextField.getText());
				float cupPrice = cupPriceTextField.getText().isEmpty() ? 0 : Float.parseFloat(cupPriceTextField.getText());
				float plankLoweringPrice = plankLoweringPriceTextField.getText().isEmpty() ? 0 : Float.parseFloat(plankLoweringPriceTextField.getText());
				float printingMetersPrice = printingMetersPriceTextField.getText().isEmpty() ? 0 : Float.parseFloat(printingMetersPriceTextField.getText());

				float plankLoweringFinalPrice = plankLoweringAmount * plankLoweringPrice;
				float printingMetersFinalPrice = printingMetersAmount * printingMetersPrice;
				float cupFinalPrice = (cupPrice + plankLoweringFinalPrice + printingMetersFinalPrice) * profit;

				plankLoweringFinalPriceTextField.setText(String.valueOf(plankLoweringFinalPrice));
				printingMetersFinalPriceTextField.setText(String.valueOf(printingMetersFinalPrice));
				cupFinalPriceTextField.setText(String.valueOf(cupFinalPrice));

			} catch (NumberFormatException | NullPointerException e) {
				showMessage(MessageTypes.FLOAT_PARSING_ERROR, containerPanel);
			}
		} catch (NumberFormatException | NullPointerException e) {
			showMessage(MessageTypes.INT_PARSING_ERROR, containerPanel);
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

	@Override
	public void loadComboBoxValues() {

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
		profit = presenter.getIndividualPrice(GANANCIAS, "Taza");
		cupPrice = presenter.getIndividualPrice(GENERAL, "Taza");
		plankLoweringPrice = presenter.getIndividualPrice(GENERAL, "En taza");
		printingMetersPrice = presenter.getIndividualPrice(GENERAL, "Sublimación");

		printingMetersPriceTextField.setText(String.valueOf(printingMetersPrice));
		plankLoweringPriceTextField.setText(String.valueOf(plankLoweringPrice));
		cupPriceTextField.setText(String.valueOf(cupPrice));
		profitTextField.setText(String.valueOf(profit));

	}

	@Override
	public ArrayList<String> getAttributes() {
		ArrayList<String> attributes = new ArrayList<>();
		attributes.add("T1A");
		attributes.add("T1B");
		attributes.add("T2A");
		attributes.add("T2B");
		attributes.add("TAZA");
		attributes.add("GANANCIA");
		return attributes;
	}

	private String getCupMaterial() {
		if(ceramicRadioButton.isSelected()){
			return "Taza Ceramica";
		} else if(plasticRadioButton.isSelected()){
			return "Taza Plastico";
		}
		return "unreachable";
	}

	private String isSublimated() {
		return sublimatedRadioButton.isSelected() ? "Sublimado" : "Blanco";
	}

}
