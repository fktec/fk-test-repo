package br.com.devops.azure.workitem.domain.azure.workItem;

import java.util.List;

import br.com.devops.azure.workitem.domain.WorkItemResource;

public abstract class AzureWorkItemResourceBase<T> implements WorkItemResource<T> {
	
	private Integer id;
	private List<AzureWorkItemRelation> relations;
	private String url;
	
	@Override
	public Integer getId() {
		return id;
	}
	
	@Override
	public List<AzureWorkItemRelation> getRelations() {
		return relations;
	}
	
	@Override
	public String getOrganization() {
		return url != null ? url.split("/")[3] : null;
	}
	
	public String getUrl() {
		return url;
	}
}
