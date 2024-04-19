package com.E2E;

import static io.restassured.RestAssured.given;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.testng.Assert;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.specification.RequestSpecification;

public class ECommerceAPITest 
{

	public static void main(String[] args) 
	{
		//it will hold the information about base URI and palyload is in the form of JSON
		RequestSpecification req = new RequestSpecBuilder().setBaseUri("https://rahulshettyacademy.com/")
			.setContentType(ContentType.JSON).build();
		
		//the serialization is performed 
		//when the lp object of the LoginRequest class is converted into a JSON payload using the body() method
		
		LoginRequest lp=new LoginRequest();
		
		lp.setUserEmail("postman39@gmail.com");
		lp.setUserPassword("Hello@123");
		//sending the object to 
		RequestSpecification regLogin = given().log().all().spec(req).body(lp);
		
		//deserialization occurs when the response is extracted 
		//and converted into an instance of the LoginResponse class using the as() method
		
		LoginResponse loginRespose = regLogin.when().post("api/ecom/auth/login")
				
				.then().extract().response().as(LoginResponse.class);
		String token=loginRespose.getToken();
		String userID=loginRespose.getUserId();
		System.out.println("Login Token is : " +loginRespose.getToken());
		System.out.println("Login User Id is : "+loginRespose.getUserId());
		System.out.println("Login Message is : "+loginRespose.getMessage());
		
		//Serialization: lp object is converted into JSON payload using the body() method.
		//Deserialization: The response is extracted and converted into an instance of the LoginResponse class using the as() method.
		
		
		//Add product
		RequestSpecification addProductBaseReq = new RequestSpecBuilder().setBaseUri("https://rahulshettyacademy.com/")
				.addHeader("Authorization", token)
				.build();
		
			RequestSpecification reqSpec = given().log().all().spec(addProductBaseReq)
			.param("productName", "laptop")
			.param("productAddedBy", userID)
			.param("productCategory", "fashion")
			.param("productSubCategory", "shirts")
			.param("productPrice","11500")
			.param("productDescription","Lenova")
			.param("productFor","men")
			.multiPart("productImage",new File("C://Users//Mallukinnis//OneDrive//Desktop//LenvovaLptop.png"));
			
			String addProductResponse=reqSpec.when().post("api/ecom/product/add-product/").then().log().all().extract().response().asPrettyString();
			
			//System.out.println("The product response is : "+addProductResponse);
			
			JsonPath js=new JsonPath(addProductResponse);
			String productId=js.get("productId");
			System.out.println("Prodcut id : "+productId);
			
			String message=js.get("message");
			System.out.println("Message is : "+message);
			//creat order or purchase order

			RequestSpecification createOrderBaseReq = new RequestSpecBuilder().setBaseUri("https://rahulshettyacademy.com/")
			.addHeader("Authorization", token).setContentType(ContentType.JSON).build();
			//first we are creating all the orderDeatils
			OrderDetails orderDetails=new OrderDetails();
			
			orderDetails.setCountry("Austria");
			orderDetails.setProductOrderedId(productId);
			//it is expecting list of orderdetails so we have are creating list of order details
			List<OrderDetails> orderDetailsList=new ArrayList<OrderDetails>();
			//we are addinf order detauls to list of orderdetails
			orderDetailsList.add(orderDetails);
			
			//we are creating the order
			Orders order=new Orders();
			order.setOrders(orderDetailsList);
			
			RequestSpecification creatOrderReq = given().log().all().spec(createOrderBaseReq).body(order);
			
			String responseAddOrderDetails = creatOrderReq.when().post("api/ecom/order/create-order").then().log().all().extract().response().asString();
			
			
			JsonPath responseJson = new JsonPath(responseAddOrderDetails);
			String orderId = responseJson.getList("orders").get(0).toString();
			System.out.println("Order ID is: " + orderId);
			
			//delete the product
			
			RequestSpecification deleteOrderbaseReq = new RequestSpecBuilder().setBaseUri("https://rahulshettyacademy.com")
			.addHeader("authorization",token).setContentType(ContentType.JSON)
			.build();
			
			RequestSpecification deleteProdcutReq = given().log().all().spec(deleteOrderbaseReq).pathParam("productId",productId);
			
			String deleteProductResponse = deleteProdcutReq.when().delete("/api/ecom/product/delete-product/{productId}")
					.then().log().all().extract().response().asString();
		
			
			JsonPath js2 = new JsonPath(deleteProductResponse);

			Assert.assertEquals("Product Deleted Successfully",js2.get("message"));
			
			
			//deleter the order
			
			RequestSpecification deleteOrderIDBaseReq = new RequestSpecBuilder().setBaseUri("https://rahulshettyacademy.com").addHeader("authorization", token)
			.setContentType(ContentType.JSON).build();
			
			RequestSpecification deleteOrderIdreq = given().log().all().spec(deleteOrderIDBaseReq).pathParam("orderId",orderId);
		
			String deletedOrderResponse = deleteOrderIdreq.when().delete("/api/ecom/order/delete-order/{orderId}").then().log().all().extract().response().asString();

			 JsonPath js3 = new JsonPath(deletedOrderResponse);
			String message2=js3.get("message");
			
			Assert.assertEquals(message2, "Orders Deleted Successfully");
			
	} 

}
