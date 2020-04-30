package br.com.devops.azure.workitem.domain.azure.workItem.update;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class AzureWorkItemFieldUpdate {
	
	private String oldValue;
	private String newValue;
	
	public String getOldValue() {
		return oldValue;
	}
	public String getNewValue() {
		return newValue;
	}
}
