package views.home;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

public class HomeView implements IHomeView{

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

	public HomeView() {
		windowFrame = new JFrame("Gráfica Bahia");
		windowFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		windowFrame.setContentPane(containerPanel);
		windowFrame.pack();
		windowFrame.setLocationRelativeTo(null);
		windowFrame.setVisible(true);
		windowFrame.setIconImage(new ImageIcon("src/main/resources/BGLogo.png").getImage());
	}

	private void createUIComponents() {

		ImageIcon imageIcon = new ImageIcon("src/main/resources/BGLogo.png");

		iconLabel.setIcon(imageIcon);
	}

//give me a much simpler solution, without involving new classes and method redefinitions

}
