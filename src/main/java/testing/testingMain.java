package testing;

import models.BudgetModel;
import utils.databases.BudgetsDatabaseConnection;
import utils.databases.DatabaseConnection;
import utils.databases.ProductsDatabaseConnection;

import javax.swing.*;
import java.awt.*;
import java.sql.*;
import java.util.*;

public class testingMain {
    private static BudgetsDatabaseConnection budgetsDBConnection = new BudgetsDatabaseConnection();
    private static ProductsDatabaseConnection productsDBConnection = new ProductsDatabaseConnection();
    private static BudgetModel bmodel;
    private DatabaseConnection dbconn;

    public static void main(String[] args) throws SQLException {
        //ArrayList<String> products = getBudgetProductsName();
    }


    public static int getBudgetID(String budgetName, int budgetNumber) throws SQLException {
        String sql = "SELECT ID, Numero_presupuesto FROM Presupuestos WHERE Nombre_Cliente = ? AND Numero_presupuesto = ?";
        Connection conn = budgetsDBConnection.connect();
        PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.setString(1, budgetName);
        pstmt.setInt(2, budgetNumber);
        ResultSet resultSet = pstmt.executeQuery();
        int budgetID = 0;
        while (resultSet.next()) {
            budgetID = resultSet.getInt("ID");
        }
        pstmt.close();
        conn.close();
        return budgetID;
    }


    public static void saveProductssss(int budgetNumber, String budgetName, Map<Integer,String> products) throws SQLException {
        String sql = "INSERT INTO PRESUPUESTO_PRODUCTOS(ID_PRESUPUESTO, ID_PRODUCTO, CANTIDAD) VALUES(?, ?, ?)";
        Connection conn = budgetsDBConnection.connect();
        PreparedStatement pstmt = conn.prepareStatement(sql);
        for (Map.Entry<Integer, String> entry : products.entrySet()) { //POR CADA TUPLA EL EL MAPA, EL INT ES LA CANTIDAD DEL PRODUCTO Y EL STRING EL NOMBRE
            int budgetID = getBudgetID(budgetName, budgetNumber); //OBTIENE EL ID DEL PRESUPUESTO
            int productID = productsDBConnection.getProductID(entry.getValue());//OBTIENE EL ID DEL PRODUCTO AGARRANDO LA PARTE STRING DEL MAPA Y PASANDOLA
            int productAmount = entry.getKey(); //LA CANTIDAD DEL PRODUCTO ES LA PARTE INT DEL MAPA              //COMO PARAMETRO A LA FUNCION GETPRODUCTID
            pstmt.setInt(1, budgetID);
            pstmt.setInt(2, productID);
            pstmt.setInt(3, productAmount);
            pstmt.executeUpdate();
        }
        pstmt.close();
        conn.close();
    }

    public static ArrayList<String> getBudgetProductsName(String budgetName, int budgetNumber) {
        Map<Integer,String> products = bmodel.getSavedProducts(budgetNumber, budgetName);
        ArrayList<String> productsName = new ArrayList<>();

        for(Map.Entry<Integer,String> entry : products.entrySet()) {
            String actualProductName = entry.getValue();
            if(!productsName.contains(actualProductName)) {
                productsName.add(actualProductName);
            }
        }

        return productsName;
    }
}
