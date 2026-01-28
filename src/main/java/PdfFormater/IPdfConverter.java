package PdfFormater;

import utils.Client;

import java.io.FileNotFoundException;
import java.util.ArrayList;

public interface IPdfConverter {
    void generateBill(boolean isClientEditing, String clientEditingDate, Client client, int billNumber, ArrayList<Row> tableContent, double total) throws FileNotFoundException;

}
