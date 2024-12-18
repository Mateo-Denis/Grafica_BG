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

	@Override
	public void updateModularPrices(List<Triplet<String, String, Double>> modularPrices) throws SQLException {
		dbConnection.updateModularPrices(modularPrices);
	}

	@Override
	public String getModularValue(SettingsTableNames tableName, String selectedValue) {
		return dbConnection.getModularValue(tableName, selectedValue);
	}

}
