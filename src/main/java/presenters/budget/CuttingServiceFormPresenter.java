package presenters.budget;

import presenters.StandardPresenter;
import views.budget.cuttingService.ICuttingServiceFormView;
import views.budget.IBudgetCreateView;
import utils.CuttingService;

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

        double subTotal = materialCost * linealMeters;
        cuttingServiceView.setSubTotal(subTotal);

        double total = subTotal + (subTotal * (profit / 100));
        cuttingServiceView.setFinalText(String.format("%.2f", total));

        return total;
    }

    public void onAddCuttingServiceButtonClicked() {
        double total = calculateCuttingService();

        CuttingService cuttingService = new CuttingService(
            cuttingServiceView.getDescription(),
            cuttingServiceView.getMaterialCost(),
            cuttingServiceView.getLinealMeters(),
            cuttingServiceView.getProfit(),
            cuttingServiceView.getAmount(),
            total
        );

        budgetCreateView.addCuttingService(cuttingService);
    }
}
