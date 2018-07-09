package com.SeleniumFramework.commons.util;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

/**
 * 
 * @author Vivek Mamgain
 * @date 05-June-2016
 * @class JiraJSONReader
 * @description 
 */
public class JiraJSONReader {
	
	private FileReader fr;
	private BufferedReader br;
	static String jsonString;
	
	private void readJiraJsonFile (String jsonFileName) throws IOException {
		fr = getFileInputStream(jsonFileName);
		
		if (fr != null) {
			br = new BufferedReader(fr);
		}			
	}
	
	private FileReader getFileInputStream (String jsonFileName) throws FileNotFoundException {
		return new FileReader(jsonFileName);
	}
	
	public String concateNewCommentsInJson(String comments, String jsonFileName) throws IOException {
		// Initiate Jira Json file
		readJiraJsonFile(jsonFileName);
		// update string to jira
		String sCurrentLine, targetStr = "";

		while ((sCurrentLine = br.readLine()) != null) {
			targetStr = targetStr + sCurrentLine;
			//System.out.println(sCurrentLine);
		}

		String[] bufJsonFile = new String[2];
		bufJsonFile = targetStr.split("\"body\":");

		String firstPart = bufJsonFile[0];
		firstPart = firstPart + "\"body\" : \"" + comments+"\",";
		String[] secondPartArray = bufJsonFile[1].split("\"updateAuthor\"");
		String secondPart = secondPartArray[1];
		secondPart = "\"updateAuthor\"" + secondPart;

		String updatedJsonWithComments = firstPart + secondPart;
		return updatedJsonWithComments;
	}

}

