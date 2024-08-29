package models;

import utils.Client;
import java.util.ArrayList;

public interface IClientListModel {
    ArrayList<Client> getClientsFromDB();
}
