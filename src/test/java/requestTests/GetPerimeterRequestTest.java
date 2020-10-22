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
import requests.GetOneRequest;
import requests.GetPerimeterRequest;
import requests.PostTriangleRequest;
import utility.LogTestsExtension;

import static config.EnvConfig.NOT_FOUND_MESSAGE;
import static config.EnvConfig.WRONG_TRIANGLE_ID;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(LogTestsExtension.class)
public class GetPerimeterRequestTest {

    @BeforeEach
    // Remove one triangle if list is full
    public void onceExecuteBeforeEach() {
        DataBaseActions.removeOneTriangleIfFull();
    }

    @Test
    @Severity(SeverityLevel.CRITICAL)
    @Description("Smoke: default triangle at perimeter request.")
    public void getPerimeterDefaultTest () {

        // post default triangle

        Triangle defaultTriangle = Triangle.getDefaultTriangle();
        Triangle responseTriangle = PostTriangleRequest.postOne(defaultTriangle);

        // check it was posted one time
        Triangle checkTriangle = GetOneRequest.getOne(responseTriangle.getId());
        defaultTriangle.setId(checkTriangle.getId());
        assertEquals(defaultTriangle, checkTriangle);

        // main check
        Double responsePerimeter = GetPerimeterRequest.getPerimeter(responseTriangle.getId());
        Double calculatedPerimeter = Triangle.calculateTrianglePerimeter(defaultTriangle);

        assertEquals(calculatedPerimeter, responsePerimeter);

        // clean after test
        DeleteRequest.deleteTriangle(responseTriangle.getId());

        // clean check one time
        String message = GetOneRequest.getOneAbsent(responseTriangle.getId());
        assertEquals(NOT_FOUND_MESSAGE, message);
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("Random sides triangle check at perimeter request.")
    public void getPerimeterRandomTest () {

        // post random triangle
        Triangle randomTriangle = Triangle.getRandomTriangle();
        Triangle responseTriangle = PostTriangleRequest.postOne(randomTriangle);

        // main check
        Double responsePerimeter = GetPerimeterRequest.getPerimeter(responseTriangle.getId());
        Double calculatedPerimeter = Triangle.calculateTrianglePerimeter(randomTriangle);

        assertEquals(calculatedPerimeter, responsePerimeter);


        // clean after test
        DeleteRequest.deleteTriangle(responseTriangle.getId());

    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("Rectangular triangle (example: 3;4;5) at perimeter request.")
    public void getPerimeterRectangularTest () {

        // post rectangular triangle
        Triangle rectangularTriangle = Triangle.getRectangularTriangle();
        Triangle responseTriangle = PostTriangleRequest.postOne(rectangularTriangle);

        // main check
        Double responsePerimeter = GetPerimeterRequest.getPerimeter(responseTriangle.getId());
        Double calculatedPerimeter = Triangle.calculateTrianglePerimeter(rectangularTriangle);

        assertEquals(calculatedPerimeter, responsePerimeter);

        // clean after test
        DeleteRequest.deleteTriangle(responseTriangle.getId());

    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("Acute-angled triangle (example: 10;11;12) at perimeter request.")
    public void getPerimeterAcuteAngledTest () {

        // post acute-angled triangle
        Triangle acuteAngledTriangle = Triangle.getAcuteAngledTriangle();
        Triangle responseTriangle = PostTriangleRequest.postOne(acuteAngledTriangle);

        // main check
        Double responsePerimeter = GetPerimeterRequest.getPerimeter(responseTriangle.getId());
        Double calculatedPerimeter = Triangle.calculateTrianglePerimeter(acuteAngledTriangle);

        assertEquals(calculatedPerimeter, responsePerimeter);

        // clean after test
        DeleteRequest.deleteTriangle(responseTriangle.getId());

    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("Obtuse triangle (example: 3;4;6) at perimeter request.")
    public void getPerimeterObtuseTest () {

        // post obtuse triangle
        Triangle obtuseTriangle = Triangle.getObtuseTriangle();
        Triangle responseTriangle = PostTriangleRequest.postOne(obtuseTriangle);

        // main check
        Double responsePerimeter = GetPerimeterRequest.getPerimeter(responseTriangle.getId());
        Double calculatedPerimeter = Triangle.calculateTrianglePerimeter(obtuseTriangle);

        assertEquals(calculatedPerimeter, responsePerimeter);

        // clean after test
        DeleteRequest.deleteTriangle(responseTriangle.getId());

    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("Isosceles triangle (example: 3;3;4) at perimeter request.")
    public void getPerimeterIsoscelesTest () {

        // post isosceles triangle
        Triangle isoscelesTriangle = Triangle.getIsoscelesTriangle();
        Triangle responseTriangle = PostTriangleRequest.postOne(isoscelesTriangle);

        // main check
        Double responsePerimeter = GetPerimeterRequest.getPerimeter(responseTriangle.getId());
        Double calculatedPerimeter = Triangle.calculateTrianglePerimeter(isoscelesTriangle);

        assertEquals(calculatedPerimeter, responsePerimeter);

        // clean after test
        DeleteRequest.deleteTriangle(responseTriangle.getId());

    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("Equilateral triangle (example: 3;3;3) at perimeter request.")
    public void getPerimeterEquilateralTest () {

        // post equilateral triangle
        Triangle equilateralTriangle = Triangle.getEquilateralTriangle();
        Triangle responseTriangle = PostTriangleRequest.postOne(equilateralTriangle);

        // main check
        Double responsePerimeter = GetPerimeterRequest.getPerimeter(responseTriangle.getId());
        Double calculatedPerimeter = Triangle.calculateTrianglePerimeter(equilateralTriangle);

        assertEquals(calculatedPerimeter, responsePerimeter);

        // clean after test
        DeleteRequest.deleteTriangle(responseTriangle.getId());

    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("Random big triangle at perimeter request.")
    public void getPerimeterBigTriangleTest () {

        // post big triangle
        Triangle randomTriangle = Triangle.getBigRandomTriangle();
        Triangle responseTriangle = PostTriangleRequest.postOne(randomTriangle);

        // main check
        Double responsePerimeter = GetPerimeterRequest.getPerimeter(responseTriangle.getId());
        Double calculatedPerimeter = Triangle.calculateTrianglePerimeter(randomTriangle);

        assertEquals(calculatedPerimeter, responsePerimeter);

        // clean after test
        DeleteRequest.deleteTriangle(responseTriangle.getId());

    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("Random small triangle at perimeter request.")
    public void getPerimeterSmallTriangleTest () {

        // post small triangle
        Triangle randomTriangle = Triangle.getSmallRandomTriangle();
        Triangle responseTriangle = PostTriangleRequest.postOne(randomTriangle);

        // main check
        Double responsePerimeter = GetPerimeterRequest.getPerimeter(responseTriangle.getId());
        Double calculatedPerimeter = Triangle.calculateTrianglePerimeter(randomTriangle);

        assertEquals(calculatedPerimeter, responsePerimeter);

        // clean after test
        DeleteRequest.deleteTriangle(responseTriangle.getId());

    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("Get perimeter request for one deleted triangle return 404")
    public void getPerimeterDeletedTriangleTest() {

        // post default triangle
        Triangle defaultTriangle = Triangle.getDefaultTriangle();
        Triangle responseTriangle = PostTriangleRequest.postOne(defaultTriangle);

        // check was posted
        GetOneRequest.getOne(responseTriangle.getId());

        // delete triangle
        DeleteRequest.deleteTriangle(responseTriangle.getId());

        // main check
        String message = GetPerimeterRequest.getOneAbsent(responseTriangle.getId());
        assertEquals(NOT_FOUND_MESSAGE, message);

    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("Get perimeter request for triangle with wrong ID return 404")
    public void getPerimeterAbsentTriangleTest() {

        // main check
        String message = GetPerimeterRequest.getOneAbsent(WRONG_TRIANGLE_ID);
        assertEquals(NOT_FOUND_MESSAGE, message);

    }
}
