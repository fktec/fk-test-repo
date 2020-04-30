package br.com.devops.azure.workitem.bean;

import java.util.Set;
import java.util.stream.Collectors;

import org.apache.camel.Exchange;
import org.springframework.stereotype.Service;

import br.com.devops.azure.workitem.domain.azure.wiql.AzureWIQLQuery;
import br.com.devops.azure.workitem.util.ConstantsUtil;

@Service
public class AzureWIQLConverter {
	
	public AzureWIQLQuery findWorkItemBySyncId(Exchange exchange) 
	{
		Integer syncId = exchange.getProperty(ConstantsUtil.WorkItem.WORKITEM_ID, Integer.class);
		
		if (syncId != null)
			return generateFindWorkItemsBySyncIds(syncId.toString());
		return null;
	}

	public AzureWIQLQuery findAllWorkItemsBySyncIds(Exchange exchange) 
	{
		@SuppressWarnings("unchecked")
		Set<Integer> syncIds = (Set<Integer>) exchange.getProperty(ConstantsUtil.WorkItem.WORKITEM_IDS);
		
		if (syncIds != null) {
			return generateFindWorkItemsBySyncIds(
					syncIds
					.stream()
					.map(v -> v.toString())
					.collect(Collectors.joining(",")));
		}
		return null;
	}
	
	private AzureWIQLQuery generateFindWorkItemsBySyncIds(String workItemsSyncIds) {
		if (workItemsSyncIds != null && !workItemsSyncIds.trim().isEmpty())
			return new AzureWIQLQuery(String.format("Select [System.Id] From WorkItems Where [Custom.SyncID] IN (%s)", workItemsSyncIds));
		return null;
	}

}
