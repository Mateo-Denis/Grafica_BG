package views.products.modular;

import javax.swing.*;
import java.util.ArrayList;

public class ModularTAZASView extends JPanel implements IModularCategoryView  {
	private JPanel containerPanel;
	private JRadioButton ceramicRadioButton;
	private JRadioButton plasticRadioButton;
	private JRadioButton whiteRadioButton;
	private JRadioButton sublimatedRadioButton;

	@Override
	public JPanel getContainerPanel() {
		return containerPanel;
	}

	@Override
	public ArrayList<String> getModularAttributes() {
		return new ArrayList<String>();
	}


}
