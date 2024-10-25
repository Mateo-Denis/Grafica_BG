package views.products.modular;

import org.javatuples.Triplet;
import presenters.product.ProductCreatePresenter;

import javax.swing.*;
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
	private JTextField textField6;
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
