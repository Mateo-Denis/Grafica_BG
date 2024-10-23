package views.products.modular;

import org.javatuples.Pair;
import org.javatuples.Triplet;
import presenters.product.ProductCreatePresenter;

import javax.swing.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static utils.databases.SettingsTableNames.*;

public class ModularClothesView extends JPanel implements IModularCategoryView {
	private JRadioButton tshirtRadioButton;
	private JRadioButton chombaRadioButton;
	private JComboBox materialComboBox;
	private JRadioButton shortSleeveRadioButton;
	private JRadioButton longSleeveRadioButton;
	private JPanel containerPanel;
	private JRadioButton tankTopRadioButton;
    private JTextField printingMetersPriceTextField;
	private JTextField clothMetersAmountTextField;
	private JTextField profitTextField;
	private JTextField printingMetersAmountTextField;
	private JPanel materialContainerPanel;
	private JPanel printingMetersContainer;
	private JPanel printingMetersAmountContainer;
	private JPanel printingMetersPriceContainer;
	private JLabel printingMultiplyLabel;
	private JLabel printingEqualsLabel;
	private JPanel clothMetersContainer;
	private JPanel printingMetersFinalPriceContainer;
	private JTextField printingMetersFinalPriceTextField;
	private JPanel clothMetersAmountContainer;
	private JPanel clothMetersPriceContainer;
	private JTextField clothMetersPriceTextField;
	private JLabel clothMultiplyLabel;
	private JLabel clothEqualsLabel;
	private JPanel clothMetersFinalPriceContainer;
	private JTextField clothMetersFinalPriceTextField;
	private JPanel plankLoweringContainer;
	private JPanel plankLoweringAmountContainer;
	private JPanel plankLoweringPriceContainer;
	private JPanel plankLoweringFinalPriceContainer;
	private JLabel plankLoweringMultiplyLabel;
	private JLabel plankLoweringEqualsLabel;
	private JPanel otherFieldsContainer;
	private JPanel seamstressPriceContainer;
	private JTextField seamstressPriceTextField;
	private JPanel zipperContainer;
	private JTextField zipperPriceTextField;
	private JCheckBox zipperAddingCheckBox;
	private JLabel otherFieldsAddingLabel;
	private JLabel otherFieldsEqualsLabel;
	private JTextField plankLoweringAmountTextField;
	private JTextField plankLoweringPriceTextField;
	private JTextField plankLoweringFinalPriceTextField;
	private JPanel otherFieldsFinalPriceContainer;
	private JTextField otherFieldsFinalPriceTextField;
	private JPanel profitContainer;
	private JLabel profitMultiplyLabel;
	private JPanel finalPriceContainer;
	private JLabel finalPriceEqualsLabel;
	private JTextField finalPriceTextField;
	private JLabel shirt;
	private JCheckBox editPriceCheckBox;
	private ArrayList<String> radioValues = new ArrayList<>();
	private Map<String,String> comboBoxValues = new HashMap<>();
	private Map<String,String> textFieldValues = new HashMap<>();
	private ProductCreatePresenter presenter;
	private double profit;
	private double printingMetersPrice;
	private double plankLoweringPrice;
	private double clothMetersPrice;
	private double seamstressPrice;
	private double zipperPrice;


	public ModularClothesView(ProductCreatePresenter presenter) {
		this.presenter = presenter;
//		ButtonGroup shirtButtonGroup = new ButtonGroup();
//		shirtButtonGroup.add(tshirtRadioButton);
//		shirtButtonGroup.add(chombaRadioButton);

		initListeners();
	}
	@Override
	public void loadComboBoxValues() {
		ArrayList<Pair<String, Double>> list = presenter.getTableAsArrayList(TELAS);
		for (Pair<String, Double> pair : list) {
			materialComboBox.addItem(pair.getValue0());
		}
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
		return 0.0;
	}


	private String getShirtMaterialSelected() {
//		System.out.println(tshirtRadioButton.getModel().isSelected());
//
//		if (tshirtRadioButton.isSelected())
//			return "Remera";
//		else if (chombaRadioButton.isSelected())
//			return "Chomba";

		return "null";
	}

	private String getShirtTypeSelected() {
		String shirt = "null";
//		if(tshirtRadioButton.isSelected()) {
//			shirt = "Tela para remera manga corta";
//		} else if(longSleeveRadioButton.isSelected()) {
//			shirt = "Tela para remera manga larga";
//		}else {
//			shirt = "Tela para remera musculosa";
//		}
		return shirt;
	}

	private String getMaterialSelected() {
		return (String) materialComboBox.getSelectedItem();
	}

	public ArrayList<String> getRelevantInformation() {
		ArrayList<String> relevantInformation = new ArrayList<>();
		relevantInformation.add(getShirtMaterialSelected());
		relevantInformation.add(getShirtTypeSelected());
		relevantInformation.add(getMaterialSelected());
		return relevantInformation;
	}

	public ArrayList<String> getExhaustiveInformation() {
		ArrayList<String> information = new ArrayList<>();

/*		information.add(tshirtRadioButton.isSelected() ? "Si" : "No");
		information.add(chombaRadioButton.isSelected() ? "Si" : "No");
		information.add(tankTopRadioButton.isSelected() ? "Si" : "No");
		information.add(shortSleeveRadioButton.isSelected() ? "Si" : "No");
		information.add(longSleeveRadioButton.isSelected() ? "Si" : "No");
		information.add(getShirtMaterialSelected());*/

		return information;
	}

	@Override
	public void setPriceTextFields() {
		profit = presenter.getIndividualPrice(GANANCIAS, "Prendas");
		printingMetersPrice = presenter.getIndividualPrice(IMPRESIONES, "Sublimación");
		plankLoweringPrice = presenter.getIndividualPrice(BAJADA_PLANCHA, "En prenda");
		clothMetersPrice = presenter.getIndividualPrice(TELAS, getMaterialSelected());
		seamstressPrice = presenter.getIndividualPrice(SERVICIOS, "Costurera remera");
		zipperPrice = presenter.getIndividualPrice(SERVICIOS, "Cierre remera");

		profitTextField.setText(String.valueOf(profit));
		printingMetersPriceTextField.setText(String.valueOf(printingMetersPrice));
		clothMetersPriceTextField.setText(String.valueOf(clothMetersPrice));
		plankLoweringPriceTextField.setText(String.valueOf(plankLoweringPrice));
		seamstressPriceTextField.setText(String.valueOf(seamstressPrice));
		zipperPriceTextField.setText(String.valueOf(zipperPrice));
	}

	@Override
	public void blockTextFields() {
		clothMetersAmountTextField.setEnabled(false);
		printingMetersPriceTextField.setEnabled(false);
		profitTextField.setEnabled(false);
		plankLoweringPriceTextField.setEnabled(false);
		seamstressPriceTextField.setEnabled(false);
		zipperPriceTextField.setEnabled(false);
	}

	@Override
	public void unlockTextFields() {
		clothMetersAmountTextField.setEnabled(true);
		printingMetersPriceTextField.setEnabled(true);
		profitTextField.setEnabled(true);
		plankLoweringPriceTextField.setEnabled(true);
		seamstressPriceTextField.setEnabled(true);
		zipperPriceTextField.setEnabled(true);
	}

	@Override
	public List<Triplet<String, String, Double>> getModularPrices() {
		double actualMetersPrice = clothMetersPriceTextField.getText().isEmpty() ? 0 : Double.parseDouble(clothMetersAmountTextField.getText());
		double actualPrintingPrice = printingMetersPriceTextField.getText().isEmpty() ? 0 : Double.parseDouble(printingMetersPriceTextField.getText());
		double actualProfit = profitTextField.getText().isEmpty() ? 0 : Double.parseDouble(profitTextField.getText());
		double actualSeamstressPrice = seamstressPriceTextField.getText().isEmpty() ? 0 : Double.parseDouble(seamstressPriceTextField.getText());
		double actualZipperPrice = zipperPriceTextField.getText().isEmpty() ? 0 : Double.parseDouble(zipperPriceTextField.getText());
		double actualPlankLoweringPrice = plankLoweringPriceTextField.getText().isEmpty() ? 0 : Double.parseDouble(plankLoweringPriceTextField.getText());
		String actualClothSelected = (String) materialComboBox.getSelectedItem();

		List<Triplet<String, String, Double>> modularPrices = new ArrayList<>();

		modularPrices.add(new Triplet<>("TELAS", actualClothSelected, actualMetersPrice));
		modularPrices.add(new Triplet<>("IMPRESIONES", "En sublimación", actualPrintingPrice));
		modularPrices.add(new Triplet<>("GANANCIAS", "Prendas", actualProfit));
		modularPrices.add(new Triplet<>("SERVICIOS", "Costurera", actualSeamstressPrice));
		modularPrices.add(new Triplet<>("SERVICIOS", "Cierre", actualZipperPrice));
		modularPrices.add(new Triplet<>("SERVICIOS", "Bajada de plancha", actualPlankLoweringPrice));

		return modularPrices;
	}
}
