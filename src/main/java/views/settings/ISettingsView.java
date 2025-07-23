package views.settings;

import org.javatuples.Pair;
import utils.MessageTypes;
import utils.databases.SettingsTableNames;
import views.IToggleableView;

import javax.swing.*;
import java.util.ArrayList;

public interface ISettingsView extends IToggleableView {

	void setModularTable(SettingsTableNames tableName, ArrayList<Pair<String, Double>> generalValues, ArrayList<String> otherValues);

	ArrayList<Pair<String, Double>> generalTableToArrayList() throws NumberFormatException;

	ArrayList<String> normalTableToArrayList(SettingsTableNames tableName);

	JTable getModularTable(SettingsTableNames table);

	void showDetailedMessage(MessageTypes messageType, SettingsTableNames tableName);

	void addEmptyRow(SettingsTableNames tableName);
	String removeRow(SettingsTableNames tableName, int rowIndex);
}
