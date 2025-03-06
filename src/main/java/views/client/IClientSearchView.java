package views.client;

import views.IToggleableView;

import javax.swing.*;

public interface IClientSearchView extends IToggleableView {
	String getnameSearchText();

	String getSelectedCity();

	void setTableValueAt(int row, int col, String value);

	void addCityToComboBox(String city);

	void clearTable();

	boolean isCityInComboBox(String lastCityAdded);

	int getSelectedTableRow();

    void deselectAllRows();

	JTable getClientResultTable();

	void clearView();

	String getClientStringTableValueAt(int row, int col);

}
