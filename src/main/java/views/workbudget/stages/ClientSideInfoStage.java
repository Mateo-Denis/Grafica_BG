package views.workbudget.stages;

import utils.NumberInputVerifier;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.AbstractDocument;

import static utils.TabKeyStrokeSetting.applyTabKeyStrokeSettingsToTable;

public class ClientSideInfoStage extends JPanel {
	private JPanel root;
	private JPanel dataPanel;
	private JTextField descriptionTextField;
	private JTextField totalTextField;
	private JTable materialsTable;

	public ClientSideInfoStage() {
		DefaultTableModel model = new DefaultTableModel(
				new Object[]{"Descripción", "Total"}, 0
		);
		materialsTable.setModel(model);
		((AbstractDocument) totalTextField.getDocument()).setDocumentFilter(new NumberInputVerifier());

		applyTabKeyStrokeSettingsToTable(materialsTable);
	}

	public JTextField getTextFieldByName(ContentListReferences name) {
		return switch (name) {
			case MATERIAL -> descriptionTextField;
			case MATERIAL_PRICE -> totalTextField;
			default -> null;
		};
	}

	public String getTextContentByName(ContentListReferences name) {
		return switch (name) {
			case MATERIAL -> descriptionTextField.getText();
			case MATERIAL_PRICE -> totalTextField.getText();
			default -> null;
		};
	}

	public void addMaterialToTable(String name, String price) {
		DefaultTableModel model = (DefaultTableModel) materialsTable.getModel();
		model.addRow(new Object[]{ name, price });
	}

	public void clearMaterialInputFields() {
		descriptionTextField.setText("");
		totalTextField.setText("");
	}


}
