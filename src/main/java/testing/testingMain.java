package testing;


import PdfFormater.WorkBudgetClientPDFConverter;
import PdfFormater.WorkBudgetPDFConverter;
import PdfFormater.NewRow;
import org.javatuples.Pair;
import utils.Client;
import utils.databases.ClientsDatabaseConnection;

import java.util.ArrayList;

public class testingMain {
    private static final WorkBudgetPDFConverter jobBudgetPDFConverter = new WorkBudgetPDFConverter();
    private static final WorkBudgetClientPDFConverter jobBudgetClientPDFConverter = new WorkBudgetClientPDFConverter();
    private static final ClientsDatabaseConnection clientsDatabaseConnection = new ClientsDatabaseConnection();

    public static void main(String[] args) {
        ArrayList<Pair<String, String>> testList = new ArrayList<>();
        testList.add(new Pair<>("AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAKkAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAPpAAAA", "5000"));
        testList.add(new Pair<>("Item 2", "200"));
        testList.add(new Pair<>("Item 3", "300"));
        testList.add(new Pair<>("Item 4", "400"));

        ArrayList<Pair<String, String>> tetoList = new ArrayList<>();
        tetoList.add(new Pair<>("Juan", "2000"));
        tetoList.add(new Pair<>("Item 2", "200"));
        tetoList.add(new Pair<>("Item 3", "300"));
        tetoList.add(new Pair<>("Item 4", "400"));


        Pair<String, String> logistics = new Pair<>("\"Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.\"", "2000");
        Pair<String, String> placing = new Pair<>("Placing item", "1500");

        String depositValue = "500";
        String balanceValue = "5000";

        String budgetCost = "9000";
        String totalCost = "12000";

        Client client = clientsDatabaseConnection.getOneClient(27);


        try {
/*            jobBudgetPDFConverter.generateWorkBill(
                    false,
                    3,
                    27,
                    testList,
                    logistics,
                    placing,
                    depositValue,
                    balanceValue,
                    budgetCost,
                    totalCost
            );*/

            jobBudgetClientPDFConverter.generateBill(
                    false,
                    client,
                    5,
                    tetoList,
                    totalCost

            );
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}