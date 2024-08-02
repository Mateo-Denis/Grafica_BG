package presenters.client;

import models.IClientModel;
import presenters.StandardPresenter;
import views.client.*;

import static utils.MessageTypes.*;

public class ClientCreatePresenter extends StandardPresenter {
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
        clientModel.addClientCreationSuccessListener(() -> clientCreateView.showMessage(CLIENT_CREATION_SUCCESS));
        clientModel.addClientCreationFailureListener(() -> clientCreateView.showMessage(CLIENT_CREATION_FAILURE));
    }

    public void onCreateClientButtonClicked() {
        clientCreateView.showView();
    }



    public void onCreateButtonClicked() {
        clientCreateView.setWorkingStatus();

        clientModel.createClient(clientCreateView.getClientTextField(),
                clientCreateView.getAddressTextField(),
                clientCreateView.getCityTextField(),
                clientCreateView.getPhoneTextField(),
                clientCreateView.isClientRadioButtonSelected());

        clientCreateView.setWaitingStatus();
    }

}
