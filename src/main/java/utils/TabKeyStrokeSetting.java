package utils;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class TabKeyStrokeSetting {

	public static void applyTabKeyStrokeSettingsToTextArea(JTextArea textArea) {
		// Remove TAB from input map
		textArea.getInputMap().put(
				KeyStroke.getKeyStroke("TAB"),
				"focusNext"
		);

		// Same for Shift+TAB (go backwards)
		textArea.getInputMap().put(
				KeyStroke.getKeyStroke("shift TAB"),
				"focusPrevious"
		);

		// Map actions to focus traversal
		textArea.getActionMap().put("focusNext", new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				textArea.transferFocus();
			}
		});

		textArea.getActionMap().put("focusPrevious", new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				textArea.transferFocusBackward();
			}
		});

	}

	public static void applyTabKeyStrokeSettingsToTable(JTable table){
		table.getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT)
				.put(KeyStroke.getKeyStroke("TAB"), "focusNext");

		table.getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT)
				.put(KeyStroke.getKeyStroke("shift TAB"), "focusPrevious");

		table.getActionMap().put("focusNext", new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				table.transferFocus();
			}
		});

		table.getActionMap().put("focusPrevious", new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				table.transferFocusBackward();
			}
		});

	}
}
