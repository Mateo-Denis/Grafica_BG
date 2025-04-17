package utils.config.parameters;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.Getter;
import lombok.Setter;
import utils.config.parameters.CanvasParametersConfig;
import utils.config.parameters.CupParametersConfig;

@Getter
@Setter
@JsonTypeInfo(
		use = JsonTypeInfo.Id.NAME,
		include = JsonTypeInfo.As.PROPERTY,
		property = "type"
)
@JsonSubTypes({
		@JsonSubTypes.Type(value = CanvasParametersConfig.class, name = "canvas"),
		@JsonSubTypes.Type(value = CapParametersConfig.class, name = "cap"),
		@JsonSubTypes.Type(value = VinylParametersConfig.class, name = "vinyl"),
		@JsonSubTypes.Type(value = PrintingParametersConfig.class, name = "printing"),
		@JsonSubTypes.Type(value = CupParametersConfig.class, name = "cup"),
		@JsonSubTypes.Type(value = ShirtParametersConfig.class, name = "shirt"),
		@JsonSubTypes.Type(value = SweaterParametersConfig.class, name = "sweater"),
		@JsonSubTypes.Type(value = FlagParametersConfig.class, name = "flag"),
		@JsonSubTypes.Type(value = ClothParametersConfig.class, name = "cloth"),
		@JsonSubTypes.Type(value = JacketParametersConfig.class, name = "jacket")
})
public abstract class ParametersConfig {

}