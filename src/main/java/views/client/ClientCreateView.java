package views.client;

import javax.swing.*;

import presenters.StandardPresenter;
import presenters.client.ClientCreatePresenter;
import views.ToggleableView;

import java.util.ArrayList;

public class ClientCreateView extends ToggleableView implements IClientCreateView {
	private JPanel containerPanel;
	private JLabel clientLabel;
	private JButton createButton;
	private JTextField clientTextField;
	private JLabel addressLabel;
	private JTextField addressTextField;
	private JLabel cityLabel;
	private JTextField cityTextField;
	private JLabel phoneLabel;
	private JTextField phoneTextField;
	private JRadioButton clientRadioButton;
	private JRadioButton particularRadioButton;
	private JComboBox cityComboBox;
	private ClientCreatePresenter clientCreatePresenter;

	public ClientCreateView() {
		windowFrame = new JFrame("Crear Cliente");
		windowFrame.setContentPane(containerPanel);
		windowFrame.pack();
		windowFrame.setLocationRelativeTo(null);
		windowFrame.setIconImage(new ImageIcon("src/main/resources/BGLogo.png").getImage());
		windowFrame.setSize(400, 300);
	}
	@Override
	public String getClientText() {
		return clientTextField.getText();
	}
	@Override
	public String getAddressText() {
		return addressTextField.getText();
	}
	@Override
	public String getCityText() {
		return cityTextField.getText();
	}
	public String getComboBoxSelectedCity() {
		return (String) cityComboBox.getSelectedItem();
	}
	@Override
	public String getPhoneText() {
		return phoneTextField.getText();
	}
	@Override
	public boolean isClientSelected() {
		return clientRadioButton.isSelected();
	}


	@Override
	public void setPresenter(StandardPresenter clientCreatePresenter) {
		this.clientCreatePresenter = (ClientCreatePresenter) clientCreatePresenter;
	}

	protected void initListeners() {
		createButton.addActionListener(e -> clientCreatePresenter.onCreateButtonClicked());

		//TESTING EMPTY FIELDS
		createButton.addItemListener(e -> clientCreatePresenter.onEmptyFields(clientTextField, cityTextField));
	}

	@Override
	public void clearView() {
		clientTextField.setText("");
		addressTextField.setText("");
		cityTextField.setText("");
		phoneTextField.setText("");
		clientRadioButton.setSelected(true);
		particularRadioButton.setSelected(false);
	}

	protected void wrapContainer() {
		containerPanelWrapper = containerPanel;
	}

	public JTextField getClientTextField() {
        return clientTextField;
    }

	public void addCityToComboBox(String city) {
		cityComboBox.addItem(city);
	}

    public JTextField getCityTextField() {
        return cityTextField;
    }

}