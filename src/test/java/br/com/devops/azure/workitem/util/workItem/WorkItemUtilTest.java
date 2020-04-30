package br.com.devops.azure.workitem.util.workItem;

import org.springframework.core.io.ClassPathResource;

import br.com.devops.azure.workitem.util.ResourceReader;

public class WorkItemUtilTest {

	private static String ROOT_PATH = "samples";
	private static String PATH_WORKITEM = ROOT_PATH + "/workItem";
	private static String PATH_WORKITEM_CREATE = PATH_WORKITEM + "/create";
	private static String PATH_WORKITEM_UPDATE = PATH_WORKITEM + "/update";

	public static String workItemCreateNotifyEPICPayloadSuccess =
			ResourceReader.asString(new ClassPathResource(PATH_WORKITEM_CREATE + "/payload-success_CREATE_NOTIFY-EPIC.json"));
	
	public static String workItemCreateNotifyFEATUREPayloadSuccess =
			ResourceReader.asString(new ClassPathResource(PATH_WORKITEM_CREATE + "/payload-success_CREATE_NOTIFY-FEATURE.json"));
	
	public static String workItemCreateNotifyPIB1PayloadSuccess =
			ResourceReader.asString(new ClassPathResource(PATH_WORKITEM_CREATE + "/payload-success_CREATE_NOTIFY-PIB-1.json"));
	
	public static String workItemUpdateNotifyEPICPayloadSuccess =
			ResourceReader.asString(new ClassPathResource(PATH_WORKITEM_UPDATE + "/payload-success_UPDATE_NOTIFY-EPIC.json"));
	
}
