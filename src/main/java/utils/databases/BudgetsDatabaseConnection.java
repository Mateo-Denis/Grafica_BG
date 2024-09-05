package utils.databases;

import utils.Budget;
import java.sql.*;
import java.util.ArrayList;
import java.util.Map;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import utils.Product;

public class BudgetsDatabaseConnection extends DatabaseConnection{

    private ProductsDatabaseConnection productsDBConnection = new ProductsDatabaseConnection();


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
        String sql = "SELECT ID FROM Presupuestos WHERE Nombre_Cliente = ? AND Numero_presupuesto = ?";
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

    public void saveProducts(int budgetNumber, String budgetName, Multimap<Integer,String> products, ArrayList<String> productObservations, ArrayList<String> productMeasures) throws SQLException {
        int observationsIndex = 0;
        int measuresIndex = 0;
        String sql = "INSERT INTO PRESUPUESTO_PRODUCTOS(ID_PRESUPUESTO, ID_PRODUCTO, CANTIDAD, OBSERVACIONES, MEDIDAS) VALUES(?, ?, ?, ?, ?)";
        Connection conn = connect();
        PreparedStatement pstmt = conn.prepareStatement(sql);
        for (Map.Entry<Integer, String> entry : products.entries()) {
            String productName = entry.getValue();
            int budgetID = getBudgetID(budgetName, budgetNumber);
            int productID = productsDBConnection.getProductID(productName);
            int productAmount = entry.getKey();
            pstmt.setInt(1, budgetID);
            pstmt.setInt(2, productID);
            pstmt.setInt(3, productAmount);
            pstmt.setString(4, productObservations.get(observationsIndex));
            pstmt.setString(5, productMeasures.get(measuresIndex));

            pstmt.executeUpdate();
            observationsIndex++;
            measuresIndex++;
        }
        pstmt.close();
        conn.close();
    }

    public Multimap<Integer,String> getSavedProducts(String budgetName, int budgetNumber) throws SQLException {
        Multimap<Integer,String> products = ArrayListMultimap.create();
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

    public ArrayList<String> getProductObservations(String budgetName, int budgetNumber) throws SQLException {
        ArrayList<String> observations = new ArrayList<>();
        String sql = "SELECT OBSERVACIONES FROM PRESUPUESTO_PRODUCTOS WHERE ID_PRESUPUESTO = ?";
        Connection conn = connect();
        PreparedStatement pstmt = conn.prepareStatement(sql);
        int budgetID = getBudgetID(budgetName, budgetNumber);
        pstmt.setInt(1, budgetID);
        ResultSet resultSet = pstmt.executeQuery();
        while (resultSet.next()) {
            observations.add(resultSet.getString("OBSERVACIONES"));
        }
        pstmt.close();
        conn.close();
        return observations;
    }

    public ArrayList<String> getProductMeasures(String budgetName, int budgetNumber) throws SQLException {
        ArrayList<String> measures = new ArrayList<>();
        String sql = "SELECT MEDIDAS FROM PRESUPUESTO_PRODUCTOS WHERE ID_PRESUPUESTO = ?";
        Connection conn = connect();
        PreparedStatement pstmt = conn.prepareStatement(sql);
        int budgetID = getBudgetID(budgetName, budgetNumber);
        pstmt.setInt(1, budgetID);
        ResultSet resultSet = pstmt.executeQuery();
        while (resultSet.next()) {
            measures.add(resultSet.getString("MEDIDAS"));
        }
        pstmt.close();
        conn.close();
        return measures;
    }

    public void updateBothBudgetTables(String oldClientName, String newClientName, String date, String clientType, int budgetNumber, Multimap<Integer,String> products, ArrayList<String> productObservations, ArrayList<String> productMeasures) {
        updateBudgetTable(newClientName, date, clientType, budgetNumber);
        updateBudgetProductsTable(budgetNumber, oldClientName, newClientName, products, productObservations, productMeasures);
    }

    public void updateBudgetTable(String clientName, String date, String clientType, int budgetNumber) {
        String deleteSQL = "DELETE FROM Presupuestos WHERE Numero_presupuesto = ?";
        String insertSQL = "INSERT INTO Presupuestos(Nombre_Cliente, Fecha, Tipo_Cliente, Numero_presupuesto) VALUES(?, ?, ?, ?)";
        try (Connection conn = connect();
             PreparedStatement deletePstmt = conn.prepareStatement(deleteSQL);
             PreparedStatement insertPstmt = conn.prepareStatement(insertSQL)) {
            deletePstmt.setInt(1, budgetNumber);
            deletePstmt.executeUpdate();
            insertPstmt.setString(1, clientName);
            insertPstmt.setString(2, date);
            insertPstmt.setString(3, clientType);
            insertPstmt.setInt(4, budgetNumber);
            insertPstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void updateBudgetProductsTable( int budgetNumber, String oldClientName, String newClientName, Multimap<Integer,String> products, ArrayList<String> observations, ArrayList<String> productMeasures) {
        try {
            deleteBudgetProducts(oldClientName, budgetNumber);
            saveProducts(budgetNumber, newClientName, products, observations, productMeasures);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    private void deleteBudgetProducts(String clientName, int budgetNumber) throws SQLException {
        int budgetID = getBudgetID(clientName, budgetNumber) - 1;
        String sql = "DELETE FROM PRESUPUESTO_PRODUCTOS WHERE ID_PRESUPUESTO = ?";
        Connection conn = connect();
        PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.setInt(1, budgetID);
        pstmt.executeUpdate();
        pstmt.close();
        conn.close();
    }

    public ArrayList<String> getSelectedBudgetData(int budgetNumber) {
        String sql = "SELECT * FROM Presupuestos WHERE Numero_presupuesto = ?";
        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, budgetNumber);
            ResultSet resultSet = pstmt.executeQuery();
            ArrayList<String> selectedBudgetData = new ArrayList<>();
            while (resultSet.next()) {
                selectedBudgetData.add(resultSet.getString("Nombre_Cliente"));
                selectedBudgetData.add(resultSet.getString("Fecha"));
                selectedBudgetData.add(resultSet.getString("Tipo_Cliente"));
            }
            return selectedBudgetData;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return new ArrayList<>();
    }

    public String getOldClientName(int budgetNumber) {
        String sql = "SELECT Nombre_Cliente FROM Presupuestos WHERE Numero_presupuesto = ?";
        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, budgetNumber);
            ResultSet resultSet = pstmt.executeQuery();
            return resultSet.getString("Nombre_Cliente");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return "";
    }
}
