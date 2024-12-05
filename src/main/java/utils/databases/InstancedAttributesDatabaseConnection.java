package utils.databases;

import javax.swing.*;
import java.sql.*;

public class InstancedAttributesDatabaseConnection extends DatabaseConnection{
	@Override
	protected void createTable(Connection connection) {
		String productSQL = "CREATE TABLE IF NOT EXISTS PRODUCTO_CON_ATRIBUTO (" +
				"ID_PRODUCTO_CON_ATRIBUTO INTEGER PRIMARY KEY AUTOINCREMENT," +
				"ID_PRODUCTO INTEGER NOT NULL," +
				"ID_ATRIBUTO INTEGER NOT NULL," +
				"VALOR VARCHAR(255)," +
				"FOREIGN KEY(ID_PRODUCTO) REFERENCES Productos(ID)," +
				"FOREIGN KEY(ID_ATRIBUTO) REFERENCES Atributos(ID)" +
				")";
		try (Statement stmt = connection.createStatement()) {
			stmt.setQueryTimeout(QUERY_TIMEOUT);
			stmt.execute(productSQL);
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
	}

	public void insertProductAttribute(int productID, int attributeID, String value) {
		String sql = "INSERT INTO PRODUCTO_CON_ATRIBUTO(ID_PRODUCTO, ID_ATRIBUTO, VALOR) VALUES(?, ?, ?)";
		try (Connection conn = connect();
			 PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setInt(1, productID);
			pstmt.setInt(2, attributeID);
			pstmt.setString(3, value);
			pstmt.executeUpdate();
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(null, "Error al instanciar los atributos del producto.", "Error", JOptionPane.ERROR_MESSAGE);
			System.out.println(e.getMessage());
		}
	}

	public String getAttributeValue(int productID, int attributeID) {
		String sql = "SELECT VALOR FROM PRODUCTO_CON_ATRIBUTO WHERE ID_PRODUCTO = ? AND ID_ATRIBUTO = ?";
		String value = "";
		try (Connection conn = connect();
			 PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setInt(1, productID);
			pstmt.setInt(2, attributeID);
			ResultSet rs = pstmt.executeQuery();
			if (rs.next()) {
				value = rs.getString("VALOR");
			}
			rs.close();
			pstmt.close();
			conn.close();
			return value;
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		return null;
	}
}
