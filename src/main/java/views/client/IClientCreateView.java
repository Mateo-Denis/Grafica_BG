package views.client;

import presenters.StandardPresenter;
import presenters.client.ClientCreatePresenter;
import views.IToggleableView;

public interface IClientCreateView extends IToggleableView {
    String getClientTextField();
    String getAddressTextField();
    String getCityTextField();
    String getPhoneTextField();
    boolean isClientRadioButtonSelected();
}
