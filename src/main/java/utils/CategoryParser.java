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
				return "Servicio de Corte";
			case "SquareMeterPrinting":
				return "Impresión por Metro Cuadrado";
			default:
				return "Unknown";
		}
	}
}
