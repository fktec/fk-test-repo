package br.com.devops.azure.workitem.domain.azure.workItem;

import br.com.devops.azure.workitem.domain.WorkItemRelationType;

public enum AzureWorkItemRelationType {
	PARENT("Parent", WorkItemRelationType.PARENT),
	PREDECESSOR("Predecessor", WorkItemRelationType.PARENT),
	DUPLICATE_OF("Duplicate Of", WorkItemRelationType.PARENT),
	TESTS("Tests", WorkItemRelationType.PARENT),
	CHILD("Child", WorkItemRelationType.CHILD),
	DUPLICATE("Duplicate", WorkItemRelationType.CHILD),
	RELATED("Related", WorkItemRelationType.CHILD),
	SUCCESSOR("Successor", WorkItemRelationType.CHILD),
	TESTED_BY("Tested By", WorkItemRelationType.CHILD);

	private String name;
	private WorkItemRelationType relationType;
	
	AzureWorkItemRelationType(String name, WorkItemRelationType relationType) {
		this.name = name;
		this.relationType = relationType;
	}
	
	public String getName() {
		return name;
	} 
	
	public WorkItemRelationType getRelationType() {
		return relationType;
	}
	
	public static WorkItemRelationType getRelationTypeByName(String typeName) {
		for (AzureWorkItemRelationType azureWorkItemRelationType : values()) {
			if (azureWorkItemRelationType.getName().equals(typeName))
				return azureWorkItemRelationType.getRelationType();
		}
		return null;
	}

}
	
