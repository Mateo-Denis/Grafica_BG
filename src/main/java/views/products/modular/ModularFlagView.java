package views.products.modular;

import javax.swing.*;

public class ModularFlagView extends JComponent implements IModularCategoryView {
	private JPanel containerPanel;
	private JComboBox clothComboBox;
	private JLabel clothLabel;
	private JComboBox sizeComboBox;
	private JLabel sizeLabel;

	@Override
	public JPanel getContainerPanel() {
		return containerPanel;
	}
}
