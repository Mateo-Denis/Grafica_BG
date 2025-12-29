package views.workbudget;

import presenters.StandardPresenter;
import presenters.workbudget.WorkBudgetCreatePresenter;
import views.ToggleableView;

import javax.swing.*;

public class WorkBudgetCreateView extends ToggleableView implements IWorkBudgetCreateView{
	private JPanel containerPanel;
	private JLabel budgetNumberLabel;
	private JButton backButton;
	private JButton nextButton;
	private JPanel clientStageContainer;
	private JPanel finalPriceStageContainer;
	private JPanel contentListStageContainer;
	private JPanel clientSideInfoStage;
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
	}

	@Override
	protected void wrapContainer() { containerPanelWrapper = containerPanel; }

	@Override
	protected void initListeners() {
		backButton.addActionListener( e -> workBudgetCreatePresenter.onBackButtonPressed() );
		nextButton.addActionListener( e -> workBudgetCreatePresenter.onNextButtonPressed() );
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
