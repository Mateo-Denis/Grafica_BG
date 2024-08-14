package views.products.modular;

import javax.swing.*;
import java.util.ArrayList;

public class ModularFlagView extends JPanel implements IModularCategoryView {
	private JPanel containerPanel;
	private JComboBox clothComboBox;
	private JLabel clothLabel;
	private JComboBox sizeComboBox;
	private JLabel sizeLabel;

	@Override
	public JPanel getContainerPanel() {
		return containerPanel;
	}

	@Override
	public ArrayList<String> getModularAttributes() {
		return null;
	}
}
