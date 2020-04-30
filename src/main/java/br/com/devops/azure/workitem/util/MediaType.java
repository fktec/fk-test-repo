package br.com.devops.azure.workitem.util;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public class MediaType {
	
	public static final String APPLICATION_JSON = "application/json";
	public static final String APPLICATION_JSON_UTF8 = getMediaTypeWithCharset(APPLICATION_JSON, StandardCharsets.UTF_8);
	public static final String APPLICATION_JSON_ISO_8859_1 = getMediaTypeWithCharset(APPLICATION_JSON, StandardCharsets.ISO_8859_1);
	public static final String APPLICATION_JSON_PATH_JSON = APPLICATION_JSON + "-patch+json";
	
	public static final String APPLICATION_XML = "application/xml";
	public static final String APPLICATION_XML_UTF8 = getMediaTypeWithCharset(APPLICATION_XML, StandardCharsets.UTF_8);
	public static final String APPLICATION_XML_ISO_8859_1 = getMediaTypeWithCharset(APPLICATION_XML, StandardCharsets.ISO_8859_1);

	public static final String TEXT_HTML = "text/html";
	
	private static String getMediaTypeWithCharset(String type, Charset charset) {
		return String.format("%s; charset=%s", type, charset.displayName().toLowerCase());
	}
}
