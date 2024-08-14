package views.products.modular;

import javax.swing.*;
import java.util.ArrayList;

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

	@Override
	public ArrayList<String> getModularAttributes() {
		return null;
	}
}
