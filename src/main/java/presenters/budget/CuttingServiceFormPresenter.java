package presenters.budget;

import presenters.StandardPresenter;
import utils.CuttingService;
import views.budget.IBudgetCreateView;
import views.budget.cuttingService.ICuttingServiceFormView;
import views.budget.modify.IBudgetModifyView;

import static utils.TextUtils.truncateAndRound;

public class CuttingServiceFormPresenter extends StandardPresenter {
    private final ICuttingServiceFormView cuttingServiceView;
    private final IBudgetCreateView budgetCreateView;
    private final IBudgetModifyView budgetModifyView;
    private boolean isInCreateMode = true;


    public CuttingServiceFormPresenter(IBudgetModifyView budgetModifyView, ICuttingServiceFormView cuttingServiceView, IBudgetCreateView budgetCreateView) {
        this.cuttingServiceView = cuttingServiceView;
        this.budgetCreateView = budgetCreateView;
        this.budgetModifyView = budgetModifyView;
        view = cuttingServiceView;
    }

    @Override
    protected void initListeners() {

    }

    public void clearView() {
        cuttingServiceView.clearView();
    }


    public double calculateCuttingService() {
        double materialCost = cuttingServiceView.getMaterialCost();
        double linealMeters = cuttingServiceView.getLinealMeters();
        double amount = cuttingServiceView.getAmount();
        double profit = cuttingServiceView.getProfit();

        double subTotal = Double.parseDouble(truncateAndRound(String.valueOf(materialCost * amount * linealMeters)));
        cuttingServiceView.setSubTotal(subTotal);

        double total = Double.parseDouble(truncateAndRound(String.valueOf(subTotal + (subTotal * (profit / 100)))));
        cuttingServiceView.setFinalText(String.valueOf(total));

        return total;
    }

    public void onAddCuttingServiceButtonClicked() {
            if(!isInCreateMode) {
                budgetModifyView.addCuttingService(getCuttingService());
                cuttingServiceView.clearView();
                isInCreateMode = true;
            } else {
                budgetCreateView.addCuttingService(getCuttingService());
                cuttingServiceView.clearView();
            }
    }

    public void setCreateMode(boolean isInCreateMode) {
        this.isInCreateMode = isInCreateMode;
    }

    public CuttingService getCuttingService() {
        double total = calculateCuttingService();

        return new CuttingService(
                cuttingServiceView.getDescription(),
                cuttingServiceView.getMaterialCost(),
                cuttingServiceView.getLinealMeters(),
                cuttingServiceView.getProfit(),
                cuttingServiceView.getAmount(),
                cuttingServiceView.getSubTotal(),
                total
        );
    }
}
