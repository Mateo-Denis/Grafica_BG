package utils;

public class CategoryParser {
	public static String parseCategory(String category) {
		switch (category) {
			case "Cap":
				return "Gorra";
			case "Cloth":
				return "Tela";
			case "Cup":
				return "Taza";
			case "Flag":
				return "Bandera";
			case "Jacket":
				return "Campera";
			case "Printing":
				return "Impresión";
			case "Shirt":
				return "Remera";
			case "Sweater":
				return "Buzo";
			case "Vinyl":
				return "Vinilo";
			case "Canvas":
				return "Lona";
			default:
				return "Unknown";
		}
	}
}
