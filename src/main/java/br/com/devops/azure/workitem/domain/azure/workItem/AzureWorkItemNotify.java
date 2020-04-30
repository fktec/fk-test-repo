package br.com.devops.azure.workitem.domain.azure.workItem;


public class AzureWorkItemNotify extends AzureWorkItemNotifyBase<AzureWorkItemResource> {

	private AzureWorkItemResource resource;

	@Override
	public AzureWorkItemResource getResource() {
		return resource;
	}

}
