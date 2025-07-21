package models.settings;

import org.javatuples.Pair;
import org.javatuples.Triplet;
import utils.databases.SettingsTableNames;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public interface ISettingsModel {
	ArrayList<Pair<String, Double>> getModularValues(SettingsTableNames tableName);

	void updateModularValue(SettingsTableNames tableName, ArrayList<Pair<String, Double>> rows) throws SQLException;

	ArrayList<SettingsTableNames> getTableNames();

	void updateModularPrices(List<Triplet<String, String, Double>> modularPrices) throws SQLException;

	void removeRow(SettingsTableNames tableName, String field);

	String getModularValue(SettingsTableNames tableName, String selectedValue);
}
