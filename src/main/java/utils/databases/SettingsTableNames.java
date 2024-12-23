package utils.databases;

public enum SettingsTableNames {
	GENERAL("General"),
	BAJADA_PLANCHA("Bajada_Plancha"),
	TELAS("Telas"),
	SERVICIOS("Servicios"),
	IMPRESIONES("Impresiones"),
	MATERIALES("Materiales"),
	GANANCIAS("Ganancias");


	private final String name;

	SettingsTableNames(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}
}
