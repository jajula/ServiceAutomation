package com.venkata.serviceAutomation;
import io.restassured.response.Response;
import static io.restassured.RestAssured.*;


public class RahulShettyService {
    
    public RahulShettyService() {
        
    }
    
    public Response addPlace() {
        return given()
                .body(InputRequestData.addPlace())
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
                .body(InputRequestData.deletePlace(place))
                .when()
                .delete("/maps/api/place/delete/json");
    }
}
