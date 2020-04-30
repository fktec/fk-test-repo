package br.com.devops.azure.workitem.domain.azure.wiql;

import java.util.List;

public class AzureWIQLResponse {

	private List<AzureWIQLWorkItemResponse> workItems;

	public List<AzureWIQLWorkItemResponse> getWorkItems() {
		return workItems;
	}

	public Integer getFirstWorkItemId() {
		if (workItems != null && !workItems.isEmpty())
			return Integer.valueOf(workItems.get(0).getId());
		return null;
	}
}
