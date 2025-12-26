package com.venkata.serviceAutomation;

import org.testng.annotations.Test;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.AfterSuite;

public class RahulShettyServiceTest {
	 RahulShettyService service;
	  
	  @BeforeSuite
	  public void beforeSuite() {
		  System.out.println("beforeSuite - Executes once before all tests in the suite");
	  }

	  @BeforeTest
	  public void beforeTest() {
		  System.out.println("beforeTest - Executes before all test classes");
	  }

	  @BeforeClass
	  public void beforeClass() {
		  System.out.println("beforeClass - Executes once before all test methods in the class");
		  RestAssured.baseURI = "https://rahulshettyacademy.com";
	  }
	  
	  @BeforeMethod
	  public void beforeMethod() {
		  System.out.println("beforeMethod - Executes before each test method");
		  service = new RahulShettyService();
	  }
	  
	  @Test
	  public void addPlace() {
		  System.out.println("addPlace - Test execution started");
		  
		  String response = service.addPlace().asString();
		  
		  JsonPath js = new JsonPath(response);
		  
		  assertThat(js.get("status"),equalTo("OK"));
		  assertThat(js.get("scope"),equalTo("APP"));
		  
		  System.out.println("Place Added :" + js.get("place_id"));
		  System.out.println("addPlace - Test execution completed" + response);
	  }
	  
	  @Test
	  public void deletePlace() {
		  System.out.println("sampleTest - This is a sample test method");
		  
		  String response = service.addPlace().asString();
		  
		  JsonPath js = new JsonPath(response);
		  
		  assertThat(js.get("status"),equalTo("OK"));
		  assertThat(js.get("scope"),equalTo("APP"));
		  
		   String placeId = js.get("place_id");
		   response = service.getPlace(placeId).asString();
		   
		   assertThat(js.get("place_id"),equalTo(placeId));
		   
		   response = service.deletePlace(placeId).asString();
		   assertThat(js.get("status"),equalTo("OK"));
	  }
	  @Test
	  public void getPlace() {
		  System.out.println("sampleTest - This is a sample test method");
	
		  String response = service.addPlace().asString();
		  
		  JsonPath js = new JsonPath(response);
		  
		  assertThat(js.get("status"),equalTo("OK"));
		  assertThat(js.get("scope"),equalTo("APP"));
		  
		   String placeId = js.get("place_id");
		   response = service.getPlace(placeId).asString();
		   
		   assertThat(js.get("place_id"),equalTo(placeId));
		  
	  }

	  @AfterMethod
	  public void afterMethod() {
		  System.out.println("afterMethod - Executes after each test method");
	  }

	  @AfterClass
	  public void afterClass() {
		  System.out.println("afterClass - Executes once after all test methods in the class");
	  }

	  @AfterTest
	  public void afterTest() {
		  System.out.println("afterTest - Executes after all test classes");
	  }

	  @AfterSuite
	  public void afterSuite() {
		  System.out.println("afterSuite - Executes once after all tests in the suite");
	  }

}
