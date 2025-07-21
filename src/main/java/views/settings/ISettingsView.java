package views.settings;

import org.javatuples.Pair;
import utils.MessageTypes;
import utils.databases.SettingsTableNames;
import views.IToggleableView;

import javax.swing.*;
import java.util.ArrayList;

public interface ISettingsView extends IToggleableView {

	void setModularTable(SettingsTableNames tableName, ArrayList<Pair<String, Double>> generalValues);

	ArrayList<Pair<String, Double>> tableToArrayList(SettingsTableNames tableName) throws NumberFormatException;

	JTable getModularTable(SettingsTableNames table);

	void showDetailedMessage(MessageTypes messageType, SettingsTableNames tableName);

	void addEmptyRow(SettingsTableNames tableName);
	String removeRow(SettingsTableNames tableName, int rowIndex);
}
