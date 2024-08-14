package views.client;

import views.IToggleableView;

import javax.swing.*;

public interface IClientCreateView extends IToggleableView {
    String getClientText();
    String getAddressText();
    String getCityText();
    String getPhoneText();
    boolean isClientSelected();

    //test empty fields
    JTextField getClientTextField();
    JTextField getCityTextField();
}
