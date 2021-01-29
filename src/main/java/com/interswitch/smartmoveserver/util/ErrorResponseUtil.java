package com.interswitch.smartmoveserver.util;

import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class ErrorResponseUtil {

	public Map<String, String> getErrorFieldMap(BindingResult result) {
		Map<String, String> errors = new HashMap<>();
		result.getAllErrors().forEach((error) -> {
			String fieldName = ((FieldError) error).getField();
			String errorMessage = error.getDefaultMessage();

			errors.put(fieldName, errorMessage);
		});
		return errors;
	}

	public Map<String, List<String>> getErrorMessagesMap(BindingResult result) {
		Map<String, List<String>> errorMap = new HashMap<>();
		List<String> errorMessages = new ArrayList<>();

		result.getAllErrors().forEach((error) -> {
			String fieldName = ((FieldError) error).getField();
			String errorMessage = error.getDefaultMessage();
			errorMessages.add(fieldName.concat(":" + errorMessage));
		});

		errorMap.put("errors", errorMessages);
		return errorMap;
	}

	public static String getErrorMessages(BindingResult result) {
		return result.getAllErrors().stream().map(error -> error.getDefaultMessage()).collect(Collectors.joining(","));
	}
}
