package views.products.modular;

import javax.swing.*;

public class ModularClothView extends JComponent implements IModularCategoryView {
	private JPanel containerPanel;
	private JComboBox clothComboBox;
	private JLabel clothLabel;

	@Override
	public JPanel getContainerPanel() {
		return containerPanel;
	}
}
