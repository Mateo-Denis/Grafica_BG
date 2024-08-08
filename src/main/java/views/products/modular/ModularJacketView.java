package views.products.modular;

import javax.swing.*;

public class ModularJacketView extends JPanel implements IModularCategoryView {
	private JPanel containerPanel;
	private JRadioButton adultRadioButton;
	private JRadioButton kidRadioButton;
	private JRadioButton sportRadioButton;
	private JRadioButton cottonRadioButton;
	private JRadioButton kettenRadioButton;

	@Override
	public JPanel getContainerPanel() {
		return containerPanel;
	}
}
