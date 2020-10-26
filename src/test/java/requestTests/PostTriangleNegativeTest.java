package requestTests;

import helper.DataBaseActions;
import helper.TriangleToJSON;
import io.qameta.allure.Description;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Step;
import models.Triangle;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import requests.PostTriangleRequest;
import utility.LogTestsExtension;

import static config.EnvConfig.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(LogTestsExtension.class)
public class PostTriangleNegativeTest {

    @BeforeEach
    // Remove one triangle if list is full
    public void onceExecuteBeforeEach() {
        DataBaseActions.removeOneTriangleIfFull();
    }

    //              NEGATIVE TESTS    (expected 422)


    @Test   //TODO: discuss expected result when all sides is zero
    @Severity(SeverityLevel.NORMAL)
    @Description("Post request when three sides are zero - expect 422")
    public void threeZeroSidesTest () {

        // create JSON request body
        Triangle triangle = new Triangle(DEFAULT_TRIANGLE_ID, (double) 0,(double) 0,(double) 0);
        String requestBody = TriangleToJSON.generateJSONForTriangle(triangle);

        // main check
        String message = PostTriangleRequest.postUnprocessableEntity(requestBody);
        assertEquals(UNPROCESSABLE_ENTITY_MESSAGE, message);
    }

    @Step("Post request when one side is zero return 422")
    public static void postOneZeroSide(Triangle triangleOneSideZero) {
        String requestBody = TriangleToJSON.generateJSONForTriangle(triangleOneSideZero);
        String message = PostTriangleRequest.postUnprocessableEntity(requestBody);
        assertEquals(UNPROCESSABLE_ENTITY_MESSAGE, message);
    }

    @Test  //TODO: discuss expected result when one sides is zero
    @Severity(SeverityLevel.NORMAL)
    @Description("Post request when one side is zero - expect 422 (3 options)")
    public void oneZeroSideTest () {

        // create 3 triangle with first, second and third side iz zero, respectively
        Triangle[] triangles = new Triangle[3];
        for (int i = 0; i < 3; i++) {
            triangles[i] = Triangle.getEquilateralTriangle();
        }
        triangles[0].setFirstSide(0.0);
        triangles[1].setSecondSide(0.0);
        triangles[2].setThirdSide(0.0);


        // main check
        for (int i = 0; i < 3; i++) {
            postOneZeroSide(triangles[i]);
        }
    }

    @Step("Post request when one side is equal to another sides sum return 422")
    public static void postTwoSidesSumEqualThird(Triangle triangleTwoSidesSumEqualThird) {
        String requestBody = TriangleToJSON.generateJSONForTriangle(triangleTwoSidesSumEqualThird);
        String message = PostTriangleRequest.postUnprocessableEntity(requestBody);
        assertEquals(UNPROCESSABLE_ENTITY_MESSAGE, message);
    }

    @Test //TODO: discuss expected result when firstSide+secondSide equal third side
    @Severity(SeverityLevel.NORMAL)
    @Description("Post request when 'firstSide+secondSide = third side' expect 422 (3 options)")
    public void twoSidesSumEqualThirdTest () {

        // create three triangles
        Triangle[] triangles = {Triangle.getRandomTriangle(), Triangle.getRandomTriangle(), Triangle.getRandomTriangle()};
        // first side = sum of other
        triangles[0].setFirstSide(triangles[0].getSecondSide()+triangles[0].getThirdSide());
        // second side = sum of other
        triangles[1].setSecondSide(triangles[1].getFirstSide()+triangles[1].getThirdSide());
        // third side = sum of other
        triangles[2].setThirdSide(triangles[2].getFirstSide()+triangles[2].getSecondSide());

        // main check
        for (int i = 0; i < 3; i++) {
            postTwoSidesSumEqualThird(triangles[i]);
        }
    }

    @Step("Post request when firstSide+secondSide less than third side - expect 422")
    public static void postTwoSidesSumLessThird(Triangle triangletwoSidesSumLessThird) {
        String requestBody = TriangleToJSON.generateJSONForTriangle(triangletwoSidesSumLessThird);
        String message = PostTriangleRequest.postUnprocessableEntity(requestBody);
        assertEquals(UNPROCESSABLE_ENTITY_MESSAGE, message);
    }

    @Test
    @Severity(SeverityLevel.CRITICAL)
    @Description("Post request when firstSide+secondSide less than third side - expect 422 (3 options)")
    public void twoSidesSumLessThirdTest () {

        // create three triangles
        Triangle[] triangles = {Triangle.getRandomTriangle(), Triangle.getRandomTriangle(), Triangle.getRandomTriangle()};
        // first side = sum of other + delta
        triangles[0].setFirstSide(triangles[0].getSecondSide()+triangles[0].getThirdSide()+DELTA_FOR_SIDE*MAX_RANDOM_TRIANGLE_SIDE);
        // second side = sum of other  + delta
        triangles[1].setSecondSide(triangles[1].getFirstSide()+triangles[1].getThirdSide()+DELTA_FOR_SIDE*MAX_RANDOM_TRIANGLE_SIDE);
        // third side = sum of other  + delta
        triangles[2].setThirdSide(triangles[2].getFirstSide()+triangles[2].getSecondSide()+DELTA_FOR_SIDE*MAX_RANDOM_TRIANGLE_SIDE);

        // main check
        for (int i = 0; i < 3; i++) {
            postTwoSidesSumLessThird(triangles[i]);
        }
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("Post one more triangle than possible - expect 422")
    public void moreThanMaxNumberOfTrianglesTest () {

        // post more triangles
        DataBaseActions.fillAllTrianglesDeFacto();

        // main check
        String message = PostTriangleRequest.postUnprocessableEntity(
                TriangleToJSON.generateJSONForTriangle(
                        Triangle.getDefaultTriangle()
                )
        );
        assertEquals(LIMIT_EXCEEDED_MESSAGE, message);
    }

    @Test //TODO: discuss expected result with separator "."
    @Severity(SeverityLevel.NORMAL)
    @Description("Post request with separator \".\" expect 422")
    public void dotAsSeparatorTest () {

        // create JSON request body
        Triangle triangle = Triangle.getDefaultTriangle();
        String separator = ".";
        String requestBody = TriangleToJSON.generateJSONForTriangle(triangle, separator);

        // main check
        String message = PostTriangleRequest.postUnprocessableEntity(requestBody);
        assertEquals(UNPROCESSABLE_ENTITY_MESSAGE, message);
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("Post request with wrong separator at request (example {\"separator\": \",\", \"input\": \"3;4;5\"} ) expect 422")
    public void wrongSeparatorTest () {

        // create JSON request body
        JSONObject issueData = new JSONObject();
        Triangle triangle = Triangle.getDefaultTriangle();
        String separator = ",";
        String wrongSeparator = ";";
        String sides = triangle.getFirstSide() + wrongSeparator + triangle.getSecondSide() +  wrongSeparator + triangle.getThirdSide();

        issueData.put("separator", separator);
        issueData.put("input", sides );

        String requestBody = issueData.toString();

        // main check
        String message = PostTriangleRequest.postUnprocessableEntity(requestBody);
        assertEquals(UNPROCESSABLE_ENTITY_MESSAGE, message);
    }

    @Test //TODO: discuss expected result when sides are negative
    @Severity(SeverityLevel.NORMAL)
    @Description("Post request with negative numbers at sides (example {\"separator\": \",\", \"input\": \"-3.0;-4.0;-5.0\"} ) expect 422")
    public void negativeSidesTest () {

        // create JSON request body
        Triangle triangle = Triangle.getDefaultTriangle();
        triangle.setFirstSide(triangle.getFirstSide()*(-1.0));
        triangle.setSecondSide(triangle.getSecondSide()*(-1.0));
        triangle.setThirdSide(triangle.getThirdSide()*(-1.0));
        String requestBody = TriangleToJSON.generateJSONForTriangle(triangle);

        // main check
        String message = PostTriangleRequest.postUnprocessableEntity(requestBody);
        assertEquals(UNPROCESSABLE_ENTITY_MESSAGE, message);
    }

    @Step("Post request with string at sides - expect 422")
    public static void postStringInsteadNumber(String side, String separator) {

        // create JSON request body
        JSONObject issueData = new JSONObject();
        String sides = side + separator + side +  separator + side;

        issueData.put("separator", separator);
        issueData.put("input", sides );
        String requestBody = issueData.toString();

        String message = PostTriangleRequest.postUnprocessableEntity(requestBody);
        assertEquals(UNPROCESSABLE_ENTITY_MESSAGE, message);

    }

    @Step("Post request with string at sides (default separator - expect 422")
    public static void postStringInsteadNumber(String side) {

        // create JSON request body
        JSONObject issueData = new JSONObject();
        String separator = ";";
        String sides = side + separator + side +  separator + side;

        issueData.put("input", sides );
        String requestBody = issueData.toString();

        String message = PostTriangleRequest.postUnprocessableEntity(requestBody);
        assertEquals(UNPROCESSABLE_ENTITY_MESSAGE, message);

    }

    @Test
    @Severity(SeverityLevel.MINOR)
    @Description("Post request with one letter (or string or mixed digits&char) were used instead number at sides - expect 422 (4 options)")
    public void stringInsteadNumberTest () {

        //main check
        postStringInsteadNumber(WRONG_TEXT_IN_SIDES);
        postStringInsteadNumber(WRONG_MIXED_IN_SIDES);
        postStringInsteadNumber(WRONG_TEXT_IN_SIDES, ";");
        postStringInsteadNumber(WRONG_MIXED_IN_SIDES,";");

    }

    @Test //TODO: discuss expected result when there are more than two default separators
    @Severity(SeverityLevel.MINOR)
    @Description("Post request with more than two default separators - 4 sides instead 3 (example {\"input\": \"3.0;4.0;5.0;6.0\"} ) - expect 422")
    public void moreThanTwoDefaultSeparatorTest () {

        // create JSON request body
        JSONObject issueData = new JSONObject();
        Triangle triangle = Triangle.getDefaultTriangle();
        String separator = ";";
        String sides = triangle.getFirstSide() +
                separator + triangle.getSecondSide() +
                separator + triangle.getThirdSide() +
                separator + (triangle.getThirdSide()+1.0);

        issueData.put("input", sides );

        String requestBody = issueData.toString();

        // main check
        String message = PostTriangleRequest.postUnprocessableEntity(requestBody);
        assertEquals(UNPROCESSABLE_ENTITY_MESSAGE, message);
    }

    @Test //TODO: discuss expected result when there are more than two separators
    @Severity(SeverityLevel.MINOR)
    @Description("Post request with more than two user separators - 4 sides instead 3 (example {\"separator\": \";\", \"input\": \"3.0;4.0;5.0;6.0\"} ) - expect 422")
    public void moreThanTwoUserSeparatorTest () {

        // create JSON request body
        JSONObject issueData = new JSONObject();
        Triangle triangle = Triangle.getDefaultTriangle();
        String separator = ";";
        String sides = triangle.getFirstSide() +
                separator + triangle.getSecondSide() +
                separator + triangle.getThirdSide() +
                separator + (triangle.getThirdSide()+1.0);

        issueData.put("separator", separator);
        issueData.put("input", sides );

        String requestBody = issueData.toString();

        // main check
        String message = PostTriangleRequest.postUnprocessableEntity(requestBody);
        assertEquals(UNPROCESSABLE_ENTITY_MESSAGE, message);
    }

    @Test //TODO: discuss expected result when JSON format has some additional fields
    @Severity(SeverityLevel.MINOR)
    @Description("Post request with some additional fields and user separator (example {\"separator\": \";\", \"input\": \"3;4.0;5.0\", \"test\": \"test\"}) expect 422")
    public void moreFieldsAtJSONUserSeparatorTest() {

        // create JSON request body
        JSONObject issueData = new JSONObject();
        Triangle triangle = Triangle.getDefaultTriangle();
        String separator = ",";
        String sides = triangle.getFirstSide() +
                separator + triangle.getSecondSide() +
                separator + triangle.getThirdSide();

        issueData.put("separator", separator);
        issueData.put("input", sides );
        issueData.put("WRONG_TEXT", WRONG_TEXT_IN_SIDES);

        String requestBody = issueData.toString();

        // main check
        String message = PostTriangleRequest.postUnprocessableEntity(requestBody);
        assertEquals(UNPROCESSABLE_ENTITY_MESSAGE, message);
    }

    @Test //TODO: discuss expected result when JSON format has some additional fields
    @Severity(SeverityLevel.MINOR)
    @Description("Post request with some additional fields with default separator (example {\"input\": \"3;4.0;5.0\", \"test\": \"test\"}) expect 422")
    public void moreFieldsAtJSONDefaultSeparatorTest() {

        // create JSON request body
        JSONObject issueData = new JSONObject();
        Triangle triangle = Triangle.getDefaultTriangle();
        String separator = ";";
        String sides = triangle.getFirstSide() +
                separator + triangle.getSecondSide() +
                separator + triangle.getThirdSide();

        issueData.put("input", sides );
        issueData.put("WRONG_TEXT", WRONG_TEXT_IN_SIDES);

        String requestBody = issueData.toString();

        // main check
        String message = PostTriangleRequest.postUnprocessableEntity(requestBody);
        assertEquals(UNPROCESSABLE_ENTITY_MESSAGE, message);
    }

    @Test
    @Severity(SeverityLevel.MINOR)
    @Description("Post request with wrong JSON format (example {\"input\":\"3;4;5}) expect 400")
    public void wrongJSONTest() {

        // main check
        String message = PostTriangleRequest.postBadRequest(WRONG_REQUEST_STRING);
        assertTrue(message.contains(BAD_REQUEST_MESSAGE));
    }
}
