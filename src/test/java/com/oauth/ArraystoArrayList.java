package com.oauth;

import static io.restassured.RestAssured.given;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.pojo.Api;
import com.pojo.GetCourses;
import com.pojo.WebAutomation;

import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;

public class ArraystoArrayList 
{


	@Test
	public void getAccesToke()
	{
		String[] actualWebCourseTitl= {"Selenium Webdriver Java", "Cypress", "Protractor"};
		String[] actualApiCourseTitle= {"Rest Assured Automation using Java", "SoapUI Webservices testing"};
		//Get The Api Response and get the access token
		Response response=given()
				.formParam("client_id", "692183103107-p0m7ent2hk7suguv4vq22hjcfhcr43pj.apps.googleusercontent.com")
				.formParam("client_secret","erZOWM9g3UtwNRj340YYaK_W")
				.formParam("grant_type", "client_credentials")
				.formParam("scope", "trust").
				when().log().all().
				post("https://rahulshettyacademy.com/oauthapi/oauth2/resourceOwner/token");

		String apirResponse=response.asPrettyString();
		JsonPath js=new JsonPath(apirResponse);
		String acessToken=js.getString("access_token");
		System.out.println("The actual access oken is  :"+acessToken);


		//After VAlidating the response get the actual response 
		GetCourses gc =given()
				.queryParam("access_token",acessToken)
				.log().all().
				when()
				.get("https://rahulshettyacademy.com/oauthapi/getCourseDetails")
				.as(GetCourses.class);
	
		// i want to get the course of API
		
		List<Api> allApiCourse = gc.getCourses().getApi();
		
		ArrayList<String> a=new ArrayList<String>();
		
		for(int i=0;i<allApiCourse.size();i++)
		{
			a.add(allApiCourse.get(i).getCourseTitle());
		}
		
		List<String> expectedTitle=Arrays.asList(actualApiCourseTitle);
		System.out.println("Expceted title : "+expectedTitle);
		Assert.assertTrue(a.equals(expectedTitle));
		//WebAutomation Courses
		List<WebAutomation> allWebCOurse = gc.getCourses().getWebAutomation();
		ArrayList<String> w=new ArrayList<String>();
		for(int j=0;j<allWebCOurse.size();j++)
		{
			w.add(allWebCOurse.get(j).getCourseTitle());
		}
		List<String> expectedWebCOurseTitle=Arrays.asList(actualWebCourseTitl);
		System.out.println("Expected titles  : "+expectedWebCOurseTitle );
		Assert.assertTrue(w.equals(expectedWebCOurseTitle));
		}
	
		//Get
		
}
