package tests.user;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.apache.commons.lang3.RandomStringUtils;
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
    private final String userEmail = RandomStringUtils.random(6, true, true)+"@tstmail.com";
    private final String userName = RandomStringUtils.random(6, true, true);
    private final String password = RandomStringUtils.random(6, true, true);

    User user = new User(userEmail, password, userName);

    @Before
    public void setUp() {
        RestAssured.baseURI = STELLARBURGERS_URL;
    }

    @Test
    @DisplayName("Проверить создание нового пользователя")
    public void testNewUserCreation() {
        Response response = CreateUserSteps.createNewUser(user);
        CreateUserSteps.verifySuccessfulCreationResponseData(response, OK_CODE, true, userEmail, userName);
    }

    @Test
    @DisplayName("Проверить создание пользователя, уже существующего в системе")
    @Description("Убедиться, что система не позволяет создать пользователя с уже существующего в системе")
    public void testDuplicateUserCreation() {
        UserResponse expectedObject = new UserResponse(false, userAlreadyExistsMessage);
        Response responseOne = CreateUserSteps.createNewUser(user);
        CreateUserSteps.verifySuccessfulCreationResponseData(responseOne, OK_CODE, true, userEmail, userName);
        Response responseTwo = CreateUserSteps.createNewUser(user);
        CreateUserSteps.verifyResponseData(responseTwo, FORBIDDEN_CODE, expectedObject);
    }

    @Test
    @DisplayName("Проверить создание пользователя, не отправляя обязательные поля")
    @Description("Убедиться, что система не позволяет создать пользователя, не предоставляя обязательные поля")
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
