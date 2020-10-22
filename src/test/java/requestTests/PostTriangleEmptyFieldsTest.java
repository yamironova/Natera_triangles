package requestTests;

import helper.DataBaseActions;
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

import static config.EnvConfig.UNPROCESSABLE_ENTITY_RESPONSE;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(LogTestsExtension.class)
public class PostTriangleEmptyFieldsTest {

    @BeforeEach
    // Remove one triangle if list is full
    public void onceExecuteBeforeEach() {
        DataBaseActions.removeOneTriangleIfFull();
    }

    //               EMPTY FIELDS CHECK (expected 422)

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("Post request when 'input' is empty with user separator (example {\"separator\": \";\", \"input\": \"\"} ) expect 422")
    public void postInputEmptyWithSeparatorTest () {

        // create JSON request body
        JSONObject issueData = new JSONObject();
        String separator = ";";
        String sides = "";

        issueData.put("separator", separator);
        issueData.put("input", sides );

        String requestBody = issueData.toString();

        // main check
        String message = PostTriangleRequest.postUnprocessableEntity(requestBody);
        assertEquals(UNPROCESSABLE_ENTITY_RESPONSE, message);
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("Post request when 'input' is empty (without user separator) (example {\"input\": \"\"} ) expect 422")
    public void postInputEmptyDefaultSeparatorTest () {

        // create JSON request body
        JSONObject issueData = new JSONObject();
        String sides = "";
        issueData.put("input", sides );
        String requestBody = issueData.toString();

        // main check
        String message = PostTriangleRequest.postUnprocessableEntity(requestBody);
        assertEquals(UNPROCESSABLE_ENTITY_RESPONSE, message);
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("Post request when all fields are empty (example {} ) expect 422")
    public void postAllFieldsEmptyTest () {

        // create JSON request body
        JSONObject issueData = new JSONObject();
        String requestBody = issueData.toString();

        // main check
        String message = PostTriangleRequest.postUnprocessableEntity(requestBody);
        assertEquals(UNPROCESSABLE_ENTITY_RESPONSE, message);
    }


    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("Post request when first side is empty (with user separator) (example {\"separator\": \";\", \"input\": \";4;5\"}) expect 422")
    public void postFirstSideEmptyWithSeparatorTest () {

        // create JSON request body
        JSONObject issueData = new JSONObject();
        Triangle triangle = Triangle.getDefaultTriangle();
        String separator = ";";
        String sides = separator + triangle.getSecondSide() +  separator + triangle.getThirdSide();

        issueData.put("separator", separator);
        issueData.put("input", sides );

        String requestBody = issueData.toString();

        // main check
        String message = PostTriangleRequest.postUnprocessableEntity(requestBody);
        assertEquals(UNPROCESSABLE_ENTITY_RESPONSE, message);
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("Post request when second side is empty (with user separator) (example {\"separator\": \";\", \"input\": \"3;;5\"} ) expect 422")
    public void postSecondSideEmptyWithSeparatorTest () {

        // create JSON request body
        JSONObject issueData = new JSONObject();
        Triangle triangle = Triangle.getDefaultTriangle();
        String separator = ";";
        String sides = triangle.getFirstSide() +  separator +  separator + triangle.getThirdSide();

        issueData.put("separator", separator);
        issueData.put("input", sides );

        String requestBody = issueData.toString();

        // main check
        String message = PostTriangleRequest.postUnprocessableEntity(requestBody);
        assertEquals(UNPROCESSABLE_ENTITY_RESPONSE, message);
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("Post request when third is side empty (with user separator) (example {\"separator\": \";\", \"input\": \"3;4;\"} ) expect 422")
    public void postThirdSideEmptyWithSeparatorTest () {

        // create JSON request body
        JSONObject issueData = new JSONObject();
        Triangle triangle = Triangle.getDefaultTriangle();
        String separator = ";";
        String sides = triangle.getFirstSide() +  separator + triangle.getSecondSide() +  separator;

        issueData.put("separator", separator);
        issueData.put("input", sides );

        String requestBody = issueData.toString();

        // main check
        String message = PostTriangleRequest.postUnprocessableEntity(requestBody);
        assertEquals(UNPROCESSABLE_ENTITY_RESPONSE, message);
    }

    @Step("Post request when two side are empty (with user separator) return 422")
    public static void postTwoSidesEmptyWithSeparator(String sides) {
        JSONObject issueData = new JSONObject();
        issueData.put("input", sides);
        issueData.put("separator", ";");
        String requestBody = issueData.toString();
        String message = PostTriangleRequest.postUnprocessableEntity(requestBody);
        assertEquals(UNPROCESSABLE_ENTITY_RESPONSE, message);
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("Post request when two side are empty (with user separator) (example {\"separator\": \";\", \"input\": \";;5\"} ) expect 422 (3 options)")
    public void postTwoSidesEmptyWithSeparatorTest () {

        // create 3 JSON request body
        String[] sides = new String[3];
        Triangle triangle = Triangle.getDefaultTriangle();
        String separator = ";";

        sides[0] = separator +  separator + triangle.getThirdSide(); // example {"input": ";;5"}
        sides[1] = separator + triangle.getSecondSide() +  separator; // example {"input": ";4;"}
        sides[2] = triangle.getFirstSide() +  separator +  separator; // example {"input": "3;;"}

        //main check
        for (int i = 0; i < 3; i++) {
            postTwoSidesEmptyWithSeparator(sides[i]);
        }

    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("Post request when first side is empty (without user separator) (example {\"input\": \";4;5\"} ) expect 422")
    public void postFirstSideEmptyDefaultSeparatorTest () {

        // create JSON request body
        JSONObject issueData = new JSONObject();
        Triangle triangle = Triangle.getDefaultTriangle();
        String separator = ";";
        String sides = separator + triangle.getSecondSide() +  separator + triangle.getThirdSide();

        issueData.put("input", sides );
        String requestBody = issueData.toString();

        // main check
        String message = PostTriangleRequest.postUnprocessableEntity(requestBody);
        assertEquals(UNPROCESSABLE_ENTITY_RESPONSE, message);
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("Post request when second side is empty (without user separator) (example {\"input\": \"3;;5\"} ) expect 422")
    public void postSecondSideEmptyDefaultSeparatorTest () {

        // create JSON request body
        JSONObject issueData = new JSONObject();
        Triangle triangle = Triangle.getDefaultTriangle();
        String separator = ";";
        String sides = triangle.getFirstSide() +  separator +  separator + triangle.getThirdSide();

        issueData.put("input", sides );
        String requestBody = issueData.toString();

        // main check
        String message = PostTriangleRequest.postUnprocessableEntity(requestBody);
        assertEquals(UNPROCESSABLE_ENTITY_RESPONSE, message);
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("Post request when third side is empty (without user separator) (example {\"input\": \"3;4;\"} ) expect 422")
    public void postThirdSideEmptyDefaultSeparatorTest () {

        // create JSON request body
        JSONObject issueData = new JSONObject();
        Triangle triangle = Triangle.getDefaultTriangle();
        String separator = ";";
        String sides = triangle.getFirstSide() +  separator + triangle.getSecondSide() +  separator;

        issueData.put("input", sides );
        String requestBody = issueData.toString();

        // main check
        String message = PostTriangleRequest.postUnprocessableEntity(requestBody);
        assertEquals(UNPROCESSABLE_ENTITY_RESPONSE, message);
    }

    @Step("Post request when two side are empty (with default separator) return 422")
    public static void postTwoSidesEmptyWithDefaultSeparator(String sides) {
        JSONObject issueData = new JSONObject();
        issueData.put("input", sides);
        String requestBody = issueData.toString();
        String message = PostTriangleRequest.postUnprocessableEntity(requestBody);
        assertEquals(UNPROCESSABLE_ENTITY_RESPONSE, message);
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("Post request when two side are empty (with default separator) (example {\"input\": \";;5\"} ) expect 422 (3 options)")
    public void postTwoSidesEmptyDefaultSeparatorTest () {

        // create 3 JSON request body
        String[] sides = new String[3];
        Triangle triangle = Triangle.getDefaultTriangle();
        String separator = ";";

        sides[0] = separator +  separator + triangle.getThirdSide(); // example {"input": ";;5"}
        sides[1] = separator + triangle.getSecondSide() +  separator; // example {"input": ";4;"}
        sides[2] = triangle.getFirstSide() +  separator +  separator; // example {"input": "3;;"}

        //main check
        for (int i = 0; i < 3; i++) {
            postTwoSidesEmptyWithDefaultSeparator(sides[i]);
        }

    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("Post request without user separators between sides inside json (example {\"separator\": \";\", \"input\": \"345\"} ) expect 422")
    public void postNoSeparatorBetweenSidesWithUSepTest () {

        // create JSON request body
        JSONObject issueData = new JSONObject();
        Triangle triangle = Triangle.getDefaultTriangle();
        String separator = ";";
        String sides = triangle.getFirstSide().toString() + triangle.getSecondSide().toString() + triangle.getThirdSide().toString();

        issueData.put("separator", separator);
        issueData.put("input", sides );

        String requestBody = issueData.toString();

        // main check
        String message = PostTriangleRequest.postUnprocessableEntity(requestBody);
        assertEquals(UNPROCESSABLE_ENTITY_RESPONSE, message);
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("Post request with only one user separator between sides inside json (example {\"separator\": \";\", \"input\": \"34;5\"} ) expect 422")
    public void postOneSeparatorBetweenSidesWithUSepTest () {

        // create JSON request body
        JSONObject issueData = new JSONObject();
        Triangle triangle = Triangle.getDefaultTriangle();
        String separator = ";";
        String sides = triangle.getFirstSide().toString() + triangle.getSecondSide().toString() +  separator + triangle.getThirdSide();

        issueData.put("separator", separator);
        issueData.put("input", sides );

        String requestBody = issueData.toString();

        // main check
        String message = PostTriangleRequest.postUnprocessableEntity(requestBody);
        assertEquals(UNPROCESSABLE_ENTITY_RESPONSE, message);
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("Post request without default separator between sides inside json (example {\"input\": \"345\"} ) expect 422")
    public void postNoSeparatorBetweenSidesTest () {

        // create JSON request body
        JSONObject issueData = new JSONObject();
        Triangle triangle = Triangle.getDefaultTriangle();
        String sides = triangle.getFirstSide().toString() + triangle.getSecondSide().toString() + triangle.getThirdSide().toString();

        issueData.put("input", sides );
        String requestBody = issueData.toString();

        // main check
        String message = PostTriangleRequest.postUnprocessableEntity(requestBody);
        assertEquals(UNPROCESSABLE_ENTITY_RESPONSE, message);
    }

    // with only one default separator between sides inside json (example {"input": "34;5"} ) (expected 422)
    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("Post request with 'input' without default separator between sides inside json (example {\"input\": \"345\"} ) expect 422")
    public void postOneSeparatorBetweenSidesTest () {

        // create JSON request body
        JSONObject issueData = new JSONObject();
        Triangle triangle = Triangle.getDefaultTriangle();
        String separator = ";";
        String sides = triangle.getFirstSide().toString() + triangle.getSecondSide().toString() +  separator + triangle.getThirdSide();

        issueData.put("input", sides );
        String requestBody = issueData.toString();

        // main check
        String message = PostTriangleRequest.postUnprocessableEntity(requestBody);
        assertEquals(UNPROCESSABLE_ENTITY_RESPONSE, message);
    }
}

