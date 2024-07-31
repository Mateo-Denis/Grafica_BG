package views.client;

import javax.swing.*;
import presenters.ClientCreatePresenter;

public class ClientCreateView  extends JDialog implements IClientCreateView {
	private JFrame windowFrame;
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
	private ClientCreatePresenter clientCreatePresenter;

	public ClientCreateView() {
		windowFrame = new JFrame("Crear Cliente");
		windowFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		windowFrame.setContentPane(containerPanel);
		windowFrame.pack();
		windowFrame.setLocationRelativeTo(null);
		windowFrame.setVisible(true);
		windowFrame.setIconImage(new ImageIcon("src/main/resources/BGLogo.png").getImage());
	}

	@Override
	public String getClientTextField() {
		return clientTextField.getText();
	}
	@Override
	public String getAddressTextField() {
		return addressTextField.getText();
	}
	@Override
	public String getCityTextField() {
		return cityTextField.getText();
	}
	@Override
	public String getPhoneTextField() {
		return phoneTextField.getText();
	}
	@Override
	public boolean isClientRadioButtonSelected() {
		return clientRadioButton.isSelected();
	}

	public void start(){
		initListeners();
	}

	public void setPresenter(ClientCreatePresenter clientCreatePresenter) {
		this.clientCreatePresenter = clientCreatePresenter;
	}

	private void initListeners() {
		createButton.addActionListener(e -> clientCreatePresenter.onCreateButtonClicked());
	}





	@Override
	public void showSuccessMessage() {
		JOptionPane.showMessageDialog(this, "Cliente creado con éxito!");
		dispose();
	}

	@Override
	public void showErrorMessage(String message) {
		JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
	}




}
