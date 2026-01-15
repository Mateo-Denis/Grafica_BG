package views.workbudget.stages;

import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import lombok.Getter;
import org.javatuples.Pair;
import utils.AlignValueToTopCellRenderer;
import utils.NumberInputVerifier;
import utils.WorkBudgetTablesCellRenderer;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.AbstractDocument;
import javax.swing.text.DefaultEditorKit;

import java.awt.*;
import java.util.ArrayList;

import static utils.TabKeyStrokeSetting.applyTabKeyStrokeSettingsToTable;
import static utils.TabKeyStrokeSetting.applyTabKeyStrokeSettingsToTextArea;

public class ClientSideInfoStage extends JPanel {
    private JPanel root;
    private JPanel dataPanel;
    private JTextField totalTextField;
    @Getter
    private JTable itemsTable;
    private JTextArea descriptionTextArea;

    public ClientSideInfoStage() {
        DefaultTableModel model = new DefaultTableModel(
                new Object[]{"Descripción", "Total"}, 0
        );
        itemsTable.setModel(model);
        addCellRendererToTable();
        ((AbstractDocument) totalTextField.getDocument()).setDocumentFilter(new NumberInputVerifier());

        applyTabKeyStrokeSettingsToTable(itemsTable);
        applyTabKeyStrokeSettingsToTextArea(descriptionTextArea);

        descriptionTextArea.setLineWrap(true);
        descriptionTextArea.setWrapStyleWord(true);

        descriptionTextArea.getInputMap().put(KeyStroke.getKeyStroke("ENTER"), "submit");

        descriptionTextArea.getInputMap().put(
                KeyStroke.getKeyStroke("shift ENTER"),
                DefaultEditorKit.insertBreakAction
        );
    }

    public JTextField getTotalTextField() {
        return totalTextField;
    }

    public JTextArea getDescriptionTextArea() {
        return descriptionTextArea;
    }

    public void addCellRendererToTable() {
        itemsTable.setIntercellSpacing(new Dimension(0, 0));
        itemsTable.setShowGrid(false);
        itemsTable.getColumnModel()
                .getColumn(0)
                .setCellRenderer(new WorkBudgetTablesCellRenderer());
/*        itemsTable.getColumnModel()
                .getColumn(1)
                .setCellRenderer(new AlignValueToTopCellRenderer());*/
        itemsTable.getColumnModel()
                .removeColumn(itemsTable.getColumnModel().getColumn(1));
    }

    public String getTextContentByName(ContentListReferences name) {
        return switch (name) {
            case MATERIAL -> descriptionTextArea.getText();
            case MATERIAL_PRICE -> totalTextField.getText();
            default -> null;
        };
    }

    public void addInfoItemToTable(String description, String price) {
        DefaultTableModel model = (DefaultTableModel) itemsTable.getModel();
        model.addRow(new Object[]{description, price});
    }

    public void clearMaterialInputFields() {
        descriptionTextArea.setText("");
        totalTextField.setText("");
    }

    public void setFocusToMaterialField() {
        descriptionTextArea.requestFocusInWindow();
    }

    public ArrayList<Pair<String, String>> getItemsListFromTable() {
        ArrayList<Pair<String, String>> items = new ArrayList<>();
        DefaultTableModel model = (DefaultTableModel) itemsTable.getModel();
        for (int row = 0; row < model.getRowCount(); row++) {
            String description = (String) model.getValueAt(row, 0);
            String priceString = "0";
            if (description != null && !description.isEmpty()) {
                items.add(new Pair<>(description, priceString));
            }
        }
        return items;
    }

    public void clearView() {
        descriptionTextArea.setText("");
        totalTextField.setText("");
        DefaultTableModel model = (DefaultTableModel) itemsTable.getModel();
        model.setRowCount(0);
    }

}
