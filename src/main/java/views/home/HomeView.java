package views.home;

import presenters.client.ClientCreatePresenter;
import presenters.client.ClientSearchPresenter;
import presenters.product.ProductSearchPresenter;
import presenters.product.ProductCreatePresenter;
import presenters.budget.BudgetSearchPresenter;
import presenters.budget.BudgetCreatePresenter;

import javax.swing.*;
import java.awt.*;

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
	private final ProductCreatePresenter productCreatePresenter;
	private final BudgetSearchPresenter budgetSearchPresenter;
	private final BudgetCreatePresenter budgetCreatePresenter;

	public HomeView(ClientCreatePresenter clientCreatePresenter, ClientSearchPresenter clientSearchPresenter, ProductSearchPresenter productSearchPresenter, ProductCreatePresenter productCreatePresenter, BudgetSearchPresenter budgetSearchPresenter, BudgetCreatePresenter budgetCreatePresenter) {

		windowFrame = new JFrame("Gráfica Bahia");
		windowFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		windowFrame.setContentPane(containerPanel);
		windowFrame.pack();
		windowFrame.setLocationRelativeTo(null);
		windowFrame.setVisible(true);
		windowFrame.setIconImage(new ImageIcon("src/main/resources/BGLogo.png").getImage());

		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

		// Calculate the x and y position
		int x = 50;  // 50 pixels from the left side of the screen
		int y = (screenSize.height - windowFrame.getHeight()) / 2;  // Centered vertically

		// Set the location of the frame
		windowFrame.setLocation(x, y);

		this.clientCreatePresenter = clientCreatePresenter;
		this.clientSearchPresenter = clientSearchPresenter;
		this.productSearchPresenter = productSearchPresenter;
		this.productCreatePresenter = productCreatePresenter;
		this.budgetCreatePresenter = budgetCreatePresenter;
		this.budgetSearchPresenter = budgetSearchPresenter;

		initListeners();
	}

	protected void initListeners() {

		productSearchButton.addActionListener(e -> productSearchPresenter.onHomeSearchProductButtonClicked());
		productCreateButton.addActionListener(e -> productCreatePresenter.onHomeCreateProductButtonClicked());

		clientCreateButton.addActionListener(e -> clientCreatePresenter.onHomeCreateClientButtonClicked());
		clientSearchButton.addActionListener(e -> clientSearchPresenter.onHomeSearchClientButtonClicked());

		budgetCreateButton.addActionListener(e -> budgetCreatePresenter.onHomeCreateBudgetButtonClicked());
		budgetSearchButton.addActionListener(e -> budgetSearchPresenter.onHomeSearchBudgetButtonClicked());
	}
}
