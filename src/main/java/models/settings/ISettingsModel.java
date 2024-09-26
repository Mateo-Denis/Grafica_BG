package models.settings;

import com.google.gson.JsonObject;
import org.javatuples.Pair;

import java.util.ArrayList;

public interface ISettingsModel {
	// Method to read the configuration from JSON file
	JsonObject readConfig();

	void writeConfig(JsonObject config);

	void updateGeneralData(String dollarValue, String plankLoweringValue, String capValue, String cupValue, String inkValue, String seamstressValue);

	ArrayList<Pair<String, String>> getTableContent();
}
