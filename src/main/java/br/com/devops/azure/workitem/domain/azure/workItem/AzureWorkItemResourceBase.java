package br.com.devops.azure.workitem.domain.azure.workItem;

import java.util.List;
import java.util.Map;

import br.com.devops.azure.workitem.domain.WorkItemResource;

public abstract class AzureWorkItemResourceBase<T> implements WorkItemResource<T> {
	
	public static final String FIELD_PROJECT_NAME = "System.TeamProject";
	public static final String FIELD_WORK_ITEM_TYPE = "System.WorkItemType";
	public static final String FIELD_ASSIGNED_TO = "System.AssignedTo";
	
	private Integer id;
	private List<AzureWorkItemRelation> relations;
	private String url;
	
	public String getUrl() {
		return url;
	}
	
	@Override
	public Integer getId() {
		return id;
	}
	
	@Override
	public List<AzureWorkItemRelation> getRelations() {
		return relations;
	}
	
	@Override
	public String getOrganization() {
		return url != null ? url.split("/")[3] : null;
	}
	
	@Override
	public String getProject() {
		return getFieldByName(FIELD_PROJECT_NAME);
	}

	@Override
	public String getWorkItemType() {
		return getFieldByName(FIELD_WORK_ITEM_TYPE);
	}
	
	@Override
	public String getAssignedToEmail() 
	{
		String assignedToEmail = getFieldByName(FIELD_ASSIGNED_TO);
		
		if (assignedToEmail == null)
			return "";
		return assignedToEmail.replaceAll("(.*)<|>(.*)", "").trim();
	}
	
	private String getFieldByName(String fieldName) {
		if (getMapFields() != null)
			for (Map.Entry<String, Object> field : getMapFields().entrySet())
				if (field.getKey().equals(fieldName))
					return (String) field.getValue();
		return null;
	}
}
