package utils.databases;

import utils.Budget;
import java.sql.*;
import java.util.ArrayList;

public class BudgetsDatabaseConnection extends DatabaseConnection{


    @Override
    protected void createTable(Connection connection) {
        String budgetSQL =  "CREATE TABLE IF NOT EXISTS Presupuestos (" +
                            "ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                            "Nombre_Cliente TEXT NOT NULL," +
                            "Fecha TEXT NOT NULL," +
                            "Tipo_Cliente TEXT NOT NULL CHECK(Tipo_Cliente IN ('Cliente', 'Particular'))," +
                            "Numero_presupuesto INTEGER NOT NULL," +
                            "Precio_Total DOUBLE NOT NULL" +
                            ")";
        try (Statement stmt = connection.createStatement()) {
            stmt.setQueryTimeout(QUERY_TIMEOUT);
            stmt.execute(budgetSQL);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void insertBudget(String budgetClientName, String budgetDate, String budgetClientType, int budgetNumber, double finalPrice) throws SQLException{
        String sql = "INSERT INTO Presupuestos(Nombre_Cliente, Fecha, Tipo_Cliente, Numero_presupuesto, Precio_Total) VALUES(?, ?, ?, ?, ?)";
        Connection conn = connect();
        PreparedStatement pstmt = conn.prepareStatement(sql);

        pstmt.setString(1, budgetClientName);
        pstmt.setString(2, budgetDate);
        pstmt.setString(3, budgetClientType);
        pstmt.setInt(4, budgetNumber);
        pstmt.setDouble(5, finalPrice);
        pstmt.executeUpdate();

        pstmt.close();
        conn.close();
    }

    public double getBudgetTotalPrice(int budgetID){
        String sql = "SELECT Precio_Total FROM Presupuestos WHERE ID = ?";
        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, budgetID);
            ResultSet resultSet = pstmt.executeQuery();
            return resultSet.getDouble("Precio_Total");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return 0;
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

    public int getMaxBudgetID() {
        String sql = "SELECT MAX(ID) FROM Presupuestos";
        try (Connection conn = connect();
             Statement stmt = conn.createStatement();
             ResultSet resultSet = stmt.executeQuery(sql)) {
            return resultSet.getInt(1);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return 0;
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

    public void saveProducts(int budgetID, ArrayList<Integer> productAmounts, ArrayList<String> productNames, ArrayList<String> productObservations,
                             ArrayList<String> productMeasures, ArrayList<Double> productPrices) throws SQLException {
        int iterableIndex = 0;

        String sql = "INSERT INTO PRESUPUESTO_PRODUCTOS(ID_PRESUPUESTO, NOMBRE_PRODUCTO, CANTIDAD, OBSERVACIONES, MEDIDAS, PRECIO) VALUES(?, ?, ?, ?, ?, ?)";
        Connection conn = connect();
        PreparedStatement pstmt = conn.prepareStatement(sql);

        for (String prodName : productNames) {
            pstmt.setInt(1, budgetID);
            pstmt.setString(2, prodName);
            pstmt.setInt(3, productAmounts.get(iterableIndex));
            pstmt.setString(4, productObservations.get(iterableIndex));
            pstmt.setString(5, productMeasures.get(iterableIndex));
            pstmt.setDouble(6, productPrices.get(iterableIndex));

            pstmt.executeUpdate();
            iterableIndex++;
        }
        pstmt.close();
        conn.close();
    }

    public ArrayList<Integer> getSavedProductAmounts(String budgetName, int budgetNumber) throws SQLException {
        ArrayList<Integer> productAmounts = new ArrayList<>();
        String sql = "SELECT CANTIDAD FROM PRESUPUESTO_PRODUCTOS WHERE ID_PRESUPUESTO = ?";
        Connection conn = connect();
        PreparedStatement pstmt = conn.prepareStatement(sql);
        int budgetID = getBudgetID(budgetName, budgetNumber);
        pstmt.setInt(1, budgetID);
        ResultSet resultSet = pstmt.executeQuery();
        while (resultSet.next()) {
            int productAmount = resultSet.getInt("CANTIDAD");
            productAmounts.add(productAmount);
        }
        pstmt.close();
        conn.close();
        return productAmounts;
    }

    public ArrayList<String> getSavedProductNames(String budgetName, int budgetNumber) throws SQLException {
        ArrayList<String> productNames = new ArrayList<>();
        String sql = "SELECT NOMBRE_PRODUCTO FROM PRESUPUESTO_PRODUCTOS WHERE ID_PRESUPUESTO = ?";
        Connection conn = connect();
        PreparedStatement pstmt = conn.prepareStatement(sql);
        int budgetID = getBudgetID(budgetName, budgetNumber);
        pstmt.setInt(1, budgetID);
        ResultSet resultSet = pstmt.executeQuery();
        while (resultSet.next()) {
            String productName = resultSet.getString("NOMBRE_PRODUCTO");
            productNames.add(productName);
        }
        pstmt.close();
        conn.close();
        return productNames;
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

    public ArrayList<Double> getProductPrices(String budgetName, int budgetNumber) throws SQLException {
        ArrayList<Double> prices = new ArrayList<>();
        String sql = "SELECT PRECIO FROM PRESUPUESTO_PRODUCTOS WHERE ID_PRESUPUESTO = ?";
        Connection conn = connect();
        PreparedStatement pstmt = conn.prepareStatement(sql);
        int budgetID = getBudgetID(budgetName, budgetNumber);
        pstmt.setInt(1, budgetID);
        ResultSet resultSet = pstmt.executeQuery();
        while (resultSet.next()) {
            prices.add(resultSet.getDouble("PRECIO"));
        }
        pstmt.close();
        conn.close();
        return prices;
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

    public void deleteBudgetProducts(int budgetID) throws SQLException {
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
                selectedBudgetData.add(String.valueOf(resultSet.getInt("ID")));
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

    public int getBudgetNumber(int budgetID) {
        String sql = "SELECT Numero_presupuesto FROM Presupuestos WHERE ID = ?";
        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, budgetID);
            ResultSet resultSet = pstmt.executeQuery();
            return resultSet.getInt("Numero_presupuesto");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return 0;
    }

    public int getNextBudgetNumber() {
        int bnumber = 1;  // Por defecto será 1 si no hay presupuestos en la tabla.
        String sql = "SELECT MAX(Numero_presupuesto) FROM Presupuestos";

        try (Connection conn = connect();
             Statement stmt = conn.createStatement();
             ResultSet resultSet = stmt.executeQuery(sql)) {
            if (resultSet.next()) {
                bnumber = resultSet.getInt(1) + 1;  // Accediendo a la columna por índice.
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return bnumber;
    }
}
