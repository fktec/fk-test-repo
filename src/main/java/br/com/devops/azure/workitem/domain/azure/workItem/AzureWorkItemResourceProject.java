package br.com.devops.azure.workitem.domain.azure.workItem;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class AzureWorkItemResourceProject {

	private String baseUrl;

	public String getBaseUrl() {
		return baseUrl;
	}
	
}
