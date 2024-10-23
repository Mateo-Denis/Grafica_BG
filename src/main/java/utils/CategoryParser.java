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
				return "Impresion";
			case "Clothes":
				return "Prenda";
			case "Sweater":
				return "Buzo";
			case "Vinyl":
				return "Vinilo";
			case "Canvas":
				return "Lona";
			case "CommonServices":
				return "Servicios Comunes";
			case "LinearPrinting":
				return "Impresion Lineal";
			case "CuttingService":
				return "Servicio de Corte";
			case "SquareMeterPrinting":
				return "Impresion por Metro Cuadrado";
			default:
				return "Unknown";
		}
	}
}
