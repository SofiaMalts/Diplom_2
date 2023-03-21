package site.nomoreparties.stellarburgers.base;

import io.restassured.config.RedirectConfig;
import io.restassured.config.RestAssuredConfig;
import io.restassured.config.SSLConfig;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class BaseMethods {
    private static final RestAssuredConfig config = RestAssuredConfig.newConfig()
            .sslConfig(new SSLConfig().relaxedHTTPSValidation())
            .redirect(new RedirectConfig().followRedirects(true));

    public static Response getRequest(String uri) {
        return given()
                .config(config)
                .header("Content-type", "application/json")
                .get(uri);
    }

    public static Response getRequestAuthenticated(String uri, String token) {
        return given()
                .config(config)
                .auth().oauth2(token)
                .get(uri);
    }

    public static Response postRequest(String uri, Object body) {
        return given()
                .config(config)
                .header("Content-type", "application/json")
                .body(body)
                .post(uri);
    }

    public static Response patchRequest(String uri, Object body) {
        return given()
                .config(config)
                .header("Content-type", "application/json")
                .body(body)
                .patch(uri);
    }

    public static Response patchRequestWithJson(String uri, String json) {
        return given()
                .config(config)
                .header("Content-type", "application/json")
                .body(json)
                .patch(uri);
    }

    public static Response patchRequestAuthenticated(String uri, Object body, String token) {
        return given()
                .config(config)
                .header("Content-type", "application/json")
                .auth().oauth2(token)
                .body(body)
                .patch(uri);
    }

    public static Response patchRequestWithJsonAuthenticated(String uri, String json, String token) {
        return given()
                .config(config)
                .header("Content-type", "application/json")
                .auth().oauth2(token)
                .body(json)
                .patch(uri);
    }

    public static Response deleteUserRequest(String uri, String token) {
        return given()
                .config(config)
                .header("Content-type", "application/json")
                .auth().oauth2(token)
                .delete(uri);
    }
}
