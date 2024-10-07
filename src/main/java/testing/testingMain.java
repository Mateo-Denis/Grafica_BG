package testing;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import models.BudgetModel;
import utils.databases.BudgetsDatabaseConnection;
import utils.databases.DatabaseConnection;
import utils.databases.ProductsDatabaseConnection;
import utils.Product;

import javax.swing.*;
import java.awt.*;
import java.sql.*;
import java.util.*;

public class testingMain {
    private static Multimap<Integer, Product> productMap = ArrayListMultimap.create();
    private static BudgetsDatabaseConnection budgetsDatabaseConnection = new BudgetsDatabaseConnection();

    public static void main(String[] args) throws SQLException {
        System.out.println(budgetsDatabaseConnection.getNextBudgetNumber());
    }
}
