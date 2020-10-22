package config;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.specification.RequestSpecification;

import static config.EnvConfig.*;

public class WrongOrAbsentTokenConfig {

    // wrong token
    private static final RequestSpecification wrongTokenRequestSpec = new RequestSpecBuilder()
            .setBaseUri(URL_BASE)
            .setBasePath(URL_TRIANGLE)
            .addHeader(AUTH_KEY, WRONG_AUTH_VALUE)
            .build();

    // no token
    private static final RequestSpecification noTokenRequestSpec = new RequestSpecBuilder()
            .setBaseUri(URL_BASE)
            .setBasePath(URL_TRIANGLE)
            .build();


    public static RequestSpecification getWrongTokenRequestSpec() {
        return wrongTokenRequestSpec;
    }

    public static RequestSpecification getNoTokenRequestSpec() {
        return noTokenRequestSpec;
    }

}
