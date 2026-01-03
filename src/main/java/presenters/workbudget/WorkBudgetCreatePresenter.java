package presenters.workbudget;

import PdfFormater.IPdfConverter;
import PdfFormater.PdfConverter;
import models.IBudgetModel;
import models.WorkBudgetModel;
import presenters.StandardPresenter;
import utils.Client;
import views.workbudget.WorkBudgetCreateView;
import views.workbudget.stages.ClientSearchingStage;
import views.workbudget.stages.ContentListReferences;
import views.workbudget.stages.ContentListStage;

import javax.swing.table.DefaultTableModel;

import java.util.ArrayList;

import static utils.MessageTypes.CLIENT_NOT_SELECTED;
import static utils.MessageTypes.INCOMPLETE_MATERIAL_FIELDS;

public class WorkBudgetCreatePresenter extends StandardPresenter {
	private final WorkBudgetCreateView workBudgetCreateView;
	private static final IPdfConverter pdfConverter = new PdfConverter();
	private final WorkBudgetModel workBudgetModel;

	private WorkBudgetCreationStage stage;

	public WorkBudgetCreatePresenter(WorkBudgetCreateView workBudgetCreateView, WorkBudgetModel workBudgetModel) {
		this.workBudgetCreateView = workBudgetCreateView;
		this.workBudgetModel = workBudgetModel;
		stage = WorkBudgetCreationStage.CLIENT_SELECTION;
		view = workBudgetCreateView;
		workBudgetCreateView.setBackButton(false);
		loadCities();
	}

	@Override
	protected void initListeners() {

	}

	private void loadCities() {
		ArrayList<String> cities = workBudgetModel.getCitiesName();
		workBudgetCreateView.getClientSearchingStage().setCitiesComboBox(cities);
	}

	public void onBackButtonPressed() {
		switch (stage) {
			case CONTENT_LIST -> {
				stage = WorkBudgetCreationStage.CLIENT_SELECTION;
				workBudgetCreateView.setBackButton(false);
				workBudgetCreateView.showClientSelectionStage();
			}
			case FINAL_PRICE -> {
				stage = WorkBudgetCreationStage.CONTENT_LIST;
				workBudgetCreateView.showContentListStage();
			}
			case CLIENT_SIDE_INFO -> {
				stage = WorkBudgetCreationStage.FINAL_PRICE;
				workBudgetCreateView.changeButtonToNext();
				workBudgetCreateView.showFinalPriceStage();
			}
		}
	}

	public void onNextButtonPressed() {
		switch (stage) {
			case CLIENT_SELECTION -> {
				if(workBudgetCreateView.getClientSearchingStage().isTableSelected()){
					stage = WorkBudgetCreationStage.CONTENT_LIST;
					workBudgetCreateView.setBackButton(true);
					workBudgetCreateView.showContentListStage();
				}else {
					workBudgetCreateView.showMessage(CLIENT_NOT_SELECTED);
				}
			}
			case CONTENT_LIST -> {
				stage = WorkBudgetCreationStage.FINAL_PRICE;
				workBudgetCreateView.showFinalPriceStage();
			}
			case FINAL_PRICE -> {
				stage = WorkBudgetCreationStage.CLIENT_SIDE_INFO;
				workBudgetCreateView.changeButtonToGenPDF();
				workBudgetCreateView.showClientSideInfoStage();
			}
			case CLIENT_SIDE_INFO -> {

				// Generate PDF using pdfConverter
			}
		}
	}

	public void onHomeCreateWorkBudgetButtonClicked() {
		view.showView();
	}

	public void onMaterialEnterPressed(ContentListStage contentListStage) {
		String material = contentListStage.getTextContentByName(ContentListReferences.MATERIAL);
		String materialPrice = contentListStage.getTextContentByName(ContentListReferences.MATERIAL_PRICE);
		if( material.isEmpty() || materialPrice.isEmpty()){
			workBudgetCreateView.showMessage(INCOMPLETE_MATERIAL_FIELDS);
		}else {

			contentListStage.addMaterialToTable(material, materialPrice);
			contentListStage.clearMaterialInputFields();
		}
	}

	public void onSearchClientButtonClicked(ClientSearchingStage clientSearchingStage) {
		String city = "";
		String clientType = "";
		city = (String) clientSearchingStage.getSelectedCity();
		String name = clientSearchingStage.getClientNameInput();
		int clientID = -1;

		if (city.equals("Seleccione una ciudad")) {
			city = "";
		}

		ArrayList<Client> clients = workBudgetModel.getClients(name, city); // LOCAL VARIABLE -> GET CLIENTS BY NAME AND CITY
		clientSearchingStage.clearClientTable(); // CLEAR CLIENT TABLE
		int rowCount = 0; // ROW COUNT VARIABLE

		// LOOP THROUGH CLIENTS
		for (Client client : clients) {
			clientType = "Cliente";

			if (!client.isClient()) {
				clientType = "Particular";
			}

			clientID = workBudgetModel.getClientID(client.getName(), clientType); // GET CLIENT ID

			// SET CLIENT TABLE VALUES
			clientSearchingStage.setClientIntTableValueAt(rowCount, 0, clientID);
			clientSearchingStage.setClientStringTableValueAt(rowCount, 1, client.getName());
			clientSearchingStage.setClientStringTableValueAt(rowCount, 2, client.getAddress());
			clientSearchingStage.setClientStringTableValueAt(rowCount, 3, client.getCity());
			clientSearchingStage.setClientStringTableValueAt(rowCount, 4, client.getPhone());
			clientSearchingStage.setClientStringTableValueAt(rowCount, 5, client.isClient() ? "Cliente" : "Particular");
			rowCount++; // INCREMENT ROW COUNT
		}
	}
}
