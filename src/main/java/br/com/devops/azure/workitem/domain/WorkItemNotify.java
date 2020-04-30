package br.com.devops.azure.workitem.domain;

import java.util.List;


public interface WorkItemNotify<R extends WorkItemResource<?>> {

	WorkItemEventTypeImpl getEventType();
	R getResource();
	
	default String getSourceProject() {
		if (getResource() == null)
			return null;
		return getResource().getProject();
	}
	
	default String getSourceOrganization() {
		if (getResource() == null)
			return null;
		return getResource().getOrganization();
	}
	
	default WorkItemEventType getWorkItemEventType() {
		if (getEventType() == null)
			return null;
		return getEventType().getWorkItemEventType();
	} 
	
	default Integer getWorkItemId() {
		if (getResource() == null)
			return null;
		return getResource().getId();
	}
	
	default List<? extends WorkItemRelation> getWorkItemRelantions() {
		if (getResource() == null)
			return null;
		return getResource().getRelations();
	}
}
