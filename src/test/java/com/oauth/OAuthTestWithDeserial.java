package com.oauth;

import static io.restassured.RestAssured.given;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.pojo.Api;
import com.pojo.GetCourses;
import com.pojo.Mobile;
import com.pojo.WebAutomation;

import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;

public class OAuthTestWithDeserial
{
	String[] courseTitle= {"Selenium Webdriver Jav","Cypress","Protractor"};
	String[] coursesOfApi = {"Rest Assured Automation using Java","SoapUI Webservices testing"};
	@Test
	public void getAccessTokenDesial()
	{
		//Get The Api Response and get the access token
		Response response=given()
				.formParam("client_id", "692183103107-p0m7ent2hk7suguv4vq22hjcfhcr43pj.apps.googleusercontent.com")
				.formParam("client_secret","erZOWM9g3UtwNRj340YYaK_W")
				.formParam("grant_type", "client_credentials")
				.formParam("scope", "trust").
				when().log().all().
				post("https://rahulshettyacademy.com/oauthapi/oauth2/resourceOwner/token");

		String ApiResponse=response.asString();
		JsonPath js=new JsonPath(ApiResponse);
		String acToken=js.getString("access_token");

		System.out.println("Access Token is:"+acToken);

		//After validating the actual acesstoken with expected acesstoken,Get the actual response from the API
		//Here to extract the respsonse we are using POJO class 
		GetCourses gc=given()
				.queryParam("access_token",acToken)
				.log().all().
				when().
				get("https://rahulshettyacademy.com/oauthapi/getCourseDetails").as(GetCourses.class);
		System.out.println("The instructor is :"+gc.getInstructor());
		System.out.println("The instructor URL is  :"+gc.getUrl());
		System.out.println("The instructor LinkedIn is :"+gc.getLinkedIn());

		String apiCourseTitle=gc.getCourses().getApi().get(0).getCourseTitle();
		System.out.println("Api course title is :"+apiCourseTitle);

		List<Api> apiCoursesize=gc.getCourses().getApi();
		for(int i=0;i<apiCoursesize.size();i++)
		{
			if(apiCoursesize.get(i).getCourseTitle().equalsIgnoreCase("SoapUI Webservices testing"))
			{
				System.out.println("The course price is : "+apiCoursesize.get(i).getPrice());

			}

		}
		//Get the Selenium WebDriver price in WebAutomationCOurse
		List<WebAutomation> webAutomationCourse = gc.getCourses().getWebAutomation();

		for(int i=0;i<webAutomationCourse.size();i++)
		{
			String webAutomationCourseTitle=webAutomationCourse.get(i).getCourseTitle();
			System.out.println("WebAutomationCOurseTitle is : "+webAutomationCourseTitle);
			if(webAutomationCourse.get(i).getCourseTitle().equalsIgnoreCase("Selenium Webdriver Java"))
			{
				System.out.println("Selenium Webdriver Java price is : "+ webAutomationCourse.get(i).getPrice());

			}

		}
		//Get price of the Appium -Mobile in Mobile Course
		List<Mobile> mobileCourse = gc.getCourses().getMobile();
		for(int i=0;i<mobileCourse.size();i++)
		{
			if(mobileCourse.get(i).getCourseTitle().equalsIgnoreCase("Appium-Mobile Automation using Java"))
			{
				System.out.println("Appium-Mobile Automation using Java price is : "+mobileCourse.get(i).getPrice());
			}
		}

		ArrayList<String> a=new ArrayList<String>();


		//Get all the title of the WebAutomation Course  
		List<WebAutomation> allWebCoursetitle = gc.getCourses().getWebAutomation();

		for(int i=0;i<allWebCoursetitle.size();i++)
		{
			a.add(allWebCoursetitle.get(i).getCourseTitle());

		}
		List<String> expectedTitle= Arrays.asList(courseTitle);

		Assert.assertTrue(a.equals(expectedTitle));

	
		//Get all the courses of Mobile
		
		List<Mobile> allMobilecourse=gc.getCourses().getMobile();
		ArrayList<String> m1=new ArrayList<String>();
		for(int j=0;j<allMobilecourse.size();j++)
		{
			m1.add(allMobilecourse.get(j).getCourseTitle());
		}
		System.out.println(m1);
		List<String> expectedMobileTitle=Arrays.asList();
	}



}
