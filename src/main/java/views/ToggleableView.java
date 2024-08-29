package views;

import utils.MessageTypes;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public abstract class ToggleableView extends JFrame {
	protected JFrame windowFrame;
	protected JPanel containerPanelWrapper;
	public void start(){
		initListeners();
		wrapContainer();
		windowFrame.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				clearView();
			}
		});
	}
	protected abstract void wrapContainer();
	protected abstract void initListeners();
	public abstract void clearView();

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
