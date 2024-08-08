package views.products.modular;

import javax.swing.*;

public class ModularCapView extends JPanel implements IModularCategoryView {
	private JPanel containerPanel;
	private JRadioButton whiteFrontRadioButton;
	private JRadioButton sublimatedRadioButton;
	private JRadioButton visorStampRadioButton;

	@Override
	public JPanel getContainerPanel() {
		return containerPanel;
	}
}
