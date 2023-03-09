package tests.user;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import responses.user.UserResponse;
import site.nomoreparties.stellarburgers.model.user.User;
import site.nomoreparties.stellarburgers.steps.user.CreateUserSteps;

import static constants.ResponseConstants.FORBIDDEN_CODE;
import static constants.ResponseConstants.OK_CODE;
import static constants.Url.STELLARBURGERS_URL;

public class CreateUserTest {
    private final String userAlreadyExistsMessage = "User already exists";
    private final String requiredFieldNotSent = "Email, password and name are required fields";
    private final String userEmail = "apiTestUser6@tstmail.com";
    private final String userName = "SofiaTestUser6";
    private final String password = "P@ssW0rd137";

    User user = new User(userEmail, password, userName);

    @Before
    public void setUp() {
        RestAssured.baseURI = STELLARBURGERS_URL;
    }

    @Test
    @DisplayName("Check user creation")
    @Description("Check if user can be created by correct api request")
    public void testNewUserCreation() {
        Response response = CreateUserSteps.createNewUser(user);
        CreateUserSteps.verifySuccessfulCreationResponseData(response, OK_CODE, true, userEmail, userName);
    }

    @Test
    @DisplayName("Check user creation of duplicate user")
    @Description("Verify that system doesn't allow to create user that already exists")
    public void testDuplicateUserCreation() {
        UserResponse expectedObject = new UserResponse(false, userAlreadyExistsMessage);
        Response responseOne = CreateUserSteps.createNewUser(user);
        CreateUserSteps.verifySuccessfulCreationResponseData(responseOne, OK_CODE, true, userEmail, userName);
        Response responseTwo = CreateUserSteps.createNewUser(user);
        CreateUserSteps.verifyResponseData(responseTwo, FORBIDDEN_CODE, expectedObject);
    }

    @Test
    @DisplayName("Check user creation without required fields")
    @Description("Verify that system doesn't allow to create user by request without required field")
    public void testNewUserCreationWithoutRequiredField() {
        UserResponse expectedObject = new UserResponse(false, requiredFieldNotSent);
        Response responseWithoutPassword = CreateUserSteps.createUserWithoutPassword(userEmail, userName);
        CreateUserSteps.verifyResponseData(responseWithoutPassword, FORBIDDEN_CODE, expectedObject);
        Response responseWithoutUsername = CreateUserSteps.createUserWithoutUsername(userEmail, password);
        CreateUserSteps.verifyResponseData(responseWithoutUsername, FORBIDDEN_CODE, expectedObject);
        Response responseWithoutEmail = CreateUserSteps.createUserWithoutEmail(userName, password);
        CreateUserSteps.verifyResponseData(responseWithoutEmail, FORBIDDEN_CODE, expectedObject);
    }

    @After
    public void clearData() {
        CreateUserSteps.deleteUser(user);
    }
}
