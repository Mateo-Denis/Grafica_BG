package PdfFormater;

import utils.Client;

import java.io.FileNotFoundException;

public interface IPdfConverter {
    public void generateBill(boolean isPreview, Client client, int billNumber, Row[] tableContent, float total) throws FileNotFoundException;

}
