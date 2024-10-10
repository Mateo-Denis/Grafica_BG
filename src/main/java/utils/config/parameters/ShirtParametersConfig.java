package utils.config.parameters;

import lombok.Getter;
import lombok.Setter;
import utils.config.parameters.ParametersConfig;

@Getter
@Setter
public class ShirtParametersConfig extends ParametersConfig {
	private String shirtType;
	private String size;
	private String material;
	private String sleeveType;

}
