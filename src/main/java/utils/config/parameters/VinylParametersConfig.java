package utils.config.parameters;

import lombok.Getter;
import lombok.Setter;
import utils.config.parameters.ParametersConfig;

@Getter
@Setter
public class VinylParametersConfig extends ParametersConfig {
	private String vinylType;
	private String dimensions;
}
