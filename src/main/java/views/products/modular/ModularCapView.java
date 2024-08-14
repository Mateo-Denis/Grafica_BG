package views.products.modular;

import javax.swing.*;
import java.util.ArrayList;

public class ModularCapView extends JPanel implements IModularCategoryView {
	private JPanel containerPanel;
	private JRadioButton whiteFrontRadioButton;
	private JRadioButton sublimatedRadioButton;
	private JRadioButton visorStampRadioButton;

	@Override
	public JPanel getContainerPanel() {
		return containerPanel;
	}

	@Override
	public ArrayList<String> getModularAttributes() {
		if(whiteFrontRadioButton.isSelected()) {
			return new ArrayList<String>() {{
				add("whiteFront");
			}};
		} else if(sublimatedRadioButton.isSelected()) {
			return new ArrayList<String>() {{
				add("sublimated");
			}};
		} else if(visorStampRadioButton.isSelected()) {
			return new ArrayList<String>() {{
				add("visorStamp");
			}};
		}
		return null;
	}
}

