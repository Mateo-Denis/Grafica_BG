package views.workbudget.stages;

import lombok.Getter;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.ArrayList;

import static utils.TableSettings.adjustColumnWidthsToHeader;

public class ClientSearchingStage extends JPanel {
	private JPanel clientResultContainer;
	private JScrollPane clientResultScrollPanel;
	@Getter
	private JTable clientResultTable;
	private DefaultTableModel clientsTableModel;
	private JPanel nameAndCityContainer;
	@Getter
	private JTextField clientTextField;
	private JLabel clientLabel;
	private JLabel cityLabel;
	private JComboBox cityComboBox;
	private JButton clientSearchButton;
	private JPanel root;


	public JButton getSearchButton() {
		return clientSearchButton;
	}

	public void setCitiesComboBox(ArrayList<String> cities) {
		cityComboBox.addItem("Seleccione una ciudad");
		for (String city : cities) {
			cityComboBox.addItem(city);
		}
	}

	public void setClientTextField(String text) {
		clientTextField.setText(text);
	}

	public String getSelectedCity() {
		return (String) cityComboBox.getSelectedItem();
	}

	public String getClientNameInput() {
		return clientTextField.getText();
	}

	public void clearClientTable() {
		for (int row = 0; row < clientResultTable.getRowCount(); row++) {
			for (int col = 0; col < clientResultTable.getColumnCount(); col++) {
				clientResultTable.setValueAt("", row, col);
			}
		}
		clientResultTable.clearSelection();
	}

	public void setClientIntTableValueAt(int rowCount, int column, int clientID) {
		clientResultTable.setValueAt(clientID, rowCount, column);
	}

	public void setClientStringTableValueAt(int rowCount, int i, String value) {
		clientResultTable.setValueAt(value, rowCount, i);
	}

	public void setClientsTableModel() {
		clientsTableModel = new DefaultTableModel(new Object[]{"ID", "Nombre", "Dirección", "Localidad", "Teléfono", "Cliente/Particular"}, 200) {
			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};
		clientResultTable.setModel(clientsTableModel);
		clientResultTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		adjustColumnWidthsToHeader(clientResultTable);
	}

	public boolean isTableSelected() {
		int row = clientResultTable.getSelectedRow();
		if( row == -1 ){
			return false;
		}else return (clientResultTable.getModel().getValueAt(row, 0) != null) && !clientResultTable.getModel().getValueAt(row, 0).equals("");
	}

	public int getSelectedClientID() {
		int row = clientResultTable.getSelectedRow();
		return (int) clientResultTable.getModel().getValueAt(row, 0);
	}

	public void selectRow(int row) {
		clientResultTable.setRowSelectionInterval(row, row);
	}

	public void clearView() {
		clientTextField.setText("");
		cityComboBox.setSelectedIndex(0);
		clearClientTable();
	}
}
