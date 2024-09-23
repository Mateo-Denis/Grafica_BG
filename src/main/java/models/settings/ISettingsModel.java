package models.settings;

import org.javatuples.Pair;

import java.util.ArrayList;

public interface ISettingsModel {
	void updateGeneralData(String dollarValue, String plankLoweringValue, String capValue, String cupValue, String inkValue, String seamstressValue);

	void updateClothData(ArrayList<Pair<String, String>> clothTable);
	void updateClothesData(ArrayList<Pair<String, String>> clothesTable);
}
