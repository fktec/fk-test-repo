package br.com.devops.azure.workitem.route;

import org.apache.camel.Exchange;
import org.apache.camel.component.http4.HttpMethods;
import org.apache.camel.model.dataformat.JsonLibrary;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpStatus;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import br.com.devops.azure.workitem.bean.AzureWIQLConverter;
import br.com.devops.azure.workitem.domain.azure.wiql.AzureWIQLQuery;
import br.com.devops.azure.workitem.domain.azure.wiql.AzureWIQLResponse;
import br.com.devops.azure.workitem.util.ConstantsUtil;
import br.com.devops.azure.workitem.util.MediaType;
import br.com.globosat.integration.common.camel.api.route.RouteBuilderBase;

@Component
public class AzureWIQLRoute extends RouteBuilderBase {

	protected static final String WIQL_FIND_BY_SYNCID_ID = "{{route.azure.wiql.find-by-syncid.id}}";
	public static final String WIQL_FIND_BY_SYNCID_URI = "{{route.azure.wiql.find-by-syncid.uri}}";
	
	protected static final String WIQL_FINDALL_BY_SYNCID_ID = "{{route.azure.wiql.findall-by-syncid.id}}";
	public static final String WIQL_FINDALL_BY_SYNCID_URI = "{{route.azure.wiql.findall-by-syncid.uri}}";
	
	protected static final String WIQL_ID = "{{route.azure.wiql.id}}";
	public static final String WIQL_URI = "{{route.azure.wiql.uri}}";
	protected static final String WIQL_OUT_URI = "{{azure.host.uri}}";
	
	@Value("${azure.wiql.path}")
	private String azureWIQLPath;
	
	@Override
	public void configure() throws Exception 
	{
		super.configure();
		
		from(WIQL_FIND_BY_SYNCID_URI)
			.routeId(WIQL_FIND_BY_SYNCID_ID)
			.bean(AzureWIQLConverter.class, "findWorkItemBySyncId")
			.to(WIQL_URI)
		.end();
		
		from(WIQL_FINDALL_BY_SYNCID_URI)
			.routeId(WIQL_FINDALL_BY_SYNCID_ID)
			.bean(AzureWIQLConverter.class, "findAllWorkItemsBySyncIds")
			.to(WIQL_URI)
		.end();
		
		from(WIQL_URI)
			.routeId(WIQL_ID)
			.setHeader(Exchange.HTTP_METHOD, HttpMethods.POST)
			.setHeader(Exchange.HTTP_PATH, simple(getPathWIQL()))
			.setHeader(Exchange.CONTENT_TYPE, constant(MediaType.APPLICATION_JSON_UTF8))
			.setHeader(HttpHeaders.AUTHORIZATION, exchangeProperty(ConstantsUtil.DevOps.AZURE_DEVOPS_AUTHORIZATION_TOKEN))
			.marshal().json(JsonLibrary.Jackson, AzureWIQLQuery.class)
			.to(WIQL_OUT_URI)
			.to(AzureDevOpsRoute.CHECK_AZURE_HTTP_RESPONSE_URI)
			.filter(header(Exchange.HTTP_RESPONSE_CODE).isEqualTo(HttpStatus.SC_OK))
				.unmarshal().json(JsonLibrary.Jackson, AzureWIQLResponse.class)
				.filter(simple("${body.workItems.size} == 0"))
					.setHeader(Exchange.HTTP_RESPONSE_CODE, constant(HttpStatus.SC_NO_CONTENT))
			.end()
		.end();
	}

	private String getPathWIQL() {
		return new StringBuilder()
				.append("/${exchangeProperty[").append(ConstantsUtil.DevOps.HTTP_PATH_ORGANIZATION).append("]}")
				.append("/${exchangeProperty[").append(ConstantsUtil.DevOps.HTTP_PATH_PROJECT).append("]}")
				.append("/").append(azureWIQLPath)
				.toString();
	}
}
