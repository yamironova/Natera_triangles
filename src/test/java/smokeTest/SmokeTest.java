package smokeTest;

import helper.DataBaseActions;
import io.qameta.allure.Description;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import models.Triangle;
import org.junit.jupiter.api.extension.ExtendWith;
import requests.*;
import utility.LogTestsExtension;

import java.io.IOException;
import java.util.Arrays;

import static config.EnvConfig.NOT_FOUND_MESSAGE;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(LogTestsExtension.class)
public class SmokeTest {

    @BeforeEach
    // Remove one triangle if list is full
    public void onceExecuteBeforeAll() {
        DataBaseActions.removeOneTriangleIfFull();
    }

    @Test
    @Severity(SeverityLevel.CRITICAL)
    @Description("Smoke test: Post, Get one triangle, Get All, Delete")
    public void smokeTest () throws IOException {

        // save current array
        Triangle[] triangleArrayOld = GetAllRequest.getAll();


        // post check
        Triangle defaultTriangle = Triangle.getDefaultTriangle();
        Triangle responseTriangle = PostTriangleRequest.postOne(defaultTriangle);
        defaultTriangle.setId(responseTriangle.getId());

        assertEquals(defaultTriangle, responseTriangle);

        // get one check
        Triangle checkTriangle = GetOneRequest.getOne(responseTriangle.getId());
        defaultTriangle.setId(responseTriangle.getId());

        assertEquals(defaultTriangle, checkTriangle);

        // area check
        Double responseArea = GetAreaRequest.getArea(responseTriangle.getId());
        Double calculatedArea = Triangle.calculateTriangleArea(defaultTriangle);

        assertEquals(responseArea, calculatedArea);

        // perimeter check
        Double responsePerimeter = GetPerimeterRequest.getPerimeter(responseTriangle.getId());
        Double calculatedPerimeter = Triangle.calculateTrianglePerimeter(defaultTriangle);

        assertEquals(responsePerimeter, calculatedPerimeter);

        // create expected array after post default
        Triangle[] triangleArrayOldWithDefault = new Triangle[(triangleArrayOld.length+1)];
        triangleArrayOldWithDefault[0] = defaultTriangle;

        System.arraycopy(triangleArrayOld, 0, triangleArrayOldWithDefault, 1, triangleArrayOld.length);

        // get all check
        Triangle[] triangleArrayNew = GetAllRequest.getAll();

        Arrays.sort(triangleArrayOldWithDefault);
        Arrays.sort(triangleArrayNew);

        assertArrayEquals(triangleArrayOldWithDefault, triangleArrayNew);

        // deleting check
        DeleteRequest.deleteTriangle(responseTriangle.getId());

        // clean check
        String message = GetOneRequest.getOneAbsent(responseTriangle.getId());
        assertEquals(message, NOT_FOUND_MESSAGE);

    }


}
