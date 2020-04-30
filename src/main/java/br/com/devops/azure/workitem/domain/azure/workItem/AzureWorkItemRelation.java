package br.com.devops.azure.workitem.domain.azure.workItem;

import br.com.devops.azure.workitem.domain.WorkItemRelation;
import br.com.devops.azure.workitem.domain.WorkItemRelationType;

public class AzureWorkItemRelation implements WorkItemRelation {

	private static final String REGEX_GET_WORKITEM_ID_FROM_URL = ".+\\/";
	
	private String rel;
	private String url;
	private AzureWorkItemRelationAttributes attributes;
	
	public String getRel() {
		return rel;
	}
	public String getUrl() {
		return url;
	}
	public AzureWorkItemRelationAttributes getAttributes() {
		return attributes;
	}
	
	public void updateUrl(String url) {
		this.url = url;
	}
	
	@Override
	public Integer getWorkItemId() {
		String idStr = url.replaceAll(REGEX_GET_WORKITEM_ID_FROM_URL, "").trim();
		return idStr != null && !idStr.isEmpty() ? Integer.parseInt(idStr) : null;
	}
	
	@Override
	public String getFieldName() {
		return rel;
	}
	@Override
	public WorkItemRelationType getRelationType() {
		if (attributes != null)
			return AzureWorkItemRelationType.getRelationTypeByName(attributes.getName());
		return null;
	}
}
