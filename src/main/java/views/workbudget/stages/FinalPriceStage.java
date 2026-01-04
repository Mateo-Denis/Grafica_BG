package views.workbudget.stages;

import utils.NumberInputVerifier;

import javax.swing.*;
import javax.swing.text.AbstractDocument;
import java.awt.*;

public class FinalPriceStage extends JPanel{
	private JPanel finalPriceContainer;
	private JPanel costContainer;
	private JPanel paymentContainer;
	private JPanel finalBudgetContainer;
	private JLabel costLabel;
	private JTextField profitTextField;
	private JTextField finalPriceTextField;
	private JTextField depositTextField;
	private JTextField balanceTextField;
	private JTextField materialsTextField;
	private JTextField totalCostsTextField;
	private JTextField logisticsTextField;
	private JTextField placingTextField;

	public FinalPriceStage(){
		profitTextField.setText("100");
		((AbstractDocument) profitTextField.getDocument()).setDocumentFilter(new NumberInputVerifier());
		((AbstractDocument) depositTextField.getDocument()).setDocumentFilter(new NumberInputVerifier());
		((AbstractDocument) balanceTextField.getDocument()).setDocumentFilter(new NumberInputVerifier());
	}

	public JTextField getTextFieldByName(FinalPriceReferences name) {
		return switch (name) {
			case MATERIALS_COST -> materialsTextField;
			case LOGISTICS_COST -> logisticsTextField;
			case PLACING_COST -> placingTextField;
			case TOTAL_COSTS -> totalCostsTextField;
			case PROFIT_MARGIN -> profitTextField;
			case FINAL_PRICE -> finalPriceTextField;
			case DEPOSIT -> depositTextField;
			case BALANCE_TO_PAY -> balanceTextField;
			default -> null;
		};
	}

	public String getTextContentByName(FinalPriceReferences name) {
		return switch (name) {
			case MATERIALS_COST -> materialsTextField.getText();
			case LOGISTICS_COST -> logisticsTextField.getText();
			case PLACING_COST -> placingTextField.getText();
			case TOTAL_COSTS -> totalCostsTextField.getText();
			case PROFIT_MARGIN -> profitTextField.getText();
			case FINAL_PRICE -> finalPriceTextField.getText();
			case DEPOSIT -> depositTextField.getText();
			case BALANCE_TO_PAY -> balanceTextField.getText();
			default -> null;
		};
	}

	public void setTextContentByName(FinalPriceReferences name, String value) {
		switch (name) {
			case MATERIALS_COST -> materialsTextField.setText(value);
			case LOGISTICS_COST -> logisticsTextField.setText(value);
			case PLACING_COST -> placingTextField.setText(value);
			case TOTAL_COSTS -> totalCostsTextField.setText(value);
			case PROFIT_MARGIN -> profitTextField.setText(value);
			case FINAL_PRICE -> finalPriceTextField.setText(value);
			case DEPOSIT -> depositTextField.setText(value);
			case BALANCE_TO_PAY -> balanceTextField.setText(value);
		}
	}






}
