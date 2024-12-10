package testing;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import models.BudgetModifyModel;
import utils.databases.BudgetsDatabaseConnection;
import utils.Product;

import java.sql.*;
import java.util.ArrayList;
import java.util.Map;

public class testingMain {
    private static Multimap<Integer, Product> productMap = ArrayListMultimap.create();
    private static BudgetsDatabaseConnection budgetsDatabaseConnection = new BudgetsDatabaseConnection();
    private static BudgetModifyModel budgetModifyModel = new BudgetModifyModel(budgetsDatabaseConnection);

    public static void main(String[] args) throws SQLException {
        Multimap<Integer,String> products = budgetModifyModel.getSavedProducts(1, "aaaa");
        for (Map.Entry<Integer, String> entry : products.entries()) {
            System.out.println("Key = " + entry.getKey() + ", Value = " + entry.getValue());
        }
    }
}
