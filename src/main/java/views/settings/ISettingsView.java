package views.settings;

import views.IToggleableView;

import javax.swing.*;

public interface ISettingsView extends IToggleableView {
	String getPlankLoweringValue();
	String getCapValue();
	String getCupValue();
	String getInkValue();
	String getSeamstressValue();
	JTable getClothTable();
	JTable getClothesTable();
}
