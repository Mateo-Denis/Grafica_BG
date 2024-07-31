package views.client;

import presenters.ClientCreatePresenter;

public interface IClientCreateView {
    String getClientTextField();
    String getAddressTextField();
    void showSuccessMessage();
    void showErrorMessage(String message);

    String getCityTextField();

    String getPhoneTextField();

    boolean isClientRadioButtonSelected();

	void setPresenter(ClientCreatePresenter clientCreatePresenter);
}
