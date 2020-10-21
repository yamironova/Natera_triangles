package helper;

import api.ValidRequests;
import models.Triangle;
import requests.DeleteRequest;
import requests.GetAllRequest;
import requests.PostTriangleRequest;

import static config.EnvConfig.*;

public class DataBaseActions {

    // Remove one triangle if list is full
    public static void removeOneTriangleIfFull () {
        if (CHECK_OVERFLOW_MODE_ON) {  // QA can OFF this option at config.EnvConfig for saving time

            Triangle[] triangleArrayReceived = GetAllRequest.getAll();
            if (triangleArrayReceived.length > (MAX_TRIANGLE_QUANTITY - 1)) {
                DeleteRequest.deleteTriangle(triangleArrayReceived[0].getId());
            }
        }
    }

    // Fill all database according to documentation (post EvnConfig.MAX_TRIANGLE_QUANTITY number of triangles)
    public static void fillAllTriangles () {
        Triangle[] triangleArray = GetAllRequest.getAll();
        int i = triangleArray.length;
        while (i < MAX_TRIANGLE_QUANTITY) {
            PostTriangleRequest.postOne(Triangle.getRandomTriangle());
            i++;
        }
    }

    // Fill all database de facto
    public static void fillAllTrianglesDeFacto () {
        String requestBody = TriangleToJSON.generateJSONForTriangle(Triangle.getDefaultTriangle());
        int code = 200;
        int i = 0;
        while ((code == 200) & (i< MAX_TRIANGLE_POST_ATTEMPTS)) {
            code = ValidRequests.post(requestBody).extract().statusCode();
            i++;
        }
    }

    // Clear all database
    public static void clearAllTriangles () {
        Triangle[] triangleArray = GetAllRequest.getAll();
        int i = triangleArray.length-1;
        while (i >= 0) {
            DeleteRequest.deleteTriangle(triangleArray[i].getId());
            i--;
        }
    }
}