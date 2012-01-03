package com.fogstack.napdroid;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * A JSON interface class, allowing simple retrieval of JSON content from REST APIs.
 * @author Sean Byrnes sean@fogstack.com
 */
public class JSONClient {
	/** Retrieve a JSON Client instance which will communicate with the given host. */
	public static JSONClient getInstance(String hostURL) {
		return new JSONClient(hostURL);
	}
	/** Retrieve a JSON Client instance which will communicate with the given host using the specified request type. */
	public static JSONClient getInstance(String hostURL, JSONConnectionType connectionType) {
		return new JSONClient(hostURL, connectionType);
	}
	
	String fHostUrl;
	JSONConnectionType fRequestType = JSONConnectionType.GET;
	
	private JSONClient(String hostURL)
	{
		fHostUrl = hostURL;
	}
	
	private JSONClient(String hostURL, JSONConnectionType connectionType)
	{
		fHostUrl = hostURL;
		fRequestType = connectionType;
	}

	/** Retrieves the JSON object resulting from a REST request. 
	 * @throws JSONException */
	public JSONObject get() throws JSONException
	{
		return get(new HashMap<String, String>());
	}
	
	/** Retrieves the JSON object resulting from a REST request with the provided parameters. 
	 * @throws JSONException */
	public JSONObject get(Map<String, String> parameters) throws JSONException
	{
		String rawResult = null;
		
		if(fRequestType == JSONConnectionType.GET)
			rawResult = getViaGET(parameters);
		else if(fRequestType == JSONConnectionType.POST)
			rawResult = getViaPOST(parameters);
		
		JSONObject jObject = new JSONObject(rawResult);
			
		return jObject;
	}
	
	/** Retrieves the raw content of a request using HTTP GET and the specified parameters. */
	private String getViaGET(Map<String, String> parameters)
	{
		String results = null;
		
        HttpClient httpclient = new DefaultHttpClient();
        
        List<NameValuePair> nameValuePairs = buildNameValuePairs(parameters);
        
        String paramString = URLEncodedUtils.format(nameValuePairs, "utf-8");
        String queryURL = fRequestType + "?" +  paramString;
        
        // Prepare a request object
        HttpGet httpget = new HttpGet(queryURL);
 
        // Execute the request
        try {            
        	httpget.setHeader("Accept", "application/json");

        	HttpResponse response = httpclient.execute(httpget);
        	int status  = response.getStatusLine().getStatusCode();
			if(status != 200)
			{
				// TODO: Log bad status code
			} else {	
				results = readResponse(response);
			}
		} catch (Exception e) {
			// TODO: Log network error
		} 
		
		return results;
	}

	/** Retrieves the raw content of a request using HTTP POST and the specified parameters. */
	private String getViaPOST(Map<String, String> parameters)
	{
		String results = null;
		
        HttpClient httpclient = new DefaultHttpClient();
        
        List<NameValuePair> nameValuePairs = buildNameValuePairs(parameters);
        
        // Prepare a request object
        HttpPost httppost = new HttpPost(fHostUrl);
 
        // Execute the request
        try {            
        	httppost.setHeader("Accept", "application/json");
        	httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

        	HttpResponse response = httpclient.execute(httppost);
        	int status  = response.getStatusLine().getStatusCode();
			if(status != 200)
			{
				// TODO: Log bad status code
			} else {	
				results = readResponse(response);
			}
		} catch (Exception e) {
			// TODO: Log that network error occurred.
		} 
		
		return results;		
	}
	
	/** Builds a list of NameValuePairs from a map of <String String> */
	private List<NameValuePair> buildNameValuePairs(Map<String, String> parameters)
	{
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
        if(parameters != null)
        {
	        for(Entry<String, String> value : parameters.entrySet())
	        {
	            nameValuePairs.add(new BasicNameValuePair(value.getKey(), value.getValue()));
	        }
        }
        return nameValuePairs;
	}
	
	/** Reads an HTTP response and returns the raw body content */
	private String readResponse(HttpResponse response) throws IllegalStateException, IOException
	{
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line;
		StringBuilder sb =  new StringBuilder();
		while ((line = rd.readLine()) != null) {
				sb.append(line);
		}
		rd.close();
		return sb.toString();
	}
}