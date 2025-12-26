package com.venkata.serviceAutomation;

import io.restassured.response.Response;

import static io.restassured.RestAssured.*;
import io.restassured.specification.RequestSpecification;
import serviceAutomation.RequestData;

public class RahulShettyService {
    
    public RahulShettyService() {
        
    }
    
    public Response addPlace() {
        return given()
                .body(RequestData.addPlace())
                .when()
                .post("/maps/api/place/add/json");
    }
    
    public Response getPlace(String placeId) {
        return given()
                .queryParam("place_id", placeId)
                .when()
                .get("/maps/api/place/get/json");
    }
    
    public Response deletePlace(String place) {
        return given()
                .body(RequestData.deletePlace(place))
                .when()
                .delete("/maps/api/place/delete/json");
    }
}
