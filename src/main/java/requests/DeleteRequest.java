package requests;

import api.ValidRequests;
import io.restassured.http.ContentType;
import io.restassured.response.ValidatableResponse;

import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;

public class DeleteRequest {

    // simple valid deleting one triangle request
    public static ValidatableResponse deleteTriangle(String triangleId) {

        ValidatableResponse response = ValidRequests.delete("/" + triangleId).
                statusCode(200);
        return response;

    }

    // delete for one absent triangle request return 404
    public static String deleteOneAbsent(String triangleId) {

        ValidatableResponse response = ValidRequests.delete("/" + triangleId).
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

