package views.products.modular;

import lombok.Getter;
import org.javatuples.Pair;
import org.javatuples.Triplet;
import presenters.product.ProductCreatePresenter;
import utils.MessageTypes;

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
	@Getter
	private ArrayList<String> radioValues = new ArrayList<>();
	@Getter
	private Map<String,String> comboBoxValues = new HashMap<>();
	@Getter
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

	@Override
	public void calculateDependantPrices() {
		try {
			float clothMetersAmount = Float.parseFloat(clothMetersAmountTextField.getText());
			float printingMetersAmount = Float.parseFloat(printingMetersAmountTextField.getText());
			float plankLoweringAmount = Float.parseFloat(plankLoweringAmountTextField.getText());
			float seamstressCost = Float.parseFloat(seamstressPriceTextField.getText());
			float clothMetersPrice = Float.parseFloat(clothMetersPriceTextField.getText());
			float printingMetersPrice = Float.parseFloat(printingMetersPriceTextField.getText());
			float plankLoweringPrice = Float.parseFloat(plankLoweringPriceTextField.getText());

			float zipperCost = 0;
			if(zipperAddingCheckBox.isSelected()) {
				zipperCost = Float.parseFloat(zipperPriceTextField.getText());
			}


			float clothMetersFinalPrice = clothMetersAmount * clothMetersPrice;
			float printingMetersFinalPrice = printingMetersAmount * printingMetersPrice;
			float plankLoweringFinalPrice = plankLoweringAmount * plankLoweringPrice;
			float profit = Float.parseFloat(profitTextField.getText());
			if(zipperAddingCheckBox.isSelected()) {
				seamstressCost = seamstressCost + zipperCost;
			}

			clothMetersFinalPriceTextField.setText(String.valueOf(clothMetersFinalPrice));
			printingMetersFinalPriceTextField.setText(String.valueOf(printingMetersFinalPrice));
			plankLoweringFinalPriceTextField.setText(String.valueOf(plankLoweringFinalPrice));
			seamstressPriceTextField.setText(String.valueOf(seamstressCost));
			finalPriceTextField.setText(String.valueOf((clothMetersFinalPrice + printingMetersFinalPrice +
					plankLoweringFinalPrice + seamstressCost) * profit));

		} catch (NumberFormatException | NullPointerException e) {
			showMessage(MessageTypes.FLOAT_PARSING_ERROR, containerPanel);
		}


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
