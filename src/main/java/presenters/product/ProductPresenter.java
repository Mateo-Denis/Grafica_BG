package presenters.product;

import org.javatuples.Pair;
import presenters.StandardPresenter;
import utils.databases.SettingsTableNames;
import models.settings.ISettingsModel;

import java.util.ArrayList;

public abstract class ProductPresenter extends StandardPresenter {

    protected ISettingsModel settingsModel;

    public ProductPresenter() {
    }

    public abstract double getIndividualPrice(SettingsTableNames tableName, String selectedValue);

    public ArrayList<String> getOtherTablesAsArrayList(SettingsTableNames tableName) {
        return settingsModel.getModularValues(tableName);
    }

    public ArrayList<Pair<String, Double>> getGeneralTableAsArrayList(SettingsTableNames tableName) {
        return settingsModel.getGeneralTableAsList(tableName);
    }
}

