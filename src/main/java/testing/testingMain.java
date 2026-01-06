package testing;


import PdfFormater.JobBudgetClientPDFConverter;
import PdfFormater.JobBudgetPDFConverter;
import PdfFormater.NewRow;
import PdfFormater.codingerror.service.CodingErrorPdfInvoiceCreator;
import utils.Client;
import utils.NewProduct;

import java.util.ArrayList;

public class testingMain {
    private final JobBudgetPDFConverter jobBudgetPDFConverter = new JobBudgetPDFConverter();
    private static final JobBudgetClientPDFConverter jobBudgetClientPDFConverter = new JobBudgetClientPDFConverter();

    public static void main(String[] args) {
        try {
/*
            JobBudgetPDFConverter.generarFactura();

*/
            Client client = new Client("Cliente de prueba", "Calle Falsa 123", "Ciudad Ejemplo", "12345", true);
            int billNumber = 1;
            ArrayList<NewRow> tableContent = new ArrayList<>();
            tableContent.add(new NewRow("Descripcion larga 1, aqui puedes ingresar todo el texto que se quiera, .....", 100.0));
            tableContent.add(new NewRow("Descripcion larga 2, aqui puedes ingresar todo el texto que se quiera, .....", 200.0));
            double total = 300.0;

            jobBudgetClientPDFConverter.generateBill(false, client, billNumber, tableContent, total);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

