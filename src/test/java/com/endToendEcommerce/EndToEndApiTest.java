package com.endToendEcommerce;

import static io.restassured.RestAssured.given;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.specification.RequestSpecification;

public class EndToEndApiTest 
{
	public static void main(String[] args) 
	{
		RequestSpecification loginBaseReq = new RequestSpecBuilder().setBaseUri("https://rahulshettyacademy.com")
		.setContentType(ContentType.JSON).build();
		//pass the user details to login the app
		LoginRequest lp=new LoginRequest();
		
		lp.setUserEmail("postman39@gmail.com");
		lp.setUserPassword("Hello@123");
		
		// these login details will be serialized to JSON or XML and sent with request(this works only for POSt or PUT,
		//suppose if i use other than POST/PUT it will cause exception
		RequestSpecification loginRequestDetails = given().log().all().spec(loginBaseReq).body(lp);
		
		String loginResponse = loginRequestDetails.when().post("/api/ecom/auth/login").then().extract().response().asString();
	
		JsonPath js=new JsonPath(loginResponse);
		String token=js.getString("token");
		String message =js.getString("message");
		
		System.out.println("Token is : "+token);
		System.out.println("Message is : "+message);
	}
}
