package requestTests;

import api.ValidRequests;
import helper.DataBaseActions;
import io.qameta.allure.Description;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.restassured.http.ContentType;
import io.restassured.response.ValidatableResponse;
import models.Triangle;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import requests.DeleteRequest;
import requests.GetAreaRequest;
import requests.GetOneRequest;
import requests.PostTriangleRequest;
import utility.LogTestsExtension;

import static config.EnvConfig.*;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(LogTestsExtension.class)
public class GetAreaRequestTest {

    @BeforeEach
    // Remove one triangle if list is full
    public void onceExecuteBeforeAll() {
        DataBaseActions.removeOneTriangleIfFull();
    }

    @Test
    @Severity(SeverityLevel.CRITICAL)
    @Description("Smoke: default triangle at area request.")
    public void getAreaDefaultTest () {

        // post default triangle

        Triangle defaultTriangle = Triangle.getDefaultTriangle();
        Triangle responseTriangle = PostTriangleRequest.postOne(defaultTriangle);

        // check it was posted one time
        Triangle checkTriangle = GetOneRequest.getOne(responseTriangle.getId());
        defaultTriangle.setId(checkTriangle.getId());
        assertEquals(defaultTriangle, checkTriangle);

        // main check
        Double responseArea = GetAreaRequest.getArea(responseTriangle.getId());
        Double calculatedArea = Triangle.calculateTriangleArea(defaultTriangle);

        assertEquals(calculatedArea, responseArea);

        // clean after test
        DeleteRequest.deleteTriangle(responseTriangle.getId());

        // clean check one time
        String message = GetOneRequest.getOneAbsent(responseTriangle.getId());
        assertEquals(NOT_FOUND_MESSAGE, message);

    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("Random sides triangle check at area request.")
    public void getAreaRandomTest () {

        // post random triangle
        Triangle randomTriangle = Triangle.getRandomTriangle();
        Triangle responseTriangle = PostTriangleRequest.postOne(randomTriangle);

        // main check
        Double responseArea = GetAreaRequest.getArea(responseTriangle.getId());
        Double calculatedArea = Triangle.calculateTriangleArea(randomTriangle);

        assertEquals(calculatedArea, responseArea);

        // clean after test
        DeleteRequest.deleteTriangle(responseTriangle.getId());

    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("Rectangular triangle (example: 3;4;5) at area request.")
    public void getAreaRectangularTest () {

        // post rectangular triangle
        Triangle rectangularTriangle = Triangle.getRectangularTriangle();
        Triangle responseTriangle = PostTriangleRequest.postOne(rectangularTriangle);

        // main check
        Double responseArea = GetAreaRequest.getArea(responseTriangle.getId());
        Double calculatedArea = Triangle.calculateTriangleArea(rectangularTriangle);

        assertEquals(calculatedArea, responseArea);

        // clean after test
        DeleteRequest.deleteTriangle(responseTriangle.getId());

    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("Acute-angled triangle (example: 10;11;12) at area request.")
    public void getAreaAcuteAngledTest () {

        // post acute-angled triangle
        Triangle acuteAngledTriangle = Triangle.getAcuteAngledTriangle();
        Triangle responseTriangle = PostTriangleRequest.postOne(acuteAngledTriangle);

        // main check
        Double responseArea = GetAreaRequest.getArea(responseTriangle.getId());
        Double calculatedArea = Triangle.calculateTriangleArea(acuteAngledTriangle);

        assertEquals(calculatedArea, responseArea);

        // clean after test
        DeleteRequest.deleteTriangle(responseTriangle.getId());

    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("Obtuse triangle (example: 3;4;6) at area request.")
    public void getAreaObtuseTest () {

        // post obtuse triangle
        Triangle obtuseTriangle = Triangle.getObtuseTriangle();
        Triangle responseTriangle = PostTriangleRequest.postOne(obtuseTriangle);

        // main check
        Double responseArea = GetAreaRequest.getArea(responseTriangle.getId());
        Double calculatedArea = Triangle.calculateTriangleArea(obtuseTriangle);

        assertEquals(calculatedArea, responseArea);

        // clean after test
        DeleteRequest.deleteTriangle(responseTriangle.getId());
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("Isosceles triangle (example: 3;3;4) at area request.")
    public void getIsoscelesDefaultTest () {

        // post isosceles triangle
        Triangle isoscelesTriangle = Triangle.getIsoscelesTriangle();
        Triangle responseTriangle = PostTriangleRequest.postOne(isoscelesTriangle);

        // main check
        Double responseArea = GetAreaRequest.getArea(responseTriangle.getId());
        Double calculatedArea = Triangle.calculateTriangleArea(isoscelesTriangle);

        assertEquals(calculatedArea, responseArea);

        // clean after test
        DeleteRequest.deleteTriangle(responseTriangle.getId());

    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("Equilateral triangle (example: 3;3;3) at area request.")
    public void getAreaEquilateralTest () {

        // post equilateral triangle
        Triangle equilateralTriangle = Triangle.getEquilateralTriangle();
        Triangle responseTriangle = PostTriangleRequest.postOne(equilateralTriangle);

        // main check
        Double responseArea = GetAreaRequest.getArea(responseTriangle.getId());
        Double calculatedArea = Triangle.calculateTriangleArea(equilateralTriangle);

        assertEquals(calculatedArea, responseArea);

        // clean after test
        DeleteRequest.deleteTriangle(responseTriangle.getId());

    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("Random big triangle at area request.")
    public void getAreaBigTriangleTest () {

        // post big triangle
        Triangle randomTriangle = Triangle.getBigRandomTriangle();
        Triangle responseTriangle = PostTriangleRequest.postOne(randomTriangle);

        // main check
        Double responseArea = GetAreaRequest.getArea(responseTriangle.getId());
        Double calculatedArea = Triangle.calculateTriangleArea(randomTriangle);

        assertEquals(calculatedArea, responseArea);

        // clean after test
        DeleteRequest.deleteTriangle(responseTriangle.getId());

    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("Really big triangle at area request return \"infinity\".")
    public void getInfinityAreaBigTriangleTest () {

        // post big triangle
        Triangle randomTriangle = Triangle.getRandomTriangle();
        Triangle veryBigTriangle = new Triangle(
                DEFAULT_TRIANGLE_ID,
                randomTriangle.getFirstSide()*MAX_RANDOM_VERY_BIG_TRIANGLE_SIDE,
                randomTriangle.getSecondSide()*MAX_RANDOM_VERY_BIG_TRIANGLE_SIDE,
                randomTriangle.getThirdSide()*MAX_RANDOM_VERY_BIG_TRIANGLE_SIDE);
        Triangle responseTriangle = PostTriangleRequest.postOne(veryBigTriangle);

        // main check
        ValidatableResponse response = ValidRequests.get("/" + responseTriangle.getId() + "/" + "area").
                statusCode(200).
                contentType(ContentType.JSON).
                assertThat().
                body(matchesJsonSchemaInClasspath("area-schema.json"));

        String result =  response.
                extract().
                jsonPath().
                get("result");

        assertEquals(BIG_RESULT_FROM_RESPONSE, result);

        // clean after test
        DeleteRequest.deleteTriangle(responseTriangle.getId());

    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("Random small triangle at area request.")
    public void getAreaSmallTriangleTest () {

        // post small triangle
        Triangle randomTriangle = Triangle.getSmallRandomTriangle();

        Triangle responseTriangle = PostTriangleRequest.postOne(randomTriangle);
        randomTriangle.setId(responseTriangle.getId());

        // main check
        Double responseArea = GetAreaRequest.getArea(responseTriangle.getId());
        Double calculatedArea = Triangle.calculateTriangleArea(randomTriangle);

        assertEquals(calculatedArea, responseArea);

        // clean after test
        DeleteRequest.deleteTriangle(responseTriangle.getId());

    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("'Get area' request for one deleted triangle return 404")
    public void getAreaDeletedAreaTest () {

        // post default triangle
        Triangle defaultTriangle = Triangle.getDefaultTriangle();
        Triangle responseTriangle = PostTriangleRequest.postOne(defaultTriangle);

        // check was posted
        GetOneRequest.getOne(responseTriangle.getId());

        // delete triangle
        DeleteRequest.deleteTriangle(responseTriangle.getId());

        // main check
        String message = GetAreaRequest.getOneAbsent(responseTriangle.getId());
        assertEquals(NOT_FOUND_MESSAGE, message);
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("'Get area' request for triangle with wrong ID return 404")
    public void getAreaAbsentTriangleTest () {

        // main check
        String message = GetAreaRequest.getOneAbsent(WRONG_TRIANGLE_ID);
        assertEquals(NOT_FOUND_MESSAGE, message);
    }
}