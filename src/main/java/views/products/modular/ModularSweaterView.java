package views.products.modular;

import javax.swing.*;

public class ModularSweaterView extends JPanel implements IModularCategoryView {
	private JPanel containerPanel;
	private JRadioButton roundNeckRadioButton;
	private JRadioButton hoodieRadioButton;
	private JRadioButton adultRadioButton;
	private JRadioButton kidRadioButton;
	private JRadioButton sportRadioButton;
	private JRadioButton kettenRadioButton;
	private JRadioButton cottonRadioButton;

	@Override
	public JPanel getContainerPanel() {
		return containerPanel;
	}
}
