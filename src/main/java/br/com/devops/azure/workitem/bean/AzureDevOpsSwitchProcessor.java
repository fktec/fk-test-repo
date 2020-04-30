package br.com.devops.azure.workitem.bean;

import org.apache.camel.Processor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.devops.azure.workitem.util.ConstantsUtil;

@Service
public class AzureDevOpsSwitchProcessor {
	
	@Autowired
	private AzureUserDevOpsBean azureUserDevOpsBean;
	
	public Processor switchDevOpsInfoToTarget() {
		return e -> {
			e.removeProperty(ConstantsUtil.DevOps.AZURE_DEVOPS_AUTHORIZATION_TOKEN);
			e.setProperty(ConstantsUtil.DevOps.HTTP_PATH_ORGANIZATION, e.getProperty(ConstantsUtil.DevOps.TARGET_HTTP_PATH_ORGANIZATION));
			e.setProperty(ConstantsUtil.DevOps.HTTP_PATH_PROJECT, e.getProperty(ConstantsUtil.DevOps.TARGET_HTTP_PATH_PROJECT));
			e.setProperty(ConstantsUtil.DevOps.AZURE_DEVOPS_AUTHORIZATION_TOKEN, azureUserDevOpsBean.getAzureDevOpsAuthByUserDevOpsToken(e.getProperty(ConstantsUtil.DevOps.TARGET_DEVOPS_AUTHORIZATION_TOKEN, String.class)));
		};
	}
	
	public Processor switchDevOpsInfoToSource() {	
		return e -> {
			e.removeProperty(ConstantsUtil.DevOps.AZURE_DEVOPS_AUTHORIZATION_TOKEN);
			e.setProperty(ConstantsUtil.DevOps.HTTP_PATH_ORGANIZATION, e.getProperty(ConstantsUtil.DevOps.SOURCE_HTTP_PATH_ORGANIZATION));
			e.setProperty(ConstantsUtil.DevOps.HTTP_PATH_PROJECT, e.getProperty(ConstantsUtil.DevOps.SOURCE_HTTP_PATH_PROJECT));
			e.setProperty(ConstantsUtil.DevOps.AZURE_DEVOPS_AUTHORIZATION_TOKEN, azureUserDevOpsBean.getAzureDevOpsAuthByUserDevOpsToken(e.getProperty(ConstantsUtil.DevOps.SOURCE_DEVOPS_AUTHORIZATION_TOKEN, String.class)));
		};
	}

}
