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

    public static void main(String[] args) throws SQLException {
        Product product1 = new Product("Producto1", "Descripcion1", 10, 1);
        Product product2 = new Product("Producto2", "Descripcion2", 20, 1);
        Product product3 = new Product("Producto3", "Descripcion3", 30, 1);
        Product product4 = new Product("Producto4", "Descripcion4", 40, 1);

        productMap.put(1, product1);
        productMap.put(2, product2);
        productMap.put(1, product3);
        productMap.put(2, product4);

        for(Map.Entry<Integer, Product> entry : productMap.entries()) {
            System.out.println("Key: " + entry.getKey() + " Value: " + entry.getValue().getName());
        }

        productMap.remove(1, product1);

        for(Map.Entry<Integer, Product> entry : productMap.entries()) {
            System.out.println("Key: " + entry.getKey() + " Value: " + entry.getValue().getName());
        }
    }
}
