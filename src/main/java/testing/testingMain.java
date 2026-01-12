package testing;


import PdfFormater.WorkBudgetClientPDFConverter;
import PdfFormater.WorkBudgetPDFConverter;
import PdfFormater.NewRow;
import utils.Client;

import java.util.ArrayList;

public class testingMain {
    private final WorkBudgetPDFConverter jobBudgetPDFConverter = new WorkBudgetPDFConverter();
    private static final WorkBudgetClientPDFConverter jobBudgetClientPDFConverter = new WorkBudgetClientPDFConverter();

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

