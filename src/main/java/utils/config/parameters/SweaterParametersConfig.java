package utils.config.parameters;

import lombok.Getter;
import lombok.Setter;
import utils.config.parameters.ParametersConfig;

@Getter
@Setter
public class SweaterParametersConfig extends ParametersConfig {
	private String size;
	private String material;
	private boolean hasHood;
	private boolean hasRoundNeck;
}
