package br.com.devops.azure.workitem.api;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import br.com.devops.DevOpsSyncApplication;
import br.com.devops.azure.workitem.util.workItem.WorkItemUtilTest;


@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = DevOpsSyncApplication.class, webEnvironment = WebEnvironment.RANDOM_PORT)
@TestPropertySource({"classpath:application.properties"})
public class AzureWorkItemRestRouteTest {

	@Value("${camel.springboot.name}")
	private String appContextName;
	
	@Autowired
	private TestRestTemplate restTemplate;
	
	@Test
	public void tA3_consumeCreateNotifyPIBWorkItem_Success() 
	{
		String uri = "/" + appContextName + "/" + AzureWorkItemRestRoute.BASE_URL + "/" + AzureWorkItemRestRoute.REST_AZURE_POST_WORKITEM_CREATE_URI;
		String payload = WorkItemUtilTest.workItemCreateNotifyPIB1PayloadSuccess;
		
		HttpHeaders headers = null;
		headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
		
		HttpEntity<String> httpEntity = new HttpEntity<>(payload, headers);
		ResponseEntity<String> response = restTemplate.exchange(uri, HttpMethod.POST, httpEntity, String.class);
		
		assertNotNull(response);
	}
	
}
