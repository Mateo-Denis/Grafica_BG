package views.client;

import views.IToggleableView;

import javax.swing.*;
import java.util.ArrayList;

public interface IClientSearchView extends IToggleableView {
	String getnameSearchText();

	String getSelectedCity();

	void setTableValueAt(int row, int col, String value);

	void addCityToComboBox(String city);

	void clearTable();

	boolean isCityInComboBox(String lastCityAdded);

	int getSelectedTableRow();

    void deselectAllRows();

	String getSelectedClientName();

	ArrayList<String> getMultipleSelectedClientNames();

	JTable getClientResultTable();

	void clearView();

	String getClientStringTableValueAt(int row, int col);

	int getClientIntTableValueAt(int row, int col);

	Double getClientDoubleTableValueAt(int row, int col);

}
