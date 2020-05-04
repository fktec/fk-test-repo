package br.com.devops.azure.workitem.domain.azure.workItem;

import br.com.devops.azure.workitem.domain.WorkItemEventTypeImpl;
import br.com.devops.azure.workitem.domain.WorkItemNotify;
import br.com.devops.azure.workitem.domain.WorkItemResource;

public abstract class AzureWorkItemNotifyBase<R extends WorkItemResource<?>> implements WorkItemNotify<R> {

	protected String eventType;
	
	@Override
	public WorkItemEventTypeImpl getEventType() {
		return AzureWorkItemEventType.getWorkItemEventTypeByName(eventType);
	}

}
