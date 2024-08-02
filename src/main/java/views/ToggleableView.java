package views;

import utils.MessageTypes;

import javax.swing.*;
import java.awt.*;

public abstract class ToggleableView {
	protected JFrame windowFrame;
	protected JPanel containerPanelWrapper;
	public void start(){
		initListeners();
		wrapContainer();
	}
	protected abstract void wrapContainer();
	protected abstract void initListeners();
	public void showView() {
		windowFrame.setVisible(true);
	}
	public void hideView() {
		windowFrame.setVisible(false);
	}
	public void setWorkingStatus() {
		for (Component c : containerPanelWrapper.getComponents()) c.setEnabled(false);
	}
	public void setWaitingStatus() {
		for (Component c : containerPanelWrapper.getComponents()) c.setEnabled(true);
	}
	public void showMessage(MessageTypes messageType) {
		JOptionPane.showMessageDialog(containerPanelWrapper, messageType.getMessage()
				, messageType.getTitle()
				, messageType.getMessageType());
	}










}
