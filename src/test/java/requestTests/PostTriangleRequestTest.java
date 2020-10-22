package requestTests;

import api.ValidRequests;
import helper.DataBaseActions;
import io.qameta.allure.Description;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.restassured.http.ContentType;
import models.Triangle;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import requests.DeleteRequest;
import requests.GetAllRequest;
import requests.GetOneRequest;
import requests.PostTriangleRequest;
import utility.LogTestsExtension;

import java.util.Arrays;

import static config.EnvConfig.*;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(LogTestsExtension.class)
public class PostTriangleRequestTest {

    @BeforeEach
    // Remove one triangle if list is full
    public void onceExecuteBeforeEach() {
        DataBaseActions.removeOneTriangleIfFull();
    }

    @Test
    @Severity(SeverityLevel.CRITICAL)
    @Description("Default check 'post one triangle'.")
    public void postDefaultTriangleTest() {

        // main check
        Triangle defaultTriangle = Triangle.getDefaultTriangle();

        Triangle responseTriangle = PostTriangleRequest.postOne(defaultTriangle);
        defaultTriangle.setId(responseTriangle.getId());

        assertEquals(defaultTriangle, responseTriangle);

        // clean after test
        DeleteRequest.deleteTriangle(responseTriangle.getId());

        // clean check at one test
        String message = GetOneRequest.getOneAbsent(responseTriangle.getId());
        assertEquals(NOT_FOUND_MESSAGE, message);

    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("Check that not a single triangle was lost or has the same id after 'post triangle'.")
    public void postDatabaseAfterPostTest () {

        // Fill all, remove one
        DataBaseActions.fillAllTriangles();
        Triangle[] triangleArrayFull = GetAllRequest.getAll();
        DeleteRequest.deleteTriangle(triangleArrayFull[0].getId());

        // save current array
        Triangle[] triangleArrayOld = GetAllRequest.getAll();

        // post default triangle
        Triangle defaultTriangle = Triangle.getDefaultTriangle();
        Triangle responseTriangle = PostTriangleRequest.postOne(defaultTriangle);
        defaultTriangle.setId(responseTriangle.getId());

        // create expected array
        Triangle[] triangleArrayOldWithDefault = new Triangle[(triangleArrayOld.length+1)];
        triangleArrayOldWithDefault[0] = defaultTriangle; // first triangle is new one
        // add all other triangle from old array
        int i = 1;
        while (i <= triangleArrayOld.length) {
            triangleArrayOldWithDefault[i] = triangleArrayOld[(i-1)];
            i++;
        }

        // receive new array and sort both (sorting by id was set at models.Triangle.compareTo)
        Triangle[] triangleArrayNew = GetAllRequest.getAll();
        Arrays.sort(triangleArrayOldWithDefault);
        Arrays.sort(triangleArrayNew);

        // main check new list of triangle = list before adding + added triangle
        assertArrayEquals(triangleArrayOldWithDefault, triangleArrayNew);

        // main check no one has the same id
        i = 0;
        while (i < (triangleArrayNew.length - 1 )) {
            assertNotEquals(triangleArrayNew[i].getId(),triangleArrayNew[i+1].getId());
            i++;
        }

        // clean after test
        DeleteRequest.deleteTriangle(responseTriangle.getId());

    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("Post triangle with random sides check")
    public void postRandomTriangleTest () {

        // main check
        Triangle randomTriangle = Triangle.getRandomTriangle();

        Triangle responseTriangle = PostTriangleRequest.postOne(randomTriangle);
        randomTriangle.setId(responseTriangle.getId());

        assertEquals(randomTriangle, responseTriangle);

        // clean after test
        DeleteRequest.deleteTriangle(responseTriangle.getId());

    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("Maximum numbers of triangles is equal to 10 (or set at config.EnvConfig.MAX_TRIANGLE_AMOUNT)")
    public void maxNumberOfTrianglesCheckTest () {

        // post more triangles
        DataBaseActions.fillAllTrianglesDeFacto();

        // main check
        int actualMaxNumber = GetAllRequest.getAll().length;
        assertEquals(MAX_TRIANGLE_QUANTITY, actualMaxNumber);
    }

    @Test
    @Severity(SeverityLevel.MINOR)
    @Description("Post triangle with sides having one digit after dot (example {\"input\": \"3.0;4.0;5.0\"} ).")
    public void postRandomTrianglePrecision1Test () {

        // main check
        Triangle randomTriangle = Triangle.getRandomTriangle(1);

        Triangle responseTriangle = PostTriangleRequest.postOne(randomTriangle);
        randomTriangle.setId(responseTriangle.getId());

        assertEquals(randomTriangle, responseTriangle);

        // clean after test
        DeleteRequest.deleteTriangle(responseTriangle.getId());

    }

    @Test
    @Severity(SeverityLevel.MINOR)
    @Description("Post triangle with integer sides (example {\"input\": \"3;4;5\"} ).")
    public void postRandomTrianglePrecision0Test () {

        // create JSON
        JSONObject issueData = new JSONObject();
        Triangle intTriangle = Triangle.getRandomTriangle(0);

        String sides = intTriangle.getFirstSide() +  ";" + intTriangle.getSecondSide() +  ";" + intTriangle.getThirdSide();

        issueData.put("input", sides );

        // main check
        Triangle responseTriangle = ValidRequests.post(issueData.toString()).
                statusCode(200).
                contentType(ContentType.JSON).
                assertThat().
                body(matchesJsonSchemaInClasspath("after-post-triangle-schema.json")).extract().
                body().
                as(Triangle.class);

        intTriangle.setId(responseTriangle.getId());

        assertEquals(intTriangle, responseTriangle);

        // clean after test
        DeleteRequest.deleteTriangle(intTriangle.getId());

    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("Post rectangular triangle (example: 3;4;5)")
    public void postRectangularTriangleTest () {

        // main check
        Triangle rectangularTriangle = Triangle.getRectangularTriangle();

        Triangle responseTriangle = PostTriangleRequest.postOne(rectangularTriangle);
        rectangularTriangle.setId(responseTriangle.getId());

        assertEquals(rectangularTriangle, responseTriangle);

        // clean after test
        DeleteRequest.deleteTriangle(responseTriangle.getId());

    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("Post acute-angled triangle (example: 10;11;12).")
    public void postAcuteAngledTriangleTest () {

        // main check
        Triangle acuteAngledTriangle = Triangle.getAcuteAngledTriangle();

        Triangle responseTriangle = PostTriangleRequest.postOne(acuteAngledTriangle);
        acuteAngledTriangle.setId(responseTriangle.getId());

        assertEquals(acuteAngledTriangle, responseTriangle);

        // clean after test
        DeleteRequest.deleteTriangle(responseTriangle.getId());

    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("Post obtuse triangle (example: 3;4;6).")
    public void postObtuseTriangleTest () {

        // main check
        Triangle obtuseTriangle = Triangle.getObtuseTriangle();

        Triangle responseTriangle = PostTriangleRequest.postOne(obtuseTriangle);
        obtuseTriangle.setId(responseTriangle.getId());

        assertEquals(obtuseTriangle, responseTriangle);

        // clean after test
        DeleteRequest.deleteTriangle(responseTriangle.getId());

    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("Post isosceles triangle (example: 3;3;4).")
    public void postIsoscelesTriangleTest () {

        // main check
        Triangle isoscelesTriangle = Triangle.getIsoscelesTriangle();

        Triangle responseTriangle = PostTriangleRequest.postOne(isoscelesTriangle);
        isoscelesTriangle.setId(responseTriangle.getId());

        assertEquals(isoscelesTriangle, responseTriangle);

        // clean after test
        DeleteRequest.deleteTriangle(responseTriangle.getId());

    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("Post equilateral triangle (example: 3;3;3).")
    public void postEquilateralTriangleTest () {

        // main check
        Triangle equilateralTriangle = Triangle.getEquilateralTriangle();

        Triangle responseTriangle = PostTriangleRequest.postOne(equilateralTriangle);
        equilateralTriangle.setId(responseTriangle.getId());

        assertEquals(equilateralTriangle, responseTriangle);

        // clean after test
        DeleteRequest.deleteTriangle(responseTriangle.getId());

    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("Post random big triangle")
    public void postBigTriangleTest () {

        // main check
        Triangle randomTriangle = Triangle.getBigRandomTriangle();

        Triangle responseTriangle = PostTriangleRequest.postOne(randomTriangle);
        randomTriangle.setId(responseTriangle.getId());

        assertEquals(randomTriangle, responseTriangle);

        // clean after test
        DeleteRequest.deleteTriangle(responseTriangle.getId());

    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("Post random small triangle")
    public void postSmallTriangleTest () {

        // main check
        Triangle randomTriangle = Triangle.getSmallRandomTriangle();

        Triangle responseTriangle = PostTriangleRequest.postOne(randomTriangle);
        randomTriangle.setId(responseTriangle.getId());

        assertEquals(randomTriangle, responseTriangle);

        // clean after test
        DeleteRequest.deleteTriangle(responseTriangle.getId());

    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("Post same triangle twice and check it receive diff id.)")
    public void postSameTriangleTwiceTest () {

        //remove all old triangle
        DataBaseActions.clearAllTriangles();

        // create two triangles
        Triangle[] sampleTwoTriangle = new Triangle[2];
        Triangle[] responseTriangles = new Triangle[2];
        sampleTwoTriangle[0] = Triangle.getRandomTriangle();
        sampleTwoTriangle[1] = new Triangle(sampleTwoTriangle[0].getId(),
                sampleTwoTriangle[0].getFirstSide(),
                sampleTwoTriangle[0].getSecondSide(),
                sampleTwoTriangle[0].getThirdSide());


        // post first triangle
        responseTriangles[0] = PostTriangleRequest.postOne(sampleTwoTriangle[0]);
        sampleTwoTriangle[0].setId(responseTriangles[0].getId());

        // post second triangle
        responseTriangles[1] = PostTriangleRequest.postOne(sampleTwoTriangle[1]);
        sampleTwoTriangle[1].setId(responseTriangles[1].getId());

        // main check
        assertNotEquals(responseTriangles[0].getId(),responseTriangles[1].getId());

        Arrays.sort(sampleTwoTriangle);
        Arrays.sort(responseTriangles);

        assertArrayEquals(sampleTwoTriangle, responseTriangles);

        // clean after test
        DeleteRequest.deleteTriangle(responseTriangles[0].getId());
        DeleteRequest.deleteTriangle(responseTriangles[1].getId());
    }

    @Test
    @Severity(SeverityLevel.CRITICAL)
    @Description("Post request with user separator \";\"")
    public void postUserSeparatorTest () {

        // main check
        Triangle randomTriangle = Triangle.getRandomTriangle();

        Triangle responseTriangle = PostTriangleRequest.postOne(randomTriangle,";");
        randomTriangle.setId(responseTriangle.getId());

        assertEquals(randomTriangle, responseTriangle);

        // clean after test
        DeleteRequest.deleteTriangle(responseTriangle.getId());
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("Post request with user separator \",\"")
    public void postCommaSeparatorTest () {

        // main check
        Triangle randomTriangle = Triangle.getRandomTriangle();

        Triangle responseTriangle = PostTriangleRequest.postOne(randomTriangle,",");
        randomTriangle.setId(responseTriangle.getId());

        assertEquals(randomTriangle, responseTriangle);

        // clean after test
        DeleteRequest.deleteTriangle(responseTriangle.getId());

    }

    @Test
    @Severity(SeverityLevel.MINOR)
    @Description("Post request with user string separator.")
    public void postStringSeparatorTest () {

        // main check
        Triangle randomTriangle = Triangle.getRandomTriangle();

        Triangle responseTriangle = PostTriangleRequest.postOne(randomTriangle,USER_TEXT_SEPARATOR);
        randomTriangle.setId(responseTriangle.getId());

        assertEquals(randomTriangle, responseTriangle);

        // clean after test
        DeleteRequest.deleteTriangle(responseTriangle.getId());
    }

    @Test
    @Severity(SeverityLevel.MINOR)
    @Description("Post request with user space separator")
    public void postSpaceSeparatorTest () {

        // main check
        Triangle randomTriangle = Triangle.getRandomTriangle();

        Triangle responseTriangle = PostTriangleRequest.postOne(randomTriangle," ");
        randomTriangle.setId(responseTriangle.getId());

        assertEquals(randomTriangle, responseTriangle);

        // clean after test
        DeleteRequest.deleteTriangle(responseTriangle.getId());
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("Post request with more than 16 digits at side")
    public void manyDigitsSideTest () {

        // create JSON request body
        JSONObject issueData = new JSONObject();
        String separator = ";";
        String sides = MANY_DIGITS_NUMBER +
                separator + MANY_DIGITS_NUMBER +
                separator + MANY_DIGITS_NUMBER;

        issueData.put("separator", separator);
        issueData.put("input", sides );

        String requestBody = issueData.toString();

        //create triangle
        Triangle longTriangle = new Triangle(
                DEFAULT_TRIANGLE_ID,
                Double.parseDouble(MANY_DIGITS_NUMBER),
                Double.parseDouble(MANY_DIGITS_NUMBER),
                Double.parseDouble(MANY_DIGITS_NUMBER));

        // main check
        Triangle responseTriangle = ValidRequests.post(requestBody).
                statusCode(200).
                contentType(ContentType.JSON).
                assertThat().
                body(matchesJsonSchemaInClasspath("after-post-triangle-schema.json")).
                extract().
                body().
                as(Triangle.class);

        longTriangle.setId(responseTriangle.getId());

        assertEquals(longTriangle, responseTriangle);
    }
}
