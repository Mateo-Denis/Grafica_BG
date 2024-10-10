package views.client;

import javax.swing.*;
import javax.swing.text.AbstractDocument;

import presenters.StandardPresenter;
import presenters.client.ClientCreatePresenter;
import views.ToggleableView;
import utils.NumberInputVerifier;

public class ClientCreateView extends ToggleableView implements IClientCreateView {
	private JPanel containerPanel;
	private JPanel clientContainer;
	private JButton createButton;
	private JTextField clientTextField;
	private JLabel clientLabel;
	private JPanel addressContainer;
	private JTextField addressTextField;
	private JPanel cityContainer;
	private JLabel cityLabel;
	private JTextField cityTextField;
	private JPanel phoneContainer;
	private JLabel phoneLabel;
	private JTextField phoneTextField;
	private JRadioButton clientRadioButton;
	private JRadioButton particularRadioButton;
	private JPanel buttonsContainer;
	private JPanel radioButtonsContainer;
	private JPanel cityFieldsContainer;
	private JComboBox<String> cityComboBox;
	private ClientCreatePresenter clientCreatePresenter;

	public ClientCreateView() {
		windowFrame = new JFrame("Crear Cliente");
		windowFrame.setContentPane(containerPanel);
		windowFrame.pack();
		windowFrame.setLocationRelativeTo(null);
		windowFrame.setIconImage(new ImageIcon("src/main/resources/BGLogo.png").getImage());
		windowFrame.setSize(400, 300);
		windowFrame.setResizable(false);
		((AbstractDocument) phoneTextField.getDocument()).setDocumentFilter(new NumberInputVerifier());

		cambiarTamanioFuente(containerPanel, 14);
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
		createButton.addItemListener(e -> clientCreatePresenter.onEmptyFields(clientTextField, cityTextField, cityComboBox));
		cityComboBox.addActionListener(e -> clientCreatePresenter.onCityComboBoxSelected());
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

	public JComboBox<String> getCityComboBox() {
		return cityComboBox;
	}

}