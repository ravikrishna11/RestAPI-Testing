package com.qa.tests;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.qa.base.TestBase;
import com.qa.client.RestClient;
import com.qa.data.Users;

public class PostAPITest extends TestBase {
	
	TestBase testBase;
	String serviceUrl;
	String apiUrl;
	String url;
	RestClient restClient;
	CloseableHttpResponse closebleHttpReponse;
	
	@BeforeMethod
	public void setUp() throws ClientProtocolException, IOException {
		
		testBase = new TestBase();
		 serviceUrl = prop.getProperty("URL");
		 apiUrl = prop.getProperty("serviceURL");
		
		 url = serviceUrl+apiUrl;
		
	}
	@Test
	public void PostAPITest() throws JsonGenerationException, JsonMappingException, IOException {
		
		restClient = new RestClient();
		HashMap<String,String> headerMap = new HashMap<String,String>();
	    headerMap.put("Content-Type", "application/json");
	    
	    //Jackson API:
	    ObjectMapper mapper = new ObjectMapper();
	    Users users = new Users("morpheus","leader","",""); // Expected users object
	    
	    //object to json file:
	    mapper.writeValue(new File("/Users/Ravi/Desktop/Selenium/RestAPI/src/main/java/com/qa/data/users.json"), users);
	    
	    //Java object to json in string:
	    String usersJsonString = mapper.writeValueAsString(users);
	    System.out.println("UsersJsonString: "+usersJsonString);
	    
	   closebleHttpReponse = restClient.post(url, usersJsonString, headerMap);//call API
	   
	   //Validate response from API
	   //1.statusCode
	   int statusCode = closebleHttpReponse.getStatusLine().getStatusCode();
	   Assert.assertEquals(statusCode, testBase.RESPONSE_STATUS_CODE_201);
	   
	   //2.JsonString:
	   String responseString = EntityUtils.toString(closebleHttpReponse.getEntity(),"UTF-8");
	   JSONObject responseJson = new JSONObject(responseString);
	   System.out.println("Response from API: "+responseJson);
	   
	   //json to java object
	   Users usersResObj = mapper.readValue(responseString, Users.class);// Actual users object.// Actual users object.
	   System.out.println("usersResObj : "+usersResObj); 
	   
	   System.out.println(users.getName().equals(usersResObj.getName()));//Use AssertTrue
	   System.out.println(users.getJob().equals(usersResObj.getJob()));
	   
	   System.out.println("Id: "+usersResObj.getId());
	   System.out.println("createdAt: "+usersResObj.getCreatedAt());
	   
		
	}

}
