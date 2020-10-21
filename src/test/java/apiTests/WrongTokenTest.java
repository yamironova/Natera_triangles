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

import static config.EnvConfig.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(LogTestsExtension.class)
public class WrongTokenTest {

    @BeforeEach
    // Remove one triangle if list is full
    public void onceExecuteBeforeAll() {
        DataBaseActions.removeOneTriangleIfFull();
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("Get all request with wrong token. 401 is expected.")
    public void wrongTokenGetAllRequest() {

        String wrongTokenError =
                WrongOrAbsentTokenRequests.getWithWrongToken("/all").
                        extract().
                        jsonPath().
                        getString("error");

        assertEquals(WRONG_TOKEN_ERROR, wrongTokenError);
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("Get one/area/perimeter request with wrong token. 401 is expected.")
    public void wrongTokenGetSomethingRequest() {

        // post default triangle
        Triangle defaultTriangle = Triangle.getDefaultTriangle();
        Triangle responseTriangle = PostTriangleRequest.postOne(defaultTriangle);

        // check get one
        String wrongTokenError =
                WrongOrAbsentTokenRequests.getWithWrongToken("/" + responseTriangle.getId()).
                        extract().
                        jsonPath().
                        getString("error");

        assertEquals(WRONG_TOKEN_ERROR, wrongTokenError);

        // check get area
        String wrongTokenErrorArea =
                WrongOrAbsentTokenRequests.getWithWrongToken("/" + responseTriangle.getId() + "/" + "area").
                        extract().
                        jsonPath().
                        getString("error");

        assertEquals(WRONG_TOKEN_ERROR, wrongTokenErrorArea);

        // check get perimeter
        String wrongTokenErrorPerimeter =
                WrongOrAbsentTokenRequests.getWithWrongToken("/" + responseTriangle.getId() + "/" + "perimeter").
                        extract().
                        jsonPath().
                        getString("error");

        assertEquals(WRONG_TOKEN_ERROR, wrongTokenErrorPerimeter);

        // clean after test
        DeleteRequest.deleteTriangle(responseTriangle.getId());

    }

    @Test
    @Severity(SeverityLevel.CRITICAL)
    @Description("Post request with wrong token. 401 is expected.")
    public void wrongTokenPostRequest() {

        //generate json request body with default triangle
        String requestBody = TriangleToJSON.generateJSONForTriangle(Triangle.getDefaultTriangle());

        // main check
        String wrongTokenError =
                WrongOrAbsentTokenRequests.postWithWrongToken(requestBody).
                        extract().
                        jsonPath().
                        getString("error");

        assertEquals(WRONG_TOKEN_ERROR, wrongTokenError);
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("Delete request with wrong token. 401 is expected.")
    public void wrongTokenDeleteRequest() {

        // post one triangle
        Triangle defaultTriangle = Triangle.getDefaultTriangle();
        Triangle responseTriangle = PostTriangleRequest.postOne(defaultTriangle);

        // main check
        String wrongTokenError =
                WrongOrAbsentTokenRequests.deleteWithWrongToken("/" + responseTriangle.getId()).
                        extract().
                        jsonPath().
                        getString("error");

        assertEquals(WRONG_TOKEN_ERROR, wrongTokenError);

        // clean after test
        DeleteRequest.deleteTriangle(responseTriangle.getId());

    }
}