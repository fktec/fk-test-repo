package br.com.devops.azure.workitem.domain.azure.workItem;

import br.com.devops.azure.workitem.domain.WorkItemEventTypeImpl;
import br.com.devops.azure.workitem.domain.WorkItemNotify;
import br.com.devops.azure.workitem.domain.WorkItemResource;

public abstract class AzureWorkItemNotifyBase<R extends WorkItemResource<?>> implements WorkItemNotify<R>{

	public static final String FIELD_PROJECT_NAME = "System.TeamProject";
	public static final String FIELD_WORK_ITEM_TYPE = "System.WorkItemType";
	
	protected String eventType;
	
	@Override
	public WorkItemEventTypeImpl getEventType() {
		return AzureWorkItemEventType.getWorkItemEventTypeByName(eventType);
	}

}
