package models.settings;

import com.google.gson.JsonObject;
import org.javatuples.Pair;
import org.javatuples.Triplet;
import utils.databases.SettingsTableNames;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public interface ISettingsModel {
	ArrayList<Pair<String, Double>> getModularValues(SettingsTableNames tableName);

	void updateModularValue(SettingsTableNames tableName, ArrayList<Pair<String, Double>> rows) throws SQLException;

	ArrayList<SettingsTableNames> getTableNames();

	void updateModularPrices(List<Triplet<String, String, Double>> modularPrices) throws SQLException;

	String getModularValue(SettingsTableNames tableName, String selectedValue);
}
