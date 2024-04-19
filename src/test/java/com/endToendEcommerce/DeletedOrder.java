package com.endToendEcommerce;

import static io.restassured.RestAssured.given;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.testng.Assert;
import org.testng.annotations.Test;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.specification.RequestSpecification;

public class DeletedOrder 
{
	@Test
	public void deletOrder()
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
			
			
			//create order
			RequestSpecification orderBaseReq = new RequestSpecBuilder().setBaseUri("https://rahulshettyacademy.com").addHeader("authorization", token)
			.setContentType(ContentType.JSON).build();
			
			OrderDetails orderDetails=new OrderDetails();
			orderDetails.setCountry("Austria");
			orderDetails.setProductOrderedId(productId);
			
			List<OrderDetails > orderDetailsList=new ArrayList<OrderDetails>();
			orderDetailsList.add(orderDetails);
			
			Orders orders=new Orders();
			orders.setOrders(orderDetailsList);
			
			RequestSpecification orderDetailsReq = given().log().all().spec(orderBaseReq).body(orders);
			
			String orderDetailsResponse = orderDetailsReq.when()
					.post("/api/ecom/order/create-order").then().log().all().extract().response().asString();
			
			JsonPath responseJson=new JsonPath(orderDetailsResponse);
	
			String orderId = responseJson.getList("orders").get(0).toString();
			
			System.out.println("The orderId is : "+orderId);
			
			//delete the added product
		
			
			RequestSpecification deleteproductBaseReq = new RequestSpecBuilder().setBaseUri("https://rahulshettyacademy.com").addHeader("authorization",token)
			.setContentType(ContentType.JSON).build();
			
			RequestSpecification detetePrductDetails = given().log().all().spec(deleteproductBaseReq).pathParam("productId", productId);
			
			String deleteResponse = detetePrductDetails.when().delete("/api/ecom/product/delete-product/{productId}").then().log().all().extract().response().asString();
			
			JsonPath deleteJsonResponse =new JsonPath(deleteResponse);
			Assert.assertEquals("Product Deleted Successfully", deleteJsonResponse.get("message"));
			
			//delete the order

			
			RequestSpecification deleteOrderBaseReq = new RequestSpecBuilder().setBaseUri("https://rahulshettyacademy.com").addHeader("authorization",token).setContentType(ContentType.JSON).build();
			
			RequestSpecification deleteOrderDetails = given().log().all().spec(deleteOrderBaseReq).pathParam("orderId", orderId);
			
			String deleteOrderResponse = deleteOrderDetails.when().delete("/api/ecom/order/delete-order/{orderId}").then().log().all().extract().response().asString();
		

			JsonPath js3 = new JsonPath(deleteOrderResponse);
			String message3=js3.get("message");
			
			Assert.assertEquals(message3, "Orders Deleted Successfully");
	}
}
