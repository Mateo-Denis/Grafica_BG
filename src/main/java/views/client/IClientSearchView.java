package views.client;

import views.IToggleableView;

public interface IClientSearchView extends IToggleableView {
	String getSearchText();

	String getSelectedAddress();
}
