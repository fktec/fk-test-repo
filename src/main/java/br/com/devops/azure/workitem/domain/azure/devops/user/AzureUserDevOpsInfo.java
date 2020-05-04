package br.com.devops.azure.workitem.domain.azure.devops.user;

import java.util.List;

public class AzureUserDevOpsInfo {

	private String organization;
	private String project;
	private String token; 							 // Token - Base64 encode
	private List<AzureUserDevOpsInfo> targetUsers;
	private String trigger;
	
	public AzureUserDevOpsInfo() {}
	
	public AzureUserDevOpsInfo(String organization, String project, String token, List<AzureUserDevOpsInfo> targetUsers, String trigger) 
	{
		this.organization = organization;
		this.project = project;
		this.token = token;
		if (targetUsers != null) this.targetUsers = targetUsers;
		this.trigger = filterTrigger(trigger);
	}
	public String getOrganization() {
		return organization;
	}
	public String getProject() {
		return project;
	}
	public String getToken() {
		return token;
	}
	public List<AzureUserDevOpsInfo> getTargetUsers() {
		return targetUsers;
	}
	public String getTrigger() {
		return this.trigger;
	}
	
	private String filterTrigger(String trigger) 
	{
		if (trigger == null) 
			return null;
		return trigger.replaceAll("(.*)<|>(.*)", "").trim();
	}
}
