package br.com.devops.azure.workitem.domain.azure.workItem;

import java.util.Set;

public class AzureWorkItemBatch {

	private Set<Integer> ids;
	private String[] fields;
	private String $expand = "Relations";
	
	public AzureWorkItemBatch(Set<Integer> ids) {
		this.ids = ids;
	}
	
	public AzureWorkItemBatch(Set<Integer> ids, String[] fields) {
		this(ids);
		this.fields = fields;
	}
	
	public Set<Integer> getIds() {
		return ids;
	}
	public String[] getFields() {
		return fields;
	}
	public String get$expand() {
		return $expand;
	}
	
}
