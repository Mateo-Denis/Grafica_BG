package presenters.workbudget;

import PdfFormater.IPdfConverter;
import PdfFormater.PdfConverter;
import models.WorkBudgetModel;
import presenters.StandardPresenter;
import utils.Client;
import views.workbudget.WorkBudgetCreateView;
import views.workbudget.stages.*;

import static utils.MessageTypes.*;
import static utils.TextUtils.truncateAndRound;


import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import java.util.ArrayList;

import static views.workbudget.stages.FinalPriceReferences.*;

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
		workBudgetCreateView.setBudgetNumberLabelText("Presupuesto N°: " + workBudgetModel.getNextBudgetNumber());
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
				calculateFinalPrice();
				setDepositAndBalance(true, true);
			}
			case FINAL_PRICE -> {
				stage = WorkBudgetCreationStage.CLIENT_SIDE_INFO;
				workBudgetCreateView.changeButtonToGenPDF();
				workBudgetCreateView.showClientSideInfoStage();
			}
			case CLIENT_SIDE_INFO -> {
				workBudgetModel.saveWorkBudget(
						workBudgetCreateView.getSelectedClientId(),
						workBudgetCreateView.getMaterialsList(),
						workBudgetCreateView.getLogisticsData(),
						workBudgetCreateView.getPlacingData(),
						workBudgetCreateView.getProfitMargin(),
						workBudgetCreateView.getFinalPrice(),
						workBudgetCreateView.getClientInfoItems()
				);
				// Generate PDF using pdfConverter
			}
		}
	}

	private boolean updating = false;

	public void onProfitMarginChanged() {
		if (updating) return;

		SwingUtilities.invokeLater(() -> {
			updating = true;
			calculateFinalPrice();
			setDepositAndBalance(true, true);
			updating = false;
		});
	}

	public void onDepositChanged(boolean depositModified) {
		if (updating) return;

		SwingUtilities.invokeLater(() -> {
			updating = true;
			setDepositAndBalance(false, depositModified);
			updating = false;
		});
	}

	private void calculateFinalPrice() {
		ContentListStage contentListStage = workBudgetCreateView.getContentListStage();
		DefaultTableModel materialsTableModel = (DefaultTableModel) contentListStage.getMaterialsTable().getModel();
		double materialsCost = 0.0;

		// Calculate total materials cost
		for (int i = 0; i < materialsTableModel.getRowCount(); i++) {
			materialsCost += Double.parseDouble((String) materialsTableModel.getValueAt(i, 1));
		}

		String logisticsCostStr = contentListStage.getTextContentByName(ContentListReferences.LOGISTICS_COST);
		String placingCostStr = contentListStage.getTextContentByName(ContentListReferences.PLACING_COST);

		double logisticsCost = logisticsCostStr.isEmpty() ? 0.0 : Double.parseDouble(logisticsCostStr);
		double placingCost = placingCostStr.isEmpty() ? 0.0 : Double.parseDouble(placingCostStr);

		double totalCosts = materialsCost + logisticsCost + placingCost;

		FinalPriceStage finalPriceStage = workBudgetCreateView.getFinalPriceStage();

		finalPriceStage.setTextContentByName(
				MATERIALS_COST, String.format("%.2f", materialsCost)
		);
		finalPriceStage.setTextContentByName(
				LOGISTICS_COST, String.format("%.2f", logisticsCost)
		);
		finalPriceStage.setTextContentByName(
				PLACING_COST, String.format("%.2f", placingCost)
		);
		finalPriceStage.setTextContentByName(
				TOTAL_COSTS, String.format("%.2f", totalCosts)
		);
		String profit = finalPriceStage.getTextContentByName(PROFIT_MARGIN);
		double profitMargin = profit.isEmpty() ? 0.00 : Double.parseDouble(profit);
		double finalPrice = totalCosts + totalCosts * ( profitMargin / 100 );
		;
		finalPriceStage.setTextContentByName(
				FINAL_PRICE, String.format("%.2f", Double.parseDouble(truncateAndRound(Double.toString(finalPrice))))
		);
	}

	private void setDepositAndBalance(boolean defaultValue, boolean depositModified){
		FinalPriceStage finalPriceStage = workBudgetCreateView.getFinalPriceStage();
		double finalPrice = Double.parseDouble(finalPriceStage.getTextContentByName(FINAL_PRICE));
		double deposit;
		double balance;
		if(defaultValue){
			deposit = finalPrice * 0.5;
			balance = finalPrice * 0.5;
		}else if(depositModified) {
			deposit = Double.parseDouble(finalPriceStage.getTextContentByName(DEPOSIT));
			balance = finalPrice - deposit;
		}else{
			balance = Double.parseDouble(finalPriceStage.getTextContentByName(BALANCE_TO_PAY));
			deposit = finalPrice - balance;
		}


		finalPriceStage.setTextContentByName(
				DEPOSIT, String.format("%.2f", deposit)
		);
		finalPriceStage.setTextContentByName(
				BALANCE_TO_PAY, String.format("%.2f", balance)
		);
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
			contentListStage.setFocusToMaterialField();
		}
	}

	public void onInfoItemEnterPressed(ClientSideInfoStage clientSideInfoStage) {
		String description = clientSideInfoStage.getTextContentByName(ContentListReferences.MATERIAL);
		String price = clientSideInfoStage.getTextContentByName(ContentListReferences.MATERIAL_PRICE);
		if( description.isEmpty() || price.isEmpty()){
			workBudgetCreateView.showMessage(INCOMPLETE_INFO_FIELDS);
		}else {
			clientSideInfoStage.addInfoItemToTable(description, price);
			clientSideInfoStage.clearMaterialInputFields();
			clientSideInfoStage.setFocusToMaterialField();
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
