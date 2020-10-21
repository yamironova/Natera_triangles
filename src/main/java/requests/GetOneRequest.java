package requests;

import models.Triangle;
import api.ValidRequests;
import io.restassured.http.ContentType;
import io.restassured.response.ValidatableResponse;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;



public class GetOneRequest {

    // simple valid get one triangle request
    public static Triangle getOne(String triangleId) {

        ValidatableResponse response = ValidRequests.get("/" + triangleId).
                statusCode(200).
                contentType(ContentType.JSON).
                assertThat().
                body(matchesJsonSchemaInClasspath("one-triangle-schema.json"));

        return response.
                extract().
                body().
                as(Triangle.class);

    }

    // get for one absent triangle request return 404
    public static String getOneAbsent(String triangleId) {

        ValidatableResponse response = ValidRequests.get("/" + triangleId).
                statusCode(404).
                contentType(ContentType.JSON).
                assertThat().
                body(matchesJsonSchemaInClasspath("not-found-schema.json"));

        return response.
                extract().
                jsonPath().
                getString("message");

    }

}