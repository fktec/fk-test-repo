package br.com.devops.azure.workitem.domain.azure.workItem;

import java.util.Arrays;

import br.com.devops.azure.workitem.domain.WorkItemEventType;
import br.com.devops.azure.workitem.domain.WorkItemEventTypeImpl;

public enum AzureWorkItemEventType implements WorkItemEventTypeImpl	 {

	CREATE(WorkItemEventType.CREATE, "workitem.created", "add"),
	UPDATE(WorkItemEventType.UPDATE, "workitem.updated");;

	private String providerWorkItemEventType;
	private WorkItemEventType eventType;
	private String operationName;
	
	private AzureWorkItemEventType(WorkItemEventType eventType, String providerWorkItemEventType) {
		this.eventType = eventType;
		this.providerWorkItemEventType = providerWorkItemEventType;
	}
	
	private AzureWorkItemEventType(WorkItemEventType eventType, String providerWorkItemEventType, String operationName) {
		this(eventType, providerWorkItemEventType);
		this.operationName = operationName;
	}
	
	public static WorkItemEventTypeImpl getWorkItemEventTypeByName(String name) { 
		for(WorkItemEventTypeImpl workItemEventType : Arrays.asList(values()))
			if (workItemEventType.getWorkItemEventTypeValue().equals(name))
				return workItemEventType;
		return null;
	}

	@Override
	public String getWorkItemEventTypeValue() {
		return providerWorkItemEventType;
	}

	@Override
	public WorkItemEventType getWorkItemEventType() {
		return eventType;
	}
	
	@Override
	public String getOperationName() {
		return operationName;
	}
}
