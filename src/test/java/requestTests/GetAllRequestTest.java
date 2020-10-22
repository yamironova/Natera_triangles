package requestTests;

import helper.DataBaseActions;
import io.qameta.allure.Description;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import models.Triangle;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import requests.DeleteRequest;
import requests.GetAllRequest;
import requests.GetOneRequest;
import requests.PostTriangleRequest;
import utility.LogTestsExtension;

import java.util.Arrays;

import static config.EnvConfig.MAX_TRIANGLE_QUANTITY;
import static config.EnvConfig.NOT_FOUND_MESSAGE;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(LogTestsExtension.class)
public class GetAllRequestTest {


    @Test
    @Severity(SeverityLevel.CRITICAL)
    @Description("Default check 'get all triangles'. New list of triangle = list before adding + added triangle.")
    public void getAllDefaultTest () {

        // Remove one triangle if list is full
        DataBaseActions.removeOneTriangleIfFull();

        // save current array
        Triangle[] triangleArrayOld = GetAllRequest.getAll();

        // post default triangle
        Triangle defaultTriangle = Triangle.getDefaultTriangle();
        Triangle responseTriangle = PostTriangleRequest.postOne(defaultTriangle);
        defaultTriangle.setId(responseTriangle.getId());

        // create expected array
        Triangle[] triangleArrayOldWithDefault = new Triangle[(triangleArrayOld.length+1)];
        triangleArrayOldWithDefault[0] = defaultTriangle; // first triangle is new one
        // add all other triangle (if there is) from old array
        int i = 1;
        while (i <= triangleArrayOld.length) {
            triangleArrayOldWithDefault[i] = triangleArrayOld[(i-1)];
            i++;
        }

        // main check
        Triangle[] triangleArrayNew = GetAllRequest.getAll();
        Arrays.sort(triangleArrayOldWithDefault);
        Arrays.sort(triangleArrayNew);

        assertArrayEquals(triangleArrayOldWithDefault, triangleArrayNew);

        // clean after test
        DeleteRequest.deleteTriangle(responseTriangle.getId());

        // clean check one time
        String message = GetOneRequest.getOneAbsent(responseTriangle.getId());
        assertEquals(NOT_FOUND_MESSAGE, message);

    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("'Get all triangles' when list of triangles at database has maximum size.")
    public void getAllMaximumTest () {

        //remove all old triangle
        DataBaseActions.clearAllTriangles();

        // create sample array
        Triangle[] sampleTriangleArray = new Triangle[MAX_TRIANGLE_QUANTITY];
        for (int i = 0; i < MAX_TRIANGLE_QUANTITY; i++) {
            sampleTriangleArray[i] = Triangle.getRandomTriangle();
        }

        // post all triangles from sample
        for (int i = 0; i < MAX_TRIANGLE_QUANTITY; i++) {
            Triangle responseTriangle = PostTriangleRequest.postOne(sampleTriangleArray[i]);
            sampleTriangleArray[i].setId(responseTriangle.getId());
        }

        // main check
        Triangle[] triangleArrayNew = GetAllRequest.getAll();
        Arrays.sort(sampleTriangleArray);
        Arrays.sort(triangleArrayNew);

        assertArrayEquals(sampleTriangleArray, triangleArrayNew);

        // clean after test
        DeleteRequest.deleteTriangle(sampleTriangleArray[0].getId());

    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("'Get all triangles' when list of triangles at database has one or two triangles.")
    public void getAllWithOneAndTwoTest () {

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
        Triangle[] oneTriangleArrayNew = GetAllRequest.getAll();
        assertEquals(1, oneTriangleArrayNew.length );
        assertEquals(sampleTwoTriangle[0], oneTriangleArrayNew[0]);

        // post second triangle
        responseTriangle = PostTriangleRequest.postOne(sampleTwoTriangle[1]);
        sampleTwoTriangle[1].setId(responseTriangle.getId());

        // main check - two triangle
        Triangle[] twoTriangleArrayNew = GetAllRequest.getAll();
        Arrays.sort(sampleTwoTriangle);
        Arrays.sort(twoTriangleArrayNew);

        assertArrayEquals(sampleTwoTriangle, twoTriangleArrayNew);
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("'Get all triangles' when list of triangles at database is empty")
    public void getAllZeroTrianglesTest () {

        //remove all old triangle
        DataBaseActions.clearAllTriangles();

        // main check
        Triangle[] TriangleArrayEmpty = GetAllRequest.getAll();
        assertEquals(0, TriangleArrayEmpty.length);
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("'Get all triangles' when one triangle is big")
    public void getAllBigTriangleTest () {

        // Remove one triangle if list is full
        DataBaseActions.removeOneTriangleIfFull();

        // save current array
        Triangle[] triangleArrayOld = GetAllRequest.getAll();

        // post big triangle
        Triangle bigTriangle = Triangle.getBigRandomTriangle();
        Triangle responseTriangle = PostTriangleRequest.postOne(bigTriangle);
        bigTriangle.setId(responseTriangle.getId());

        // create expected array
        Triangle[] triangleArrayOldWithBig = new Triangle[(triangleArrayOld.length+1)];
        triangleArrayOldWithBig[0] = bigTriangle; // first triangle is new one
        // add all other triangle (if there is) from old array
        int i = 1;
        while (i <= triangleArrayOld.length) {
            triangleArrayOldWithBig[i] = triangleArrayOld[(i-1)];
            i++;
        }

        // main check
        Triangle[] triangleArrayNew = GetAllRequest.getAll();
        Arrays.sort(triangleArrayOldWithBig);
        Arrays.sort(triangleArrayNew);

        assertArrayEquals(triangleArrayOldWithBig, triangleArrayNew);

        // clean after test
        DeleteRequest.deleteTriangle(responseTriangle.getId());

    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("'Get all triangles' when one triangle is small")
    public void getAllSmallTriangleTest () {

        // Remove one triangle if list is full
        DataBaseActions.removeOneTriangleIfFull();

        // save current array
        Triangle[] triangleArrayOld = GetAllRequest.getAll();

        // post big triangle
        Triangle smallTriangle = Triangle.getSmallRandomTriangle();
        Triangle responseTriangle = PostTriangleRequest.postOne(smallTriangle);
        smallTriangle.setId(responseTriangle.getId());

        // create expected array
        Triangle[] triangleArrayOldWithSmall = new Triangle[(triangleArrayOld.length+1)];
        triangleArrayOldWithSmall [0] = smallTriangle; // first triangle is new one
        // add all other triangle (if there is) from old array
        int i = 1;
        while (i <= triangleArrayOld.length) {
            triangleArrayOldWithSmall [i] = triangleArrayOld[(i-1)];
            i++;
        }

        // main check
        Triangle[] triangleArrayNew = GetAllRequest.getAll();
        Arrays.sort(triangleArrayOldWithSmall);
        Arrays.sort(triangleArrayNew);

        assertArrayEquals(triangleArrayOldWithSmall, triangleArrayNew);

        // clean after test
        DeleteRequest.deleteTriangle(responseTriangle.getId());

    }
}
