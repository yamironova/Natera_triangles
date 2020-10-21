package api;

import config.WrongOrAbsentTokenConfig;
import io.restassured.http.ContentType;
import io.restassured.response.ValidatableResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static io.restassured.RestAssured.given;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;

public class WrongOrAbsentTokenRequests {

    private static final Logger logger = LogManager.getLogger(WrongOrAbsentTokenRequests.class.getName());

    // get request with wrong token
    public static ValidatableResponse getWithWrongToken(String urlPath) {

        ValidatableResponse response = given().
                spec(WrongOrAbsentTokenConfig.getWrongTokenRequestSpec()).
                when().
                get(urlPath).
                then().
                statusCode(401).
                contentType(ContentType.JSON).
                assertThat().
                body(matchesJsonSchemaInClasspath("no-token-schema.json"));

        logger.info( "GET request to " + urlPath +
                " has code response " + response.extract().statusCode() +
                " and response body " + response.extract().body().asString());

        return response;
    }

    // post request with wrong token
    public static ValidatableResponse postWithWrongToken(String requestBody) {

        ValidatableResponse response = given().
                spec(WrongOrAbsentTokenConfig.getWrongTokenRequestSpec()).
                header("Content-Type", ContentType.JSON).
                body(requestBody).
                when().
                post().
                then().
                statusCode(401).
                contentType(ContentType.JSON).
                assertThat().
                body(matchesJsonSchemaInClasspath("no-token-schema.json"));

        logger.info( "POST request with body " + requestBody +
                " has code response " + response.extract().statusCode() +
                " and response body " + response.extract().body().asString());

        return response;
    }

    // delete request with wrong token
    public static ValidatableResponse deleteWithWrongToken(String urlPath) {

        ValidatableResponse response = given().
                spec(WrongOrAbsentTokenConfig.getWrongTokenRequestSpec()).
                when().
                delete(urlPath).
                then().
                statusCode(401).
                contentType(ContentType.JSON).
                assertThat().
                body(matchesJsonSchemaInClasspath("no-token-schema.json"));

        logger.info( "DELETE request to " + urlPath +
                " has code response " + response.extract().statusCode() +
                " has code response " + response.extract().body().asString());


        return response;
    }

    // get request without token
    public static ValidatableResponse getNoToken(String urlPath) {

        ValidatableResponse response = given().
                spec(WrongOrAbsentTokenConfig.getNoTokenRequestSpec()).
                when().
                get(urlPath).
                then().
                statusCode(401).
                contentType(ContentType.JSON).
                assertThat().
                body(matchesJsonSchemaInClasspath("no-token-schema.json"));

        logger.info( "GET request to " + urlPath +
                " has code response " + response.extract().statusCode() +
                " and response body " + response.extract().body().asString());

        return response;
    }

    // post request without token
    public static ValidatableResponse postNoToken(String requestBody) {

        ValidatableResponse response = given().
                spec(WrongOrAbsentTokenConfig.getNoTokenRequestSpec()).
                header("Content-Type", ContentType.JSON).
                body(requestBody).
                when().
                post().
                then().
                statusCode(401).
                contentType(ContentType.JSON).
                assertThat().
                body(matchesJsonSchemaInClasspath("no-token-schema.json"));

        logger.info( "POST request with body " + requestBody +
                " has code response " + response.extract().statusCode() +
                " and response body " + response.extract().body().asString());

        return response;
    }

    // delete request without token
    public static ValidatableResponse deleteNoToken(String urlPath) {

        ValidatableResponse response = given().
                spec(WrongOrAbsentTokenConfig.getNoTokenRequestSpec()).
                when().
                delete(urlPath).
                then().
                statusCode(401).
                contentType(ContentType.JSON).
                assertThat().
                body(matchesJsonSchemaInClasspath("no-token-schema.json"));

        logger.info( "DELETE request to " + urlPath +
                " has code response " + response.extract().statusCode() +
                " has code response " + response.extract().body().asString());


        return response;
    }
}

