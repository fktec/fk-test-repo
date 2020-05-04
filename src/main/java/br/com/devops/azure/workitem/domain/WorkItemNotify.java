package br.com.devops.azure.workitem.domain;

public interface WorkItemNotify<R extends WorkItemResource<?>> {

	WorkItemEventTypeImpl getEventType();
	R getResource();
}
