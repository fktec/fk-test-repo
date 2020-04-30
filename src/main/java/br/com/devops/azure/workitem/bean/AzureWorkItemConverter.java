package br.com.devops.azure.workitem.bean;

import java.util.List;
import java.util.stream.Collectors;

import org.apache.camel.Body;
import org.apache.camel.Exchange;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import br.com.devops.azure.workitem.domain.WorkItemNotify;
import br.com.devops.azure.workitem.domain.WorkItemResource;
import br.com.devops.azure.workitem.domain.azure.workItem.AzureWorkItemOperation;
import br.com.devops.azure.workitem.domain.azure.workItem.AzureWorkItemResource;
import br.com.devops.azure.workitem.domain.azure.workItem.AzureWorkItemResourceBase;
import br.com.devops.azure.workitem.domain.azure.workItem.update.AzureWorkItemResourceUpdate;
import br.com.devops.azure.workitem.util.ConstantsUtil;

@Service
public class AzureWorkItemConverter {
	
	@Value("#{'${azure.operations.field.not-allowed}'.split(',')}")
	private String[] fieldsNotAllowed;   
	
	@Value("#{'${azure.operations.field.update-not-allowed}'.split(',')}")
	private String[] fieldsUpdateNotAllowed;
	
	@Value("${azure.host}")
	private String azureHost;
	
	public List<AzureWorkItemOperation> createWorkItemOperations(@Body AzureWorkItemResource workItemResource, Exchange exchange) 
	{
		if (workItemResource != null) 
		{
			List<AzureWorkItemOperation> operations = mergeOperationsWithRelationOperations(exchange, extractCreateFieldOperations(workItemResource));
			
			addCurrentWorkItemType(workItemResource, exchange);
			addSyncWorkItemInfo(workItemResource, operations);
			return operations;
		}
		return null;
	}

	public AzureWorkItemResourceBase<?> extractWorkItemRelations(@Body AzureWorkItemResourceBase<?> workItemResource, Exchange exchange) 
	{
		if (workItemResource != null) {
			if (workItemResource.getRelations() != null)
				exchange.setProperty(ConstantsUtil.WorkItem.WORKITEM_RELATIONS, workItemResource.getRelations());
		}
		return workItemResource;
	}
	
	public List<AzureWorkItemOperation> filterUpdateWorkItemOperations(@Body List<AzureWorkItemOperation> operations, Exchange exchange) 
	{
		return operations
			.stream()
			.filter(f -> isFieldAllowed(f.getFieldName(), fieldsUpdateNotAllowed))
			.collect(Collectors.toList());
	}
	
	public List<AzureWorkItemOperation> updateWorkItem(@Body WorkItemNotify<AzureWorkItemResourceUpdate> workItemNotify, Exchange exchange) {
		return null;
	}
	
	public List<AzureWorkItemOperation> deleteWorkItem(@Body WorkItemNotify<AzureWorkItemResourceUpdate> workItemNotify, Exchange exchange) {
		return null;
	}
	
	private List<AzureWorkItemOperation> mergeOperationsWithRelationOperations(Exchange exchange, List<AzureWorkItemOperation> operations) 
	{
		@SuppressWarnings("unchecked")
		List<AzureWorkItemOperation> relationOperations = (List<AzureWorkItemOperation>) exchange.getProperty(ConstantsUtil.WorkItem.WORKITEM_RELATIONS_OPERATIONS, List.class);
		
		if ((operations != null && !operations.isEmpty()) && (relationOperations != null && !relationOperations.isEmpty()))
			operations.addAll(relationOperations);
		return operations;
	}
	
	private List<AzureWorkItemOperation> extractCreateFieldOperations(AzureWorkItemResource workItemResource) 
	{
		return workItemResource.getFields().entrySet()
				.stream()
				.filter(f -> isFieldAllowed(f.getKey(), fieldsNotAllowed))
				.map(f -> generateCreateFieldOperation(f.getKey(), f.getValue()))
				.collect(Collectors.toList());
	}
	
	private void addCurrentWorkItemType(AzureWorkItemResource workItemResource, Exchange exchange) {
		exchange.setProperty(ConstantsUtil.WorkItem.CURRENT_WORKITEM_TYPE, workItemResource.getWorkItemType());
	}
	
	private void addSyncWorkItemInfo(WorkItemResource<?> workItemResource, List<AzureWorkItemOperation> operations) {
		operations.add(generateCreateFieldOperation("Custom.SyncID", workItemResource.getId()));
		operations.add(generateCreateFieldOperation("Custom.SyncReference", workItemResource.getSourceReference()));
	}
	
	private AzureWorkItemOperation generateCreateFieldOperation(String fieldName, Object value) {
		return generateFieldOperation("add", fieldName, value);
	}

	private AzureWorkItemOperation generateFieldOperation(String operation, String fieldName, Object value) {
		return AzureWorkItemOperation.builder()
				.addOperation(operation)
				.addField(fieldName)
				.addValue(value);
	}
	
	private boolean isFieldAllowed(String fieldName , String[] fieldNotAllowed) {
		for (int i = 0; i < fieldNotAllowed.length; i++)
			if (fieldName.trim().replaceAll(fieldNotAllowed[i], "").isEmpty())
				return false;
		return true;
	}
	
}
