package com.SeleniumFramework.commons.util;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.commons.lang3.StringUtils;

/**
 * 
 * @author Vivek Mamgain
 * @date 05-June-2016
 * @class JiraConfigManager
 * @description 
 */
public class JiraConfigManager {
	
	public static String BASE_URL;
	public static String UserId;
	public static String Password;
	public static String JiraId;
	private static String comments;
	Properties properties;
	InputStream is;
	private static String filePath;
	private static String fileName;
	
	public JiraConfigManager(String filePath, String fileName) {
		JiraConfigManager.filePath = filePath;
		JiraConfigManager.fileName = fileName;
	}
	
	public void getJiraConfigrationDetails () throws Exception {
		properties = new Properties();
		is = getFileInputStream(JiraConfigManager.filePath, JiraConfigManager.fileName);
		if (is != null) {
			properties.load(is);
			//Read Jira Base URL
			BASE_URL = getPropertyFromJiraConfig("jiraurl");
			if (BASE_URL != null) {
				UserId = getPropertyFromJiraConfig("username");
				if (UserId != null) {
					Password = getPropertyFromJiraConfig("password");
					if (Password != null) {
						JiraId = getPropertyFromJiraConfig("jiraId");
						if (JiraId != null) {}	
						else
							System.out.println("Jira id is not provided, Jira will not be updated!!");;
					} else 
						throw new Exception("Password is not provided - "+Password);
				} else {
					throw new Exception("UserId is not provided - "+UserId);
				}
			} else {
				throw new Exception("Jira base url is not provided - "+BASE_URL);
			}
		} else {
			throw new IOException("Jira config Properties file seems to be empty ");
		}
	}
	
	private InputStream getFileInputStream (String filePath, String fileName) throws IOException {
		return new FileInputStream(filePath+fileName);
	}
	
	private String getPropertyFromJiraConfig (String property) {
		if (!StringUtils.isBlank(properties.getProperty(property)))
			return properties.getProperty(property);
		else
			return null;
	}

}
