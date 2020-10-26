package config;

public class EnvConfig {

    // network settings
    public static final String URL_BASE = "https://qa-quiz.natera.com";
    public static final String URL_TRIANGLE = "/triangle";
    public static final String AUTH_KEY = "X-User";
    public static final String AUTH_VALUE = "248ae583-69c2-4e2c-a6ab-40d98b593e70";

    // product settings
    public static final int MAX_TRIANGLE_QUANTITY = 10;

    // api settings
    public static final String NOT_FOUND_MESSAGE = "Not Found";
    public static final String WRONG_METHOD_MESSAGE = "Request method 'POST' not supported";
    public static final String WRONG_TOKEN_ERROR = "Unauthorized";
    public static final String BIG_RESULT_FROM_RESPONSE = "Infinity";
    public static final String UNPROCESSABLE_ENTITY_MESSAGE = "Cannot process input";
    public static final String LIMIT_EXCEEDED_MESSAGE = "Limit exceeded";
    public static final String BAD_REQUEST_MESSAGE = "Could not read document";

    /* MODE "Check Overflow"
     Description:
           Before Each Test: Check list of triangles at server: if it is full - remove one triangle.
     Note:
           This mode taking 40% times and generate 40% requests.
           But it guarantees that test would not fall due to triangle list on server is full (it can happens if one or more tests fails).
     You can off this option for tests run by QA.
     DO NOT OFF this option if you run tests by schedule! */
    public static boolean CHECK_OVERFLOW_MODE_ON = true;

    //          ***QA SETTINGS***

    //default triangle settings
    public static final String DEFAULT_TRIANGLE_ID = "defaultId";
    public static final double DEFAULT_TRIANGLE_FIRST_SIDE = 3.0;
    public static final double DEFAULT_TRIANGLE_SECOND_SIDE = 4.0;
    public static final double DEFAULT_TRIANGLE_THIRD_SIDE = 5.0;

    //set lengths of random triangle side (first and second only)
    public static final double MAX_RANDOM_TRIANGLE_SIDE = 100.0;
    public static final double MAX_RANDOM_SMALL_TRIANGLE_SIDE = 0.1E-10;
    public static final double MAX_RANDOM_BIG_TRIANGLE_SIDE = 1E10; // do not set more than 1E75
    public static final double MAX_RANDOM_VERY_BIG_TRIANGLE_SIDE = 1E90; // set more than 1E75 for test requestTests.GetAreaRequestTest.getInfinityAreaBigTriangleTest
    public static final double DELTA_FOR_SIDE = 0.1E-14; // to be sure that random side !=0

    //set string for separator test
    public static final String USER_TEXT_SEPARATOR = "test";

    //set wrong triangle id for negative tests
    public static final String WRONG_TRIANGLE_ID = "WRONG-ID";

    //set wrong token for negative tests
    public static final String WRONG_AUTH_VALUE = "WRONG-TOKEN";

    //set max post request to fill database (use at DataBaseActions.fillAllTrianglesDeFacto )
    public static final int MAX_TRIANGLE_POST_ATTEMPTS = 100;

    //sides for negative post tests
    public static final String WRONG_TEXT_IN_SIDES = "test";
    public static final String WRONG_MIXED_IN_SIDES = "1test";
    public static final String MANY_DIGITS_NUMBER = "1234567890" + "1234567890" + "1234567890";
    public static final String WRONG_REQUEST_STRING = "{\"input\":\"3;4;5}";
}

