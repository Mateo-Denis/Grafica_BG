package testing;


import PdfFormater.WorkBudgetClientPDFConverter;
import PdfFormater.WorkBudgetPDFConverter;
import PdfFormater.NewRow;
import org.javatuples.Pair;
import utils.Client;
import utils.databases.BudgetsDatabaseConnection;
import utils.databases.ClientsDatabaseConnection;

import java.sql.SQLException;
import java.util.ArrayList;

public class testingMain {
    private static final WorkBudgetPDFConverter jobBudgetPDFConverter = new WorkBudgetPDFConverter();
    private static final WorkBudgetClientPDFConverter jobBudgetClientPDFConverter = new WorkBudgetClientPDFConverter();
    private static final ClientsDatabaseConnection clientsDatabaseConnection = new ClientsDatabaseConnection();
    private static final BudgetsDatabaseConnection budgetsDatabaseConnection = new BudgetsDatabaseConnection();

    public static void main(String[] args) throws SQLException {
        ArrayList<String> budgetDates = budgetsDatabaseConnection.getBudgetDatesByClientName("Bruno de cote");
        for (String date : budgetDates) {
            System.out.println("Date: " + date);
        }
    }
}