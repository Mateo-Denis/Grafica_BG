package views.products.modular;

import javax.swing.*;

public class ModularRemerasView extends JPanel implements IModularCategoryView {
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
