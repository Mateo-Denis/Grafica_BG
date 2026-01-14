package views.workbudget.stages;

import lombok.Getter;
import org.javatuples.Pair;
import utils.AlignValueToTopCellRenderer;
import utils.NumberInputVerifier;
import utils.WorkBudgetTablesCellRenderer;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.AbstractDocument;
import javax.swing.text.DefaultEditorKit;

import java.awt.*;
import java.util.ArrayList;

import static utils.TabKeyStrokeSetting.applyTabKeyStrokeSettingsToTable;
import static utils.TabKeyStrokeSetting.applyTabKeyStrokeSettingsToTextArea;

public class ContentListStage extends JPanel {
	private JTextArea logisticsTextArea;
	private JTextField logisticsCostTextField;
	private JTextField placerTextField;
	private JTextField placingCostTextField;
	private JTextField materialPriceTextField;
	private JPanel root;
	private JPanel placingPanel;
	private JPanel logisticsPanel;
	private JPanel materialsPanel;
	@Getter
	private JTable materialsTable;
	private JTextArea materialTextArea;

	public ContentListStage() {
		DefaultTableModel model = new DefaultTableModel(
				new Object[]{"Material", "Precio"}, 0
		);

		materialsTable.setModel(model);
        addCellRendererToTable();

		((AbstractDocument) materialPriceTextField.getDocument()).setDocumentFilter(new NumberInputVerifier());
		((AbstractDocument) logisticsCostTextField.getDocument()).setDocumentFilter(new NumberInputVerifier());
		((AbstractDocument) placingCostTextField.getDocument()).setDocumentFilter(new NumberInputVerifier());

		applyTabKeyStrokeSettingsToTextArea(materialTextArea);
		applyTabKeyStrokeSettingsToTextArea(logisticsTextArea);
		applyTabKeyStrokeSettingsToTable(materialsTable);

		materialTextArea.setLineWrap(true);
		materialTextArea.setWrapStyleWord(true);

		materialTextArea.getInputMap().put(KeyStroke.getKeyStroke("ENTER"), "submit");

		materialTextArea.getInputMap().put(
				KeyStroke.getKeyStroke("shift ENTER"),
				DefaultEditorKit.insertBreakAction
		);


	}

    public void addCellRendererToTable(){
        materialsTable.setIntercellSpacing(new Dimension(0, 0));
        materialsTable.setShowGrid(false);
        materialsTable.getColumnModel()
                .getColumn(0)
                .setCellRenderer(new WorkBudgetTablesCellRenderer());
        materialsTable.getColumnModel()
                .getColumn(1)
                .setCellRenderer(new AlignValueToTopCellRenderer());
    }

	public JTextField getTextFieldByName(ContentListReferences name) {
		return switch (name) {
			case MATERIAL_PRICE -> materialPriceTextField;
			case LOGISTICS_COST -> logisticsCostTextField;
			case PLACER -> placerTextField;
			case PLACING_COST -> placingCostTextField;
			default -> null;
		};
	}

	public JTextArea getTextAreaByName(ContentListReferences name) {
		return switch (name) {
			case MATERIAL -> materialTextArea;
			case LOGISTICS_DESCRIPTION -> logisticsTextArea;
			default -> null;
		};
	}

	public String getTextContentByName(ContentListReferences name) {
		return switch (name) {
			case MATERIAL -> materialTextArea.getText();
			case MATERIAL_PRICE -> materialPriceTextField.getText();
			case LOGISTICS_DESCRIPTION -> logisticsTextArea.getText();
			case LOGISTICS_COST -> logisticsCostTextField.getText();
			case PLACER -> placerTextField.getText();
			case PLACING_COST -> placingCostTextField.getText();
			default -> null;
		};
	}

	public void setTextContentByName(ContentListReferences name, String text) {
		switch (name) {
			case MATERIAL -> materialTextArea.setText(text);
			case MATERIAL_PRICE -> materialPriceTextField.setText(text);
			case LOGISTICS_DESCRIPTION -> logisticsTextArea.setText(text);
			case LOGISTICS_COST -> logisticsCostTextField.setText(text);
			case PLACER -> placerTextField.setText(text);
			case PLACING_COST -> placingCostTextField.setText(text);
		}
	}

	public void addMaterialToTable(String name, String price) {
		DefaultTableModel model = (DefaultTableModel) materialsTable.getModel();
		model.addRow(new Object[]{ name, price });
	}

	public void clearMaterialInputFields() {
		materialTextArea.setText("");
		materialPriceTextField.setText("");
	}

	public void setFocusToMaterialField() {
		materialTextArea.requestFocusInWindow();
	}

	public ArrayList<Pair<String, String>> getMaterialsListFromTable() {
		ArrayList<Pair<String, String>> materials = new ArrayList<>();
		DefaultTableModel model = (DefaultTableModel) materialsTable.getModel();
		for (int row = 0; row < model.getRowCount(); row++) {
			String materialName = (String) model.getValueAt(row, 0);
			String materialPriceString = (String) model.getValueAt(row, 1);
			if (materialName != null && !materialName.isEmpty()) {
				materials.add(new Pair<>(materialName, materialPriceString));
			}
		}
		return materials;
	}

	public void clearView() {
		logisticsTextArea.setText("");
		logisticsCostTextField.setText("");
		placerTextField.setText("");
		placingCostTextField.setText("");
		materialTextArea.setText("");
		materialPriceTextField.setText("");
		DefaultTableModel model = (DefaultTableModel) materialsTable.getModel();
		model.setRowCount(0);
	}
}
