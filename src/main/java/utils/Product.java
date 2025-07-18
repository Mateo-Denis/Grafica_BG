package utils;

import lombok.Getter;
import org.javatuples.Pair;
import utils.databases.AttributesDatabaseConnection;
import utils.databases.SettingsDatabaseConnection;
import utils.databases.SettingsTableNames;

import java.util.Objects;

import static utils.TextUtils.truncateAndRound;

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

                double plPrice = Double.parseDouble(attributesDBConnection.getAttributeValue(ID, "PRECIO_BAJADA"));
                double plAmount = Double.parseDouble(attributesDBConnection.getAttributeValue(ID, "CANTIDAD_BAJADA"));

                double printingPrice = Double.parseDouble(attributesDBConnection.getAttributeValue(ID, "PRECIO_IMP"));
                double printingAmount = Double.parseDouble(attributesDBConnection.getAttributeValue(ID, "CANTIDAD_IMP"));

                double capV = Double.parseDouble(attributesDBConnection.getAttributeValue(ID, "TAZA"));

                double profit = Double.parseDouble(attributesDBConnection.getAttributeValue(ID, "GANANCIA")) / 100;


                return applyIVA(((plPrice * plAmount) + (printingPrice * printingAmount) + capV) * profit, attributesDBConnection);            }
            case 2:{//gorra

                double plPrice = Double.parseDouble(attributesDBConnection.getAttributeValue(ID, "PRECIO_BAJADA"));
                double plAmount = Double.parseDouble(attributesDBConnection.getAttributeValue(ID, "CANTIDAD_BAJADA"));

                double printingPrice = Double.parseDouble(attributesDBConnection.getAttributeValue(ID, "PRECIO_IMP"));
                double printingAmount = Double.parseDouble(attributesDBConnection.getAttributeValue(ID, "CANTIDAD_IMP"));

                double capV = Double.parseDouble(attributesDBConnection.getAttributeValue(ID, "GORRA"));

                double profit = Double.parseDouble(attributesDBConnection.getAttributeValue(ID, "GANANCIA")) / 100;

                return applyIVA(((plPrice * plAmount) + (printingPrice * printingAmount) + capV) * profit, attributesDBConnection);
            }
            case 3: { //tela
                String tela = attributesDBConnection.getAttributeValue(ID, "TELA");

                double clothPrice =Double.parseDouble(attributesDBConnection.getAttributeValue(ID, "PRECIO_TELA"));

                double profit = Double.parseDouble(attributesDBConnection.getAttributeValue(ID, "GANANCIA")) / 100;

                return applyIVA(clothPrice * profit, attributesDBConnection);
            }

            case 4: { //bandera

            }
            case 5: { //prendas

                double plPrice = Double.parseDouble(attributesDBConnection.getAttributeValue(ID, "PRECIO_BAJADA"));
                double plAmount = Double.parseDouble(attributesDBConnection.getAttributeValue(ID, "CANTIDAD_BAJADA"));

                double printingPrice = Double.parseDouble(attributesDBConnection.getAttributeValue(ID, "PRECIO_IMP"));
                double printingAmount = Double.parseDouble(attributesDBConnection.getAttributeValue(ID, "CANTIDAD_IMP"));

                double clothPrice = Double.parseDouble(attributesDBConnection.getAttributeValue(ID, "PRECIO_TELA"));
                double clothAmount = Double.parseDouble(attributesDBConnection.getAttributeValue(ID, "CANTIDAD_TELA"));

                double seamstressV = Double.parseDouble(attributesDBConnection.getAttributeValue(ID, "COSTURERA"));

                double profit = Double.parseDouble(attributesDBConnection.getAttributeValue(ID, "GANANCIA")) / 100;

                return applyIVA(((plPrice * plAmount) + (printingPrice * printingAmount) + (clothPrice * clothAmount) + seamstressV) * profit, attributesDBConnection);

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

                double profit = Double.parseDouble(attributesDBConnection.getAttributeValue(ID, "GANANCIA")) / 100;

                return applyIVA(t1aV * profit, attributesDBConnection);
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
                double profitV = Double.parseDouble(profitS) / 100;

                return applyIVA((t1aV + t2aV) * profitV, attributesDBConnection);
            }
            case 9: {

                double materialPrice = Double.parseDouble(attributesDBConnection.getAttributeValue(ID, "PRECIO_MATERIAL"));

                double inkPrice = Double.parseDouble(attributesDBConnection.getAttributeValue(ID, "PRECIO_TINTA"));

                String tipoDolar = attributesDBConnection.getAttributeValue(ID, "TIPO_DOLAR");
                double dollarV;
                String dollarS = attributesDBConnection.getAttributeValue(ID, "DOLAR");
                if (Objects.equals(dollarS, "###")) {
                    dollarV = getIndividualPrice(SettingsTableNames.GENERAL, tipoDolar);
                }else {
                    dollarV = Double.parseDouble(dollarS);
                }

                double profit = Double.parseDouble(attributesDBConnection.getAttributeValue(ID, "GANANCIA")) / 100;


                return applyIVA((materialPrice + inkPrice) * dollarV * profit, attributesDBConnection);

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

        return new Pair<>(Double.parseDouble(truncateAndRound(String.valueOf(productWiva))), Double.parseDouble(truncateAndRound(String.valueOf(productParticular))));
    }
}
