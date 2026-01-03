package views.workbudget;

import presenters.StandardPresenter;
import presenters.workbudget.WorkBudgetCreatePresenter;
import views.ToggleableView;
import views.workbudget.stages.ClientSearchingStage;
import views.workbudget.stages.ContentListReferences;
import views.workbudget.stages.ContentListStage;
import views.workbudget.stages.FinalPriceStage;

import javax.swing.*;
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
	private JPanel clientSideInfoStage;
	private ContentListStage contentListStage;
	private ClientSearchingStage clientSearchingStage;
	private FinalPriceStage finalPriceStage1;
	private WorkBudgetCreatePresenter workBudgetCreatePresenter;


	public WorkBudgetCreateView(){
		windowFrame = new JFrame("Crear Presupuesto de Trabajo");
		windowFrame.setContentPane(containerPanel);
		windowFrame.pack();
		windowFrame.setLocationRelativeTo(null);
		windowFrame.setIconImage(new ImageIcon("src/main/resources/BGLogo.png").getImage());
		windowFrame.setResizable(true);

		contentListStageContainer.setVisible(false);
		finalPriceStageContainer.setVisible(false);
		clientSideInfoStage.setVisible(false);
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
		nextButton.addActionListener( e -> workBudgetCreatePresenter.onNextButtonPressed() );
		contentListStage.getTextFieldByName(ContentListReferences.MATERIAL).addActionListener(e -> workBudgetCreatePresenter.onMaterialEnterPressed(contentListStage));
		contentListStage.getTextFieldByName(ContentListReferences.MATERIAL_PRICE).addActionListener(e -> workBudgetCreatePresenter.onMaterialEnterPressed(contentListStage));
		clientSearchingStage.getSearchButton().addActionListener( e -> workBudgetCreatePresenter.onSearchClientButtonClicked(clientSearchingStage) );
	}

	public ClientSearchingStage getClientSearchingStage() {
		return clientSearchingStage;
	}

	@Override
	public void clearView() {

	}

	@Override
	public void setPresenter(StandardPresenter standardPresenter) {
		this.workBudgetCreatePresenter = (WorkBudgetCreatePresenter) standardPresenter;
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
		clientSideInfoStage.setVisible(false);
	}

	public void showClientSideInfoStage(){
		finalPriceStageContainer.setVisible(false);
		clientSideInfoStage.setVisible(true);
	}
}
