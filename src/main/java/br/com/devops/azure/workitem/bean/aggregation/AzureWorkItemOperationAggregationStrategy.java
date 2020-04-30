package br.com.devops.azure.workitem.bean.aggregation;

import java.util.List;
import java.util.stream.Collectors;

import org.apache.camel.Exchange;
import org.apache.camel.processor.aggregate.AggregationStrategy;
import org.springframework.stereotype.Component;

import br.com.devops.azure.workitem.domain.WorkItemRelation;
import br.com.devops.azure.workitem.domain.WorkItemRelationType;
import br.com.devops.azure.workitem.domain.azure.workItem.AzureWorkItemOperation;
import br.com.devops.azure.workitem.util.ConstantsUtil;

@Component
public class AzureWorkItemOperationAggregationStrategy implements AggregationStrategy{

	@SuppressWarnings("unchecked")
	@Override
	public Exchange aggregate(Exchange oldExchange, Exchange newExchange) 
	{
		oldExchange.removeProperty(ConstantsUtil.WorkItem.WORKITEM_RELATIONS_OPERATIONS);
		
		List<WorkItemRelation> workItemRelationsAggregation = (List<WorkItemRelation>) newExchange.getProperty(ConstantsUtil.WorkItem.WORKITEM_RELATIONS_AGGREGATION);
		if (workItemRelationsAggregation != null && !workItemRelationsAggregation.isEmpty())
		{
			List<AzureWorkItemOperation> relationOperations = extractRelationOperationsAllowed(workItemRelationsAggregation, oldExchange.getProperty(ConstantsUtil.WorkItem.CURRENT_WORKITEM_RELATION_TYPE, WorkItemRelationType.class));
			
			if (!relationOperations.isEmpty())
				oldExchange.setProperty(ConstantsUtil.WorkItem.WORKITEM_RELATIONS_OPERATIONS, relationOperations);
		} 
		else 
			oldExchange.removeProperty(ConstantsUtil.WorkItem.WORKITEM_RELATIONS_OPERATIONS);
	
		return oldExchange;
	}

	private List<AzureWorkItemOperation> extractRelationOperationsAllowed(List<WorkItemRelation> workItemRelationsAggregation, WorkItemRelationType relationTypeAllowed) {
		return workItemRelationsAggregation
			.stream()
			.filter(r -> (r.getRelationType().equals(relationTypeAllowed) || relationTypeAllowed == null))
			.map(r -> {
				return AzureWorkItemOperation.builder()
					.addOperation("add")
					.addRelation("-")
					.addValue(r);
			})
			.collect(Collectors.toList());
	}
}
