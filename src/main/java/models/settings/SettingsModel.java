package models.settings;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.javatuples.Pair;
import utils.databases.SettingsDatabaseConnection;
import utils.databases.SettingsTableNames;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

import static utils.databases.SettingsTableNames.*;

public class SettingsModel implements ISettingsModel {

	private final SettingsDatabaseConnection dbConnection;

	public SettingsModel(SettingsDatabaseConnection dbConnection) {
		this.dbConnection = dbConnection;
	}

	@Override
	public ArrayList<Pair<String, Double>> getModularValues(SettingsTableNames tableName){
		return dbConnection.getTableAsList(tableName);
	}

	@Override
	public void updateModularValue(SettingsTableNames tableName, ArrayList<Pair<String, Double>> rows) throws SQLException {
		dbConnection.insertOrUpdateBatch(tableName, rows);
	}

	@Override
	public ArrayList<SettingsTableNames> getTableNames() {
		ArrayList<SettingsTableNames> list = new ArrayList<>();
		list.add(GENERAL);
		list.add(BAJADA_PLANCHA);
		list.add(TELAS);
		list.add(CORTE);
		list.add(PRENDAS);
		list.add(SERVICIOS);
		list.add(IMPRESIONES);
		list.add(VINILOS);
		list.add(LONAS);
		list.add(GANANCIAS);
		list.add(MEDIDAS);
		return list;
	}

}
