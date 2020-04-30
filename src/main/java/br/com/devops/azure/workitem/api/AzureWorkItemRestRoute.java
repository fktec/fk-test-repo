package br.com.devops.azure.workitem.api;

import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.PredicateBuilder;
import org.apache.camel.model.dataformat.JsonLibrary;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

import br.com.devops.azure.workitem.domain.WorkItemEventType;
import br.com.devops.azure.workitem.domain.azure.workItem.AzureWorkItemNotify;
import br.com.devops.azure.workitem.domain.azure.workItem.AzureWorkItemNotifyBase;
import br.com.devops.azure.workitem.route.AzureDevOpsRoute;
import br.com.devops.azure.workitem.route.AzureWorkItemRoute;
import br.com.devops.azure.workitem.util.ConstantsUtil;
import br.com.devops.bean.RestResponseBuilder;
import br.com.globosat.integration.common.camel.api.route.RouteBuilderBase;

@Component
public class AzureWorkItemRestRoute extends RouteBuilderBase {

	protected static final String BASE_URL = "azure/workItem";
	
	private static final String REST_AZURE_WORKITEM_POST_CREATE_ROUTE_ID = "REST_AZURE_POST_WORKITEM_CREATE";
	protected static final String REST_AZURE_POST_WORKITEM_CREATE_URI = "create";
	//private static final String REST_AZURE_WORKITEM_POST_UPDATE_ROUTE_ID = "REST_AZURE_POST_WORKITEM_UPDATE";
	//protected static final String REST_AZURE_POST_WORKITEM_UPDATE_URI = "update";
	
	private static final String REST_FILTER_WORKITEM_ID = "{{route.azure.rest.filter-workitem.id}}";
	private static final String REST_FILTER_WORKITEM_URI = "{{route.azure.rest.filter-workitem.uri}}";
	
	private static final String REST_EXTRACT_WORKITEM_BY_NOTIFY_ID = "{{route.azure.extract-workitem-by-notify.id}}";
	private static final String REST_EXTRACT_WORKITEM_BY_NOTIFY_URI = "{{route.azure.extract-workitem-by-notify.uri}}";
	
	@Override
	public void configure() throws Exception 
	{
		super.configure();
		
		rest(BASE_URL)
			
		.get()
			.produces(MediaType.APPLICATION_JSON_UTF8_VALUE)
			.route()
				.setBody(constant("## Servidor OK! ##"))
			.end()
		.endRest()
		
		.post(REST_AZURE_POST_WORKITEM_CREATE_URI)
			.type(AzureWorkItemNotifyBase.class)
			.route()
			.routeId(REST_AZURE_WORKITEM_POST_CREATE_ROUTE_ID)
				.log("## [CREATE] Notificado por um Work item criado ##")
				.unmarshal().json(JsonLibrary.Jackson, AzureWorkItemNotify.class)
				.setProperty(ConstantsUtil.WorkItem.WORKITEM_TYPE, constant(WorkItemEventType.CREATE))
				.to(REST_FILTER_WORKITEM_URI)
				.to(AzureDevOpsRoute.EXTRACT_AZURE_DEVOPS_USER_INFO_URI)
				.to(REST_EXTRACT_WORKITEM_BY_NOTIFY_URI)
				.log("Enviando para a rota de criação de work item..")
				.to(AzureWorkItemRoute.OPERATIONS_CREATE_WORKITEM_URI)
				.bean(RestResponseBuilder.class)
		.endRest();
		
		/*
		.post(REST_AZURE_POST_WORKITEM_UPDATE_URI)
			.type(AzureWorkItemNotifyBase.class)
			.route()
			.routeId(REST_AZURE_WORKITEM_POST_UPDATE_ROUTE_ID)
				.log("## [UPDATE] Notificado por um Work item modificado ##")
				.unmarshal().json(JsonLibrary.Jackson, AzureWorkItemNotifyUpdate.class)
				.setProperty(ConstantsUtil.WorkItem.WORKITEM_TYPE, constant(WorkItemEventType.UPDATE))
				.to(REST_FILTER_WORKITEM_URI)
				.to(AzureDevOpsRoute.EXTRACT_AZURE_DEVOPS_USER_INFO_URI)
				.log("Enviando para a rota de modificação de work item..")
				//.to(AzureWorkItemRoute.UPDATE_WORKITEM_URI)
			.end()
		.endRest();
		*/
		
		from(REST_FILTER_WORKITEM_URI)
			.routeId(REST_FILTER_WORKITEM_ID)
			.choice()
				.when(PredicateBuilder.or(body().isNull(), bodyAs(String.class).isEqualTo("")))
					.log(LoggingLevel.ERROR, "Body Inválido")
					.stop()
				.when(PredicateBuilder.isNotEqualTo(exchangeProperty(ConstantsUtil.WorkItem.WORKITEM_TYPE), simple("${body.getEventType}")))
					.log(LoggingLevel.ERROR, String.format("EventType não permitido para [${exchangeProperty[%s]}]", ConstantsUtil.WorkItem.WORKITEM_TYPE))
					.stop()
			.end()
			.removeProperty(ConstantsUtil.WorkItem.WORKITEM_TYPE)
			.removeHeader("CamelServletContextPath")
			.removeHeader("CamelHttpUri")
		.end();
		
		from(REST_EXTRACT_WORKITEM_BY_NOTIFY_URI)
			.routeId(REST_EXTRACT_WORKITEM_BY_NOTIFY_ID)
			.filter(PredicateBuilder.and(
					body().isNotNull(),
					body().isInstanceOf(AzureWorkItemNotifyBase.class)))
				.setBody(simple("${body.getResource}"))
			.end()
		.end();
		
	}	
	
}
