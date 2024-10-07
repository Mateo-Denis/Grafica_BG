package utils.databases;

public enum SettingsTableNames {
	GENERAL("General"),
	BAJADA_PLANCHA("Bajada_Plancha"),
	TELAS("Telas"),
	CORTE("Corte"),
	PRENDAS("Prendas"),
	SERVICIOS("Servicios"),
	IMPRESIONES("Impresiones"),
	VINILOS("Vinilos"),
	LONAS("Lonas");

	private final String name;

	SettingsTableNames(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}
}
