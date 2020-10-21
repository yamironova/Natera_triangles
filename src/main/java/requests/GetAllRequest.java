package requests;

import models.Triangle;
import api.ValidRequests;
import io.restassured.http.ContentType;
import io.restassured.response.ValidatableResponse;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;


public class GetAllRequest {


    public static Triangle[] getAll() {


        ValidatableResponse response = ValidRequests.get("/all").
                statusCode(200).
                contentType(ContentType.JSON).
                assertThat().
                body(matchesJsonSchemaInClasspath("one-triangle-schema.json"));

        return response.
                extract().
                body().
                as(Triangle[].class);


    }
}
