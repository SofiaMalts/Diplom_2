package site.nomoreparties.stellarburgers.steps.user;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import org.assertj.core.api.AssertionsForClassTypes;
import responses.user.UserResponse;
import site.nomoreparties.stellarburgers.base.BaseMethods;

import static constants.ResponseConstants.OK_CODE;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static site.nomoreparties.stellarburgers.base.BaseMethods.*;

public class CreateUserSteps {
    private static final String createUserEndpoint = "/api/auth/register";
    private static final String loginUserEndpoint = "/api/auth/login";
    private static final String userDataEndpoint = "/api/auth/user";

    //Create user
    @Step("Create user")
    public static Response createNewUser(Object body) {
        return BaseMethods.postRequest(createUserEndpoint, body);
    }

    //Create user without password
    @Step("Try to create user without password")
    public static Response createUserWithoutPassword(String email, String login) {
        String body = "{\"email\": \"" + email + "\",\"name\": \"" + login + "\"}";
        return given()
                .header("Content-type", "application/json")
                .body(body)
                .post(createUserEndpoint);
    }

    //Create user without login
    @Step("Try to create user without login")
    public static Response createUserWithoutUsername(String email, String password) {
        String body = "{\"email\": \"" + email + "\",\"password\": \"" + password + "\"}";
        return given()
                .header("Content-type", "application/json")
                .body(body)
                .post(createUserEndpoint);
    }

    //Create user without email
    @Step("Try to create user without email")
    public static Response createUserWithoutEmail(String login, String password) {
        String body = "{\"name\": \"" + login + "\",\"password\": \"" + password + "\"}";
        return given()
                .header("Content-type", "application/json")
                .body(body)
                .post(createUserEndpoint);
    }

    //Get authorization token
    public static String getUserTokenFromResponse(Response response) {
        UserResponse responseAsObject = responseToObject(response);
        String tokenString = responseAsObject.getAccessToken().replace("Bearer ", "");
        return tokenString;
    }

    public static String getUserTokenByUser(Object body) {
        Response response = BaseMethods.postRequest(loginUserEndpoint, body);
        UserResponse responseAsObject = responseToObject(response);
        String tokenString = responseAsObject.getAccessToken().replace("Bearer ", "");
        return tokenString;
    }

    //Delete created user
    @Step("Delete user")
    public static void deleteUser(Object body) {
        Response response = BaseMethods.postRequest(loginUserEndpoint, body);
        if (response.statusCode() == OK_CODE) {
            String token = getUserTokenFromResponse(response);
            BaseMethods.deleteUserRequest(userDataEndpoint, token);
        }
    }

    //Update user
    public static Response updateUserParameter(String parameter, String newParameter) {
        String json = "{\"" + parameter + "\": \"" + newParameter + "\"}";
        return patchRequestWithJson(userDataEndpoint, json);
    }

    public static Response updateWholeUser(Object newUser) {
        return patchRequest(userDataEndpoint, newUser);
    }

    public static Response updateUserParameterAuth(Object body, String parameter, String newParameter) {
        String json = "{\"" + parameter + "\": \"" + newParameter + "\"}";
        String token = getUserTokenByUser(body);
        Response response = patchRequestWithJsonAuthenticated(userDataEndpoint, json, token);
        deleteByToken(token);
        return response;
    }

    public static Response updateWholeUserAuth(Object currentUser, Object newUser) {
        String token = getUserTokenByUser(currentUser);
        Response response = patchRequestAuthenticated(userDataEndpoint, newUser, token);
        deleteByToken(token);
        return response;
    }
    @Step("Delete user")
    public static void deleteByToken(String token) {
        BaseMethods.deleteUserRequest(userDataEndpoint, token);
    }

    @Step("Login")
    public static Response loginUser(Object body) {
        return BaseMethods.postRequest(loginUserEndpoint, body);
    }

    //Check response body
    @Step("Check response body")
    public static void checkResponseBody(Response response, UserResponse expectedObject) {
        UserResponse responseAsObject = responseToObject(response);
        AssertionsForClassTypes.assertThat(responseAsObject).usingRecursiveComparison().isEqualTo(expectedObject);
    }

    //Check response body
    @Step("Check response body")
    public static void checkSuccessResponseBody(Response response, boolean expectedSuccess, String expectedEmail, String expectedUserName) {
        //System.out.println(response.body().asString());
        response.then().assertThat().body("success", equalTo(expectedSuccess));
        response.then().assertThat().body("user.email", equalTo(expectedEmail.toLowerCase()));
        response.then().assertThat().body("user.name", equalTo(expectedUserName));
        response.then().assertThat().body("accessToken", notNullValue());
        response.then().assertThat().body("refreshToken", notNullValue());
    }

    @Step("Check user update response body")
    public static void checkSuccessUpdateResponseBody(Response response, boolean expectedSuccess, String expectedEmail, String expectedUserName) {
        response.then().assertThat().body("success", equalTo(expectedSuccess));
        response.then().assertThat().body("user.email", equalTo(expectedEmail.toLowerCase()));
        response.then().assertThat().body("user.name", equalTo(expectedUserName));
    }

    //Check status code
    @Step("Check status code")
    public static void checkResponseCode(Response response, int expectedCode) {
        response.then().assertThat().statusCode(expectedCode);
    }

    //Check response
    @Step("Check response body and status code")
    public static void verifyResponseData(Response response, int expectedCode, UserResponse expectedObject) {
        checkResponseCode(response, expectedCode);
        checkResponseBody(response, expectedObject);
    }

    //Check successful creation response data
    @Step("Check response body and status code")
    public static void verifySuccessfulCreationResponseData(Response response, int expectedCode, boolean expectedSuccess, String expectedEmail, String expectedUserName) {
        checkSuccessResponseBody(response, expectedSuccess, expectedEmail, expectedUserName);
        checkResponseCode(response, expectedCode);

    }

    @Step("Check update response body and status code")
    public static void verifySuccessfulUpdateResponseData(Response response, int expectedCode, boolean expectedSuccess, String expectedEmail, String expectedUserName) {
        checkSuccessUpdateResponseBody(response, expectedSuccess, expectedEmail, expectedUserName);
        checkResponseCode(response, expectedCode);
    }

    public static UserResponse responseToObject(Response response) {
        return response.body().as(UserResponse.class);
    }

}
