package presenters.settings;

import models.settings.ISettingsModel;
import org.javatuples.Pair;
import presenters.StandardPresenter;
import utils.databases.SettingsTableNames;
import views.settings.ISettingsView;

import javax.swing.*;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

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
		updateValues();
		showValues();
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
				settingsModel.updateModularValue(table, tableToArrayList(settingsView.getModularTable(table)));
			}catch (SQLException e){
				foundError = true;
				settingsView.showMessage(SETTINGS_SAVE_FAILURE);
			}
		}
		if(!foundError){
			settingsView.showMessage(SETTINGS_SAVE_SUCCESS);
		}
	}

	private void updateValues() {

	}

	@Override
	protected void initListeners() {

	}

	private ArrayList<Pair<String, Double>> tableToArrayList(JTable table) {
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
