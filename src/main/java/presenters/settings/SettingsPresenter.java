package presenters.settings;

import models.settings.ISettingsModel;
import presenters.StandardPresenter;
import org.javatuples.Pair;
import utils.databases.SettingsTableNames;
import views.settings.ISettingsView;

import javax.swing.*;
import java.sql.SQLException;
import java.util.ArrayList;

import static utils.MessageTypes.SETTINGS_SAVE_FAILURE;
import static utils.MessageTypes.SETTINGS_SAVE_SUCCESS;

public class SettingsPresenter extends StandardPresenter {

	private final ISettingsView settingsView;
	private final ISettingsModel settingsModel;
	ArrayList<SettingsTableNames> tableNames;


	public SettingsPresenter(ISettingsView settingsView, ISettingsModel settingsModel) {
		this.settingsView = settingsView;
		view = settingsView;
		this.settingsModel = settingsModel;
		tableNames = settingsModel.getTableNames();
	}
	private void showValues() {
		for(SettingsTableNames table : tableNames){
			settingsView.setModularTable(table, settingsModel.getModularValues(table));
		}
	}
	public void onUpdateDataButtonPressed() {
		boolean foundError = false;
		for(SettingsTableNames table : tableNames){
			try{
				settingsModel.updateModularValue(table, settingsView.tableToArrayList(table));
			}catch (SQLException | NumberFormatException e){
				foundError = true;
				settingsView.showDetailedMessage(SETTINGS_SAVE_FAILURE, table);
			}
		}
		if(!foundError){
			settingsView.showMessage(SETTINGS_SAVE_SUCCESS);
		}
	}

	@Override
	protected void initListeners() {

	}

	private ArrayList<Pair<String, Double>> tableToArrayList(JTable table) throws NumberFormatException{
		ArrayList<Pair<String, Double>> arrayList = new ArrayList<>();
		Object obj;
		for (int i = 0; i < table.getRowCount(); i++) {
			obj = table.getValueAt(i, 1);
			arrayList.add(new Pair<>(table.getValueAt(i, 0).toString(), Double.parseDouble(obj.toString())));
		}
		return arrayList;
	}

	public void onHomeSettingsButtonClicked() {
		settingsView.showView();
		showValues();
	}
}
