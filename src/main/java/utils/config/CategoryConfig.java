package utils.config;

import lombok.Getter;
import lombok.Setter;

import java.util.List;
@Getter
@Setter
public class CategoryConfig {
	private String category;
	private List<ProductConfig> products;
}
