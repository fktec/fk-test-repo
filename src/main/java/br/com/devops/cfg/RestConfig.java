package br.com.devops.cfg;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.servlet.CamelHttpTransportServlet;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RestConfig extends RouteBuilder {

	@Value("${camel.springboot.name}")
	private String appContextName;
	
	@Override
	public void configure() throws Exception 
	{
		restConfiguration()
			.contextPath("/" + appContextName)
	        .component("servlet");
	}
	
	@Bean
    public ServletRegistrationBean servletRegistrationBean() 
	{
        ServletRegistrationBean servlet = 
        	new ServletRegistrationBean(new CamelHttpTransportServlet(), "/" + appContextName + "/*");

        servlet.setName("CamelServlet");								
        return servlet;
    }
	
}
