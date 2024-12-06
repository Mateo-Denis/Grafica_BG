package utils;

import lombok.Getter;
import org.javatuples.Pair;
import utils.databases.AttributesDatabaseConnection;
import utils.databases.SettingsDatabaseConnection;
import utils.databases.SettingsTableNames;

import java.util.ArrayList;
import java.util.Objects;

public class Product {
    private final int ID;
    @Getter
    private final String name;
    @Getter
    private final int categoryID;
    private SettingsDatabaseConnection settingsDBConnection;

    public Product(int id, String name, int categoryID) {
        this.name = name;
        this.categoryID = categoryID;
        this.ID = id;
    }

    public double getIndividualPrice(SettingsTableNames tableName, String selectedValue) {
        ArrayList<Pair<String, Double>> rows = settingsDBConnection.getTableAsList(tableName);
        double individualPrice = 0.0;
        for (Pair<String, Double> row : rows) {
            if (row.getValue0().equals(selectedValue)) {
                individualPrice = row.getValue1();
            }
        }
        return individualPrice;
    }

    public double calculateRealTimePrice() {
        AttributesDatabaseConnection attributesDBConnection = new AttributesDatabaseConnection();
        settingsDBConnection = new SettingsDatabaseConnection();

        switch (categoryID) {
            case 1:{

                double t1aV = Double.parseDouble(attributesDBConnection.getAttributeValue(ID, "T1A"));

                double t1bV;
                String t1bS = attributesDBConnection.getAttributeValue(ID, "T1B");
                if(Objects.equals(t1bS, "###")){
                    t1bV = getIndividualPrice(SettingsTableNames.BAJADA_PLANCHA, "En taza");
                }else {
                    t1bV = Double.parseDouble(t1bS);
                }

                double t2aV = Double.parseDouble(attributesDBConnection.getAttributeValue(ID, "T2A"));

                double t2bV;
                String t2bS = attributesDBConnection.getAttributeValue(ID, "T2B");
                if(Objects.equals(t2bS, "###")){
                    t2bV = getIndividualPrice(SettingsTableNames.IMPRESIONES, "Metro de Sublimación");
                }else {
                    t2bV = Double.parseDouble(t2bS);
                }

                double cupV;
                String cupS = attributesDBConnection.getAttributeValue(ID, "TAZA");
                if(Objects.equals(cupS, "###")) {
                    cupV = getIndividualPrice(SettingsTableNames.GENERAL, "Taza");
                }else {
                    cupV = Double.parseDouble(cupS);
                }

                double profitV;
                String profitS = attributesDBConnection.getAttributeValue(ID, "GANANCIA");
                if(Objects.equals(profitS, "###")) {
                    profitV = getIndividualPrice(SettingsTableNames.GANANCIAS, "Taza");
                }else {
                    profitV = Double.parseDouble(profitS);
                }


                return ((t1aV * t1bV) + (t2aV * t2bV) + cupV) * profitV;
            }
            case 2:

                return 20;
            case 3:
                return 30;
            case 4:
                return 40;
            case 5:
                return 50;
            case 6:
                return 60;
            case 7:
                return 70;
            case 8:
                return 80;
            case 9:
                return 90;
            default:
                return 0;
        }
    }
}
