package views.settings;

import views.IToggleableView;

import javax.swing.*;

public interface ISettingsView extends IToggleableView {
	String getDollarValue();
	String getPlankLoweringValue();
	String getCapValue();
	String getCupValue();
	String getInkValue();
	String getSeamstressValue();
	JTable getClothTable();
	JTable getClothesTable();


	void setDollarValue(String value);

	void setPlankLoweringValue(String value);

	void setCapValue(String value);

	void setCupValue(String value);

	void setInkValue(String value);

	void setSeamstressValue(String value);

	void setClothTableValue(String value, int row, int column);

	void setClothesTableValue(String value, int row, int column);
}
