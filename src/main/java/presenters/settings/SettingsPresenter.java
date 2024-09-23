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
	private String dollarValue;
	private String plankLoweringValue;
	private String capValue;
	private String cupValue;
	private String inkValue;
	private String seamstressValue;

	public SettingsPresenter(ISettingsView settingsView, ISettingsModel settingsModel) {
		this.settingsView = settingsView;
		view = settingsView;
		this.settingsModel = settingsModel;
		initValues();
	}

	private void initValues() {
		ArrayList<Pair<String, String>> tableContent = settingsModel.getTableContent();
		dollarValue = tableContent.get(0).getValue1();
		plankLoweringValue = tableContent.get(1).getValue1();
		capValue = tableContent.get(2).getValue1();
		cupValue = tableContent.get(3).getValue1();
		inkValue = tableContent.get(4).getValue1();
		seamstressValue = tableContent.get(5).getValue1();
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

		String local_dollarValue = settingsView.getDollarValue();
		String local_plankLoweringValue = settingsView.getPlankLoweringValue();
		String local_capValue = settingsView.getCapValue();
		String local_cupValue = settingsView.getCupValue();
		String local_inkValue = settingsView.getInkValue();
		String local_seamstressValue = settingsView.getSeamstressValue();

		settingsModel.updateGeneralData(dollarValue, plankLoweringValue, capValue, cupValue, inkValue, seamstressValue);

		//settingsModel.updateClothData(tableToArrayList(settingsView.getClothTable()));

		//settingsModel.updateClothesData(tableToArrayList(settingsView.getClothesTable()));

		dollarValue = local_dollarValue;
		plankLoweringValue = local_plankLoweringValue;
		capValue = local_capValue;
		cupValue = local_cupValue;
		inkValue = local_inkValue;
		seamstressValue = local_seamstressValue;

	}

	public void onHomeSettingsButtonClicked() {
		settingsView.showView();

		settingsView.setDollarValue(dollarValue);
		settingsView.setPlankLoweringValue(plankLoweringValue);
		settingsView.setCapValue(capValue);
		settingsView.setCupValue(cupValue);
		settingsView.setInkValue(inkValue);
		settingsView.setSeamstressValue(seamstressValue);

	}
}
