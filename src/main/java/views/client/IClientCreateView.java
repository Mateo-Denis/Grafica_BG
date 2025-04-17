package views.client;

import views.IToggleableView;
import javax.swing.*;

public interface IClientCreateView extends IToggleableView {
    String getClientText();
    String getAddressText();
    String getCityText();
    String getPhoneText();
    boolean isClientSelected();
    void addCityToComboBox(String city);
    String getComboBoxSelectedCity();
    JTextField getClientTextField();
    JTextField getCityTextField();
    JComboBox<String> getCityComboBox();
    void clearView();
}
