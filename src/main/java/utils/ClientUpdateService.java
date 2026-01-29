package utils;

import PdfFormater.PdfConverter;
import PdfFormater.Row;
import PdfFormater.WorkBudgetClientPDFConverter;
import PdfFormater.WorkBudgetPDFConverter;
import org.javatuples.Pair;
import utils.databases.BudgetsDatabaseConnection;
import utils.databases.WorkBudgetsDatabaseConnection;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

public class ClientUpdateService {
    private final BudgetsDatabaseConnection budgetDb;
    private final WorkBudgetsDatabaseConnection workBudgetDb;

    private static final PdfConverter pdfConverter = new PdfConverter();
    private static final WorkBudgetPDFConverter workBudgetPDFConverter = new WorkBudgetPDFConverter();
    private static final PDFOpener pdfOpener = new PDFOpener();
    private static final WorkBudgetClientPDFConverter workBudgetClientPDFConverter = new WorkBudgetClientPDFConverter();

    public ClientUpdateService(BudgetsDatabaseConnection budgetDb, WorkBudgetsDatabaseConnection workBudgetDb) {
        this.budgetDb = budgetDb;
        this.workBudgetDb = workBudgetDb;
    }

    public void registrarCambioDeNombre(Client client, String oldName, String newName) {
        // 1. Actualizar la DB de Presupuestos (Operación de base de datos)
        budgetDb.updateClientNameOnBudgets(oldName, newName);
        pdfOpener.clientUpdateDeletePDFS("/PresupuestosPDF/", oldName);
        modifyClientNameOnBudgets(client, newName);
    }

    public void registrarCambioDeNombreEnWorkBudgets(Client newClient, int oldClientID, int newClientID, String oldClientName) {
        try {
            workBudgetDb.updateClientIDOnWorkBudgets(oldClientID, newClientID);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        pdfOpener.clientUpdateDeleteWorkPDFS("/Presupuestos_Trabajo_Internos_PDF/", oldClientName);
        pdfOpener.clientUpdateDeleteClientWorkPDFs("/Presupuestos_Trabajo_Clientes_PDF/", oldClientName);
        modifyClientNameOnWorkBudgets(newClientID, newClient);
    }

    private void modifyClientNameOnBudgets(Client client, String newName) {
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

    private void modifyClientNameOnWorkBudgets(int clientID, Client client) {
        ArrayList<Integer> workBudgetIDs = null;
        ArrayList<String> workBudgetDates = null;

        try {
            workBudgetIDs = workBudgetDb.getWorkBudgetIDsByClientID(clientID);
            workBudgetDates = workBudgetDb.getWorkBudgetDatesByClientID(clientID);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        for(int i = 0; i < workBudgetIDs.size(); i++) {
            int workBudgetID = workBudgetIDs.get(i);
            String workBudgetDate = workBudgetDates.get(i);
            System.out.println("Testing modified PDF generation for work budget ID: " + workBudgetID);
            GenerateWorkBudgetModifiedPDF(client, workBudgetID, workBudgetDate);
        }
    }

    private void GenerateModifiedPDF(String budgetDate, Client client, int budgetNumber) {
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

    private void GenerateWorkBudgetModifiedPDF(Client client, int workBudgetID, String workBudgetDate) {
        WorkBudgetData workBudgetData = getWorkBudgetData(workBudgetID);
        WorkBudget workBudget = getWorkBudget(workBudgetID);
        double finalPrice = Double.parseDouble(workBudget.getFinalPrice());
        double deposit = finalPrice * 0.5;
        double balance = finalPrice - deposit;
        double profit = Double.parseDouble(workBudgetData.getProfit());
        double budgetCost = finalPrice / (1 + (profit / 100));

        Pair<String, String> logistics = new Pair<String, String>(workBudgetData.getLogistics(), workBudgetData.getLogisticsCost());

        try {
            System.out.println("Generating modified work budget PDF for client ID: " + workBudgetData.getClientID());
            workBudgetPDFConverter.generateWorkBill(true, workBudgetDate, workBudgetData.getBudgetNumber(), workBudgetData.getClientID(),
                    workBudgetData.getMaterials(), logistics, workBudgetData.getPlacers(), String.valueOf(deposit), String.valueOf(balance),
                    String.valueOf(budgetCost), String.valueOf(finalPrice));
            workBudgetClientPDFConverter.generateBill(true, workBudgetDate, client, workBudgetID, workBudgetData.getDescriptions(),
                    String.valueOf(finalPrice), new Pair<>(String.valueOf(deposit), String.valueOf(balance)));
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private ArrayList<Row> getBudgetData(String clientName, int budgetNumber) {
        ArrayList<String> productObservations = null;
        ArrayList<Double> productPrices = null;
        ArrayList<String> productNames = null;
        ArrayList<Integer> productAmounts = null;
        ArrayList<String> productMeasures = null;

        ArrayList<Row> budgetData = null;

        try {
            productMeasures = budgetDb.getProductMeasures(clientName, budgetNumber);
            productObservations = budgetDb.getProductObservations(clientName, budgetNumber);
            productPrices = budgetDb.getProductPrices(clientName, budgetNumber);
            productNames = budgetDb.getSavedProductNames(clientName, budgetNumber);
            productAmounts = budgetDb.getSavedProductAmounts(clientName, budgetNumber);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return createBudgetDataList(productNames, productAmounts, productMeasures,
                productObservations, productPrices);

    }

    private ArrayList<Row> createBudgetDataList(ArrayList<String> productNames, ArrayList<Integer> productAmounts,
                                                ArrayList<String> productMeasures, ArrayList<String> productObservations,
                                                ArrayList<Double> productPrices) {

        ArrayList<Row> budgetData = new ArrayList<>();

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
            budgetData.add(row);
        }

        return budgetData;
    }

    public WorkBudgetData getWorkBudgetData(int workBudgetID){
        try {
            return workBudgetDb.getWorkBudgetData(workBudgetID);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public WorkBudget getWorkBudget(int workBudgetID){
        try {
            return workBudgetDb.getWorkBudget(workBudgetID);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
