package br.com.devops.azure.workitem.domain;

import java.util.List;

public interface WorkItemResource<T> {

	public T getFields();
	public String getOrganization();
	public String getProject();
	public String getWorkItemType();
	public Integer getId();
	public List<? extends WorkItemRelation> getRelations();
	public String getSyncWorkItemFieldName();
	
	default String getSourceReference() {
		return String.format("/%s/%s", getOrganization(), getProject());
	}
}
