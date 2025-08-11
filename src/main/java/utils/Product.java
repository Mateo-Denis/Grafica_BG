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
                String tipoCambio = attributesDBConnection.getAttributeValue(ID, "TIPO_CAMBIO");

                double dollarV;
                String dollarS = attributesDBConnection.getAttributeValue(ID, "VALOR_TIPO_CAMBIO");
                if (Objects.equals(dollarS, "###")) {
                    dollarV = getIndividualPrice(SettingsTableNames.GENERAL, tipoCambio);
                }else {
                    dollarV = Double.parseDouble(dollarS);
                }
                double price = ((plPrice * plAmount) + (printingPrice * printingAmount) + capV) * dollarV;
                return applyIVA((price + price * profit), attributesDBConnection);            }
            case 2:{//gorra

                double plPrice = Double.parseDouble(attributesDBConnection.getAttributeValue(ID, "PRECIO_BAJADA"));
                double plAmount = Double.parseDouble(attributesDBConnection.getAttributeValue(ID, "CANTIDAD_BAJADA"));

                double printingPrice = Double.parseDouble(attributesDBConnection.getAttributeValue(ID, "PRECIO_IMP"));
                double printingAmount = Double.parseDouble(attributesDBConnection.getAttributeValue(ID, "CANTIDAD_IMP"));

                double capV = Double.parseDouble(attributesDBConnection.getAttributeValue(ID, "GORRA"));
                String tipoCambio = attributesDBConnection.getAttributeValue(ID, "TIPO_CAMBIO");

                double dollarV;
                String dollarS = attributesDBConnection.getAttributeValue(ID, "VALOR_TIPO_CAMBIO");
                if (Objects.equals(dollarS, "###")) {
                    dollarV = getIndividualPrice(SettingsTableNames.GENERAL, tipoCambio);
                }else {
                    dollarV = Double.parseDouble(dollarS);
                }
                double profit = Double.parseDouble(attributesDBConnection.getAttributeValue(ID, "GANANCIA")) / 100;
                double price = ((plPrice * plAmount) + (printingPrice * printingAmount) + capV) * dollarV;
                return applyIVA(price + (price * profit), attributesDBConnection);
            }
            case 3: { //tela

                double clothPrice =Double.parseDouble(attributesDBConnection.getAttributeValue(ID, "PRECIO_TELA"));

                double profit = Double.parseDouble(attributesDBConnection.getAttributeValue(ID, "GANANCIA")) / 100;
                String tipoCambio = attributesDBConnection.getAttributeValue(ID, "TIPO_CAMBIO");

                double dollarV;
                String dollarS = attributesDBConnection.getAttributeValue(ID, "VALOR_TIPO_CAMBIO");
                if (Objects.equals(dollarS, "###")) {
                    dollarV = getIndividualPrice(SettingsTableNames.GENERAL, tipoCambio);
                }else {
                    dollarV = Double.parseDouble(dollarS);
                }
                return applyIVA((clothPrice + clothPrice * profit) * dollarV, attributesDBConnection);
            }

            case 4: { //bandera
                double clothPrice = Double.parseDouble(attributesDBConnection.getAttributeValue(ID, "PRECIO_TELA"));

                double printingPrice = Double.parseDouble(attributesDBConnection.getAttributeValue(ID, "PRECIO_IMP"));
                double printingAmount = Double.parseDouble(attributesDBConnection.getAttributeValue(ID, "CANTIDAD_IMP"));

                double seamstressPrice = Double.parseDouble(attributesDBConnection.getAttributeValue(ID, "COSTURERA"));

                double plankPrice = Double.parseDouble(attributesDBConnection.getAttributeValue(ID, "PRECIO_BAJADA"));
                double plankAmount = Double.parseDouble(attributesDBConnection.getAttributeValue(ID, "CANTIDAD_BAJADA"));

                double profit = Double.parseDouble(attributesDBConnection.getAttributeValue(ID, "GANANCIA")) / 100;
                String tipoCambio = attributesDBConnection.getAttributeValue(ID, "TIPO_CAMBIO");

                double dollarV;
                String dollarS = attributesDBConnection.getAttributeValue(ID, "VALOR_TIPO_CAMBIO");
                if (Objects.equals(dollarS, "###")) {
                    dollarV = getIndividualPrice(SettingsTableNames.GENERAL, tipoCambio);
                }else {
                    dollarV = Double.parseDouble(dollarS);
                }
                double clothWidth = Double.parseDouble(attributesDBConnection.getAttributeValue(ID, "ANCHO_TELA"));
                double price = ((clothPrice * clothWidth) + (printingPrice * printingAmount) + seamstressPrice + (plankPrice * plankAmount)) * dollarV;
                return applyIVA(price + (price * profit), attributesDBConnection);

            }
            case 5: { //prendas

                double plPrice = Double.parseDouble(attributesDBConnection.getAttributeValue(ID, "PRECIO_BAJADA"));
                double plAmount = Double.parseDouble(attributesDBConnection.getAttributeValue(ID, "CANTIDAD_BAJADA"));

                double printingPrice = Double.parseDouble(attributesDBConnection.getAttributeValue(ID, "PRECIO_IMP"));
                double printingAmount = Double.parseDouble(attributesDBConnection.getAttributeValue(ID, "CANTIDAD_IMP"));

                double clothPrice = Double.parseDouble(attributesDBConnection.getAttributeValue(ID, "PRECIO_TELA"));
                double clothAmount = Double.parseDouble(attributesDBConnection.getAttributeValue(ID, "CANTIDAD_TELA"));

                double seamstressV = Double.parseDouble(attributesDBConnection.getAttributeValue(ID, "COSTURERA"));
                String tipoCambio = attributesDBConnection.getAttributeValue(ID, "TIPO_CAMBIO");

                double dollarV;
                String dollarS = attributesDBConnection.getAttributeValue(ID, "VALOR_TIPO_CAMBIO");
                if (Objects.equals(dollarS, "###")) {
                    dollarV = getIndividualPrice(SettingsTableNames.GENERAL, tipoCambio);
                }else {
                    dollarV = Double.parseDouble(dollarS);
                }
                double profit = Double.parseDouble(attributesDBConnection.getAttributeValue(ID, "GANANCIA")) / 100;
                double price = ((plPrice * plAmount) + (printingPrice * printingAmount) + (clothPrice * clothAmount) + seamstressV) * dollarV;
                return applyIVA(price + (price * profit), attributesDBConnection);

            }
            case 6: {
                double servicePrice = Double.parseDouble(attributesDBConnection.getAttributeValue(ID, "SERVICIO"));
                String tipoCambio = attributesDBConnection.getAttributeValue(ID, "TIPO_CAMBIO");

                double dollarV;
                String dollarS = attributesDBConnection.getAttributeValue(ID, "VALOR_TIPO_CAMBIO");
                if (Objects.equals(dollarS, "###")) {
                    dollarV = getIndividualPrice(SettingsTableNames.GENERAL, tipoCambio);
                }else {
                    dollarV = Double.parseDouble(dollarS);
                }
                return applyIVA(servicePrice * dollarV, attributesDBConnection);
            }
            case 7: { //servicio de corte
                double vinylPrice = Double.parseDouble(attributesDBConnection.getAttributeValue(ID, "PRECIO_VINILO"));

                double profit = Double.parseDouble(attributesDBConnection.getAttributeValue(ID, "GANANCIA")) / 100;
                String tipoCambio = attributesDBConnection.getAttributeValue(ID, "TIPO_CAMBIO");

                double dollarV;
                String dollarS = attributesDBConnection.getAttributeValue(ID, "VALOR_TIPO_CAMBIO");
                if (Objects.equals(dollarS, "###")) {
                    dollarV = getIndividualPrice(SettingsTableNames.GENERAL, tipoCambio);
                }else {
                    dollarV = Double.parseDouble(dollarS);
                }
                return applyIVA((vinylPrice + (vinylPrice * profit)) * dollarV, attributesDBConnection);
            }
            case 8: { // impresión lineal
                double paperPrice = Double.parseDouble(attributesDBConnection.getAttributeValue(ID, "PRECIO_PAPEL"));
                double inkPrice =   Double.parseDouble(attributesDBConnection.getAttributeValue(ID, "PRECIO_TINTA"));
                double profit =     Double.parseDouble(attributesDBConnection.getAttributeValue(ID, "GANANCIA")) / 100;
                String tipoCambio = attributesDBConnection.getAttributeValue(ID, "TIPO_CAMBIO");

                double dollarV;
                String dollarS = attributesDBConnection.getAttributeValue(ID, "VALOR_TIPO_CAMBIO");
                if (Objects.equals(dollarS, "###")) {
                    dollarV = getIndividualPrice(SettingsTableNames.GENERAL, tipoCambio);
                }else {
                    dollarV = Double.parseDouble(dollarS);
                }
                double price = ((paperPrice + inkPrice)) * dollarV;
                return applyIVA(price + (price * profit), attributesDBConnection);
            }
            case 9: { // impresion por metro cuadrado

                double materialPrice = Double.parseDouble(attributesDBConnection.getAttributeValue(ID, "PRECIO_MATERIAL"));
                double inkPrice = Double.parseDouble(attributesDBConnection.getAttributeValue(ID, "PRECIO_TINTA"));
                String tipoCambio = attributesDBConnection.getAttributeValue(ID, "TIPO_CAMBIO");

                double dollarV;
                String dollarS = attributesDBConnection.getAttributeValue(ID, "VALOR_TIPO_CAMBIO");
                if (Objects.equals(dollarS, "###")) {
                    dollarV = getIndividualPrice(SettingsTableNames.GENERAL, tipoCambio);
                }else {
                    dollarV = Double.parseDouble(dollarS);
                }

                double profit = Double.parseDouble(attributesDBConnection.getAttributeValue(ID, "GANANCIA")) / 100;

                double price = (materialPrice + inkPrice) * dollarV;
                return applyIVA(price + (price * profit), attributesDBConnection);

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
