package views.client;

import presenters.ClientCreatePresenter;
import utils.MessageTypes;
import views.IToggableView;

public interface IClientCreateView extends IToggableView {
    String getClientTextField();
    String getAddressTextField();
    String getCityTextField();
    String getPhoneTextField();
    boolean isClientRadioButtonSelected();

	void setPresenter(ClientCreatePresenter clientCreatePresenter);

}
