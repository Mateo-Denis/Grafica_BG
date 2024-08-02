package views.client;

import views.IToggleableView;

public interface IClientCreateView extends IToggleableView {
    String getClientText();
    String getAddressText();
    String getCityText();
    String getPhoneText();
    boolean isClientSelected();
}
