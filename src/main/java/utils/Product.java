package utils;

import lombok.Getter;
import org.javatuples.Pair;
import utils.databases.AttributesDatabaseConnection;
import utils.databases.SettingsDatabaseConnection;
import utils.databases.SettingsTableNames;

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
        return Double.parseDouble(settingsDBConnection.getModularValue(tableName, selectedValue));
    }

    public Pair<Double, Double> calculateRealTimePrice() {
        AttributesDatabaseConnection attributesDBConnection = new AttributesDatabaseConnection();
        settingsDBConnection = new SettingsDatabaseConnection();

        switch (categoryID) {
            case 1:{ //taza

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

                String profitS = attributesDBConnection.getAttributeValue(ID, "GANANCIA");
                double profitV = Double.parseDouble(profitS);

                return applyIVA(((t1aV * t1bV) + (t2aV * t2bV) + cupV) * profitV, attributesDBConnection);            }
            case 2:{//gorra

                double t1aV = Double.parseDouble(attributesDBConnection.getAttributeValue(ID, "T1A"));

                double t1bV;
                String t1bS = attributesDBConnection.getAttributeValue(ID, "T1B");
                if(Objects.equals(t1bS, "###")){
                    t1bV = getIndividualPrice(SettingsTableNames.BAJADA_PLANCHA, "En gorra");
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

                double capV;
                String cupS = attributesDBConnection.getAttributeValue(ID, "GORRA");
                if(Objects.equals(cupS, "###")) {
                    capV = getIndividualPrice(SettingsTableNames.GENERAL, "Gorra");
                }else {
                    capV = Double.parseDouble(cupS);
                }

                String profitS = attributesDBConnection.getAttributeValue(ID, "GANANCIA");
                double profitV = Double.parseDouble(profitS);

                return applyIVA(((t1aV * t1bV) + (t2aV * t2bV) + capV) * profitV, attributesDBConnection);
            }
            case 3: { //tela
                String tela = attributesDBConnection.getAttributeValue(ID, "TELA");

                double t1aV;
                String t1aS = attributesDBConnection.getAttributeValue(ID, "T1A");
                if(Objects.equals(t1aS, "###")) {
                    t1aV = getIndividualPrice(SettingsTableNames.TELAS, tela);
                }else {
                    t1aV = Double.parseDouble(t1aS);
                }

                String profitS = attributesDBConnection.getAttributeValue(ID, "GANANCIA");
                double profitV = Double.parseDouble(profitS);

                return applyIVA(t1aV * profitV, attributesDBConnection);
            }

            case 4: { //bandera
                double t1aV = Double.parseDouble(attributesDBConnection.getAttributeValue(ID, "T1A"));
                double t1bV = Double.parseDouble(attributesDBConnection.getAttributeValue(ID, "T1B"));

                String tela = attributesDBConnection.getAttributeValue(ID, "TELA");
                double t1cV;
                String t1cS = attributesDBConnection.getAttributeValue(ID, "T1C");
                if(Objects.equals(t1cS, "###")) {
                    t1cV = getIndividualPrice(SettingsTableNames.TELAS, tela);
                }else {
                    t1cV = Double.parseDouble(t1cS);
                }


                double t2aV = Double.parseDouble(attributesDBConnection.getAttributeValue(ID, "T2A"));

                double t2bV;
                String t2bS = attributesDBConnection.getAttributeValue(ID, "T2B");
                if(Objects.equals(t2bS, "###")) {
                    t2bV = getIndividualPrice(SettingsTableNames.BAJADA_PLANCHA, "En bandera");
                }else{
                    t2bV = Double.parseDouble(t2bS);
                }


                double t3aV = Double.parseDouble(attributesDBConnection.getAttributeValue(ID, "T3A"));

                double t3bV;
                String t3bS = attributesDBConnection.getAttributeValue(ID, "T3B");
                if(Objects.equals(t2bS, "###")) {
                    t3bV = getIndividualPrice(SettingsTableNames.IMPRESIONES, "Metro de Sublimación");
                }else{
                    t3bV = Double.parseDouble(t3bS);
                }

                double seamstressV;
                String seamstressS = attributesDBConnection.getAttributeValue(ID, "COSTURERA");
                if(Objects.equals(seamstressS, "###")) {
                    seamstressV = getIndividualPrice(SettingsTableNames.SERVICIOS, "Costurera bandera");
                }else {
                    seamstressV = Double.parseDouble(seamstressS);
                }

                String profitS = attributesDBConnection.getAttributeValue(ID, "GANANCIA");
                double profitV = Double.parseDouble(profitS);

                return applyIVA(((t1aV * t1bV * t1cV) + (t2aV * t2bV) + (t3aV * t3bV) + seamstressV) * profitV, attributesDBConnection);
            }
            case 5: { //prendas

                double t1aV = Double.parseDouble(attributesDBConnection.getAttributeValue(ID, "T1A"));

                double t1bV;
                String t1bS = attributesDBConnection.getAttributeValue(ID, "T1B");
                if(Objects.equals(t1bS, "###")) {
                    t1bV = getIndividualPrice(SettingsTableNames.IMPRESIONES, "Metro de Sublimación");
                }else {
                    t1bV = Double.parseDouble(t1bS);
                }


                double t2aV = Double.parseDouble(attributesDBConnection.getAttributeValue(ID, "T2A"));

                String tela = attributesDBConnection.getAttributeValue(ID, "TELA");
                double t2bV;
                String t2bS = attributesDBConnection.getAttributeValue(ID, "T2B");
                if(Objects.equals(t2bS, "###")) {
                    t2bV = getIndividualPrice(SettingsTableNames.TELAS, tela);
                }else {
                    t2bV = Double.parseDouble(t2bS);
                }

                double t3aV = Double.parseDouble(attributesDBConnection.getAttributeValue(ID, "T3A"));

                double t3bV;
                String t3bS = attributesDBConnection.getAttributeValue(ID, "T3B");
                if(Objects.equals(t3bS, "###")) {
                    t3bV = getIndividualPrice(SettingsTableNames.BAJADA_PLANCHA, "En prenda");
                }else {
                    t3bV = Double.parseDouble(t3bS);
                }
                

                String seamstressType = attributesDBConnection.getAttributeValue(ID, "TIPO_COSTURERA");
                double seamstressV;
                String seamstressS = attributesDBConnection.getAttributeValue(ID, "COSTURERA");
                if(Objects.equals(seamstressS, "###")) {
                    seamstressV = getIndividualPrice(SettingsTableNames.SERVICIOS, seamstressType);
                }else {
                    seamstressV = Double.parseDouble(seamstressS);
                }

                String profitS = attributesDBConnection.getAttributeValue(ID, "GANANCIA");
                double profitV = Double.parseDouble(profitS);

                return applyIVA(((t1aV * t1bV) + (t2aV * t2bV) + (t3aV * t3bV) + seamstressV) * profitV, attributesDBConnection);

            }
            case 6: {

                String serviceType = attributesDBConnection.getAttributeValue(ID, "TIPO_SERVICIO");

                double serviceV;
                String serviceS = attributesDBConnection.getAttributeValue(ID, "SERVICIO");
                if(Objects.equals(serviceS, "###")) {
                    serviceV = getIndividualPrice(SettingsTableNames.SERVICIOS, serviceType);
                }else {
                    serviceV = Double.parseDouble(serviceS);
                }

                return applyIVA(serviceV, attributesDBConnection);
            }
            case 7: { //servicio de corte
                String vinilo = attributesDBConnection.getAttributeValue(ID, "VINILO");

                double t1aV;
                String t1aS = attributesDBConnection.getAttributeValue(ID, "T1A");
                if(Objects.equals(t1aS, "###")) {
                    t1aV = getIndividualPrice(SettingsTableNames.MATERIALES, vinilo);
                }else {
                    t1aV = Double.parseDouble(t1aS);
                }

                String profitS = attributesDBConnection.getAttributeValue(ID, "GANANCIA");
                double profitV = Double.parseDouble(profitS);

                return applyIVA(t1aV * profitV, attributesDBConnection);
            }
            case 8: { // impresión lineal
                double t1aV;
                String paper = attributesDBConnection.getAttributeValue(ID, "T1A");

                if(Objects.equals(paper, "###")) {
                    t1aV = getIndividualPrice(SettingsTableNames.IMPRESIONES, "Metro de papel");
                }else {
                    t1aV = Double.parseDouble(paper);
                }

                double t2aV;
                String ink = attributesDBConnection.getAttributeValue(ID, "T2A");
                if(Objects.equals(ink, "###")) {
                    t2aV = getIndividualPrice(SettingsTableNames.IMPRESIONES, "Metro de tinta");
                }else {
                    t2aV = Double.parseDouble(ink);
                }

                String profitS = attributesDBConnection.getAttributeValue(ID, "GANANCIA");
                double profitV = Double.parseDouble(profitS);

                return applyIVA((t1aV + t2aV) * profitV, attributesDBConnection);
            }
            case 9: {

                String material = attributesDBConnection.getAttributeValue(ID, "MATERIAL");
                double t1aV;
                String t1aS = attributesDBConnection.getAttributeValue(ID, "T1A");
                if(Objects.equals(t1aS, "###")) {
                    t1aV = getIndividualPrice(SettingsTableNames.MATERIALES, material);
                }else {
                    t1aV = Double.parseDouble(t1aS);
                }

                String isUV = attributesDBConnection.getAttributeValue(ID, "UV");
                double t2aV;
                if(Objects.equals(isUV, "SI")) {
                    t2aV = getIndividualPrice(SettingsTableNames.IMPRESIONES, "Metro2 de tinta UV");
                }else {
                    t2aV = getIndividualPrice(SettingsTableNames.IMPRESIONES, "Metro2 de tinta ECO");
                }

                String tipoDolar = attributesDBConnection.getAttributeValue(ID, "TIPO_DOLAR");
                double dollarV;
                String dollarS = attributesDBConnection.getAttributeValue(ID, "DOLAR");
                if (Objects.equals(dollarS, "###")) {
                    dollarV = getIndividualPrice(SettingsTableNames.GENERAL, tipoDolar);
                }else {
                    dollarV = Double.parseDouble(dollarS);
                }

                String profitS = attributesDBConnection.getAttributeValue(ID, "GANANCIA");
                double profitV = Double.parseDouble(profitS);


                return applyIVA((t1aV + t2aV) * dollarV * profitV, attributesDBConnection);

            }
            default:
                return new Pair<>(0.0, 0.0);
        }
    }

    private Pair<Double, Double> applyIVA(double productPrice, AttributesDatabaseConnection attributesDBConnection) {
        String ivaS = attributesDBConnection.getAttributeValue(ID, "IVA");
        double iva = ivaS == null || ivaS.isEmpty()  ? 0.0 : Double.parseDouble(ivaS);

        String rechargeS = attributesDBConnection.getAttributeValue(ID, "RECARGO");
        double recharge = rechargeS == null || rechargeS.isEmpty()  ? 0.0 : Double.parseDouble(rechargeS);

        double productWiva = productPrice + (productPrice * iva / 100);
        double productParticular = productWiva + (productWiva * recharge / 100);

        return new Pair<>(productWiva, productParticular);
    }
}
