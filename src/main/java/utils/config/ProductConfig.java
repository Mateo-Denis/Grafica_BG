package utils.config;

import lombok.Getter;
import lombok.Setter;
import utils.config.parameters.ParametersConfig;

@Getter
@Setter
public class ProductConfig {
	private String name;
	private ParametersConfig parameters;
}