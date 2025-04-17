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
			case "Clothes":
				return "Prenda";
			case "CommonServices":
				return "Servicios comunes";
			case "LinearPrinting":
				return "Impresión lineal";
			case "CuttingService":
				return "Servicio de corte";
			case "SquareMeterPrinting":
				return "Impresión en metro cuadrado";
			default:
				return "Unknown";
		}
	}

	public static String getProductCategoryEnglish(String category){
		switch (category) {
			case "Taza":
				return "Cup";
			case "Gorra":
				return "Cap";
			case "Prenda":
				return "Clothes";
			case "Tela":
				return "Cloth";
			case "Bandera":
				return "Flag";
			case "Servicios comunes":
				return "CommonServices";
			case "Servicio de corte":
				return "CuttingService";
			case "Impresión lineal":
				return "LinearPrinting";
			case "Impresión en metro cuadrado":
				return "SquareMeterPrinting";
			default:
				return category;
		}
	}
}
