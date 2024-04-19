package com.oauth;

import static io.restassured.RestAssured.given;

import org.testng.annotations.Test;

import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
public class GetAccessToken
{
	@Test
	public void getAcessToken()
	{
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
		
		//Get the resoonse from the api which we are looking for 
		
		String courseDetails=given()
			.queryParam("access_token",acToken)
			.log().all().
		when().
			get("https://rahulshettyacademy.com/oauthapi/getCourseDetails").prettyPrint().toString();
		System.out.println("Course Details is : "+courseDetails);
	}
	
}
