package models;

import models.listeners.ClientCreationFailureListener;
import models.listeners.ClientCreationSuccessListener;

public interface IClientModel {
	void createClient(String clientName, String clientAddress, String clientCity, String clientPhone, boolean clientType);

	void addClientCreationSuccessListener(ClientCreationSuccessListener listener);

	void addClientCreationFailureListener(ClientCreationFailureListener listener);
}
