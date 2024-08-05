package models;

public class CategoryModel {



	public void getSubCategories(String category) {
		String sql = "SELECT * FROM Productos WHERE (Nombre LIKE ?)";
	}
}
