package com.SeleniumFramework.commons.util;

import javax.naming.AuthenticationException;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONException;
import org.json.JSONObject;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientHandlerException;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.core.util.Base64;

/**
 * 
 * @author Vivek Mamgain
 * @date 05-June-2016
 * @class JiraRESTClient
 * @description 
 */
public class JiraRESTClient {
	static String jiraFileName;
	static String filePath;
	private JiraConfigManager jiraConfigManager;
	
	public JiraRESTClient (String filePath, String jiraFileName, String jsonFileName) {
		JiraRESTClient.jiraFileName = jiraFileName;
		JiraRESTClient.filePath = filePath;
		JiraRESTClient.jsonFileName = JiraRESTClient.filePath+jsonFileName;
		jiraConfigManager = new JiraConfigManager(filePath, jiraFileName);
	}
	
	private JiraJSONReader jiraJsonReader = new JiraJSONReader();
	private String urlRESTProject, urlRESTIssue, urlRESTIssueComment;
	private String updatedJsonWithComments;
	static String jsonFileName;
	static String auth;
	
	private String createJiraProjectRESTURL() {
		return JiraConfigManager.BASE_URL+"/rest/api/2/project";
	}
	
	private String createJiraIssueCommentRESTURL() {
		return JiraConfigManager.BASE_URL+"//rest/api/2/issue/"+JiraConfigManager.JiraId+"/comment";
	}

	private String createJiraIssueRESTURL() {
		return JiraConfigManager.BASE_URL+"/rest/api/2/issue/"+JiraConfigManager.JiraId;
	}
	
	public void updateJiraItemComments(String comments) throws Exception {

		if (!StringUtils.isBlank(JiraConfigManager.JiraId)) {
			//Read and set the config file
			//jiraConfigManager.getJiraConfigrationDetails(filePath, jiraFileName);
			jiraConfigManager.getJiraConfigrationDetails();
			
			//Create REST for Jira
			urlRESTProject=createJiraProjectRESTURL();
			urlRESTIssueComment=createJiraIssueCommentRESTURL();
			
			//Create REST for Jira Issue
			urlRESTIssue = createJiraIssueRESTURL();
			
			String uid = JiraConfigManager.UserId;
			String pwd = JiraConfigManager.Password;
			String uidpwd = uid +":"+pwd;
			auth = new String(Base64.encode(uidpwd));
			String keyValue = getJiraStatusFromJson(urlRESTIssue, auth);
			
			if (keyValue.equalsIgnoreCase("In Progress")) {
				//Updating comments in Jira JSON
				updatedJsonWithComments = jiraJsonReader.concateNewCommentsInJson(comments, jsonFileName);
				uid = JiraConfigManager.UserId;
				pwd = JiraConfigManager.Password;
				uidpwd = uid +":"+pwd;
				auth = new String(Base64.encode(uidpwd));
				invokePostMethod(auth, urlRESTIssueComment, updatedJsonWithComments);
			} else {
				System.out.println("Jira status is not In Progress!! its status is = "+keyValue);
			}
		} else {
			System.out.println("Jira ID is not provided in jira.properties, hence skipping Jira integration !!");
		}
	}
	
	private static String invokeGetMethod(String auth, String url) throws AuthenticationException, ClientHandlerException {
		Client client = Client.create();
		WebResource webResource = client.resource(url);
		ClientResponse response = webResource.header("Authorization", "Basic " + auth).type("application/json")
				.accept("application/json").get(ClientResponse.class);
		int statusCode = response.getStatus();
		if (statusCode == 401) {
			throw new AuthenticationException("Invalid Username or Password");
		}
		return response.getEntity(String.class);
	}
	
	private static String invokePostMethod(String auth, String url, String data) throws AuthenticationException, ClientHandlerException {
		Client client = Client.create();
		WebResource webResource = client.resource(url);
		ClientResponse response = webResource.header("Authorization", "Basic " + auth).type("application/json")
				.accept("application/json").post(ClientResponse.class, data);
		int statusCode = response.getStatus();
		if (statusCode == 401) {
			throw new AuthenticationException("Invalid Username or Password");
		}
		return response.getEntity(String.class);
	}
	
	private static void invokePutMethod(String auth, String url, String data) throws AuthenticationException, ClientHandlerException {
		Client client = Client.create();
		WebResource webResource = client.resource(url);
		ClientResponse response = webResource.header("Authorization", "Basic " + auth).type("application/json")
				.accept("application/json").put(ClientResponse.class, data);
		int statusCode = response.getStatus();
		if (statusCode == 401) {
			throw new AuthenticationException("Invalid Username or Password");
		}
	}
	
	private static void invokeDeleteMethod(String auth, String url) throws AuthenticationException, ClientHandlerException {
		Client client = Client.create();
		WebResource webResource = client.resource(url);
		ClientResponse response = webResource.header("Authorization", "Basic " + auth).type("application/json")
				.accept("application/json").delete(ClientResponse.class);
		int statusCode = response.getStatus();
		if (statusCode == 401) {
			throw new AuthenticationException("Invalid Username or Password");
		}
	}
	
	private static String getJiraStatusFromJson(String url, String auth) throws AuthenticationException, ClientHandlerException, JSONException {
		String issue = invokeGetMethod(auth, url);
		JSONObject issueObj = new JSONObject(issue);
		String  newKey = new JSONObject(new JSONObject(issueObj.getString("fields")).getString("status")).getString("name");
		System.out.println("field:"+newKey);
			
		return newKey;
	}
}

