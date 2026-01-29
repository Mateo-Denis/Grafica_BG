package presenters.client;

import models.IClientModel;
import presenters.StandardPresenter;
import utils.Client;
import views.client.IClientCreateView;
import utils.ClientUpdateService;

import javax.swing.*;

import java.io.FileNotFoundException;
import java.sql.SQLException;
import java.util.ArrayList;

import static utils.MessageTypes.*;

public class ClientCreatePresenter extends StandardPresenter {
    private final IClientCreateView clientCreateView;
    private final IClientModel clientModel;
    private final ClientUpdateService clientUpdateService;

    public ClientCreatePresenter(IClientCreateView clientCreateView, IClientModel clientModel, ClientUpdateService clientUpdateService) {
        this.clientCreateView = clientCreateView;
        view = clientCreateView;
        this.clientModel = clientModel;
        this.clientUpdateService = clientUpdateService;

    }

    public void initListeners() {
        clientModel.addClientCreationSuccessListener(() -> clientCreateView.showMessage(CLIENT_CREATION_SUCCESS));
        clientModel.addClientCreationFailureListener(() -> clientCreateView.showMessage(CLIENT_CREATION_FAILURE));


        clientModel.addCitiesFetchingSuccessListener(() -> {
            clientCreateView.addCityToComboBox("Nueva localidad");
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
        return nameField.getText().trim().isEmpty() || (cityField.getText().trim().isEmpty() && cityComboBox.getSelectedItem().equals("Nueva localidad"));
    }

        public void onCreateButtonClicked () {
            clientCreateView.setWorkingStatus();
            String city = "";

            //test
            boolean anyEmpty = onEmptyFields(clientCreateView.getClientTextField(), clientCreateView.getCityTextField(), clientCreateView.getCityComboBox());

            if(anyEmpty) {
                clientCreateView.showMessage(ANY_CREATION_EMPTY_FIELDS);
            } else {

                if(clientCreateView.isEditMode()){
                    city = clientCreateView.getComboBoxSelectedCity();

                    int oldClientID = clientCreateView.getEditingClientID();
                    Client oldClient = clientModel.getClientByID(String.valueOf(oldClientID));
                    String oldClientName = oldClient.getName();

                    clientModel.updateClient(clientCreateView.getEditingClientID(),
                            clientCreateView.getClientText(),
                            clientCreateView.getAddressText(),
                            city,
                            clientCreateView.getPhoneText(),
                            clientCreateView.isClientSelected());
                    clientModel.queryClients("", "");

                    Client newClient = clientModel.getClientByID(String.valueOf(oldClientID));
                    int newClientID = clientModel.getClientID(newClient.getName(), clientCreateView.isClient());

                    clientUpdateService.registrarCambioDeNombre(newClient, oldClientName, clientCreateView.getClientText());
                    clientUpdateService.registrarCambioDeNombreEnWorkBudgets(newClient, oldClientID, newClientID, oldClientName);

                    clientCreateView.clearView();
                    clientCreateView.hideView();

                }else {
                    if(clientCreateView.getComboBoxSelectedCity().equals("Nueva localidad")){
                        city = clientCreateView.getCityText();
                    } else {
                        city = clientCreateView.getComboBoxSelectedCity();
                    }

                    clientModel.createClient(clientCreateView.getClientText(),
                            clientCreateView.getAddressText(),
                            city,
                            clientCreateView.getPhoneText(),
                            clientCreateView.isClientSelected());

                    clientCreateView.clearView();

                }

            }
            clientCreateView.setWaitingStatus();
        }

        public void onCityComboBoxSelected () {
            if (clientCreateView.getComboBoxSelectedCity().equals("Nueva localidad")) {
                clientCreateView.getCityTextField().setEnabled(true);
            } else {
                clientCreateView.getCityTextField().setText("");
                clientCreateView.getCityTextField().setEnabled(false);
            }
        }

        public void loadClientToEdit(String clientID) {
            Client toLoad = clientModel.getClientByID(clientID);
            clientCreateView.getClientTextField().setText(toLoad.getName());
            clientCreateView.getAddressTextField().setText(toLoad.getAddress());
            if (toLoad.isClient()) {
                clientCreateView.toggleToClientRadioButton();
            } else {
                clientCreateView.toggleToParticularRadioButton();
            }
            clientCreateView.getCityComboBox().setSelectedItem(toLoad.getCity());
            clientCreateView.getPhoneTextField().setText(toLoad.getPhone());
        }

        public String getIsClientTypeSelected() {
            if (clientCreateView.isClientSelected()) {
                return "Cliente";
            } else {
                return "Particular";
            }
        }

        public int getClientIDByName(String clientName) {
            int id = clientModel.getClientID(clientName, "Cliente");
            if(id == 0){
                id = clientModel.getClientID(clientName, "Particular");
            }
            return id;
        }
    }
