package requests;

import helper.TriangleToJSON;
import models.Triangle;
import api.ValidRequests;
import io.restassured.http.ContentType;
import io.restassured.response.ValidatableResponse;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;


public class PostTriangleRequest {

    // valid post with separator request
    public static Triangle postOne(Triangle triangle, String separator) {

        String requestBody = TriangleToJSON.generateJSONForTriangle(triangle, separator);

        ValidatableResponse response = ValidRequests.post(requestBody).
                statusCode(200).
                contentType(ContentType.JSON).
                assertThat().
                body(matchesJsonSchemaInClasspath("after-post-triangle-schema.json"));

        return response.
                extract().
                body().
                as(Triangle.class);

    }

    // valid post without separator request
    public static Triangle postOne(Triangle triangle) {

        String requestBody = TriangleToJSON.generateJSONForTriangle(triangle);

        ValidatableResponse response = ValidRequests.post(requestBody).
                statusCode(200).
                contentType(ContentType.JSON).
                assertThat().body(matchesJsonSchemaInClasspath("after-post-triangle-schema.json"));

        return response.
                extract().
                body().
                as(Triangle.class);

    }

    // post request with Unprocessable Entity (422)
    public static String postUnprocessableEntity(String requestBody) {

        ValidatableResponse response = ValidRequests.post(requestBody).
                statusCode(422).
                contentType(ContentType.JSON).
                assertThat().
                body(matchesJsonSchemaInClasspath("unprocessable-entity-schema.json"));

        return response.
                extract().
                jsonPath().
                getString("message");

    }

    // post request with wrong JSON (400)
    public static String postBadRequest(String requestBody) {

        ValidatableResponse response = ValidRequests.post(requestBody).
                statusCode(400).
                contentType(ContentType.JSON).
                assertThat().
                body(matchesJsonSchemaInClasspath("bad-request-schema.json"));

        return response.
                extract().
                jsonPath().
                getString("message");

    }
}