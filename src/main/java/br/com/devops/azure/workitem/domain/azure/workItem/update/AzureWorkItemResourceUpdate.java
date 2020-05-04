package br.com.devops.azure.workitem.domain.azure.workItem.update;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import br.com.devops.azure.workitem.domain.azure.workItem.AzureWorkItemResource;
import br.com.devops.azure.workitem.domain.azure.workItem.AzureWorkItemResourceBase;

@JsonIgnoreProperties(ignoreUnknown = true)
public class AzureWorkItemResourceUpdate extends AzureWorkItemResourceBase<Map<String, AzureWorkItemFieldUpdate>> {
	
	private Map<String, AzureWorkItemFieldUpdate> fields;
	private AzureWorkItemResource revision;
	
	public AzureWorkItemResource getRevision() {
		return revision;
	}
	
	@Override
	public Map<String, AzureWorkItemFieldUpdate> getFields() {
		return fields;
	}

	@Override
	public Map<String, Object> getMapFields() {
		return revision.getFields();
	}

}
