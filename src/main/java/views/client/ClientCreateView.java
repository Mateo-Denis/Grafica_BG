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
	private ClientCreatePresenter presenter;

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
	public String getClient() {
		return clientTextField.getText();
	}

	@Override
	public String getAddress() {
		return addressTextField.getText();
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
