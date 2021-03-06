package requests;

import api.ValidRequests;
import io.restassured.http.ContentType;
import io.restassured.path.json.config.JsonPathConfig;
import io.restassured.response.ValidatableResponse;

import java.math.BigDecimal;

import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;


public class GetAreaRequest {

    // simple valid get area request
    public static Double getArea(String triangleId) {

        JsonPathConfig cfg = new JsonPathConfig(JsonPathConfig.NumberReturnType.BIG_DECIMAL); // need for receive normal double (without rounding)

        ValidatableResponse response = ValidRequests.get("/" + triangleId + "/" + "area").
                statusCode(200).
                contentType(ContentType.JSON).
                assertThat().
                body(matchesJsonSchemaInClasspath("area-schema.json"));

        BigDecimal area =  response.
                extract().
                jsonPath().
                using(cfg).
                get("result");

        return area.doubleValue();  // the only way to save precision

    }

    // get for one absent triangle request return 404
    public static String getOneAbsent(String triangleId) {

        ValidatableResponse response = ValidRequests.get("/" + triangleId + "/" + "area").
                statusCode(404).
                contentType(ContentType.JSON).
                assertThat().
                body(matchesJsonSchemaInClasspath("not-found-schema.json"));

        return response.
                extract().
                jsonPath().
                getString("message");

    }
}
