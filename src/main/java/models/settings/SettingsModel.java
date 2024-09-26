package models.settings;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.javatuples.Pair;
import utils.databases.SettingsDatabaseConnection;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class SettingsModel implements ISettingsModel {

	private final SettingsDatabaseConnection dbConnection;
	private static final String CONFIG_FILE_PATH = "src/main/utils/config/config.json";

	public SettingsModel(SettingsDatabaseConnection dbConnection) {
		this.dbConnection = dbConnection;
	}

	// Method to read the configuration from JSON file
	@Override
	public JsonObject readConfig() {
		try (FileReader reader = new FileReader(CONFIG_FILE_PATH)) {
			// Parse the JSON file into a JsonObject
			JsonElement jsonElement = JsonParser.parseReader(reader);
			return jsonElement.getAsJsonObject();
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}
	@Override
	public void writeConfig(JsonObject config) {
		try (FileWriter file = new FileWriter(CONFIG_FILE_PATH)) {
			Gson gson = new Gson();
			// Write the updated JsonObject back to the file
			gson.toJson(config, file);
			System.out.println("Configuration updated successfully.");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	@Override
	public void updateGeneralData(String dollarValue, String plankLoweringValue, String capValue, String cupValue, String inkValue, String seamstressValue) {
		dbConnection.updateTable(dollarValue, plankLoweringValue, capValue, cupValue, inkValue, seamstressValue);

	}

	public ArrayList<Pair<String, String>> getTableContent(){
		return dbConnection.getTableContent();
	}

}
