package views.client;

import views.IToggleableView;

public interface IClientSearchView extends IToggleableView {
	String getnameSearchText();

	String getSelectedCity();

	void setTableValueAt(int row, int col, String value);

}
