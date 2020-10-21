package helper;

import models.Triangle;
import org.json.JSONObject;

public class TriangleToJSON {

    // with separator
    public static String generateJSONForTriangle(Triangle triangle, String separator) {

        JSONObject issueData = new JSONObject();
        String sides = triangle.getFirstSide() +  separator + triangle.getSecondSide() +  separator + triangle.getThirdSide();

        issueData.put("separator", separator);
        issueData.put("input", sides );

        return issueData.toString();
    }

    // without separator
    public static String generateJSONForTriangle(Triangle triangle) {

        JSONObject issueData = new JSONObject();
        String sides = triangle.getFirstSide() + ";" + triangle.getSecondSide() +  ";" + triangle.getThirdSide();

        issueData.put("input", sides );

        return issueData.toString();
    }
}