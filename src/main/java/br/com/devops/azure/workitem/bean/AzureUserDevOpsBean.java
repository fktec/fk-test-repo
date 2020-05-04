package br.com.devops.azure.workitem.bean;

import java.util.List;

import org.apache.camel.Exchange;
import org.springframework.stereotype.Service;

import br.com.devops.azure.workitem.domain.WorkItemResource;
import br.com.devops.azure.workitem.domain.azure.devops.user.AzureUserDevOpsInfo;
import br.com.devops.azure.workitem.util.ConstantsUtil;

@Service
public class AzureUserDevOpsBean {

	public void extractSourceAndTargetDevOps(Exchange exchange) 
	{
		@SuppressWarnings("unchecked")
		List<AzureUserDevOpsInfo> usersDevOps = (List<AzureUserDevOpsInfo>) exchange.getIn().getBody(List.class);
		
		if (usersDevOps != null && !usersDevOps.isEmpty() && usersDevOps.get(0) instanceof AzureUserDevOpsInfo) 
		{
			WorkItemResource<?> workItemResource = exchange.getProperty(ConstantsUtil.Exchange.TEMPORARY_BODY, WorkItemResource.class);
			
			if (workItemResource != null) {
				extractSourceDevopsInfo(exchange, usersDevOps, workItemResource);
				extractDevopsInfo(exchange);
			}
			else
				exchange.removeProperty(ConstantsUtil.DevOps.AZURE_DEVOPS_AUTHORIZATION_TOKEN);
		}
		
	}

	private void extractSourceDevopsInfo(Exchange exchange, List<AzureUserDevOpsInfo> usersDevOps, WorkItemResource<?> workItemResource) {
		for (AzureUserDevOpsInfo sourceUserDevOpsInfo : usersDevOps) 
		{
			if (sourceUserDevOpsInfo.getOrganization().equals(workItemResource.getOrganization()) && 
				sourceUserDevOpsInfo.getProject().equals(workItemResource.getProject())) 
			{
				exchange.setProperty(ConstantsUtil.DevOps.SOURCE_HTTP_PATH_ORGANIZATION, sourceUserDevOpsInfo.getOrganization());
				exchange.setProperty(ConstantsUtil.DevOps.SOURCE_HTTP_PATH_PROJECT, sourceUserDevOpsInfo.getProject());
				exchange.setProperty(ConstantsUtil.DevOps.SOURCE_DEVOPS_AUTHORIZATION_TOKEN, sourceUserDevOpsInfo.getToken());
				
				extractTargetDevopsInfo(exchange, workItemResource, sourceUserDevOpsInfo);
				break;
			}
		}
	}

	private void extractTargetDevopsInfo(Exchange exchange, WorkItemResource<?> workItemResource, AzureUserDevOpsInfo sourceUserDevOpsInfo) {
		for (AzureUserDevOpsInfo targetUserDevOpsInfo : sourceUserDevOpsInfo.getTargetUsers())
		{
			if (isTrigger(workItemResource, targetUserDevOpsInfo)) {
				exchange.setProperty(ConstantsUtil.DevOps.TARGET_HTTP_PATH_ORGANIZATION, targetUserDevOpsInfo.getOrganization());
				exchange.setProperty(ConstantsUtil.DevOps.TARGET_HTTP_PATH_PROJECT, targetUserDevOpsInfo.getProject());
				exchange.setProperty(ConstantsUtil.DevOps.TARGET_DEVOPS_AUTHORIZATION_TOKEN, targetUserDevOpsInfo.getToken());
				break;
			}
		}
	}
	
	private void extractDevopsInfo(Exchange exchange) {
		exchange.setProperty(ConstantsUtil.DevOps.HTTP_PATH_ORGANIZATION, exchange.getProperty(ConstantsUtil.DevOps.TARGET_HTTP_PATH_ORGANIZATION));
		exchange.setProperty(ConstantsUtil.DevOps.HTTP_PATH_PROJECT, exchange.getProperty(ConstantsUtil.DevOps.TARGET_HTTP_PATH_PROJECT));
		exchange.setProperty(ConstantsUtil.DevOps.AZURE_DEVOPS_AUTHORIZATION_TOKEN, exchange.getProperty(ConstantsUtil.DevOps.TARGET_DEVOPS_AUTHORIZATION_TOKEN));
	}

	private boolean isTrigger(WorkItemResource<?> workItemResource, AzureUserDevOpsInfo targetUserDevOpsInfo) {
		return targetUserDevOpsInfo.getTrigger() != null && (targetUserDevOpsInfo.getTrigger().equals(workItemResource.getAssignedToEmail()));
	}
	
	public String getAzureDevOpsAuthByUserDevOpsToken(String userDevOpsToken) {
		return new StringBuilder().append("Basic ").append(userDevOpsToken).toString();
	}
}
