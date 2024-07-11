package views.home;

import javax.swing.*;
import java.security.PublicKey;

public class HomeView implements IHomeView{

	private JFrame windowFrame;
	private JPanel containerPanel;

	public HomeView() {
		windowFrame = new JFrame("Grafica Bahia");
		windowFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		windowFrame.pack();
		windowFrame.setLocationRelativeTo(null);
		windowFrame.setVisible(true);
	}


}
