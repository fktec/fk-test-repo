package br.com.devops.azure.workitem.route;

import java.io.IOException;

import org.apache.camel.Exchange;
import org.apache.camel.ExchangePattern;
import org.apache.camel.LoggingLevel;
import org.apache.camel.model.dataformat.JsonLibrary;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpStatus;
import org.springframework.stereotype.Component;

import br.com.devops.azure.workitem.bean.AzureUserDevOpsBean;
import br.com.devops.azure.workitem.domain.azure.devops.user.AzureUserDevOpsInfo;
import br.com.devops.azure.workitem.util.ConstantsUtil;
import br.com.devops.azure.workitem.util.MediaType;
import br.com.globosat.integration.common.camel.api.route.RouteBuilderBase;

@Component
public class AzureDevOpsRoute extends RouteBuilderBase {
	
	protected static final String CHECK_AZURE_RESPONSE_ID = "{{route.azure.http.check-response.id}}";
	public static final String CHECK_AZURE_HTTP_RESPONSE_URI = "{{route.azure.http.check-response.uri}}";
	
	private static final String EXTRACT_AZURE_DEVOPS_USER_INFO_ID = "{{route.azure.devops.user.id}}";
	public static final String EXTRACT_AZURE_DEVOPS_USER_INFO_URI = "{{route.azure.devops.user.uri}}";
	private static final String EXTRACT_AZURE_DEVOPS_USER_INFO_OUT_URI = "{{route.azure.devops.user.out.uri}}";
	
	private static final String HAS_TRIGGER_ID = "{{route.azure.devops.has-trigger.id}}";
	public static final String HAS_TRIGGER_URI = "{{route.azure.devops.has-trigger.uri}}";	
	
	@Override
	public void configure() throws Exception {
		super.configure();
		
		from(CHECK_AZURE_HTTP_RESPONSE_URI)
			.routeId(CHECK_AZURE_RESPONSE_ID)
			.setExchangePattern(ExchangePattern.InOnly)
			.choice()
				.when(header(Exchange.HTTP_RESPONSE_CODE).isEqualTo(HttpStatus.SC_OK))
					.log(LoggingLevel.INFO, "Requisição feita ao Azure DevOps realizada com sucesso! >> [PATH]: ${header.CamelHttpPath}")
				.when(header(Exchange.CONTENT_TYPE).startsWith(MediaType.TEXT_HTML))
					.log(LoggingLevel.ERROR, "Possível erro de autenticação")
				.otherwise()
					.log(LoggingLevel.ERROR, "Erro na requisição para o Azure DevOps. >> [PATH]: ${header.CamelHttpPath}")
					.log(LoggingLevel.ERROR, "${body}")
			.end()
			.removeHeader(Exchange.HTTP_METHOD)
			.removeHeader(Exchange.HTTP_PATH)
			.removeHeader(Exchange.CONTENT_TYPE)
			.removeHeader(HttpHeaders.AUTHORIZATION)
		.end();
		
		from(EXTRACT_AZURE_DEVOPS_USER_INFO_URI)
			.routeId(EXTRACT_AZURE_DEVOPS_USER_INFO_ID)
			.streamCaching()
			.setProperty(ConstantsUtil.Exchange.TEMPORARY_BODY, body())
			.log(LoggingLevel.INFO, "Buscando arquivo com informações de usuários do Azure Devops")
			.doTry()
				.pollEnrich(EXTRACT_AZURE_DEVOPS_USER_INFO_OUT_URI, 0)
				.choice()
					.when(body().isNotNull())
						.unmarshal().json(JsonLibrary.Jackson, AzureUserDevOpsInfo[].class)
						.bean(AzureUserDevOpsBean.class, "extractSourceAndTargetDevOps")
						.to(HAS_TRIGGER_URI)
						.setBody(exchangeProperty(ConstantsUtil.Exchange.TEMPORARY_BODY))
						.removeProperty(ConstantsUtil.Exchange.TEMPORARY_BODY)
					.endChoice()
					.otherwise()
						.log(LoggingLevel.ERROR, "Arquivo não encontrado!")
				.end()
			.endDoTry()
			.doCatch(IOException.class)
				.log(LoggingLevel.ERROR, "Erro ao tentar ler o arquivo")
			.end()
		.end();
		
		from(HAS_TRIGGER_URI)
			.routeId(HAS_TRIGGER_ID)
			.filter(exchangeProperty(ConstantsUtil.DevOps.AZURE_DEVOPS_AUTHORIZATION_TOKEN).isNull())
				.log(LoggingLevel.WARN, "Nenhum trigger encontrado!")
				.stop()
			.end()
		.end();
		
	}
	
}
