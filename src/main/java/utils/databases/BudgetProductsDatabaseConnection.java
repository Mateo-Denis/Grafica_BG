package utils.databases;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class BudgetProductsDatabaseConnection  extends DatabaseConnection{
    @Override
    protected void createTable(Connection connection) {
        String budgetProductsSQL = "CREATE TABLE IF NOT EXISTS PRESUPUESTO_PRODUCTOS(" +
                "ID_PRESUPUESTO_PRODUCTO INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
                "ID_PRESUPUESTO INT NOT NULL," +
                "ID_PRODUCTO INT NOT NULL," +
                "CANTIDAD INT NOT NULL, OBSERVACIONES TEXT NOT NULL, MEDIDAS TEXT NOT NULL, PRECIO double not null," +
                "FOREIGN KEY (ID_PRESUPUESTO) REFERENCES PRESUPUESTOS(ID)," +
                "FOREIGN KEY (ID_PRODUCTO) REFERENCES PRODUCTOS(ID)" +
                ");";
        try (Statement stmt = connection.createStatement()) {
            stmt.setQueryTimeout(QUERY_TIMEOUT);
            stmt.execute(budgetProductsSQL);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}
