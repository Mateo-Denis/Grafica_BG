package views.budget.cuttingService;

import utils.MessageTypes;
import views.IToggleableView;

import javax.swing.*;

public interface ICuttingServiceFormView extends IToggleableView {
    public double getMaterialCost();
    public double getLinealMeters();
    public double getProfit();
    public void setSubTotal(double subTotal);
    public void setFinalText(String total);
    public int getAmount();
    public String getDescription();
    public void clearView();
    default void showMessage(MessageTypes messageType, JPanel containerPanelWrapper) {
        JOptionPane.showMessageDialog(containerPanelWrapper, messageType.getMessage()
                , messageType.getTitle()
                , messageType.getMessageType());
    }
    public void setCreateMode(boolean isInCreateMode);

}
