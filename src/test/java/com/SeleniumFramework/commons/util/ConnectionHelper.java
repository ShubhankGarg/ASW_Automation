package com.SeleniumFramework.commons.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;

import org.json.JSONObject;

public class ConnectionHelper {

	//
	public static URL url = null;
	public static HttpURLConnection httpURLConnection = null;
	public static URLConnection urlConnectionObject = null;

	public static URLConnection getConnectionToUrl(String inputUrl, RequestHeaderInfo requestInfo) {

		try {
			url = new URL(inputUrl);
			urlConnectionObject = url.openConnection();
			if(requestInfo != null) {
				//TODO: Future enhancement: Iterate over properties of request info
				urlConnectionObject.setRequestProperty("scope", requestInfo.authToken);
				urlConnectionObject.setRequestProperty("scope", requestInfo.scope);
				urlConnectionObject.setRequestProperty("correlation_id", requestInfo.cid);
				urlConnectionObject.setRequestProperty("timestamp", requestInfo.timestamp);
				urlConnectionObject.setRequestProperty("actor", requestInfo.actor);
			}
			urlConnectionObject.connect();
			return urlConnectionObject;
		} catch (Exception e) {
			e.printStackTrace();
			return urlConnectionObject;
		}

	}


//	public static HttpURLConnection getPostConnectionObject(String endPointUrl) throws IOException {
//		 
//		url = new URL(endPointUrl);
//
//		httpURLConnection = (HttpURLConnection) url.openConnection();
//		httpURLConnection.setRequestMethod("POST");
//		httpURLConnection.setRequestProperty("Content-Type", "application/json");
//		httpURLConnection.setDoOutput(true);
//		
//		return httpURLConnection;	
//	}
	
	public static HttpURLConnection createGetConnection(String inputUrl, HashMap<String, String> headerParameters)
	  {
	    URL url = null;
	    try
	    {
	      url = new URL(inputUrl);
	      httpURLConnection = (HttpURLConnection)url.openConnection();
	      httpURLConnection.setRequestMethod("GET");
	      httpURLConnection.setDoOutput(true);
	      if (!headerParameters.isEmpty()) {
	        for (String headerKey : headerParameters.keySet()) {
	          httpURLConnection.setRequestProperty(headerKey, (String)headerParameters.get(headerKey));
	        }
	      }
	      httpURLConnection.connect();
	      return httpURLConnection;
	    }
	    catch (Exception e)
	    {
	      e.printStackTrace();
	    }
	    return httpURLConnection;
	  }
	  
	  public static HttpURLConnection createPostConnection(String endPointUrl, HashMap<String, String> headerParameters)
	  {
	    URL url = null;
	    try
	    {
	      url = new URL(endPointUrl);
	      httpURLConnection = (HttpURLConnection)url.openConnection();
	      httpURLConnection.setRequestMethod("POST");
	      httpURLConnection.setDoOutput(true);
	      if (!headerParameters.isEmpty()) {
	        for (String key : headerParameters.keySet()) {
	          httpURLConnection.setRequestProperty(key, (String)headerParameters.get(key));
	        }
	      }
	    }
	    catch (Exception e)
	    {
	      e.printStackTrace();
	      return httpURLConnection;
	    }
	    return httpURLConnection;
	  }
	
}
