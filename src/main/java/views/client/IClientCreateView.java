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

    //test empty fields
    JTextField getClientTextField();
    JTextField getCityTextField();
}
