package br.com.devops.azure.workitem.bean.aggregation;

import java.util.List;

import org.apache.camel.Exchange;
import org.apache.camel.processor.aggregate.AggregationStrategy;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import br.com.devops.azure.workitem.domain.WorkItemRelation;
import br.com.devops.azure.workitem.domain.WorkItemResource;
import br.com.devops.azure.workitem.domain.azure.workItem.AzureWorkItemRelation;
import br.com.devops.azure.workitem.domain.azure.workItem.AzureWorkItemResourceBase;
import br.com.devops.azure.workitem.util.ConstantsUtil;

@Component
public class AzureWorkItemRelationAggregationStrategy implements AggregationStrategy {
	
	@Value("${azure.host}")
	private String azureHost;
	
	@Value("${azure.workitem.path}")
	private String azureWorkItemPath;

	@SuppressWarnings("unchecked")
	@Override
	public Exchange aggregate(Exchange oldExchange, Exchange newExchange) 
	{
		Integer workItemRelationId = extractWorkItemRelationId(newExchange);
		
		if (workItemRelationId == null)
			return oldExchange;
		
		List<AzureWorkItemRelation> lastWorkItemRelations = (List<AzureWorkItemRelation>) oldExchange.getProperty(ConstantsUtil.WorkItem.WORKITEM_RELATIONS);
		List<WorkItemRelation> currentWorkItemRelations = (List<WorkItemRelation>) oldExchange.getProperty(ConstantsUtil.WorkItem.WORKITEM_RELATIONS_AGGREGATION);
		WorkItemResource<?> sourceWorkItem = oldExchange.getIn().getBody(WorkItemResource.class);
		
		for (AzureWorkItemRelation lastRelation : lastWorkItemRelations) {
			if (lastRelation.getWorkItemId() == sourceWorkItem.getId()) {
				lastRelation.updateUrl(updateRelationUrlToTarget(newExchange, workItemRelationId));
				currentWorkItemRelations.add(lastRelation);
			}
		}
		return oldExchange;
	}
	
	private Integer extractWorkItemRelationId(Exchange exchange) 
	{
		Boolean existsWorkItem = exchange.getProperty(ConstantsUtil.WorkItem.EXISTS_WORKITEM, Boolean.class);
		if (existsWorkItem != null && existsWorkItem)
			return exchange.getIn().getBody(Integer.class);
		
		AzureWorkItemResourceBase<?> targetWorkItem = exchange.getIn().getBody(AzureWorkItemResourceBase.class);
		if (targetWorkItem != null)
			return targetWorkItem.getId();
		
		return null;
	}
	
	private String updateRelationUrlToTarget(Exchange exchange, Integer workItemRelationId) {
		return new StringBuilder()
				.append("https://").append(azureHost).append("/")
				.append(exchange.getProperty(ConstantsUtil.DevOps.HTTP_PATH_ORGANIZATION)).append("/").append(exchange.getProperty(ConstantsUtil.DevOps.HTTP_PATH_PROJECT)).append("/")
				.append(azureWorkItemPath).append("/").append(workItemRelationId)
				.toString();
	}
	
}
