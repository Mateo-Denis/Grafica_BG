package views.workbudget.stages;

import lombok.Getter;
import utils.NumberInputVerifier;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.AbstractDocument;
import java.awt.event.ActionEvent;

import static utils.TabKeyStrokeSetting.applyTabKeyStrokeSettingsToTable;
import static utils.TabKeyStrokeSetting.applyTabKeyStrokeSettingsToTextArea;

public class ContentListStage extends JPanel {
	private JTextArea logisticsTextArea;
	private JTextField logisticsCostTextField;
	private JTextField placerTextField;
	private JTextField placingCostTextField;
	private JTextField materialTextField;
	private JTextField materialPriceTextField;
	private JPanel root;
	private JPanel placingPanel;
	private JPanel logisticsPanel;
	private JPanel materialsPanel;
	@Getter
	private JTable materialsTable;

	public ContentListStage() {
		DefaultTableModel model = new DefaultTableModel(
				new Object[]{"Material", "Precio"}, 0
		);
		materialsTable.setModel(model);
		((AbstractDocument) materialPriceTextField.getDocument()).setDocumentFilter(new NumberInputVerifier());
		((AbstractDocument) logisticsCostTextField.getDocument()).setDocumentFilter(new NumberInputVerifier());
		((AbstractDocument) placingCostTextField.getDocument()).setDocumentFilter(new NumberInputVerifier());

		applyTabKeyStrokeSettingsToTextArea(logisticsTextArea);
		applyTabKeyStrokeSettingsToTable(materialsTable);
	}

	public JTextField getTextFieldByName(ContentListReferences name) {
		return switch (name) {
			case MATERIAL -> materialTextField;
			case MATERIAL_PRICE -> materialPriceTextField;
			case LOGISTICS_COST -> logisticsCostTextField;
			case PLACER -> placerTextField;
			case PLACING_COST -> placingCostTextField;
			default -> null;
		};
	}

	public String getTextContentByName(ContentListReferences name) {
		return switch (name) {
			case MATERIAL -> materialTextField.getText();
			case MATERIAL_PRICE -> materialPriceTextField.getText();
			case LOGISTICS_DESCRIPTION -> logisticsTextArea.getText();
			case LOGISTICS_COST -> logisticsCostTextField.getText();
			case PLACER -> placerTextField.getText();
			case PLACING_COST -> placingCostTextField.getText();
			default -> null;
		};
	}

	public void addMaterialToTable(String name, String price) {
		DefaultTableModel model = (DefaultTableModel) materialsTable.getModel();
		model.addRow(new Object[]{ name, price });
	}

	public void clearMaterialInputFields() {
		materialTextField.setText("");
		materialPriceTextField.setText("");
	}

}
