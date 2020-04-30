package br.com.devops.azure.workitem.domain.azure.workItem.update;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import br.com.devops.azure.workitem.domain.azure.workItem.AzureWorkItemNotifyBase;

@JsonIgnoreProperties(ignoreUnknown = true)
public class AzureWorkItemNotifyUpdate extends AzureWorkItemNotifyBase<AzureWorkItemResourceUpdate> {

	private AzureWorkItemResourceUpdate resource;
	
	@Override
	public AzureWorkItemResourceUpdate getResource() {
		return resource;
	}
}
