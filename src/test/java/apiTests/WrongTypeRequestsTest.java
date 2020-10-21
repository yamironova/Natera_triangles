package apiTests;

import api.WrongTypeRequests;
import helper.DataBaseActions;
import io.qameta.allure.Description;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import models.Triangle;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import requests.PostTriangleRequest;
import utility.LogTestsExtension;

import static config.EnvConfig.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(LogTestsExtension.class)
public class WrongTypeRequestsTest {

    @BeforeEach
    // Remove one triangle if list is full
    public void onceExecuteBeforeAll() {
        DataBaseActions.removeOneTriangleIfFull();
    }

    @Test
    @Severity(SeverityLevel.MINOR)
    @Description("Use 'post' instead 'get' at 'get all' request return 405")
    public void postInsteadGetAllTest() {

        // main check
        String message = WrongTypeRequests.postInsteadGet("/all");
        assertEquals(WRONG_METHOD_MESSAGE, message);
    }


    @Test
    @Severity(SeverityLevel.MINOR)
    @Description("Use 'post' instead 'get' at 'get one triangle', 'get area', 'get perimeter' requests return 405")
    public void postInsteadGetSomethingTest() {

        // get some triangle valid ID
        Triangle defaultTriangle = Triangle.getDefaultTriangle();
        Triangle responseTriangle = PostTriangleRequest.postOne(defaultTriangle);

        // post at "get one" requests return 405
        String messageOne = WrongTypeRequests.postInsteadGet("/" + responseTriangle.getId());
        assertEquals(WRONG_METHOD_MESSAGE, messageOne);

        // post at "get area" requests return 405
        String messageArea = WrongTypeRequests.postInsteadGet("/" + responseTriangle.getId() + "/" + "area");
        assertEquals(WRONG_METHOD_MESSAGE, messageArea);

        // post at "get perimeter" requests return 405
        String messagePerimeter = WrongTypeRequests.postInsteadGet("/" + responseTriangle.getId() + "/" + "perimeter");
        assertEquals(WRONG_METHOD_MESSAGE, messagePerimeter);
    }
}
