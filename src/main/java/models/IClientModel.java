package models;

import models.listeners.failed.ClientCreationFailureListener;
import models.listeners.failed.ClientSearchFailureListener;
import models.listeners.successful.ClientCreationSuccessListener;
import models.listeners.successful.ClientSearchSuccessListener;
import utils.Client;

import java.util.ArrayList;

public interface IClientModel {
	void createClient(String clientName, String clientAddress, String clientCity, String clientPhone, boolean clientType);

	void addClientCreationSuccessListener(ClientCreationSuccessListener listener);
	void addClientCreationFailureListener(ClientCreationFailureListener listener);
	void addClientSearchSuccessListener(ClientSearchSuccessListener listener);
	void addClientSearchFailureListener(ClientSearchFailureListener listener);

	void queryClients(String searchedName, String searchedAddress);
	ArrayList<Client> getLastClientsQuery();

}
