package br.com.devops.azure.workitem.bean;

import java.util.List;
import java.util.stream.Collectors;

import org.apache.camel.Body;
import org.apache.camel.Exchange;
import org.springframework.stereotype.Service;

import br.com.devops.azure.workitem.domain.WorkItemRelation;
import br.com.devops.azure.workitem.domain.WorkItemRelationType;
import br.com.devops.azure.workitem.domain.azure.workItem.AzureWorkItemBatch;
import br.com.devops.azure.workitem.domain.azure.workItem.AzureWorkItemResourceBase;
import br.com.devops.azure.workitem.util.ConstantsUtil;

@Service
public class AzureWorkItemBean {
	
	public boolean workItemRelationTypeIsAllowed(@Body AzureWorkItemResourceBase<?> workItemResource, Exchange exchange) 
	{
		@SuppressWarnings("unchecked")
		List<WorkItemRelation> lastRelations = (List<WorkItemRelation>) exchange.getProperty(ConstantsUtil.WorkItem.WORKITEM_RELATIONS);
		boolean isRelationAllowed = true;
		
		removeRelationsInfo(exchange);
		
		if ((lastRelations != null && !lastRelations.isEmpty())) 
		{
			WorkItemRelationType lastRelationType = exchange.getProperty(ConstantsUtil.WorkItem.CURRENT_WORKITEM_RELATION_TYPE, WorkItemRelationType.class);
			
			for (WorkItemRelation workItemRelation : lastRelations) {
				if (workItemRelation.getWorkItemId() == workItemResource.getId()) {
					if (lastRelationType == null) {
						exchange.setProperty(ConstantsUtil.WorkItem.CURRENT_WORKITEM_RELATION_TYPE, workItemRelation.getRelationType());
						break;
					} 
					else {
						WorkItemRelationType relationType = workItemRelation.getRelationType();
						if (relationType == null)
							exchange.setProperty(ConstantsUtil.WorkItem.CURRENT_WORKITEM_RELATION_TYPE, WorkItemRelationType.CHILD);
						else
							isRelationAllowed = workItemRelation.getRelationType().equals(lastRelationType);
						break;
					}
				}
			}
		}
		return isRelationAllowed;
	}

	public void extractWorkItemId(Exchange exchange) 
	{
		if (exchange.getIn().getBody(AzureWorkItemResourceBase.class) != null) {
			AzureWorkItemResourceBase<?> workItemResource = exchange.getIn().getBody(AzureWorkItemResourceBase.class);			
			exchange.setProperty(ConstantsUtil.WorkItem.WORKITEM_ID, workItemResource.getId());
		}
	}
	
	public void extractWorkItemIdsByRelations(Exchange exchange) 
	{
		@SuppressWarnings("unchecked")
		List<WorkItemRelation> sourceWorkItemRelations = (List<WorkItemRelation>) exchange.getProperty(ConstantsUtil.WorkItem.WORKITEM_RELATIONS);
		
		if (sourceWorkItemRelations != null && !sourceWorkItemRelations.isEmpty()) 
		{
			exchange.getIn().setBody(new AzureWorkItemBatch(
					sourceWorkItemRelations.stream()
					.map(v -> v.getWorkItemId())
					.collect(Collectors.toSet())));
		} else
			exchange.getIn().setBody(null);
	}
	
	private void removeRelationsInfo(Exchange exchange) {
		exchange.removeProperty(ConstantsUtil.WorkItem.WORKITEM_RELATIONS);
		exchange.removeProperty(ConstantsUtil.WorkItem.WORKITEM_IDS);
	}
	
}
