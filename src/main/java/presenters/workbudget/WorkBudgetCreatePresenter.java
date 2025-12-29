package presenters.workbudget;

import PdfFormater.IPdfConverter;
import PdfFormater.PdfConverter;
import presenters.StandardPresenter;
import views.workbudget.WorkBudgetCreateView;

public class WorkBudgetCreatePresenter extends StandardPresenter {
	private final WorkBudgetCreateView workBudgetCreateView;
	private static final IPdfConverter pdfConverter = new PdfConverter();

	private WorkBudgetCreationStage stage;

	public WorkBudgetCreatePresenter(WorkBudgetCreateView workBudgetCreateView) {
		this.workBudgetCreateView = workBudgetCreateView;
		stage = WorkBudgetCreationStage.CLIENT_SELECTION;
		view = workBudgetCreateView;
		workBudgetCreateView.setBackButton(false);
	}

	@Override
	protected void initListeners() {

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
				stage = WorkBudgetCreationStage.CONTENT_LIST;
				workBudgetCreateView.setBackButton(true);
				workBudgetCreateView.showContentListStage();
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
}
