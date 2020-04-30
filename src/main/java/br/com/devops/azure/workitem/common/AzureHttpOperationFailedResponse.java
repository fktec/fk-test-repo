package br.com.devops.azure.workitem.common;

import com.fasterxml.jackson.annotation.JsonProperty;

public class AzureHttpOperationFailedResponse {

	@JsonProperty("$id")
	private String id;
	private Object customProperties;
	private String innerException;
	private String message;
	private String typeName;
	private String typeKey;
	private Integer errorCode;
	private Integer eventId;
	
	public String getId() {
		return id;
	}
	public Object getCustomProperties() {
		return customProperties;
	}
	public String getInnerException() {
		return innerException;
	}
	public String getMessage() {
		return message;
	}
	public String getTypeName() {
		return typeName;
	}
	public String getTypeKey() {
		return typeKey;
	}
	public Integer getErrorCode() {
		return errorCode;
	}
	public Integer getEventId() {
		return eventId;
	}
	
}
