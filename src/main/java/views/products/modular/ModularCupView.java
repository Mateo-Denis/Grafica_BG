package views.products.modular;

import org.javatuples.Triplet;
import presenters.product.ProductCreatePresenter;

import javax.swing.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
		return presenter.calculatePrice("cup");
	}

	@Override
	public ArrayList<String> getRelevantInformation() {
		ArrayList<String> relevantInformation = new ArrayList<>();
		relevantInformation.add(getCupMaterial());
		relevantInformation.add(isSublimated());
		return relevantInformation;
	}

	@Override
	public void loadComboBoxValues() {

	}

	@Override
	public void loadTextFieldsValues() {

	}

	@Override
	public ArrayList<String> getExhaustiveInformation() {
		ArrayList<String> information = new ArrayList<>();

		information.add(ceramicRadioButton.isSelected() ? "Si" : "No");
		information.add(plasticRadioButton.isSelected() ? "Si" : "No");
		information.add(whiteRadioButton.isSelected() ? "Si" : "No");
		information.add(sublimatedRadioButton.isSelected() ? "Si" : "No");

		return information;
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
