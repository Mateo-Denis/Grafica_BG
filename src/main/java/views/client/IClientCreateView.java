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
    JTextField getAddressTextField();
    JTextField getPhoneTextField();
    JTextField getCityTextField();
    JComboBox<String> getCityComboBox();

    void toggleToClientRadioButton();

    void toggleToParticularRadioButton();

    void clearView();

    boolean isEditMode();

    int getEditingClientID();
}
