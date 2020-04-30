package br.com.devops.azure.workitem.util;

import java.io.IOException;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonFormatterUtil {

	private static final String REGEX_JSON_FORMAT = "\\{[^\\+]+\\}"	;
	private static final String REGEX_ARRAY_JSON_FORMAT = "\\[(\\n+|\\s+|)\\{[^\\+]+\\}(\\n+|\\s+|)\\]";

	public static String objectToJson(Object obj)
	{
		try {
			final ObjectMapper mapper = new ObjectMapper();
			return mapper.writeValueAsString(obj);
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static <T> T jsonToObject(String json, Class<T> type)
	{
		try {
			final ObjectMapper mapper = new ObjectMapper();
			return mapper.readValue(json, type);
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static <T> String stringToJson(String string, Class<T> type)
	{
		T obj = jsonToObject(string, type);
		return objectToJson(obj);
	}
	
	public static boolean isJSONFormat(String json) 
	{
		if (json.trim().isEmpty())
			return false;
		return json.replaceAll(REGEX_JSON_FORMAT, "").trim().isEmpty();
	}
	
	public static boolean isArrayJSONFormat(String json) 
	{
		if (json.trim().isEmpty())
			return false;
		return json.replaceAll(REGEX_ARRAY_JSON_FORMAT, "").trim().isEmpty();
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static Object getJsonPropertyValue(Map<String, Object> jsonProperties, String propertyName)
	{
		String[] properties = propertyName.split("\\.");
		for (int i = 0; i < properties.length; i++) 
		{
			for (Map.Entry<String, Object> jsonProperty : jsonProperties.entrySet()) 
			{
				if (jsonProperty.getKey().equals(properties[i])) {
					if (properties.length > 1 && jsonProperty.getValue() instanceof Map) {
						return getJsonPropertyValue((Map) jsonProperty.getValue(), propertyName.replace(properties[i] + ".", ""));
					} else {
						return jsonProperty.getValue();
					}
				}
			}
		}
		return null;
	}
}
