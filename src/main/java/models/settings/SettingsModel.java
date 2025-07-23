package models.settings;

import org.javatuples.Pair;
import org.javatuples.Triplet;
import utils.databases.SettingsDatabaseConnection;
import utils.databases.SettingsTableNames;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static utils.databases.SettingsTableNames.*;

public class SettingsModel implements ISettingsModel {

	private final SettingsDatabaseConnection dbConnection;

	public SettingsModel(SettingsDatabaseConnection dbConnection) {
		this.dbConnection = dbConnection;
	}

	@Override
	public ArrayList<String> getOtherTablesAsList(SettingsTableNames tableName){
		return dbConnection.getOtherTablesAsList(tableName);
	}

	@Override
	public ArrayList<Pair<String, Double>> getGeneralTableAsList(SettingsTableNames tableName) {
		return dbConnection.getGeneralTableAsList(tableName);
	}

	@Override
	public void updateModularValue(SettingsTableNames tableName, ArrayList<Pair<String, Double>> rows) throws SQLException {
		dbConnection.insertOrUpdateBatch(tableName, rows);
	}
	@Override
	public void updateModularNames(SettingsTableNames tableName, ArrayList<String> names) throws SQLException {
		dbConnection.insertOrUpdateBatchNames(tableName, names);
	}

	@Override
	public ArrayList<SettingsTableNames> getTableNames() {
		ArrayList<SettingsTableNames> list = new ArrayList<>();
		list.add(GENERAL);
		list.add(TELAS);
		list.add(SERVICIOS);
		list.add(MATERIALES);
		return list;
	}

	@Override
	public void removeRow(SettingsTableNames tableName, String field) {
		dbConnection.removeRow(tableName, field);
	}

	@Override
	public void updateModularPrices(List<Triplet<String, String, Double>> modularPrices) throws SQLException {
		dbConnection.updateModularPrices(modularPrices);
	}

	@Override
	public String getModularValue(SettingsTableNames tableName, String selectedValue) {
		return dbConnection.getModularValue(tableName, selectedValue);
	}

}
