package views.settings;

import org.javatuples.Pair;
import presenters.StandardPresenter;
import presenters.settings.SettingsPresenter;
import utils.MessageTypes;
import utils.databases.SettingsTableNames;
import views.ToggleableView;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class SettingsView extends ToggleableView implements ISettingsView {
	private JPanel containerPanel;
	private JPanel generalValuesPanel;
	private JPanel clothValuesPanel;
	private JTable clothValuesTable;
	private JTable clothesValuesTable;
	private JButton updateDataButton;
	private JPanel plankLoweringPanel;
	private JPanel printingValuesPanel;
	private JTable cutValuesTable;
	private JTable plankLoweringValuesTable;
	private JTable serviceValuesTable;
	private JTable printingValuesTable;
	private JTable canvasValuesTable;
	private JTable materialsValuesTable;
	private JTable generalValuesTable;
	private JPanel vinylValuesPanel;
	private JPanel serviceValuesPanel;
	private JPanel profitValuesPanel;
	private JTable profitValuesTable;
	private JTable measuresValuesTable;
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
		initTableListeners(serviceValuesTable);
		initTableListeners(printingValuesTable);
		initTableListeners(materialsValuesTable);
		initTableListeners(profitValuesTable);

		// Obtener el tamaño de la pantalla
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

		// Obtener ancho y alto
		double screenWidth = screenSize.width * 0.69;
		double screenHeight = screenSize.height * 0.73;

		windowFrame.setSize((int) screenWidth, (int) screenHeight);

		// Calculate position to center the frame
		int x = (screenSize.width - windowFrame.getWidth()) / 2;
		int y = (screenSize.height - windowFrame.getHeight()) / 2;

		windowFrame.setLocation(x, y);
		windowFrame.setResizable(false);

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

	public void autoResizeColumns(JTable table)
	{
		for (int column = 0; column < table.getColumnCount(); column++) {
			int width = 0;
			for (int row = 0; row < table.getRowCount(); row++) {
				TableCellRenderer renderer = table.getCellRenderer(row, column);
				Component comp = table.prepareRenderer(renderer, row, column);
				width = Math.max(comp.getPreferredSize().width, width);
			}
			table.getColumnModel().getColumn(column).setPreferredWidth(width + 10); // Añadir un pequeño margen
		}
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
		JTable table = getModularTable(tableName);
		table.setModel(model);
		autoResizeColumns(table);
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
			case SERVICIOS -> serviceValuesTable;
			case IMPRESIONES -> printingValuesTable;
			case GANANCIAS -> profitValuesTable;
			case MATERIALES -> materialsValuesTable;
		};
	}
	@Override
	public void showDetailedMessage(MessageTypes messageType, SettingsTableNames tableName) {
		JOptionPane.showMessageDialog(containerPanelWrapper, messageType.getMessage() +  tableName.getName() + "."
				, messageType.getTitle()
				, messageType.getMessageType());
	}


}
