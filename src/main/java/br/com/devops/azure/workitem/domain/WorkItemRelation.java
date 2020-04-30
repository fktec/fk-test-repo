package br.com.devops.azure.workitem.domain;

public interface WorkItemRelation {

	Integer getWorkItemId();
	String getFieldName(); 
	WorkItemRelationType getRelationType();
}
