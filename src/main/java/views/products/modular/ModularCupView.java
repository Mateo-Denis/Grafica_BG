package views.products.modular;

import javax.swing.*;

public class ModularCupView extends JPanel implements IModularCategoryView  {
	private JPanel containerPanel;
	private JRadioButton ceramicRadioButton;
	private JRadioButton plasticRadioButton;
	private JRadioButton whiteRadioButton;
	private JRadioButton sublimatedRadioButton;

	@Override
	public JPanel getContainerPanel() {
		return containerPanel;
	}


}
