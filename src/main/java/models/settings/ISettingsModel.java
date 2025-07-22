package models.settings;

import org.javatuples.Pair;
import org.javatuples.Triplet;
import utils.databases.SettingsTableNames;

import java.sql.Array;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public interface ISettingsModel {
	ArrayList<String> getModularValues(SettingsTableNames tableName);

	void updateModularValue(SettingsTableNames tableName, ArrayList<Pair<String, Double>> rows) throws SQLException;

	void updateModularNames(SettingsTableNames tableName, ArrayList<String> names) throws SQLException;

	ArrayList<SettingsTableNames> getTableNames();

	void updateModularPrices(List<Triplet<String, String, Double>> modularPrices) throws SQLException;

	void removeRow(SettingsTableNames tableName, String field);

	String getModularValue(SettingsTableNames tableName, String selectedValue);

	ArrayList<Pair<String,Double>> getGeneralTableAsList(SettingsTableNames tableName);
}
