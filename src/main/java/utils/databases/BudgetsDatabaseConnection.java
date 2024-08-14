package utils.databases;

import utils.Budget;
import java.sql.*;
import java.util.ArrayList;
import utils.IsNumeric;

public class BudgetsDatabaseConnection extends DatabaseConnection{

    @Override
    protected void createTable(Connection connection) {
        String budgetSQL =  "CREATE TABLE IF NOT EXISTS Presupuestos (" +
                            "ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                            "NombreCliente TEXT NOT NULL," +
                            "Fecha TEXT NOT NULL," +
                            "TipoCliente TEXT NOT NULL CHECK(TipoCliente IN ('Cliente', 'Particular'))," +
                            "NumeroPresupuesto INTEGER NOT NULL" +
                            ")";
        try (Statement stmt = connection.createStatement()) {
            stmt.setQueryTimeout(QUERY_TIMEOUT);
            stmt.execute(budgetSQL);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void insertBudget(String budgetClientName, String budgetDate, String budgetClientType, int budgetNumber) throws SQLException{
        String sql = "INSERT INTO Presupuestos(Nombre, Fecha, TipoCliente, NumeroPresupuesto) VALUES(?, ?, ?, ?)";
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

/*    public ArrayList<Budget> getBudgets(String budgetSearch) throws SQLException {
        String sql = "SELECT * FROM Presupuestos WHERE ('Nombre Cliente' LIKE ?)";
        Connection conn = connect();
        PreparedStatement pstmt = conn.prepareStatement(sql);

        pstmt.setString(1, "%" + budgetSearch + "%");
        ResultSet resultSet = pstmt.executeQuery();
        ArrayList<Budget> budgets = new ArrayList<>();

        while (resultSet.next()) {
            Budget budget = new Budget(
                    resultSet.getString("Nombre Cliente"),
                    resultSet.getString("Fecha"),
                    resultSet.getString("Tipo Cliente"),
                    resultSet.getInt("Numero de presupuesto")
            );
            budgets.add(budget);
        }

        pstmt.close();
        conn.close();
        return budgets;
    }*/


    public ArrayList<Budget> getBudgets(String budgetSearch) throws SQLException {
        String sql;
        Connection conn = connect();
        PreparedStatement pstmt;

        // Verifica si el budgetSearch es numérico
        if (budgetSearch.matches("\\d+")) {
            // Si es numérico, buscar por número de presupuesto
            sql = "SELECT * FROM Presupuestos WHERE NumeroPresupuesto = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, Integer.parseInt(budgetSearch));
        } else {
            // Si no es numérico, buscar por nombre
            sql = "SELECT * FROM Presupuestos WHERE Nombre LIKE ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, "%" + budgetSearch + "%");
        }

        ResultSet resultSet = pstmt.executeQuery();
        ArrayList<Budget> budgets = new ArrayList<>();

        while (resultSet.next()) {
            Budget budget = new Budget(
                    resultSet.getString("Nombre"),
                    resultSet.getString("Fecha"),
                    resultSet.getString("TipoCliente"),
                    resultSet.getInt("NumeroPresupuesto")
            );
            budgets.add(budget);
        }

        pstmt.close();
        conn.close();
        return budgets;
    }


}
