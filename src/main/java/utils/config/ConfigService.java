package utils.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import utils.config.parameters.ParametersConfig;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ConfigService {

	public static ParametersConfig loadParametersFromJson(String filePath) throws IOException {
		ObjectMapper mapper = new ObjectMapper();
		// You need to handle different subclasses of Parameters here
		return mapper.readValue(new File(filePath), ParametersConfig.class);
	}
	public static void saveParametersToJson(ParametersConfig parameters, String filePath) throws IOException {
		ObjectMapper mapper = new ObjectMapper();
		File file = new File(filePath);

		List<ParametersConfig> parametersList;

		// Check if the file exists and is not empty
		if (file.exists() && file.length() != 0) {
			// Read the existing parameters from the file
			parametersList = Arrays.asList(mapper.readValue(file, ParametersConfig[].class));
			parametersList = new ArrayList<>(parametersList); // Convert to ArrayList to modify
		} else {
			// If file doesn't exist or is empty, start a new list
			parametersList = new ArrayList<>();
		}

		// Add the new parameters to the list
		parametersList.add(parameters);

		// Write the updated list back to the file
		mapper.writeValue(file, parametersList);
	}
}
