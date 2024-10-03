package views.settings;

import org.javatuples.Pair;
import presenters.StandardPresenter;
import presenters.settings.SettingsPresenter;
import utils.MessageTypes;
import utils.databases.SettingsTableNames;
import views.ToggleableView;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
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

	@Override
	public void clearView() {

	}
	@Override
	public void setModularTable(SettingsTableNames tableName, ArrayList<Pair<String, Double>> generalValues) {
		DefaultTableModel model = new DefaultTableModel(new Object[]{"Campo", "Valor"}, 0) {
			@Override
			public boolean isCellEditable(int row, int column) {
				// Only allow editing of the "Value" column (index 1)
				return column != 0; // Disable editing for the "Label" column (index 0)
			}
		};
		// Convert the ArrayList<Pair<String, Double>> into table rows
		for (Pair<String, Double> pair : generalValues) {
			model.addRow(new Object[]{pair.getValue0(), pair.getValue1()});
		}
		getModularTable(tableName).setModel(model);
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

	public void showMessage(MessageTypes messageType, SettingsTableNames tableName) {
		JOptionPane.showMessageDialog(containerPanelWrapper, messageType.getMessage() +  tableName.getName() + "."
				, messageType.getTitle()
				, messageType.getMessageType());
	}


}
