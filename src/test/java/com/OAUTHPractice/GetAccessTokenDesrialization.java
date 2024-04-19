package com.OAUTHPractice;

import static io.restassured.RestAssured.given;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.courses.Api;
import com.courses.GetCourses;
import com.courses.WebAutomation;

import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;

public class GetAccessTokenDesrialization
{
	String[] webAutomationCourseTitles = {"Selenium Webdriver Java","Cypress","Protractor"};
	String[] apiCoursesTitle= {"Rest Assured Automation using Java", "SoapUI Webservices testing"};
	
	@Test
	public void getAccesToken()
	{
		Response response1=given().log().all()
				.formParam("client_id", "692183103107-p0m7ent2hk7suguv4vq22hjcfhcr43pj.apps.googleusercontent.com")
				.formParam("client_secret", "erZOWM9g3UtwNRj340YYaK_W")
				.formParam("grant_type", "client_credentials")
				.formParam("scope", "trust").
			when().log().all()
				.post("https://rahulshettyacademy.com/oauthapi/oauth2/resourceOwner/token");
		
		String responseString=response1.asString();
		System.out.println("Response-1 as a String is : "+responseString);
	
		//I need to get the Response from the string and we need to parse the String
		
	
		
		JsonPath js=new JsonPath(responseString);
		String accessToken=js.getString("access_token");
		System.out.println("The access Token is  : "+accessToken);
		
		//Get the response by keeping the access token
//		String response2=given().log().all()
//				
//				.queryParam("access_token", accessToken)
//		.when()
//				.get("https://rahulshettyacademy.com/oauthapi/getCourseDetails").prettyPeek().asPrettyString();
//		
//		System.out.println("The Response-2 as a String is : "+response2);
		
		//when we get JSON response ,we are defining here convert that Json Response in to Java Object  with help og GetCOurse
		GetCourses gc=given().log().all()
				
				.queryParam("access_token", accessToken)
		.when()
				.get("https://rahulshettyacademy.com/oauthapi/getCourseDetails").as(GetCourses.class);
		
		System.out.println("The linkedIn URL is : " +gc.getLinkedIn());
		System.out.println("The instructor name is : " +gc.getInstructor());
		
		String apiCourseTitle = gc.getCourses().getApi().get(0).getCourseTitle();
		System.out.println("API course title is : "+apiCourseTitle);
		
		//price of Api course
		List<Api> apiCoursesize = gc.getCourses().getApi();
		for(int i=0;i<apiCoursesize.size();i++)
		{
			if(apiCoursesize.get(i).getCourseTitle().equalsIgnoreCase("SoapUI Webservices testing"))
			{
				System.out.println("SoapUI Webservices testing price is : "+apiCoursesize.get(i).getPrice());
			}
		}
		
		ArrayList<String> actaulCousetitle=new ArrayList<String>();
		
		//all the title of the WebAutomation
	List<WebAutomation> webAutomatomationCourses = gc.getCourses().getWebAutomation();
	for(int i=0;i<webAutomatomationCourses.size();i++)
	{
		
		if(webAutomatomationCourses.get(i).getCourseTitle().equalsIgnoreCase("Selenium Webdriver Java"))
		{
			System.out.println("Selenium Webdriver Java course price is : "+webAutomatomationCourses.get(i).getPrice());
		}
		
		actaulCousetitle.add(webAutomatomationCourses.get(i).getCourseTitle());
	
	}

	List<String> expectedTilte=Arrays.asList(webAutomationCourseTitles);
	
	Assert.assertTrue(actaulCousetitle.equals(expectedTilte));	
	
	ArrayList<String> actualapiCourse=new ArrayList<>();
	//Get all the Mobile Course titles
	List<Api> apiCourse = gc.getCourses().getApi();
	for(int i=0;i<apiCourse.size();i++)
	{
		actualapiCourse.add(apiCourse.get(i).getCourseTitle());
	}
	
	List<String> expectedTitleOfApi=Arrays.asList(apiCoursesTitle);
	Assert.assertTrue(actualapiCourse.equals(expectedTitleOfApi));
	
	}
	
	
	
	
	
	
	
}
