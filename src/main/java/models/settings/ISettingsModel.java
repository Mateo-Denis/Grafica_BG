package models.settings;

import com.google.gson.JsonObject;
import org.javatuples.Pair;
import utils.databases.SettingsTableNames;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Set;

public interface ISettingsModel {
	ArrayList<Pair<String, Double>> getModularValues(SettingsTableNames tableName);

	void updateModularValue(SettingsTableNames tableName, ArrayList<Pair<String, Double>> rows) throws SQLException;


	ArrayList<SettingsTableNames> getTableNames();
}
