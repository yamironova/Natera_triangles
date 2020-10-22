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

import static config.EnvConfig.NOT_FOUND_MESSAGE;
import static config.EnvConfig.WRONG_TRIANGLE_ID;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(LogTestsExtension.class)
public class DeleteRequestTest {

    @Test
    @Severity(SeverityLevel.CRITICAL)
    @Description("Delete default last added triangle.")
    public void deleteDefaultTest () {

        // post default triangle
        DataBaseActions.removeOneTriangleIfFull();
        Triangle defaultTriangle = Triangle.getDefaultTriangle();
        Triangle responseTriangle = PostTriangleRequest.postOne(defaultTriangle);

        // check it was posted
        Triangle checkTriangle = GetOneRequest.getOne(responseTriangle.getId());
        defaultTriangle.setId(checkTriangle.getId());
        assertEquals(defaultTriangle, checkTriangle);

        //main test
        DeleteRequest.deleteTriangle(responseTriangle.getId());
        String message = GetOneRequest.getOneAbsent(responseTriangle.getId());
        assertEquals(NOT_FOUND_MESSAGE, message);
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("Delete first added triangle")
    public void deleteFirstAddedTest () {

        DataBaseActions.clearAllTriangles();

        // post one default triangle
        Triangle defaultTriangle = Triangle.getDefaultTriangle();
        Triangle responseTriangle = PostTriangleRequest.postOne(defaultTriangle);

        // post more triangles
        DataBaseActions.fillAllTriangles();

        //main test
        DeleteRequest.deleteTriangle(responseTriangle.getId());
        String message = GetOneRequest.getOneAbsent(responseTriangle.getId());
        assertEquals(NOT_FOUND_MESSAGE, message);
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("Delete random triangle at the list and check that no one more was deleted.")
    public void deleteRandomByJSONTest () {

        // post more triangles
        DataBaseActions.fillAllTriangles();

        // get random
        Triangle[] triangleArray = GetAllRequest.getAll();
        int i = (int) Math.round(Math.random()*(triangleArray.length-1));

        //main test
        DeleteRequest.deleteTriangle(triangleArray[i].getId());
        String message = GetOneRequest.getOneAbsent(triangleArray[i].getId());
        assertEquals(NOT_FOUND_MESSAGE, message);
    }

    @Test
    @Severity(SeverityLevel.MINOR)
    @Description("Delete first (at json response) triangle")
    public void deleteFirstByJSONTest () {

        // post more triangles
        DataBaseActions.fillAllTriangles();

        // get first
        Triangle[] triangleArray = GetAllRequest.getAll();

        //main test
        DeleteRequest.deleteTriangle(triangleArray[0].getId());
        String message = GetOneRequest.getOneAbsent(triangleArray[0].getId());
        assertEquals(NOT_FOUND_MESSAGE, message);
    }

    @Test
    @Severity(SeverityLevel.MINOR)
    @Description("Delete last (at json response) triangle")
    public void deleteLastByJSONTest () {

        // post more triangles
        DataBaseActions.fillAllTriangles();

        // get last
        Triangle[] triangleArray = GetAllRequest.getAll();
        int lastId = triangleArray.length-1;

        //main test
        DeleteRequest.deleteTriangle(triangleArray[lastId].getId());
        String message = GetOneRequest.getOneAbsent(triangleArray[lastId].getId());
        assertEquals(NOT_FOUND_MESSAGE, message);
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("Delete all triangles at database.")
    public void deleteAllTest () {

        // post more triangles
        DataBaseActions.fillAllTriangles();

        //main test
        DataBaseActions.clearAllTriangles();
        Triangle[] triangleArrayNew = GetAllRequest.getAll();
        assertEquals(0, triangleArrayNew.length);
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("Delete request for deleted triangle return 404.")
    public void deleteDeletedTest () {

        // post default triangle
        DataBaseActions.removeOneTriangleIfFull();
        Triangle defaultTriangle = Triangle.getDefaultTriangle();
        Triangle responseTriangle = PostTriangleRequest.postOne(defaultTriangle);

        //delete it
        DeleteRequest.deleteTriangle(responseTriangle.getId());

        //main check
        String message = DeleteRequest.deleteOneAbsent(responseTriangle.getId());
        assertEquals(NOT_FOUND_MESSAGE, message);
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("Delete request for triangle with wrong ID return 404.")
    public void deleteWrongIdTest () {

        //main check
        String message = DeleteRequest.deleteOneAbsent(WRONG_TRIANGLE_ID);
        assertEquals(NOT_FOUND_MESSAGE, message);
    }
}
