package presenters.client;

import models.IClientModel;
import presenters.StandardPresenter;
import views.client.*;

import javax.swing.*;

import static utils.MessageTypes.*;

public class ClientCreatePresenter extends StandardPresenter {
    private final IClientCreateView clientCreateView;
    private final IClientModel clientModel;

    public ClientCreatePresenter(IClientCreateView clientCreateView, IClientModel clientModel) {
        this.clientCreateView = clientCreateView;
        view = clientCreateView;
        this.clientModel = clientModel;

    }

    public void initListeners() {
        clientModel.addClientCreationSuccessListener(() -> clientCreateView.showMessage(CLIENT_CREATION_SUCCESS));
        clientModel.addClientCreationFailureListener(() -> clientCreateView.showMessage(CLIENT_CREATION_FAILURE));


        //TEST CAMPOS OBLIGATORIOS AL CREAR CLIENTE
        clientModel.addClientCreationEmptyFieldListener(() -> clientCreateView.showMessage(CLIENT_CREATION_EMPTY_FIELDS));
    }

    public void onHomeCreateClientButtonClicked() {
        clientCreateView.showView();
    }



    //TEST CAMPOS OBLIGATORIOS AL CREAR CLIENTE
    public boolean onEmptyFields(JTextField nameField, JTextField cityField) {
        boolean anyEmpty = false;
        if (nameField.getText().trim().isEmpty() || cityField.getText().trim().isEmpty()) {
            anyEmpty = true;
        }
        return anyEmpty;
    }

        public void onCreateButtonClicked () {
            clientCreateView.setWorkingStatus();
            //test
            boolean anyEmpty = onEmptyFields(clientCreateView.getClientTextField(), clientCreateView.getCityTextField());
            if(anyEmpty)
            {
                clientCreateView.showMessage(CLIENT_CREATION_EMPTY_FIELDS);
            } else {
                clientModel.createClient(clientCreateView.getClientText(),
                        clientCreateView.getAddressText(),
                        clientCreateView.getCityText(),
                        clientCreateView.getPhoneText(),
                        clientCreateView.isClientSelected());

            }
            clientCreateView.setWaitingStatus();
            //test
/*            clientModel.createClient(clientCreateView.getClientText(),
                    clientCreateView.getAddressText(),
                    clientCreateView.getCityText(),
                    clientCreateView.getPhoneText(),
                    clientCreateView.isClientSelected());
            clientCreateView.setWaitingStatus();
*/
        }

    }
