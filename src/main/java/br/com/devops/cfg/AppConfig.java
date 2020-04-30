package br.com.devops.cfg;

import org.apache.camel.CamelContext;
import org.apache.camel.impl.DefaultShutdownStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {

	@Autowired
	private CamelContext camelContext;
	
	@Bean
	public DefaultShutdownStrategy defaultShutdownStrategy() {
		DefaultShutdownStrategy defaultShutdownStrategy = new DefaultShutdownStrategy(camelContext);
		defaultShutdownStrategy.setTimeout(5);
		return defaultShutdownStrategy;
	} 
}
