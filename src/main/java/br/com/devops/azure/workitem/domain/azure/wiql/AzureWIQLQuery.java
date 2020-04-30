package br.com.devops.azure.workitem.domain.azure.wiql;

public class AzureWIQLQuery {

	private String query;
	
	public AzureWIQLQuery(String query) {
		this.query = query;
	}

	public String getQuery() {
		return query;
	}
	
}
