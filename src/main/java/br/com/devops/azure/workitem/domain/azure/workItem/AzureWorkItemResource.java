package br.com.devops.azure.workitem.domain.azure.workItem;

import java.util.Map;

public class AzureWorkItemResource extends AzureWorkItemResourceBase<Map<String, Object>>{
	
	private Map<String, Object> fields;
	
	private String getFieldByName(String fieldName) {
		if (fields != null)
			for (Map.Entry<String, Object> field : fields.entrySet())
				if (field.getKey().equals(fieldName))
					return (String) field.getValue();
		return null;
	}
	
	@Override
	public Map<String, Object> getFields() {
		return fields;
	}

	@Override
	public String getProject() {
		return getFieldByName(AzureWorkItemNotifyBase.FIELD_PROJECT_NAME);
	}

	@Override
	public String getWorkItemType() {
		return getFieldByName(AzureWorkItemNotifyBase.FIELD_WORK_ITEM_TYPE);
	}

	@Override
	public String getSyncWorkItemFieldName() {
		return null;
	}

}
