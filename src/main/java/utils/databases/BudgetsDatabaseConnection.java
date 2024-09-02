package utils.databases;

import utils.Budget;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import utils.IsNumeric;
import utils.Product;

public class BudgetsDatabaseConnection extends DatabaseConnection{

    private ProductsDatabaseConnection productsDBConnection;


    @Override
    protected void createTable(Connection connection) {
        String budgetSQL =  "CREATE TABLE IF NOT EXISTS Presupuestos (" +
                            "ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                            "Nombre_Cliente TEXT NOT NULL," +
                            "Fecha TEXT NOT NULL," +
                            "Tipo_Cliente TEXT NOT NULL CHECK(Tipo_Cliente IN ('Cliente', 'Particular'))," +
                            "Numero_Presupuesto INTEGER NOT NULL" +
                            ")";
        try (Statement stmt = connection.createStatement()) {
            stmt.setQueryTimeout(QUERY_TIMEOUT);
            stmt.execute(budgetSQL);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void insertBudget(String budgetClientName, String budgetDate, String budgetClientType, int budgetNumber) throws SQLException{
        String sql = "INSERT INTO Presupuestos(Nombre_Cliente, Fecha, Tipo_Cliente, Numero_presupuesto) VALUES(?, ?, ?, ?)";
        Connection conn = connect();
        PreparedStatement pstmt = conn.prepareStatement(sql);

        pstmt.setString(1, budgetClientName);
        pstmt.setString(2, budgetDate);
        pstmt.setString(3, budgetClientType);
        pstmt.setInt(4, budgetNumber);
        pstmt.executeUpdate();

        pstmt.close();
        conn.close();
    }


    public ArrayList<Budget> getBudgets(String budgetSearch) throws SQLException {
        String sql;
        Connection conn = connect();
        PreparedStatement pstmt;

        // Verifica si el budgetSearch es numérico
        if (budgetSearch.matches("\\d+")) {
            // Si es numérico, buscar por número de presupuesto
            sql = "SELECT * FROM Presupuestos WHERE Numero_presupuesto= ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, Integer.parseInt(budgetSearch));
        } else {
            // Si no es numérico, buscar por nombre
            sql = "SELECT * FROM Presupuestos WHERE Nombre_Cliente LIKE ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, "%" + budgetSearch + "%");
        }

        ResultSet resultSet = pstmt.executeQuery();
        ArrayList<Budget> budgets = new ArrayList<>();

        while (resultSet.next()) {
            Budget budget = new Budget(
                    resultSet.getString("Nombre_Cliente"),
                    resultSet.getString("Fecha"),
                    resultSet.getString("Tipo_Cliente"),
                    resultSet.getInt("Numero_Presupuesto")
            );
            budgets.add(budget);
        }

        pstmt.close();
        conn.close();
        return budgets;
    }

    public ArrayList<Budget> getAllBudgets() {
        String sql = "SELECT * FROM Presupuestos";
        try (Connection conn = connect();
             Statement stmt = conn.createStatement();
             ResultSet resultSet = stmt.executeQuery(sql)) {

            ArrayList<Budget> budgets = new ArrayList<>();
            while (resultSet.next()) {
                Budget budget = new Budget(
                        resultSet.getString("Nombre_Cliente"),
                        resultSet.getString("Fecha"),
                        resultSet.getString("Tipo_Cliente"),
                        resultSet.getInt("Numero_Presupuesto")
                );
                budgets.add(budget);
            }
            return budgets;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return new ArrayList<>();
    }

    public int getBudgetID(String budgetName, int budgetNumber) throws SQLException {
        String sql = "SELECT ID, Numero_presupuesto FROM Presupuestos WHERE Nombre_Cliente = ? AND Numero_presupuesto = ?";
        Connection conn = connect();
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

    public void deleteOneBudget(int budgetID) throws SQLException {
        String sql = "DELETE FROM Presupuestos WHERE ID = ?";
        Connection conn = connect();
        PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.setInt(1, budgetID);
        pstmt.executeUpdate();
        pstmt.close();
        conn.close();
    }

    public void deleteMultipleBudgets(ArrayList<Integer> budgetIDs) throws SQLException {
        String sql = "DELETE FROM Presupuestos WHERE ID = ?";
        Connection conn = connect();
        PreparedStatement pstmt = conn.prepareStatement(sql);
        for (int budgetID : budgetIDs) {
            pstmt.setInt(1, budgetID);
            pstmt.executeUpdate();
        }
        pstmt.close();
        conn.close();
    }

    public void saveProducts(int budgetNumber, String budgetName, Map<Integer,String> products) throws SQLException {
        String sql = "INSERT INTO PRESUPUESTO_PRODUCTOS(ID_PRESUPUESTO, ID_PRODUCTO, CANTIDAD) VALUES(?, ?, ?)";
        Connection conn = connect();
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

    public Map<Integer,String> getSavedProducts(String budgetName, int budgetNumber) throws SQLException {
        Map<Integer,String> products = new HashMap<>();
        String sql = "SELECT ID_PRODUCTO, CANTIDAD FROM PRESUPUESTO_PRODUCTOS WHERE ID_PRESUPUESTO = ?";
        Connection conn = connect();
        PreparedStatement pstmt = conn.prepareStatement(sql);
        int budgetID = getBudgetID(budgetName, budgetNumber);
        pstmt.setInt(1, budgetID);
        ResultSet resultSet = pstmt.executeQuery();
        while (resultSet.next()) {
            Product product = productsDBConnection.getOneProduct(resultSet.getInt("ID_PRODUCTO"));
            int productID = resultSet.getInt("ID_PRODUCTO");
            int productAmount = resultSet.getInt("CANTIDAD");
            String productName = product.getName();
            products.put(productAmount, productName);
        }
        pstmt.close();
        conn.close();
        return products;
    }

}
