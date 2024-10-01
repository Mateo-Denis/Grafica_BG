package presenters.settings;

import com.google.gson.JsonObject;
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
		updateValues();
		showValues();
	}

	private void showValues() {
		settingsView.setDollarValue(dollarValue);
		settingsView.setPlankLoweringValue(plankLoweringValue);
		settingsView.setCapValue(capValue);
		settingsView.setCupValue(cupValue);
		settingsView.setInkValue(inkValue);
		settingsView.setSeamstressValue(seamstressValue);
	}

	private void updateValues() {
		JsonObject json = settingsModel.readConfig();
		dollarValue = json.get("Dollar").getAsString();
		plankLoweringValue = json.get("PlankLowering").getAsString();
		capValue = json.get("Cap").getAsString();
		cupValue = json.get("Cup").getAsString();
		inkValue = json.get("Ink").getAsString();
		seamstressValue = json.get("Seamstress").getAsString();
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

		boolean changesWereMade = false;
		String local_dollarValue = settingsView.getDollarValue();
		String local_plankLoweringValue = settingsView.getPlankLoweringValue();
		String local_capValue = settingsView.getCapValue();
		String local_cupValue = settingsView.getCupValue();
		String local_inkValue = settingsView.getInkValue();
		String local_seamstressValue = settingsView.getSeamstressValue();

		JsonObject json = settingsModel.readConfig();

		try {
			Double.parseDouble(local_dollarValue);
			json.addProperty("Dollar", local_dollarValue);
			dollarValue = local_dollarValue;
			changesWereMade = true;
		} catch (NumberFormatException e) {
			//TODO show error message
			System.out.println("Error while updating Dollar.");
		}

		try {
			Double.parseDouble(local_plankLoweringValue);
			json.addProperty("PlankLowering", local_plankLoweringValue);
			plankLoweringValue = local_plankLoweringValue;
			changesWereMade = true;
		} catch (NumberFormatException e) {
			//TODO show error message
			System.out.println("Error while updating PL.");
		}

		try {
			Double.parseDouble(local_capValue);
			json.addProperty("Cap", local_capValue);
			capValue = local_capValue;
			changesWereMade = true;
		} catch (NumberFormatException e) {
			//TODO show error message
			System.out.println("Error while updating Cap.");
		}

		try {
			Double.parseDouble(local_cupValue);
			json.addProperty("Cup", local_cupValue);
			cupValue = local_cupValue;
			changesWereMade = true;
		} catch (NumberFormatException e) {
			//TODO show error message
			System.out.println("Error while updating Cup.");
		}

		try {
			Double.parseDouble(local_inkValue);
			json.addProperty("Ink", local_inkValue);
			inkValue = local_inkValue;
			changesWereMade = true;
		} catch (NumberFormatException e) {
			//TODO show error message
			System.out.println("Error while updating Ink.");
		}

		try {
			Double.parseDouble(local_seamstressValue);
			json.addProperty("Seamstress", local_seamstressValue);
			seamstressValue = local_seamstressValue;
			changesWereMade = true;
		} catch (NumberFormatException e) {
			//TODO show error message
			System.out.println("Error while updating Seamstress.");
		}

		if (changesWereMade) {
			settingsModel.writeConfig(json);
		}

		//settingsModel.updateClothData(tableToArrayList(settingsView.getClothTable()));
		//settingsModel.updateClothesData(tableToArrayList(settingsView.getClothesTable()));
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
