package presenters.client;

import models.IClientModel;
import presenters.StandardPresenter;
import views.client.IClientCreateView;

import javax.swing.*;

import java.util.ArrayList;

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


        clientModel.addCitiesFetchingSuccessListener(() -> {
            clientCreateView.addCityToComboBox("Cualquier localidad");
            ArrayList<String> cities = clientModel.getQueriedCities();
            for (String city : cities) {
                clientCreateView.addCityToComboBox(city);
            }
        });


        //TEST CAMPOS OBLIGATORIOS AL CREAR CLIENTE
        clientModel.addClientCreationEmptyFieldListener(() -> clientCreateView.showMessage(ANY_CREATION_EMPTY_FIELDS));
    }

    public void onHomeCreateClientButtonClicked() {
        clientCreateView.showView();
    }



    //TEST CAMPOS OBLIGATORIOS AL CREAR CLIENTE
    public boolean onEmptyFields(JTextField nameField, JTextField cityField, JComboBox cityComboBox) {
        boolean anyEmpty = false;
        if (nameField.getText().trim().isEmpty() || (cityField.getText().trim().isEmpty() && cityComboBox.getSelectedItem().equals("Cualquier localidad"))) {
            anyEmpty = true;
        }
        return anyEmpty;
    }

        public void onCreateButtonClicked () {
            clientCreateView.setWorkingStatus();
            String city = "";

            //test
            boolean anyEmpty = onEmptyFields(clientCreateView.getClientTextField(), clientCreateView.getCityTextField(), clientCreateView.getCityComboBox());

            if(anyEmpty)
            {
                clientCreateView.showMessage(ANY_CREATION_EMPTY_FIELDS);
            } else {

                if(clientCreateView.getComboBoxSelectedCity().equals("Cualquier localidad"))
                {
                    city = clientCreateView.getCityText();
                }
                else
                {
                    city = clientCreateView.getComboBoxSelectedCity();
                }



                clientModel.createClient(clientCreateView.getClientText(),
                        clientCreateView.getAddressText(),
                        city,
                        clientCreateView.getPhoneText(),
                        clientCreateView.isClientSelected());

            }
            clientCreateView.setWaitingStatus();
        }

        public void onCityComboBoxSelected () {
            if (clientCreateView.getComboBoxSelectedCity().equals("Cualquier localidad")) {
                clientCreateView.getCityTextField().setEnabled(true);
            } else {
                clientCreateView.getCityTextField().setText("");
                clientCreateView.getCityTextField().setEnabled(false);
            }
        }

    }
