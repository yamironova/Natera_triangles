package api;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


import static config.EnvConfig.*;
import static config.EnvConfig.AUTH_VALUE;
import static io.restassured.RestAssured.given;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;

// send post request instead get request
public class WrongTypeRequests {

    private static final Logger logger = LogManager.getLogger(WrongTypeRequests.class.getName());

    // set base url and token
    private static final RequestSpecification requestSpec = new RequestSpecBuilder()
            .setBaseUri(URL_BASE)
            .setBasePath(URL_TRIANGLE).addHeader(AUTH_KEY, AUTH_VALUE)
            .build();

    // post request instead get return 405
    public static String postInsteadGet(String urlPath) {

        ValidatableResponse response = given().
                spec(requestSpec).
                when().
                post(urlPath).
                then().
                statusCode(405).
                contentType(ContentType.JSON).
                assertThat().
                body(matchesJsonSchemaInClasspath("method-not-supported.json"));

        logger.info( "POST request to " + urlPath +
                " has code response " + response.extract().statusCode() +
                " and response body " + response.extract().body().asString());

        return response.
                extract().
                jsonPath().
                getString("message");

    }
}

