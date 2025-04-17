package views.products.modular;

import org.javatuples.Triplet;
import utils.Attribute;
import utils.MessageTypes;

import javax.swing.*;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public interface IModularCategoryView{

    JPanel getContainerPanel();
    void initListeners();
    void calculateDependantPrices();
    default void showMessage(MessageTypes messageType, JPanel containerPanelWrapper) {
        JOptionPane.showMessageDialog(containerPanelWrapper, messageType.getMessage()
                , messageType.getTitle()
                , messageType.getMessageType());
    }

    Map<String,String> getComboBoxValues();

    Map<String,String> getTextFieldValues();

    ArrayList<String> getRadioValues();

	void loadComboBoxValues();

    List<Triplet<String, String, Double>> getModularPrices();

    void unlockTextFields();

    void blockTextFields();

    void setPriceTextFields();

	ArrayList<Attribute> getAttributes();

    void comboBoxListenerSet(ItemListener listener);
}
