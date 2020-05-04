package br.com.devops.azure.workitem.domain;

import java.util.List;
import java.util.Map;

public interface WorkItemResource<T> {
	
	public String getAssignedToEmail();

	public T getFields();
	public Map<String, Object> getMapFields();
	public String getOrganization();
	public String getProject();
	public String getWorkItemType();
	public Integer getId();
	public List<? extends WorkItemRelation> getRelations();
	
	default String getSourceReference() {
		return String.format("/%s/%s", getOrganization(), getProject());
	}
}
