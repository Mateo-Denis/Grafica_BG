package views.settings;

import org.javatuples.Pair;
import presenters.StandardPresenter;
import presenters.settings.SettingsPresenter;
import utils.MessageTypes;
import utils.databases.SettingsTableNames;
import views.ToggleableView;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class SettingsView extends ToggleableView implements ISettingsView {
	private JPanel containerPanel;
	private JPanel generalValuesPanel;
	private JPanel clothValuesPanel;
	private JTable clothValuesTable;
	private JPanel clothesValuesPanel;
	private JTable clothesValuesTable;
	private JButton updateDataButton;
	private JPanel plankLoweringPanel;
	private JPanel cutValuesPanel;
	private JPanel printingValuesPanel;
	private JTable cutValuesTable;
	private JTable plankLoweringValuesTable;
	private JTable serviceValuesTable;
	private JTable printingValuesTable;
	private JTable canvasValuesTable;
	private JTable vinylValuesTable;
	private JTable generalValuesTable;
	private JPanel vinylValuesPanel;
	private JPanel serviceValuesPanel;
	private JPanel canvasValuesPanel;
	private SettingsPresenter settingsPresenter;


	public SettingsView() {
		windowFrame = new JFrame("Configuración");
		windowFrame.setContentPane(containerPanel);
		windowFrame.pack();
		windowFrame.setLocationRelativeTo(null);
		windowFrame.setIconImage(new ImageIcon("src/main/resources/BGLogo.png").getImage());
		wrapContainer();

		initTableListeners(generalValuesTable);
		initTableListeners(plankLoweringValuesTable);
		initTableListeners(clothValuesTable);
		initTableListeners(cutValuesTable);
		initTableListeners(clothesValuesTable);
		initTableListeners(serviceValuesTable);
		initTableListeners(printingValuesTable);
		initTableListeners(vinylValuesTable);
		initTableListeners(canvasValuesTable);

	}
	@Override
	public void setPresenter(StandardPresenter standardPresenter) {
		this.settingsPresenter = (SettingsPresenter) standardPresenter;
	}

	@Override
	protected void wrapContainer() {
		containerPanelWrapper = containerPanel;
	}

	@Override
	protected void initListeners() {
		updateDataButton.addActionListener(e -> settingsPresenter.onUpdateDataButtonPressed());
	}

	private void initTableListeners(JTable table) {

		table.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent e) {
				// Stop editing if focus is lost
				if (table.isEditing()) {
					table.getCellEditor().stopCellEditing();
				}
			}
		});

		// Add a listener to commit changes on selection change (cell navigation)
		table.getSelectionModel().addListSelectionListener(e -> {
			if (table.isEditing()) {
				table.getCellEditor().stopCellEditing();
			}
		});

//		table.addKeyListener(new KeyAdapter() {
//			@Override
//			public void keyPressed(KeyEvent e) {
//				if (table.isEditing()) {
//					table.getCellEditor().stopCellEditing();
//				}
//			}
//		});

		table.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				JTable table = (JTable) e.getSource();
				int row = table.rowAtPoint(e.getPoint());
				int column = table.columnAtPoint(e.getPoint());

				// Check if the click is on a different cell
				if (!table.isEditing() || table.getEditingRow() != row || table.getEditingColumn() != column) {
					if (table.isEditing()) {
						table.getCellEditor().stopCellEditing();
					}

					table.editCellAt(row, column);
				}
			}
		});

		table.setSelectionBackground(table.getBackground());
		table.setSelectionForeground(table.getForeground());

		table.setShowGrid(true);
		table.setGridColor(Color.LIGHT_GRAY);

	}

	@Override
	public void clearView() {

	}
	@Override
	public void setModularTable(SettingsTableNames tableName, ArrayList<Pair<String, Double>> values) {
		DefaultTableModel model = new DefaultTableModel(new Object[]{"Campo", "Valor"}, 0) {
			@Override
			public boolean isCellEditable(int row, int column) {
				return column != 0; // Disable editing for the index 0 column
			}
		};
		// Convert the ArrayList<Pair<String, Double>> into table rows
		for (Pair<String, Double> pair : values) {
			model.addRow(new Object[]{pair.getValue0(), pair.getValue1()});
		}
		getModularTable(tableName).setModel(model);
	}
	@Override
	public ArrayList<Pair<String, Double>> tableToArrayList(SettingsTableNames tableName) throws NumberFormatException{
		ArrayList<Pair<String, Double>> arrayList = new ArrayList<>();
		Object obj;
		JTable table = getModularTable(tableName);
		for (int i = 0; i < table.getRowCount(); i++) {
			obj = table.getValueAt(i, 1);
			arrayList.add(new Pair<>(table.getValueAt(i, 0).toString(), Double.parseDouble(obj.toString())));
		}
		return arrayList;
	}

	@Override
	public JTable getModularTable(SettingsTableNames table) {
		return switch (table) {
			case GENERAL -> generalValuesTable;
			case BAJADA_PLANCHA -> plankLoweringValuesTable;
			case TELAS -> clothValuesTable;
			case CORTE -> cutValuesTable;
			case PRENDAS -> clothesValuesTable;
			case SERVICIOS -> serviceValuesTable;
			case IMPRESIONES -> printingValuesTable;
			case VINILOS -> vinylValuesTable;
			case LONAS -> canvasValuesTable;
		};
	}
	@Override
	public void showDetailedMessage(MessageTypes messageType, SettingsTableNames tableName) {
		JOptionPane.showMessageDialog(containerPanelWrapper, messageType.getMessage() +  tableName.getName() + "."
				, messageType.getTitle()
				, messageType.getMessageType());
	}


}
