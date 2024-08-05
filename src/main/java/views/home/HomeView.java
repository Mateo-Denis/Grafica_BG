package views.home;

import presenters.client.ClientCreatePresenter;
import presenters.client.ClientSearchPresenter;
import presenters.product.ProductSearchPresenter;

import javax.swing.*;
//30-7-2024 --> SE AGREGA EL IMPORT "java.awt.event.ActionListener":

import java.awt.event.ActionListener;

public class HomeView extends JFrame implements IHomeView {

	private JFrame windowFrame;
	private JPanel containerPanel;
	private JButton productCreateButton;
	private JButton productSearchButton;
	private JButton budgetCreateButton;
	private JButton budgetSearchButton;
	private JButton clientCreateButton;
	private JButton clientSearchButton;
	private JPanel budgetPanel;
	private JPanel iconPanel;
	private JPanel clientPanel;
	private JPanel productPanel;
	private JButton categorySearchButton;
	private JButton categoryCreateButton;
	private JPanel categoryPanel;
	private JLabel iconLabel;
	private JButton settingsButton;
	private final ClientCreatePresenter clientCreatePresenter;
	private final ClientSearchPresenter clientSearchPresenter;
	private final ProductSearchPresenter productSearchPresenter;

	public HomeView(ClientCreatePresenter clientCreatePresenter, ClientSearchPresenter clientSearchPresenter, ProductSearchPresenter productSearchPresenter) {

		windowFrame = new JFrame("Gráfica Bahia");
		windowFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		windowFrame.setContentPane(containerPanel);
		windowFrame.pack();
		windowFrame.setLocationRelativeTo(null);
		windowFrame.setVisible(true);
		windowFrame.setIconImage(new ImageIcon("src/main/resources/BGLogo.png").getImage());

		this.clientCreatePresenter = clientCreatePresenter;
		this.clientSearchPresenter = clientSearchPresenter;
		this.productSearchPresenter = productSearchPresenter;

		initListeners();
	}

	protected void initListeners() {
		clientCreateButton.addActionListener(e -> clientCreatePresenter.onHomeCreateClientButtonClicked());
		clientSearchButton.addActionListener(e -> clientSearchPresenter.onHomeSearchClientButtonClicked());
		clientCreateButton.addActionListener(e -> clientCreatePresenter.onMainCreateClientButtonClicked());
		clientSearchButton.addActionListener(e -> clientSearchPresenter.onSearchClientButtonClicked());
		productSearchButton.addActionListener(e -> productSearchPresenter.onSearchProductButtonClicked());



	}
}
