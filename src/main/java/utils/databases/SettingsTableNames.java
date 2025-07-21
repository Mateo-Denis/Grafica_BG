package utils.databases;

import lombok.Getter;

@Getter
public enum SettingsTableNames {
	GENERAL("General"),
	TELAS("Telas"),
	SERVICIOS("Servicios"),
	MATERIALES("Materiales");


	private final String name;

	SettingsTableNames(String name) {
		this.name = name;
	}

}
