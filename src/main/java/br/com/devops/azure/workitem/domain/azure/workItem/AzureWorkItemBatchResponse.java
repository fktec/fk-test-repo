package br.com.devops.azure.workitem.domain.azure.workItem;

import java.util.List;

public class AzureWorkItemBatchResponse {

	private Integer count;
	private List<AzureWorkItemResource> value;
	
	public Integer getCount() {
		return count;
	}
	public List<AzureWorkItemResource> getValue() {
		return value;
	}
	
}
