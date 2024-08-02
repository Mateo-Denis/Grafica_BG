package views;

import utils.MessageTypes;

public interface IToggableView {
	void start();
	void showView();
	void hideView();
	void setWorkingStatus();
	void setWaitingStatus();
	void showMessage(MessageTypes messageType);
}
