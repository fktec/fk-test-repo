package br.com.devops.azure.workitem.route;

import java.util.ArrayList;

import org.apache.camel.Exchange;
import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.PredicateBuilder;
import org.apache.camel.component.http4.HttpMethods;
import org.apache.camel.model.dataformat.JsonLibrary;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import br.com.devops.azure.workitem.bean.AzureDevOpsSwitchProcessor;
import br.com.devops.azure.workitem.bean.AzureWorkItemBean;
import br.com.devops.azure.workitem.bean.AzureWorkItemConverter;
import br.com.devops.azure.workitem.bean.aggregation.AzureWorkItemOperationAggregationStrategy;
import br.com.devops.azure.workitem.bean.aggregation.AzureWorkItemRelationAggregationStrategy;
import br.com.devops.azure.workitem.domain.WorkItemRelation;
import br.com.devops.azure.workitem.domain.azure.wiql.AzureWIQLResponse;
import br.com.devops.azure.workitem.domain.azure.workItem.AzureWorkItemBatch;
import br.com.devops.azure.workitem.domain.azure.workItem.AzureWorkItemBatchResponse;
import br.com.devops.azure.workitem.domain.azure.workItem.AzureWorkItemOperation;
import br.com.devops.azure.workitem.domain.azure.workItem.AzureWorkItemResource;
import br.com.devops.azure.workitem.util.ConstantsUtil;
import br.com.devops.azure.workitem.util.MediaType;
import br.com.globosat.integration.common.camel.api.route.RouteBuilderBase;

@Component
public class AzureWorkItemRoute extends RouteBuilderBase {
	
	protected static final String EXISTS_WORKITEM_ID = "{{route.azure.workitem.exists.id}}";
	public static final String EXISTS_WORKITEM_URI = "{{route.azure.workitem.exists.uri}}";

	protected static final String CREATE_WORKITEM_ID = "{{route.azure.workitem.create.id}}";
	public static final String CREATE_WORKITEM_URI = "{{route.azure.workitem.create.uri}}";
	protected static final String CREATE_WORKITEM_OUT_URI = "{{azure.host.uri}}";
	
	protected static final String UPDATE_WORKITEM_ID = "{{route.azure.workitem.update.id}}";
	public static final String UPDATE_WORKITEM_URI = "{{route.azure.workitem.update.uri}}";
	protected static final String UPDATE_WORKITEM_OUT_URI = "{{azure.host.uri}}";
	
	protected static final String OPERATIONS_CREATE_WORKITEM_ID = "{{route.azure.workitem.operations.create.id}}";
	public static final String OPERATIONS_CREATE_WORKITEM_URI = "{{route.azure.workitem.operations.create.uri}}";
	
	protected static final String OPERATIONS_UPDATE_WORKITEM_ID = "{{route.azure.workitem.operations.update.id}}";
	public static final String OPERATIONS_UPDATE_WORKITEM_URI = "{{route.azure.workitem.operations.update.uri}}";
	
	protected static final String GET_WORKITEM_BATCH_ID = "{{route.azure.workitem.get-batch.id}}";
	public static final String GET_WORKITEM_BATCH_URI = "{{route.azure.workitem.get-batch.uri}}";
	protected static final String GET_WORKITEM_BATCH_OUT_URI = "{{azure.host.uri}}";
	
	protected static final String CREATE_WORKITEM_RELANTIONS_ID = "{{route.azure.workitem.relation.create.id}}";
	public static final String CREATE_WORKITEM_RELANTIONS_URI = "{{route.azure.workitem.relation.create.uri}}";
	
	@Autowired
	private AzureDevOpsSwitchProcessor azureDevOpsBean;
	
	@Autowired
	private AzureWorkItemOperationAggregationStrategy relationOperationAggregationStrategy;
	
	@Autowired
	private AzureWorkItemRelationAggregationStrategy workItemRelationAggregationStrategy;
	
	@Value("${azure.workitem.path}")
	private String azureWorkItemPath;
	
	@Value("${azure.workitem.batch.path}")
	private String azureWorkItemBatchPath;
	
	@Override
	public void configure() throws Exception 
	{
		super.configure();
		
		from(OPERATIONS_CREATE_WORKITEM_URI)
			.routeId(OPERATIONS_CREATE_WORKITEM_ID)
			.bean(AzureWorkItemConverter.class, "extractWorkItemRelations")
			.enrich(CREATE_WORKITEM_RELANTIONS_URI, relationOperationAggregationStrategy)
			.setProperty(ConstantsUtil.Exchange.TEMPORARY_BODY, body())
			.to(EXISTS_WORKITEM_URI)
			.setBody(exchangeProperty(ConstantsUtil.Exchange.TEMPORARY_BODY))
			.bean(AzureWorkItemConverter.class, "createWorkItemOperations")
			.choice()
				.when(PredicateBuilder.and(
						exchangeProperty(ConstantsUtil.WorkItem.EXISTS_WORKITEM).isEqualTo(true),
						exchangeProperty(ConstantsUtil.WorkItem.WORKITEM_RELATIONS_OPERATIONS).isNotNull()))
					.to(UPDATE_WORKITEM_URI)
				.endChoice()
				.when(header(Exchange.HTTP_RESPONSE_CODE).isEqualTo(HttpStatus.SC_NO_CONTENT))
					.to(CREATE_WORKITEM_URI)
				.endChoice()
			.end()
		.end();
		
		from(OPERATIONS_UPDATE_WORKITEM_URI)
			.routeId(OPERATIONS_UPDATE_WORKITEM_ID)
			.bean(AzureWorkItemConverter.class, "updateWorkItem")
		.end();
		
		from(UPDATE_WORKITEM_URI)
			.routeId(UPDATE_WORKITEM_ID)
			.filter(body().isNotNull())
				.bean(AzureWorkItemConverter.class, "filterUpdateWorkItemOperations")
				.marshal().json(JsonLibrary.Jackson, AzureWorkItemOperation[].class)
				.setHeader(Exchange.HTTP_METHOD, HttpMethods.PATCH)
				.setHeader(Exchange.HTTP_PATH, simple(getPathWorkItemUpdateById()))
				.setHeader(Exchange.CONTENT_TYPE, constant(MediaType.APPLICATION_JSON_PATH_JSON))
				.setHeader(HttpHeaders.AUTHORIZATION, exchangeProperty(ConstantsUtil.DevOps.AZURE_DEVOPS_AUTHORIZATION_TOKEN))
				.to(UPDATE_WORKITEM_OUT_URI)
				.filter(header(Exchange.HTTP_RESPONSE_CODE).isEqualTo(HttpStatus.SC_OK))
					.unmarshal().json(JsonLibrary.Jackson, AzureWorkItemResource.class)
				.end()
				.to(AzureDevOpsRoute.CHECK_AZURE_HTTP_RESPONSE_URI)
			.end()
		.end();
		
		from(CREATE_WORKITEM_URI)
			.routeId(CREATE_WORKITEM_ID)
			.filter(body().isNotNull())
				.marshal().json(JsonLibrary.Jackson, AzureWorkItemOperation[].class)
				.setHeader(Exchange.HTTP_METHOD, HttpMethods.POST)
				.setHeader(Exchange.HTTP_PATH, simple(getPathWorkItemCreateByType()))
				.setHeader(Exchange.CONTENT_TYPE, constant(MediaType.APPLICATION_JSON_PATH_JSON))
				.setHeader(HttpHeaders.AUTHORIZATION, exchangeProperty(ConstantsUtil.DevOps.AZURE_DEVOPS_AUTHORIZATION_TOKEN))
				.to(CREATE_WORKITEM_OUT_URI)
				.filter(header(Exchange.HTTP_RESPONSE_CODE).isEqualTo(HttpStatus.SC_OK))
					.unmarshal().json(JsonLibrary.Jackson, AzureWorkItemResource.class)
				.end()
				.to(AzureDevOpsRoute.CHECK_AZURE_HTTP_RESPONSE_URI)
			.end()
		.end();
		
		from(EXISTS_WORKITEM_URI)
			.routeId(EXISTS_WORKITEM_ID)		
			.removeProperty(ConstantsUtil.WorkItem.EXISTS_WORKITEM)
			.bean(AzureWorkItemBean.class, "extractWorkItemId")
			.to(AzureWIQLRoute.WIQL_FIND_BY_SYNCID_URI)
			.choice()
				.when(header(Exchange.HTTP_RESPONSE_CODE).isEqualTo(HttpStatus.SC_OK))
					.log(LoggingLevel.WARN, "## WorkItem já existente! ##")
					.setProperty(ConstantsUtil.WorkItem.EXISTS_WORKITEM, constant(true))
					.setBody().exchange(e -> extractFirtWorktItemIdByWIQLResponse(e))
				.endChoice()
				.otherwise()
					.setBody(exchangeProperty(ConstantsUtil.Exchange.TEMPORARY_BODY))
					.setBody(constant(null))
				.endChoice()
			.end()
		.end();
		
		from(GET_WORKITEM_BATCH_URI)
			.routeId(GET_WORKITEM_BATCH_ID)
			.filter(body().isNotNull())
				.marshal().json(JsonLibrary.Jackson, AzureWorkItemBatch.class)
				.setHeader(Exchange.HTTP_METHOD, HttpMethods.POST)
				.setHeader(Exchange.HTTP_PATH, simple(getPathWorkItemBatch()))
				.setHeader(Exchange.CONTENT_TYPE, constant(MediaType.APPLICATION_JSON_UTF8))
				.setHeader(HttpHeaders.AUTHORIZATION, exchangeProperty(ConstantsUtil.DevOps.AZURE_DEVOPS_AUTHORIZATION_TOKEN))
				.to(GET_WORKITEM_BATCH_OUT_URI)
				.to(AzureDevOpsRoute.CHECK_AZURE_HTTP_RESPONSE_URI)
				.filter(header(Exchange.HTTP_RESPONSE_CODE).isEqualTo(HttpStatus.SC_OK))
					.unmarshal().json(JsonLibrary.Jackson, AzureWorkItemBatchResponse.class)
				.end()
			.end()
		.end();
		
		from(CREATE_WORKITEM_RELANTIONS_URI)
			.routeId(CREATE_WORKITEM_RELANTIONS_ID)
			.filter(exchangeProperty(ConstantsUtil.WorkItem.WORKITEM_RELATIONS).isNotNull())
				.bean(AzureWorkItemBean.class, "extractWorkItemIdsByRelations")
				.filter(body().isNotNull())
					.process(azureDevOpsBean.switchDevOpsInfoToSource())
					.to(GET_WORKITEM_BATCH_URI)
					.process(azureDevOpsBean.switchDevOpsInfoToTarget())
					.filter(PredicateBuilder.and(header(Exchange.HTTP_RESPONSE_CODE).isEqualTo(HttpStatus.SC_OK), body().isNotNull()))
						.setProperty(ConstantsUtil.WorkItem.WORKITEM_RELATIONS_AGGREGATION).exchange(ex -> new ArrayList<WorkItemRelation>())
						.split(simple("${body.value}"))
							.filter().method(AzureWorkItemBean.class, "workItemRelationTypeIsAllowed")
								.enrich(OPERATIONS_CREATE_WORKITEM_URI, workItemRelationAggregationStrategy)
							.end()
						.end()
					.end()
				.end()
			.end()
			.removeProperty(ConstantsUtil.WorkItem.WORKITEM_RELATIONS)
		.end();
	}
	
	private Object extractFirtWorktItemIdByWIQLResponse(Exchange e) 
	{
		AzureWIQLResponse azureWIQLResponse = e.getIn().getBody(AzureWIQLResponse.class);
		if (azureWIQLResponse != null) {
			e.setProperty(ConstantsUtil.WorkItem.WORKITEM_ID, azureWIQLResponse.getFirstWorkItemId());
			return azureWIQLResponse.getFirstWorkItemId();
		}
		return null;
	}
	
	private String getPathWorkItemCreateByType() {
		return new StringBuilder()
				.append(getPathWorkItem())
				.append("/$${exchangeProperty[").append(ConstantsUtil.WorkItem.CURRENT_WORKITEM_TYPE).append("]}")
				.toString();
	}
	
	private String getPathWorkItemUpdateById() {		
		return new StringBuilder()
				.append(getPathWorkItem())
				.append("/${exchangeProperty[").append(ConstantsUtil.WorkItem.WORKITEM_ID).append("]}")
				.toString();
	}
	
	private String getPathWorkItem() {
		return new StringBuilder()
				.append("/${exchangeProperty[").append(ConstantsUtil.DevOps.HTTP_PATH_ORGANIZATION).append("]}")
				.append("/${exchangeProperty[").append(ConstantsUtil.DevOps.HTTP_PATH_PROJECT).append("]}")
				.append("/").append(azureWorkItemPath)
				.toString();
	}
	
	private String getPathWorkItemBatch() {
		return new StringBuilder()
				.append("/${exchangeProperty[").append(ConstantsUtil.DevOps.HTTP_PATH_ORGANIZATION).append("]}")
				.append("/${exchangeProperty[").append(ConstantsUtil.DevOps.HTTP_PATH_PROJECT).append("]}")
				.append("/").append(azureWorkItemBatchPath)
				.toString();
	}
	
}
