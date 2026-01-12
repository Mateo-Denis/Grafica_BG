package views.workbudget;

import lombok.Getter;
import lombok.Setter;
import org.javatuples.Pair;
import presenters.StandardPresenter;
import presenters.workbudget.WorkBudgetCreatePresenter;
import presenters.workbudget.WorkBudgetCreationStage;
import views.ToggleableView;
import views.workbudget.stages.*;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.util.ArrayList;

public class WorkBudgetCreateView extends ToggleableView implements IWorkBudgetCreateView{
	private JPanel containerPanel;
	private JLabel budgetNumberLabel;
	private JButton backButton;
	private JButton nextButton;
	private JPanel clientStageContainer;
	private JPanel finalPriceStageContainer;
	private JPanel contentListStageContainer;
	private JPanel clientSideInfoStageContainer;
	@Getter
	private ContentListStage contentListStage;
	@Getter
	private ClientSearchingStage clientSearchingStage;
	@Getter
	private FinalPriceStage finalPriceStage;
	@Getter
	private ClientSideInfoStage clientSideInfoStage;
	private WorkBudgetCreatePresenter workBudgetCreatePresenter;
	@Setter
	private boolean isBeingModified  = false;


	public WorkBudgetCreateView(){
		windowFrame = new JFrame("Crear Presupuesto de Trabajo");
		windowFrame.setContentPane(containerPanel);
		windowFrame.pack();
		windowFrame.setLocationRelativeTo(null);
		windowFrame.setIconImage(new ImageIcon("src/main/resources/BGLogo.png").getImage());
		windowFrame.setResizable(true);

		contentListStageContainer.setVisible(false);
		finalPriceStageContainer.setVisible(false);
		clientSideInfoStageContainer.setVisible(false);
		backButton.setEnabled(false);

		cambiarTamanioFuente(containerPanel, 14);
		windowFrame.setSize(550, 588);

		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		int x = (screenSize.width - windowFrame.getWidth()) / 2;    // Centered horizontally
		int y = (screenSize.height - windowFrame.getHeight()) / 2;  // Centered vertically

		// Set the location of the frame
		windowFrame.setLocation(x, y);
	}

	@Override
	public void start(){
		super.start();
		clientSearchingStage.setClientsTableModel();
	}

	@Override
	protected void wrapContainer() { containerPanelWrapper = containerPanel; }

	@Override
	protected void initListeners() {
		backButton.addActionListener( e -> workBudgetCreatePresenter.onBackButtonPressed() );
		nextButton.addActionListener( e -> workBudgetCreatePresenter.onNextButtonPressed(isBeingModified));

		contentListStage.getTextFieldByName(ContentListReferences.MATERIAL).addActionListener(e -> workBudgetCreatePresenter.onMaterialEnterPressed(contentListStage));
		contentListStage.getTextFieldByName(ContentListReferences.MATERIAL_PRICE).addActionListener(e -> workBudgetCreatePresenter.onMaterialEnterPressed(contentListStage));

		clientSearchingStage.getSearchButton().addActionListener( e -> workBudgetCreatePresenter.onSearchClientButtonClicked(clientSearchingStage) );
		clientSearchingStage.getClientTextField().addActionListener(e -> workBudgetCreatePresenter.onSearchClientButtonClicked(clientSearchingStage));
		ArrayList<JTextField> textFields = new ArrayList<>();

		clientSideInfoStage.getTextFieldByName(ClientSideInfoReferences.DESCRIPTION).addActionListener(e -> workBudgetCreatePresenter.onInfoItemEnterPressed(clientSideInfoStage));
		clientSideInfoStage.getTextFieldByName(ClientSideInfoReferences.TOTAL).addActionListener(e -> workBudgetCreatePresenter.onInfoItemEnterPressed(clientSideInfoStage));



		textFields.add(finalPriceStage.getTextFieldByName(FinalPriceReferences.PROFIT_MARGIN));

		for (JTextField textField : textFields) {
			textField.getDocument().addDocumentListener(new DocumentListener() {
				@Override
				public void insertUpdate(DocumentEvent e) {
					workBudgetCreatePresenter.onProfitMarginChanged();
				}
				@Override
				public void removeUpdate(DocumentEvent e) {
					workBudgetCreatePresenter.onProfitMarginChanged();
				}
				@Override
				public void changedUpdate(DocumentEvent e) {
					workBudgetCreatePresenter.onProfitMarginChanged();
				}
			});
		}

		finalPriceStage.getTextFieldByName(FinalPriceReferences.DEPOSIT).getDocument().addDocumentListener(new DocumentListener() {
				@Override
				public void insertUpdate(DocumentEvent e) {
					workBudgetCreatePresenter.onDepositChanged(true);
				}
				@Override
				public void removeUpdate(DocumentEvent e) {
					workBudgetCreatePresenter.onDepositChanged(true);
				}
				@Override
				public void changedUpdate(DocumentEvent e) {
					workBudgetCreatePresenter.onDepositChanged(true);
				}
			});

		finalPriceStage.getTextFieldByName(FinalPriceReferences.BALANCE_TO_PAY).getDocument().addDocumentListener(new DocumentListener() {
			@Override
			public void insertUpdate(DocumentEvent e) {
				workBudgetCreatePresenter.onDepositChanged(false);
			}
			@Override
			public void removeUpdate(DocumentEvent e) {
				workBudgetCreatePresenter.onDepositChanged(false);
			}
			@Override
			public void changedUpdate(DocumentEvent e) {
				workBudgetCreatePresenter.onDepositChanged(false);
			}
		});
	}

	@Override
	public void clearView() {
		workBudgetCreatePresenter.setStage(WorkBudgetCreationStage.CLIENT_SELECTION);
		clientStageContainer.setVisible(true);
		contentListStageContainer.setVisible(false);
		finalPriceStageContainer.setVisible(false);
		clientSideInfoStageContainer.setVisible(false);
		isBeingModified = false;
		clientSearchingStage.clearView();
		contentListStage.clearView();
		finalPriceStage.clearView();
		clientSideInfoStage.clearView();
		backButton.setEnabled(false);
		nextButton.setText("Siguiente");
	}

	@Override
	public void setPresenter(StandardPresenter standardPresenter) {
		this.workBudgetCreatePresenter = (WorkBudgetCreatePresenter) standardPresenter;
	}

	public void loadExistingBudget(int budgetId){
		isBeingModified = true;
		workBudgetCreatePresenter.loadExistingBudget(budgetId);
	}

	public void setLogisticsData(String description, String cost){
		contentListStage.setTextContentByName(ContentListReferences.LOGISTICS_DESCRIPTION, description);
		contentListStage.setTextContentByName(ContentListReferences.LOGISTICS_COST, cost);
	}

	public void setPlacingData(String placer, String cost){
		contentListStage.setTextContentByName(ContentListReferences.PLACER, placer);
		contentListStage.setTextContentByName(ContentListReferences.PLACING_COST, cost);
	}

	public void setProfitMargin(String profitMargin){
		finalPriceStage.setTextContentByName(FinalPriceReferences.PROFIT_MARGIN, profitMargin);
	}

	public void setBackButton( boolean enabled ){
		backButton.setEnabled( enabled );
	}

	public void changeButtonToGenPDF(){
		nextButton.setText("Generar PDFs");
	}

	public void changeButtonToNext(){
		nextButton.setText("Siguiente");
	}

	public void showClientSelectionStage(){
		clientStageContainer.setVisible(true);
		contentListStageContainer.setVisible(false);
	}

	public void showContentListStage(){
		clientStageContainer.setVisible(false);
		contentListStageContainer.setVisible(true);
		finalPriceStageContainer.setVisible(false);
	}

	public void showFinalPriceStage(){
		contentListStageContainer.setVisible(false);
		finalPriceStageContainer.setVisible(true);
		clientSideInfoStageContainer.setVisible(false);
	}

	public void showClientSideInfoStage(){
		finalPriceStageContainer.setVisible(false);
		clientSideInfoStageContainer.setVisible(true);
	}

	public int getSelectedClientId(){
		return clientSearchingStage.getSelectedClientID();
	}

	public ArrayList<Pair<String, String>> getMaterialsList(){
		return contentListStage.getMaterialsListFromTable();
	}

	public Pair<String, String> getLogisticsData(){
		String logisticsCostText = contentListStage.getTextContentByName(ContentListReferences.LOGISTICS_COST);
		String logisticsDescriptionText = contentListStage.getTextContentByName(ContentListReferences.LOGISTICS_DESCRIPTION);
		if (logisticsCostText == null || logisticsCostText.isEmpty()) {
			return new Pair<>(logisticsDescriptionText, "0.00");
		}
		return new Pair<>(logisticsDescriptionText, logisticsCostText);
	}

	public Pair<String, String> getPlacingData(){
		String placingCostText = contentListStage.getTextContentByName(ContentListReferences.PLACING_COST);
		String placerText = contentListStage.getTextContentByName(ContentListReferences.PLACER);
		if (placingCostText == null || placingCostText.isEmpty()) {
			return new Pair<>(placerText, "0.0");
		}
		return new Pair<>(placerText, placingCostText);
	}

	public String getProfitMargin() {
		return finalPriceStage.getTextContentByName(FinalPriceReferences.PROFIT_MARGIN);
	}

	public String getFinalPrice() {
		return finalPriceStage.getTextContentByName(FinalPriceReferences.FINAL_PRICE);
	}


	public ArrayList<Pair<String, String>> getClientInfoItems(){
		return clientSideInfoStage.getItemsListFromTable();
	}

	public void setBudgetNumberLabelText(String text){
		budgetNumberLabel.setText(text);
	}

	public int getBudgetNumberLabelValue(){
		String labelText = budgetNumberLabel.getText();
		String[] parts = labelText.split(":");
		if (parts.length > 1) {
			try {
				return Integer.parseInt(parts[1].trim());
			} catch (NumberFormatException e) {
				return -1;
			}
		}
		return -1;
	}

}
