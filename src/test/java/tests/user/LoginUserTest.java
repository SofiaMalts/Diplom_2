package tests.user;

import io.qameta.allure.Description;
import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import responses.user.UserResponse;
import site.nomoreparties.stellarburgers.model.user.User;
import site.nomoreparties.stellarburgers.steps.user.CreateUserSteps;

import static constants.ResponseConstants.OK_CODE;
import static constants.ResponseConstants.UNAUTHORIZED_CODE;
import static constants.Url.STELLARBURGERS_URL;
import static site.nomoreparties.stellarburgers.steps.user.CreateUserSteps.deleteUser;

public class LoginUserTest {
    private final String errorMessage = "email or password are incorrect";
    private final String userEmail = "apiTestUser6@tstmail.com";
    private final String validUserName = "SofiaTestUser6";
    private final String validPassword = "P@ssW0rd137";
    private final String invalidUserName = "SofiaTestUser60";
    private final String invalidPassword = "P@ssW0rd1370";
    User validUser = new User(userEmail, validPassword, validUserName);
    User invalidUserOne = new User(invalidPassword, validUserName);
    User invalidUserTwo = new User(validPassword, invalidUserName);
    User invalidUserThree = new User(invalidPassword, invalidUserName);

    @Before
    public void setUp() {
        RestAssured.baseURI = STELLARBURGERS_URL;
    }

    @Test
    @DisplayName("Check user login")
    @Description("Verify that user can login with valid credentials")
    public void testUserLogin() {
        CreateUserSteps.createNewUser(validUser);
        Response response = CreateUserSteps.loginUser(validUser);
        CreateUserSteps.verifySuccessfulCreationResponseData(response, OK_CODE, true, userEmail, validUserName);
    }

    @Test
    @DisplayName("Check user login with invalid credentials")
    @Description("Verify that system doesn't allow to login with invalid credentials")
    public void testLoginInvalidCredentials() {
        CreateUserSteps.createNewUser(validUser);
        testLoginWithInvalidPassword();
        testLoginWithInvalidLogin();
        testLoginWithInvalidLoginAndPassword();
    }

    @Step("Test login with invalid password")
    public void testLoginWithInvalidPassword() {
        UserResponse expectedObject = new UserResponse(false, errorMessage);
        Response response = CreateUserSteps.loginUser(invalidUserOne);
        CreateUserSteps.verifyResponseData(response, UNAUTHORIZED_CODE, expectedObject);
    }

    @Step("Test login with invalid login")
    public void testLoginWithInvalidLogin() {
        UserResponse expectedObject = new UserResponse(false, errorMessage);
        Response response = CreateUserSteps.loginUser(invalidUserTwo);
        CreateUserSteps.verifyResponseData(response, UNAUTHORIZED_CODE, expectedObject);
    }

    @Step("Test login with invalid login and password")
    public void testLoginWithInvalidLoginAndPassword() {
        UserResponse expectedObject = new UserResponse(false, errorMessage);
        Response response = CreateUserSteps.loginUser(invalidUserThree);
        CreateUserSteps.verifyResponseData(response, UNAUTHORIZED_CODE, expectedObject);
    }

    @After
    public void clearData() {
        deleteUser(validUser);
    }
}
