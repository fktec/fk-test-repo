package br.com.devops.azure.workitem.domain.azure.workItem;

public class AzureWorkItemOperation {

	private static final String PREFIX_FIELD_NAME = "/fields/";
	private static final String PREFIX_RELATION_NAME = "/relations/";
	
	private String op;
	private String path;
	private String from = null;
	private Object value;
	
	private AzureWorkItemOperation() {}
	
	public static AzureWorkItemOperation builder() {
		return new AzureWorkItemOperation();
	}
	
	public AzureWorkItemOperation addOperation(String operation) {
		this.op = operation;
		return this;
	}
	
	public AzureWorkItemOperation addField(String fieldName) {
		this.path = PREFIX_FIELD_NAME + fieldName;
		return this;
	}
	
	public AzureWorkItemOperation addRelation(String position) {
		this.path = PREFIX_RELATION_NAME + position;
		return this;
	}
	
	public AzureWorkItemOperation addFrom(String from) {
		this.from = from; 
		return this;
	}
	
	public AzureWorkItemOperation addValue(Object value) {
		this.value = value; 
		return this;
	}
	
	public String getOp() {
		return op;
	}
	public String getPath() {
		return path;
	}
	public String getFrom() {
		return from;
	}
	public Object getValue() {
		return value;
	}
	
	public String getFieldName() {
		return getPath().replace(PREFIX_FIELD_NAME, "");
	}
	
	public boolean isField(String fieldName) {
		if (getPath() == null)
			return false;
		return getPath().replace(PREFIX_FIELD_NAME, "").equals(fieldName);
	}
}
