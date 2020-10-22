package apiTests;

import api.WrongOrAbsentTokenRequests;
import helper.DataBaseActions;
import helper.TriangleToJSON;
import io.qameta.allure.Description;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import models.Triangle;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import requests.DeleteRequest;
import requests.PostTriangleRequest;
import utility.LogTestsExtension;

import static config.EnvConfig.WRONG_TOKEN_ERROR;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(LogTestsExtension.class)
public class NoTokenTest {

    @BeforeEach
    // Remove one triangle if list is full
    public void onceExecuteBeforeEach() {
        DataBaseActions.removeOneTriangleIfFull();
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("Get all request without token. 401 is expected.")
    public void noTokenGetAllRequest() {

        String wrongTokenError =
                WrongOrAbsentTokenRequests.getNoToken("/all").
                        extract().
                        jsonPath().
                        getString("error");

        assertEquals(WRONG_TOKEN_ERROR, wrongTokenError);

    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("Get one/area/perimeter request without token. 401 is expected.")
    public void noTokenGetSomethingRequest() {

        // post default triangle
        Triangle defaultTriangle = Triangle.getDefaultTriangle();
        Triangle responseTriangle = PostTriangleRequest.postOne(defaultTriangle);

        // check get one
        String wrongTokenError =
                WrongOrAbsentTokenRequests.getNoToken("/" + responseTriangle.getId()).
                        extract().
                        jsonPath().
                        getString("error");

        assertEquals(WRONG_TOKEN_ERROR, wrongTokenError);

        // check get area
        String wrongTokenErrorArea =
                WrongOrAbsentTokenRequests.getNoToken("/" + responseTriangle.getId() + "/" + "area").
                        extract().
                        jsonPath().
                        getString("error");

        assertEquals(WRONG_TOKEN_ERROR, wrongTokenErrorArea);

        // check get perimeter
        String wrongTokenErrorPerimeter =
                WrongOrAbsentTokenRequests.getNoToken("/" + responseTriangle.getId() + "/" + "perimeter").
                        extract().
                        jsonPath().
                        getString("error");

        assertEquals(WRONG_TOKEN_ERROR, wrongTokenErrorPerimeter);

        // clean after test
        DeleteRequest.deleteTriangle(responseTriangle.getId());

    }

    @Test
    @Severity(SeverityLevel.CRITICAL)
    @Description("Post request without token. 401 is expected.")
    public void noTokenPostRequest() {

        //generate json request body with default triangle
        String requestBody = TriangleToJSON.generateJSONForTriangle(Triangle.getDefaultTriangle());

        // main check
        String wrongTokenError =
                WrongOrAbsentTokenRequests.postNoToken(requestBody).
                        extract().
                        jsonPath().
                        getString("error");

        assertEquals(WRONG_TOKEN_ERROR, wrongTokenError);
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("Delete request without token. 401 is expected.")
    public void noTokenDeleteRequest() {

        // post one triangle
        Triangle defaultTriangle = Triangle.getDefaultTriangle();
        Triangle responseTriangle = PostTriangleRequest.postOne(defaultTriangle);

        // main check
        String wrongTokenError =
                WrongOrAbsentTokenRequests.deleteNoToken("/" + responseTriangle.getId()).
                        extract().
                        jsonPath().
                        getString("error");

        assertEquals(WRONG_TOKEN_ERROR, wrongTokenError);

        // clean after test
        DeleteRequest.deleteTriangle(responseTriangle.getId());

    }
}