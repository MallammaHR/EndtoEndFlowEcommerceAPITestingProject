package com.endToendEcommerce;
import static io.restassured.RestAssured.given;

import java.io.File;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.specification.RequestSpecification;

public class CreatProduct
{
	public static void main(String[] args)
	{
		//set the uri
		RequestSpecification loginBaseReq=new RequestSpecBuilder().setBaseUri("https://rahulshettyacademy.com")
		.setContentType(ContentType.JSON).build();
	
		//need to pass the user details
		LoginRequest lp1=new LoginRequest();
		
		lp1.setUserEmail("postman39@gmail.com");
		lp1.setUserPassword("Hello@123");
		
		///need pass the user details and that i need to serialize it in the JSON form by using the POST method
		RequestSpecification loginDetails=given().log().all().spec(loginBaseReq).body(lp1);
		
		//now i will call the post method
		String loginResponse = loginDetails.when().post("/api/ecom/auth/login").then().extract().response().asString();
	
		
		JsonPath js=new JsonPath(loginResponse);
		String token=js.getString("token");
		String message1=js.getString("message");
		String userId=js.getString("userId");
		System.out.println("userId is : "+userId);
		System.out.println("Message is : "+message1);
		System.out.println("Token is : "+token);
		
		//create the product
			RequestSpecification creatproductBaseReq =
					new RequestSpecBuilder()
					.setBaseUri("https://rahulshettyacademy.com").addHeader("Authorization",token)
					.build();
			
			
			RequestSpecification productDetails = given().log().all().spec(creatproductBaseReq)
			.param("productName", "Tablet")
			.param("productAddedBy", userId)
			.param("productCategory", "fashion")
			.param("productSubCategory", "shirts")
			.param("productPrice", "11500")
			.param("productDescription", "Addias Originals")
			.param("productFor", "women")
			.multiPart("productImage",new File("C://Users//Mallukinnis//OneDrive//Desktop//Tablet.png"));
			
			
			String productResponse = productDetails.when().post("/api/ecom/product/add-product/").then().extract().response().asString();
			
			JsonPath js1=new JsonPath(productResponse);
			
			String productId=js1.getString("productId");
			System.out.println("productId is  : " +productId);
			String message2 =js1.getString("message");
			System.out.println("Message2 is : "+message2);
			
			
			
			
			
		
	}
}
