package presenters.settings;

import models.settings.ISettingsModel;
import presenters.StandardPresenter;
import org.javatuples.Pair;
import utils.databases.SettingsTableNames;
import views.settings.ISettingsView;

import javax.swing.*;
import java.sql.SQLException;
import java.util.ArrayList;

import static utils.MessageTypes.*;

public class SettingsPresenter extends StandardPresenter {

	private final ISettingsView settingsView;
	private final ISettingsModel settingsModel;
	public ArrayList<SettingsTableNames> tableNames;


	public SettingsPresenter(ISettingsView settingsView, ISettingsModel settingsModel) {
		this.settingsView = settingsView;
		view = settingsView;
		this.settingsModel = settingsModel;
		tableNames = settingsModel.getTableNames();
	}
	private void showValues() {
		for(SettingsTableNames table : tableNames){
			if(table == SettingsTableNames.GENERAL){
				settingsView.setModularTable(table, settingsModel.getGeneralTableAsList(table), null);
			} else {
				settingsView.setModularTable(table,null, settingsModel.getOtherTablesAsList(table));
			}
		}
	}

	public void onAddButtonPressed(SettingsTableNames tableName){
		settingsView.addEmptyRow(tableName);
	}

	public void onDeleteRowButtonPressed(SettingsTableNames tableName, int selectedRow) {
		if(selectedRow < 0) {
			settingsView.showMessage(NO_ROW_SELECTED_FOR_DELETION);
			return;
		}
		String removedField = settingsView.getRowName(tableName, selectedRow);
		boolean confirmed = settingsView.confirmDeletion(removedField);
		if (!confirmed) return;

		removedField = settingsView.removeRow(tableName, selectedRow);
		try {
			settingsModel.removeRow(tableName, removedField);
		} catch (NumberFormatException e) {
			settingsView.showDetailedMessage(SETTINGS_SAVE_FAILURE, SettingsTableNames.valueOf(tableName.getName()));
		}
	}

	public void onSaveButtonPressed(SettingsTableNames tableName) {
		try{
			if(tableName == SettingsTableNames.GENERAL){

				settingsModel.updateModularValue(SettingsTableNames.GENERAL, settingsView.generalTableToArrayList());

			}else {
				settingsModel.updateModularNames(tableName, settingsView.normalTableToArrayList(tableName));
			}
		}catch (SQLException e){
			settingsView.showDetailedMessage(SETTINGS_SAVE_FAILURE, SettingsTableNames.valueOf(tableName.getName()));
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
