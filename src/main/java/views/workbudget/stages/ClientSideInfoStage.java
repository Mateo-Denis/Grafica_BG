package views.workbudget.stages;

import lombok.Getter;
import org.javatuples.Pair;
import utils.NumberInputVerifier;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.AbstractDocument;

import java.util.ArrayList;

import static utils.TabKeyStrokeSetting.applyTabKeyStrokeSettingsToTable;

public class ClientSideInfoStage extends JPanel {
	private JPanel root;
	private JPanel dataPanel;
	private JTextField descriptionTextField;
	private JTextField totalTextField;
	@Getter
	private JTable itemsTable;

	public ClientSideInfoStage() {
		DefaultTableModel model = new DefaultTableModel(
				new Object[]{"Descripción", "Total"}, 0
		);
		itemsTable.setModel(model);
		((AbstractDocument) totalTextField.getDocument()).setDocumentFilter(new NumberInputVerifier());

		applyTabKeyStrokeSettingsToTable(itemsTable);
	}

	public JTextField getTextFieldByName(ClientSideInfoReferences name) {
		return switch (name) {
			case DESCRIPTION -> descriptionTextField;
			case TOTAL -> totalTextField;
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

	public void addInfoItemToTable(String description, String price) {
		DefaultTableModel model = (DefaultTableModel) itemsTable.getModel();
		model.addRow(new Object[]{ description, price });
	}

	public void clearMaterialInputFields() {
		descriptionTextField.setText("");
		totalTextField.setText("");
	}

	public void setFocusToMaterialField() {
		descriptionTextField.requestFocusInWindow();
	}

	public ArrayList<Pair<String, String>> getItemsListFromTable() {
		ArrayList<Pair<String, String>> items = new ArrayList<>();
		DefaultTableModel model = (DefaultTableModel) itemsTable.getModel();
		for (int row = 0; row < model.getRowCount(); row++) {
			String description = (String) model.getValueAt(row, 0);
			String priceString = (String) model.getValueAt(row, 1);
			if (description != null && !description.isEmpty() &&
					priceString != null && !priceString.isEmpty()) {
				items.add(new Pair<>(description, priceString));
			}
		}
		return items;
	}

	public void clearView() {
		descriptionTextField.setText("");
		totalTextField.setText("");
		DefaultTableModel model = (DefaultTableModel) itemsTable.getModel();
		model.setRowCount(0);
	}
}
