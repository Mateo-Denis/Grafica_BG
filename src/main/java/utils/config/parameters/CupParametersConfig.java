package utils.config.parameters;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CupParametersConfig extends ParametersConfig {
	private boolean isCeramic;
	private boolean isPlastic;
	private boolean isSublimated;
}
