package views.settings;

import presenters.StandardPresenter;
import presenters.settings.SettingsPresenter;
import views.ToggleableView;

import javax.swing.*;

public class SettingsView extends ToggleableView implements ISettingsView {
	private JPanel containerPanel;
	private JPanel generalValuesPanel;
	private JTextField plankLoweringTextField;
	private JPanel clothValuesPanel;
	private JTable clothValuesTable;
	private JTextField capTextField;
	private JTextField cupTextField;
	private JPanel clothesValuesPanel;
	private JTable clothesValuesTable;
	private JButton updateDataButton;
	private JTextField inkTextField;
	private JLabel inkLabel;
	private JTextField seamstressTextField;
	private JLabel seamstressLabel;
	private JLabel plankLoweringLabel;
	private JLabel capLabel;
	private JLabel cupLabel;
	private JLabel dollarLabel;
	private JTextField dollarTextField;
	private SettingsPresenter settingsPresenter;


	public SettingsView() {
		windowFrame = new JFrame("Configuración");
		windowFrame.setContentPane(containerPanel);
		windowFrame.pack();
		windowFrame.setLocationRelativeTo(null);
		windowFrame.setIconImage(new ImageIcon("src/main/resources/BGLogo.png").getImage());
		wrapContainer();
	}
	@Override
	public void setPresenter(StandardPresenter standardPresenter) {
		this.settingsPresenter = (SettingsPresenter) standardPresenter;
	}

	@Override
	protected void wrapContainer() {
		containerPanelWrapper = containerPanel;
	}

	@Override
	protected void initListeners() {
		updateDataButton.addActionListener(e -> settingsPresenter.onUpdateDataButtonPressed());
	}

	@Override
	public void clearView() {

	}

	@Override
	public String getDollarValue() { return dollarTextField.getText(); }
	@Override
	public String getPlankLoweringValue() {
		return plankLoweringTextField.getText();
	}

	@Override
	public String getCapValue() {
		return capTextField.getText();
	}

	@Override
	public String getCupValue() {
		return cupTextField.getText();
	}

	@Override
	public String getInkValue() {
		return inkTextField.getText();
	}

	@Override
	public String getSeamstressValue() {
		return seamstressTextField.getText();
	}

	@Override
	public JTable getClothTable() {
		return clothValuesTable;
	}

	@Override
	public JTable getClothesTable() {
		return clothesValuesTable;
	}

	@Override
	public void setDollarValue(String value) { dollarTextField.setText(value); }
	@Override
	public void setPlankLoweringValue(String value) {
		plankLoweringTextField.setText(value);
	}
	@Override
	public void setCapValue(String value) {
		capTextField.setText(value);
	}
	@Override
	public void setCupValue(String value) {
		cupTextField.setText(value);
	}
	@Override
	public void setInkValue(String value) {
		inkTextField.setText(value);
	}
	@Override
	public void setSeamstressValue(String value) {
		seamstressTextField.setText(value);
	}
	@Override
	public void setClothTableValue(String value, int row, int column) {
		clothValuesTable.setValueAt(value, row, column);
	}
	@Override
	public void setClothesTableValue(String value, int row, int column) {
		clothValuesTable.setValueAt(value, row, column);
	}
}
