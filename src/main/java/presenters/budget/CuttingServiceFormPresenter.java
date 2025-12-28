package presenters.budget;

import presenters.StandardPresenter;
import utils.CuttingService;
import views.budget.IBudgetCreateView;
import views.budget.cuttingService.ICuttingServiceFormView;

import static utils.TextUtils.truncateAndRound;

public class CuttingServiceFormPresenter extends StandardPresenter {
    private final ICuttingServiceFormView cuttingServiceView;
    private final IBudgetCreateView budgetCreateView;


    public CuttingServiceFormPresenter(ICuttingServiceFormView cuttingServiceView, IBudgetCreateView budgetCreateView) {
        this.cuttingServiceView = cuttingServiceView;
        this.budgetCreateView = budgetCreateView;
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
        double profit = cuttingServiceView.getProfit();

        double subTotal = Double.parseDouble(truncateAndRound(String.valueOf(materialCost * linealMeters)));
        cuttingServiceView.setSubTotal(subTotal);

        double total = Double.parseDouble(truncateAndRound(String.valueOf(subTotal + (subTotal * (profit / 100)))));
        cuttingServiceView.setFinalText(String.valueOf(total));

        return total;
    }

    public void onAddCuttingServiceButtonClicked() {
        budgetCreateView.addCuttingService(getCuttingService());
    }

    public CuttingService getCuttingService() {
        double total = calculateCuttingService();

        return new CuttingService(
                cuttingServiceView.getDescription(),
                cuttingServiceView.getMaterialCost(),
                cuttingServiceView.getLinealMeters(),
                cuttingServiceView.getProfit(),
                cuttingServiceView.getAmount(),
                total
        );
    }
}
