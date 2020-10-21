package api;

import static config.EnvConfig.*;
import static io.restassured.RestAssured.given;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ValidRequests {

    private static final Logger logger = LogManager.getLogger(ValidRequests.class.getName());

    // set base url and token
    private static final RequestSpecification requestSpec = new RequestSpecBuilder()
            .setBaseUri(URL_BASE)
            .setBasePath(URL_TRIANGLE).addHeader(AUTH_KEY, AUTH_VALUE)
            .build();

    // simple valid get request
    public static ValidatableResponse get(String urlPath) {

        ValidatableResponse response = given().
                spec(requestSpec).
                when().
                get(urlPath).
                then();

        logger.info( "GET request to " + urlPath +
                " has code response " + response.extract().statusCode() +
                " and response body " + response.extract().body().asString());
        return response;

    }
    // simple valid post request
    public static ValidatableResponse post(String requestBody) {

        ValidatableResponse response = given().spec(requestSpec).
                header("Content-Type", ContentType.JSON).
                body(requestBody).
                when().
                post().
                then();

        logger.info( "POST request with body " + requestBody +
                " has code response " + response.extract().statusCode() +
                " and response body " + response.extract().body().asString());

        return response;
    }

    // simple valid delete request
    public static ValidatableResponse delete(String urlPath) {

        ValidatableResponse response = given().
                spec(requestSpec).
                when().
                delete(urlPath).
                then();

        logger.info( "DELETE request to " + urlPath +
                " has code response " + response.extract().statusCode() +
                " has code response " + response.extract().body().asString());


        return response;
    }

}
