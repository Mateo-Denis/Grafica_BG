package presenters.settings;

import models.settings.ISettingsModel;
import org.javatuples.Pair;
import presenters.StandardPresenter;
import views.settings.ISettingsView;

import javax.swing.*;
import java.util.ArrayList;

public class SettingsPresenter extends StandardPresenter {

	private final ISettingsView settingsView;
	private final ISettingsModel settingsModel;
	public SettingsPresenter(ISettingsView settingsView, ISettingsModel settingsModel) {
		this.settingsView = settingsView;
		view = settingsView;
		this.settingsModel = settingsModel;
	}

	@Override
	protected void initListeners() {

	}

	private ArrayList<Pair<String, String>> tableToArrayList(JTable table) {
		ArrayList<Pair<String, String>> arrayList = new ArrayList<>();
		for (int i = 0; i < table.getRowCount(); i++) {
			arrayList.add(new Pair<>(table.getValueAt(i, 0).toString(), table.getValueAt(i, 1).toString()));
		}
		return arrayList;
	}

	public void onUpdateDataButtonPressed() {
		settingsModel.updateGeneralData(settingsView.getPlankLoweringValue(), settingsView.getCapValue(),
				settingsView.getCupValue(), settingsView.getInkValue(), settingsView.getSeamstressValue());

		settingsModel.updateClothData(tableToArrayList(settingsView.getClothTable()));

		settingsModel.updateClothesData(tableToArrayList(settingsView.getClothesTable()));


	}

	public void onHomeSettingsButtonClicked() {
		settingsView.showView();
	}
}
