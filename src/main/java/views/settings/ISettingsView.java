package views.settings;

import org.javatuples.Pair;
import utils.databases.SettingsTableNames;
import views.IToggleableView;

import javax.swing.*;
import java.util.ArrayList;

public interface ISettingsView extends IToggleableView {

	void setModularTable(SettingsTableNames tableName, ArrayList<Pair<String, Double>> generalValues);

	JTable getModularTable(SettingsTableNames table);
}
