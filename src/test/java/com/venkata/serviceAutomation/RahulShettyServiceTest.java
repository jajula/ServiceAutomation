package com.venkata.serviceAutomation;

import org.testng.annotations.Test;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.AfterSuite;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class RahulShettyServiceTest {
	
	private static final Logger logger = LogManager.getLogger(RahulShettyServiceTest.class);
	private RahulShettyService service;
	  
	@BeforeSuite
	public void beforeSuite() {
		logger.info("beforeSuite - Executes once before all tests in the suite");
	}

	@BeforeTest
	public void beforeTest() {
		logger.info("beforeTest - Executes before all test classes");
	}

	@BeforeClass
	public void beforeClass() {
		logger.info("beforeClass - Executes once before all test methods in the class");
		RestAssured.baseURI = "https://rahulshettyacademy.com";
		// Set default request specifications
		RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
	}
	  
	@BeforeMethod
	public void beforeMethod() {
		logger.info("beforeMethod - Executes before each test method");
		service = new RahulShettyService();
	}
	  
	@Test(priority = 1, description = "Test adding a new place via POST API")
	public void addPlace() {
		logger.info("addPlace - Test execution started");
		  
		Response response = service.addPlace();
		
		// Validate HTTP status code
		response.then()
			.assertThat()
			.statusCode(200)
			.body("status", equalTo("OK"))
			.body("scope", equalTo("APP"))
			.body("place_id", notNullValue());
		
		JsonPath js = new JsonPath(response.asString());
		String placeId = js.get("place_id");
		
		logger.info("Place Added Successfully - Place ID: " + placeId);
		logger.debug("Full Response: " + response.asString());
	}
	  
	@Test(priority = 2, description = "Test retrieving a place via GET API")
	public void getPlace() {
		logger.info("getPlace - Test execution started");
	
		// First, add a place to retrieve
		Response addResponse = service.addPlace();
		JsonPath addJs = new JsonPath(addResponse.asString());
		
		addResponse.then()
			.assertThat()
			.statusCode(200)
			.body("status", equalTo("OK"));
		
		String placeId = addJs.get("place_id");
		logger.info("Place created for retrieval - Place ID: " + placeId);
		
		// Add a small delay to ensure place is available (if needed)
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
		}
		
		// Now retrieve the place
		Response getResponse = service.getPlace(placeId);
		
		// Log the raw response for debugging
		String responseBody = getResponse.asString();
		logger.info("Get Place Response Status Code: " + getResponse.getStatusCode());
		logger.info("Get Place Response Content-Type: " + getResponse.getContentType());
		logger.info("Get Place Response Body: " + responseBody);
		
		// Validate HTTP status code first
		int statusCode = getResponse.getStatusCode();
		assertThat("Status code should be 200", statusCode, equalTo(200));
		
		// Check if response is valid JSON before parsing
		if (responseBody == null || responseBody.trim().isEmpty()) {
			throw new AssertionError("Response body is empty");
		}
		
		// Try to parse JSON - if it fails, log the actual response
		JsonPath getJs;
		try {
			getJs = new JsonPath(responseBody);
		} catch (Exception e) {
			logger.error("Failed to parse JSON response. Response was: " + responseBody);
			throw new AssertionError("Response is not valid JSON. Response: " + responseBody, e);
		}
		
		// Validate the retrieved place details
		assertThat("Place ID should match", getJs.get("place_id"), equalTo(placeId));
		assertThat("Location should exist", getJs.get("location"), notNullValue());
		assertThat("Name should exist", getJs.get("name"), notNullValue());
		
		logger.info("Place Retrieved Successfully - Place ID: " + placeId);
		logger.debug("Retrieved Place Details: " + responseBody);
	}
	  
	@Test(priority = 3, description = "Test deleting a place via DELETE API")
	public void deletePlace() {
		logger.info("deletePlace - Test execution started");
		  
		// First, add a place to delete
		Response addResponse = service.addPlace();
		JsonPath addJs = new JsonPath(addResponse.asString());
		
		addResponse.then()
			.assertThat()
			.statusCode(200)
			.body("status", equalTo("OK"))
			.body("scope", equalTo("APP"));
		
		String placeId = addJs.get("place_id");
		logger.info("Place created for deletion - Place ID: " + placeId);
		
		// Verify the place exists before deletion
		Response getResponse = service.getPlace(placeId);
		String getResponseBody = getResponse.asString();
		logger.info("Get Place Response (before delete): " + getResponseBody);
		logger.info("Get Place Status Code (before delete): " + getResponse.getStatusCode());
		
		getResponse.then()
			.assertThat()
			.statusCode(200);
		
		// Try to parse JSON - if it fails, log the actual response
		JsonPath getJs;
		try {
			getJs = new JsonPath(getResponseBody);
		} catch (Exception e) {
			logger.error("Failed to parse JSON response before delete. Response was: " + getResponseBody);
			throw new AssertionError("Response is not valid JSON. Response: " + getResponseBody, e);
		}
		
		assertThat("Place should exist before deletion", getJs.get("place_id"), equalTo(placeId));
		
		// Delete the place
		Response deleteResponse = service.deletePlace(placeId);
		
		// Validate HTTP status code and response
		deleteResponse.then()
			.assertThat()
			.statusCode(200)
			.body("status", equalTo("OK"));
		
		JsonPath deleteJs = new JsonPath(deleteResponse.asString());
		logger.info("Place Deleted Successfully - Status: " + deleteJs.get("status"));
		
		// Verify the place is actually deleted (optional - depends on API behavior)
		// Note: Some APIs may return 404, others may return 200 with different status
		logger.debug("Delete Response: " + deleteResponse.asString());
	}
	
	@Test(priority = 4, description = "Test complete workflow: Add -> Get -> Delete")
	public void testCompleteWorkflow() {
		logger.info("testCompleteWorkflow - Test execution started");
		
		// Step 1: Add a place
		Response addResponse = service.addPlace();
		addResponse.then()
			.assertThat()
			.statusCode(200)
			.body("status", equalTo("OK"));
		
		JsonPath addJs = new JsonPath(addResponse.asString());
		String placeId = addJs.get("place_id");
		logger.info("Step 1: Place added - Place ID: " + placeId);
		
		// Step 2: Get the place
		Response getResponse = service.getPlace(placeId);
		String getResponseBody = getResponse.asString();
		logger.info("Get Place Response (workflow): " + getResponseBody);
		logger.info("Get Place Status Code (workflow): " + getResponse.getStatusCode());
		
		getResponse.then()
			.assertThat()
			.statusCode(200);
		
		// Try to parse JSON - if it fails, log the actual response
		JsonPath getJs;
		try {
			getJs = new JsonPath(getResponseBody);
		} catch (Exception e) {
			logger.error("Failed to parse JSON response in workflow. Response was: " + getResponseBody);
			throw new AssertionError("Response is not valid JSON. Response: " + getResponseBody, e);
		}
		
		assertThat("Place ID should match", getJs.get("place_id"), equalTo(placeId));
		logger.info("Step 2: Place retrieved successfully");
		
		// Step 3: Delete the place
		Response deleteResponse = service.deletePlace(placeId);
		deleteResponse.then()
			.assertThat()
			.statusCode(200)
			.body("status", equalTo("OK"));
		
		logger.info("Step 3: Place deleted successfully");
		logger.info("testCompleteWorkflow - All steps completed successfully");
	}

	@AfterMethod
	public void afterMethod() {
		logger.info("afterMethod - Executes after each test method");
		// Optional: Cleanup created place if test failed
		// This is a best practice for test data management
	}

	@AfterClass
	public void afterClass() {
		logger.info("afterClass - Executes once after all test methods in the class");
	}

	@AfterTest
	public void afterTest() {
		logger.info("afterTest - Executes after all test classes");
	}

	@AfterSuite
	public void afterSuite() {
		logger.info("afterSuite - Executes once after all tests in the suite");
	}
}
