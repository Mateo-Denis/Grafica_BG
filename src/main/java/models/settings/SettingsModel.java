package models.settings;

import org.javatuples.Pair;
import utils.databases.SettingsDatabaseConnection;

import java.util.ArrayList;

public class SettingsModel implements ISettingsModel {

	private final SettingsDatabaseConnection dbConnection;

	public SettingsModel(SettingsDatabaseConnection dbConnection) {
		this.dbConnection = dbConnection;
	}

	@Override
	public void updateGeneralData(String dollarValue, String plankLoweringValue, String capValue, String cupValue, String inkValue, String seamstressValue) {
		dbConnection.updateTable(dollarValue, plankLoweringValue, capValue, cupValue, inkValue, seamstressValue);
	}

	public ArrayList<Pair<String, String>> getTableContent(){
		return dbConnection.getTableContent();
	}

}
