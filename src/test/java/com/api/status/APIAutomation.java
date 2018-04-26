/*
* * Author: Anushka Jain
*/

package com.api.status;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;

import org.json.JSONArray;
import org.json.JSONObject;
import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

@Listeners(Listener.class)

public class APIAutomation {

	// data driven approach to test the API response
	@BeforeTest
	@DataProvider(name = "Generator")
	public static Iterator<CountryStatus> DataGenerator() {
		HashMap<String, CountryStatus> responseMap = new HashMap<String, CountryStatus>();
		String urlEndpoint = "https://istheapplestoredown.com/api/v1/status/worldwide";
		URL obj = null;
		HttpURLConnection connection = null;
		int responseCode = 0;
		try {
			obj = new URL(urlEndpoint);
			connection = (HttpURLConnection) obj.openConnection();
			connection.setRequestMethod("GET");
			responseCode = connection.getResponseCode();
		}
		catch (ProtocolException e) {
			System.out.println("Wrong Protocol" + e.getMessage());
		}
		catch (MalformedURLException e1) {
			System.out.println("Invalid URL encountered" + e1.getMessage());
		}
		catch (IOException e) {
			System.out.println("Problem in connection" + e.getMessage());
		}

		if (responseCode!=200) {
			throw new RuntimeException("HTTP ResponseCode for the GET request: "+responseCode);
		}
		else {
			BufferedReader in = null;
			StringBuffer APIresponse = new StringBuffer();
			try {
				in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
				String inputLine;
				while ((inputLine = in.readLine()) != null) {
					APIresponse.append(inputLine);
				}
				in.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			//converting the APIresponse to HashMap
			JSONObject APIResponseObject = new JSONObject(APIresponse.toString());
			JSONArray keys = APIResponseObject.names();
			for(int i=0;i<keys.length(); i++){
				String temp = (String) keys.get(i);
				CountryStatus cs = new CountryStatus();
				cs.setName(APIResponseObject.getJSONObject(temp).get("name").toString());
				cs.setStatus(APIResponseObject.getJSONObject(temp).get("status").toString());
				responseMap.put(temp, cs);
			}
			return responseMap.values().iterator();
		}
	}
	
	//using the responseMap iterator for testing against each key-value pair
	@Test(dataProvider = "Generator")
	public void testURL(CountryStatus c){
		String status = c.getStatus();
		String country = c.getName();
		Assert.assertEquals("y", status, country);

	}

}
