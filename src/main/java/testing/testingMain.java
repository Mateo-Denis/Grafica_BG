package testing;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import utils.databases.BudgetsDatabaseConnection;
import utils.Product;

import java.sql.*;

public class testingMain {
    private static Multimap<Integer, Product> productMap = ArrayListMultimap.create();
    private static BudgetsDatabaseConnection budgetsDatabaseConnection = new BudgetsDatabaseConnection();

    public static void main(String[] args) throws SQLException {
        System.out.println(budgetsDatabaseConnection.getNextBudgetNumber());
    }
}
