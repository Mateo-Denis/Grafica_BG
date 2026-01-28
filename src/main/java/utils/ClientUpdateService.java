package utils;

import PdfFormater.PdfConverter;
import PdfFormater.Row;
import utils.databases.BudgetsDatabaseConnection;

import java.io.FileNotFoundException;
import java.sql.SQLException;
import java.util.ArrayList;

public class ClientUpdateService {
    private final BudgetsDatabaseConnection budgetDb;
    private final PdfConverter pdfConverter;
    private final EditPdfFileName editPdfFileName;

    public ClientUpdateService(BudgetsDatabaseConnection budgetDb, PdfConverter pdfConverter, EditPdfFileName editPdfFileName) {
        this.budgetDb = budgetDb;
        this.pdfConverter = pdfConverter;
        this.editPdfFileName = editPdfFileName;
    }

    public void registrarCambioDeNombre(Client client, String oldName, String newName) {
        // 1. Actualizar la DB de Presupuestos (Operación de base de datos)
        budgetDb.updateClientNameOnBudgets(oldName, newName);

        modifyClientNameOnBudgets(client, newName);
    }

    public void modifyClientNameOnBudgets(Client client, String newName) {
        ArrayList<Integer> budgetNumbers = null;
        ArrayList<String> budgetDates = null;

        try {
            budgetNumbers = budgetDb.getBudgetNumbersByClientName(newName);
            budgetDates = budgetDb.getBudgetDatesByClientName(newName);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        String budgetDate = "";
        int budgetNumber = 0;
        for(int i = 0; i < budgetNumbers.size(); i++) {
            System.out.println("Testing modified PDF generation for budget date: " + budgetDates.get(i));
            budgetDate = budgetDates.get(i);
            budgetNumber = budgetNumbers.get(i);
            GenerateModifiedPDF(budgetDate, client, budgetNumber);
        }
    }

    public void GenerateModifiedPDF(String budgetDate, Client client, int budgetNumber) {
        double total = 0.0;
        ArrayList<Row>  budgetData = getBudgetData(client.getName(), budgetNumber);

        for(Row row : budgetData) {
            total += row.getTotal();
        }
        try {
            System.out.println("Client for modified PDF: " + client.getName());
            pdfConverter.generateBill(true, budgetDate, client, budgetNumber, budgetData, total);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public ArrayList<Row> getBudgetData(String clientName, int budgetNumber) {
        ArrayList<Row> budgetDateAndNumberFromFileName = new ArrayList<>();
        ArrayList<String> productObservations = null;
        ArrayList<Double> productPrices = null;
        ArrayList<String> productNames = null;
        ArrayList<Integer> productAmounts = null;
        ArrayList<String> productMeasures = null;

        try {
            productMeasures = budgetDb.getProductMeasures(clientName, budgetNumber);
            productObservations = budgetDb.getProductObservations(clientName, budgetNumber);
            productPrices = budgetDb.getProductPrices(clientName, budgetNumber);
            productNames = budgetDb.getSavedProductNames(clientName, budgetNumber);
            productAmounts = budgetDb.getSavedProductAmounts(clientName, budgetNumber);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        String productName = "";
        int productAmount = 0;
        String productMeasure = "";
        String productObservation = "";
        double productPrice = 0.0;
        double totalPrice = 0.0;
        Row row;

        for (int i = 0; i < productNames.size(); i++) {
            productName = productNames.get(i);
            productAmount = productAmounts.get(i);
            productMeasure = productMeasures.get(i);
            productObservation = productObservations.get(i);
            productPrice = productPrices.get(i);
            totalPrice = productAmount * productPrice;

            row = new Row(productName, productAmount, productMeasure, productObservation, productPrice, totalPrice);
            budgetDateAndNumberFromFileName.add(row);
        }

        return budgetDateAndNumberFromFileName;
    }
}
