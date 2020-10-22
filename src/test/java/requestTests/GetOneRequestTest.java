package requestTests;

import helper.DataBaseActions;
import io.qameta.allure.Description;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import models.Triangle;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import requests.DeleteRequest;
import requests.GetAllRequest;
import requests.GetOneRequest;
import requests.PostTriangleRequest;
import utility.LogTestsExtension;

import static config.EnvConfig.NOT_FOUND_MESSAGE;
import static config.EnvConfig.WRONG_TRIANGLE_ID;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(LogTestsExtension.class)
public class GetOneRequestTest {

    @BeforeEach
    // Remove one triangle if list is full
    public void onceExecuteBeforeAll() {
        DataBaseActions.removeOneTriangleIfFull();
    }

    @Test
    @Severity(SeverityLevel.CRITICAL)
    @Description("Default check 'get one triangle'. Add one and check that it is in the list.")
    public void getOneDefaultTest () {

        // post default triangle
        Triangle defaultTriangle = Triangle.getDefaultTriangle();
        Triangle responseTriangle = PostTriangleRequest.postOne(defaultTriangle);

        // main check
        Triangle checkTriangle = GetOneRequest.getOne(responseTriangle.getId());
        defaultTriangle.setId(responseTriangle.getId());
        assertEquals(defaultTriangle, checkTriangle);

        // clean after test
        DeleteRequest.deleteTriangle(responseTriangle.getId());

        // clean check one time
        String message = GetOneRequest.getOneAbsent(responseTriangle.getId());
        assertEquals(NOT_FOUND_MESSAGE, message);
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("Check 'get one triangle' when list of triangles at database has maximum size.")
    public void getOneMaximumTest () {

        //fill to maximum - 1
        DataBaseActions.fillAllTriangles();
        Triangle[] triangleArrayReceived = GetAllRequest.getAll();
        DeleteRequest.deleteTriangle(triangleArrayReceived[0].getId());

        // post random triangle
        Triangle randomTriangle = Triangle.getRandomTriangle();
        Triangle responseTriangle = PostTriangleRequest.postOne(randomTriangle);

        // main check
        Triangle checkTriangle = GetOneRequest.getOne(responseTriangle.getId());
        randomTriangle.setId(responseTriangle.getId());
        assertEquals(randomTriangle, checkTriangle);

        // clean after test
        DeleteRequest.deleteTriangle(responseTriangle.getId());

    }

    @Test
    @Severity(SeverityLevel.MINOR)
    @Description("Check 'get one triangle' when list of triangles at database has one or two triangles.")
    public void getOneWithOneAndTwoTest () {

        //remove all old triangle
        DataBaseActions.clearAllTriangles();

        // create two triangles
        Triangle[] sampleTwoTriangle = new Triangle[2];
        sampleTwoTriangle[0] = Triangle.getRandomTriangle();
        sampleTwoTriangle[1] = Triangle.getRandomTriangle();

        // post first triangle
        Triangle responseTriangle = PostTriangleRequest.postOne(sampleTwoTriangle[0]);
        sampleTwoTriangle[0].setId(responseTriangle.getId());

        // main check - one triangle
        Triangle checkTriangle = GetOneRequest.getOne(sampleTwoTriangle[0].getId());
        sampleTwoTriangle[0].setId(responseTriangle.getId());
        assertEquals(sampleTwoTriangle[0], checkTriangle);

        // post second triangle
        responseTriangle = PostTriangleRequest.postOne(sampleTwoTriangle[1]);
        sampleTwoTriangle[1].setId(responseTriangle.getId());

        // main check - two triangle
        // first
        checkTriangle = GetOneRequest.getOne(sampleTwoTriangle[0].getId());
        assertEquals(sampleTwoTriangle[0], checkTriangle);
        // second
        checkTriangle = GetOneRequest.getOne(sampleTwoTriangle[1].getId());
        assertEquals(sampleTwoTriangle[1], checkTriangle);

    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("Check 'get one triangle' when one triangle is big.")
    public void getOneBigTest () {

        // post big triangle
        Triangle bigTriangle = Triangle.getBigRandomTriangle();
        Triangle responseTriangle = PostTriangleRequest.postOne(bigTriangle);

        // main check
        Triangle checkTriangle = GetOneRequest.getOne(responseTriangle.getId());
        bigTriangle.setId(responseTriangle.getId());
        assertEquals(bigTriangle, checkTriangle);

        // clean after test
        DeleteRequest.deleteTriangle(responseTriangle.getId());

    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("Check 'get one triangle' when one triangle is small.")
    public void getOneSmallTest () {

        // post big triangle
        Triangle smallTriangle = Triangle.getSmallRandomTriangle();
        Triangle responseTriangle = PostTriangleRequest.postOne(smallTriangle);

        // main check
        Triangle checkTriangle = GetOneRequest.getOne(responseTriangle.getId());
        smallTriangle.setId(responseTriangle.getId());
        assertEquals(smallTriangle, checkTriangle);

        // clean after test
        DeleteRequest.deleteTriangle(responseTriangle.getId());

    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("Check 'get one triangle' when list is empty - return 404 (triangle id - last deleted).")
    public void getOneDeletedTriangleTest() {

        // post default triangle
        Triangle defaultTriangle = Triangle.getDefaultTriangle();
        Triangle responseTriangle = PostTriangleRequest.postOne(defaultTriangle);

        // check was posted
        GetOneRequest.getOne(responseTriangle.getId());

        // delete triangle
        DeleteRequest.deleteTriangle(responseTriangle.getId());

        // main check
        String message = GetOneRequest.getOneAbsent(responseTriangle.getId());
        assertEquals(NOT_FOUND_MESSAGE, message);
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("Check 'get one triangle' with wrong id return 404.")
    public void getOneAbsentTriangleTest() {

        // main check
        String message = GetOneRequest.getOneAbsent(WRONG_TRIANGLE_ID);
        assertEquals(NOT_FOUND_MESSAGE, message);
    }

}
