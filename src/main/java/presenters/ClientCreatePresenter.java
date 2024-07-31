package presenters;

import models.IClientModel;
import views.client.*;

public class ClientCreatePresenter{
    private final IClientCreateView clientCreateView;
    private final IClientModel clientModel;

    public ClientCreatePresenter(IClientCreateView clientCreateView, IClientModel clientModel) {
        this.clientCreateView = clientCreateView;
        this.clientModel = clientModel;
    }

    public void start(){
        clientCreateView.setPresenter(this);
        initListeners();
    }

    public void initListeners() {
        clientModel.addClientCreationSuccessListener(clientCreateView::showSuccessMessage);
    }



    public void onCreateButtonClicked() {
        clientModel.createClient(clientCreateView.getClientTextField(),
                clientCreateView.getAddressTextField(),
                clientCreateView.getCityTextField(),
                clientCreateView.getPhoneTextField(),
                clientCreateView.isClientRadioButtonSelected());
        clientCreateView.showSuccessMessage();
    }

}
