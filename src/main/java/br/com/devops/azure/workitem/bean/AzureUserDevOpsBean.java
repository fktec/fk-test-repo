package br.com.devops.azure.workitem.bean;

import java.util.List;

import org.apache.camel.Exchange;
import org.springframework.stereotype.Service;

import br.com.devops.azure.workitem.domain.WorkItemNotify;
import br.com.devops.azure.workitem.domain.azure.devops.user.AzureUserDevOpsInfo;
import br.com.devops.azure.workitem.util.ConstantsUtil;

@Service
public class AzureUserDevOpsBean {

	public void extractSourceAndTargetDevOps(Exchange exchange) 
	{
		@SuppressWarnings("unchecked")
		List<AzureUserDevOpsInfo> usersDevOps = (List<AzureUserDevOpsInfo>) exchange.getIn().getBody(List.class);
		
		if (usersDevOps != null && !usersDevOps.isEmpty() && usersDevOps.get(0) instanceof AzureUserDevOpsInfo) {
			WorkItemNotify<?> workItemNotify = exchange.getProperty(ConstantsUtil.Exchange.TEMPORARY_BODY, WorkItemNotify.class);
			
			for (AzureUserDevOpsInfo sourceUserDevOpsInfo : usersDevOps) {
				if (sourceUserDevOpsInfo.getOrganization().equals(workItemNotify.getSourceOrganization()) && 
					sourceUserDevOpsInfo.getProject().equals(workItemNotify.getSourceProject())) 
				{
					exchange.setProperty(ConstantsUtil.DevOps.SOURCE_HTTP_PATH_ORGANIZATION, sourceUserDevOpsInfo.getOrganization());
					exchange.setProperty(ConstantsUtil.DevOps.SOURCE_HTTP_PATH_PROJECT, sourceUserDevOpsInfo.getProject());
					exchange.setProperty(ConstantsUtil.DevOps.SOURCE_DEVOPS_AUTHORIZATION_TOKEN, sourceUserDevOpsInfo.getToken());
					
					for (AzureUserDevOpsInfo targetUserDevOpsInfo : sourceUserDevOpsInfo.getTargetUsers()) {
						exchange.setProperty(ConstantsUtil.DevOps.TARGET_HTTP_PATH_ORGANIZATION, targetUserDevOpsInfo.getOrganization());
						exchange.setProperty(ConstantsUtil.DevOps.TARGET_HTTP_PATH_PROJECT, targetUserDevOpsInfo.getProject());
						exchange.setProperty(ConstantsUtil.DevOps.TARGET_DEVOPS_AUTHORIZATION_TOKEN, targetUserDevOpsInfo.getToken());
						
						exchange.setProperty(ConstantsUtil.DevOps.HTTP_PATH_ORGANIZATION, targetUserDevOpsInfo.getOrganization());
						exchange.setProperty(ConstantsUtil.DevOps.HTTP_PATH_PROJECT, targetUserDevOpsInfo.getProject());
						exchange.setProperty(ConstantsUtil.DevOps.AZURE_DEVOPS_AUTHORIZATION_TOKEN, getAzureDevOpsAuthByUserDevOpsToken(targetUserDevOpsInfo.getToken()));
						
						break;
					}
					break;
				}
			}
		}
		
		System.out.println();
	}
	
	public String getAzureDevOpsAuthByUserDevOpsToken(String userDevOpsToken) {
		return new StringBuilder().append("Basic ").append(userDevOpsToken).toString();
	}
}

/*
 * WorkItemNotify<?> workItemNotify = e.getIn().getBody(WorkItemNotify.class);
		e.setProperty(ConstantsUtil.DevOps.SOURCE_HTTP_PATH_ORGANIZATION, workItemNotify.getSourceOrganizationName());
		e.setProperty(ConstantsUtil.DevOps.SOURCE_HTTP_PATH_PROJECT, workItemNotify.getSourceProjectName());
		e.setProperty(ConstantsUtil.DevOps.SOURCE_DEVOPS_AUTHORIZATION_TOKEN, ConstantsUtil.DevOps.SOURCE_DEVOPS_AUTHORIZATION_TOKEN_VALUE);
		
		e.setProperty(ConstantsUtil.DevOps.TARGET_HTTP_PATH_ORGANIZATION, "fktec2014");
		e.setProperty(ConstantsUtil.DevOps.TARGET_HTTP_PATH_PROJECT, "FK-TESTE-FABRICA");
		e.setProperty(ConstantsUtil.DevOps.TARGET_DEVOPS_AUTHORIZATION_TOKEN, ConstantsUtil.DevOps.TARGET_DEVOPS_AUTHORIZATION_TOKEN_VALUE);
	})
	.setProperty(ConstantsUtil.DevOps.HTTP_PATH_ORGANIZATION, exchangeProperty(ConstantsUtil.DevOps.TARGET_HTTP_PATH_ORGANIZATION))
	.setProperty(ConstantsUtil.DevOps.HTTP_PATH_PROJECT, exchangeProperty(ConstantsUtil.DevOps.TARGET_HTTP_PATH_PROJECT))
	.setProperty(ConstantsUtil.DevOps.AZURE_DEVOPS_AUTHORIZATION_TOKEN, exchangeProperty(ConstantsUtil.DevOps.TARGET_DEVOPS_AUTHORIZATION_TOKEN))
 */