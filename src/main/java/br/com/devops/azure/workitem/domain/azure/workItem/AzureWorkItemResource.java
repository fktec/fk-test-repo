package br.com.devops.azure.workitem.domain.azure.workItem;

import java.util.Map;

public class AzureWorkItemResource extends AzureWorkItemResourceBase<Map<String, Object>>{
	
	private Map<String, Object> fields;
	
	@Override
	public Map<String, Object> getFields() {
		return fields;
	}

	@Override
	public Map<String, Object> getMapFields() {
		return fields;
	}
	
}
