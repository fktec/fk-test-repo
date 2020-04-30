package br.com.devops.bean;

import org.apache.camel.Exchange;
import org.apache.camel.Handler;
import org.apache.commons.httpclient.HttpStatus;
import org.springframework.stereotype.Service;

@Service
public class RestResponseBuilder {

	@Handler
	public void checkRestResponse(Exchange exchange) 
	{
		Integer statusCode = exchange.getIn().getHeader(Exchange.HTTP_RESPONSE_CODE, Integer.class);
		
		if (statusCode != null && statusCode.equals(HttpStatus.SC_OK)) 
			exchange.getIn().setBody(null);
	}
}
