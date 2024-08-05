package views.products.modular;

import javax.swing.*;

public class ModularTShirtView extends JComponent implements IModularCategoryView {
	private JRadioButton tshirtRadioButton;
	private JRadioButton chombaRadioButton;
	private JComboBox materialComboBox;
	private JLabel materialLabel;
	private JRadioButton shortSleeveRadioButton;
	private JRadioButton longSleeveRadioButton;
	private JPanel containerPanel;

	@Override
	public JPanel getContainerPanel() {
		return containerPanel;
	}
}
