package tests.user;

import io.qameta.allure.Description;
import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.Before;
import org.junit.Test;
import responses.user.UserResponse;
import site.nomoreparties.stellarburgers.model.user.User;
import site.nomoreparties.stellarburgers.steps.user.CreateUserSteps;

import static constants.ResponseConstants.OK_CODE;
import static constants.ResponseConstants.UNAUTHORIZED_CODE;
import static constants.Url.STELLARBURGERS_URL;

public class UpdateUserTest {
    private final String unauthorizedError = "You should be authorised";
    private final String userEmailOne = "apiTestUser123@tstmail.com";
    private final String userEmailTwo = "apiTestUser456@tstmail.com";
    private final String userName = "SofiaTestUser123";
    private final String password = "P@ssW0rd137";
    private final String newUserName = "SofiaTestUser345";
    private final String newUserEmail = "apiTestUser987@tstmail.com";
    User userOne = new User(userEmailOne, password, userName);
    User userTwo = new User(userEmailTwo, password, userName);
    User updatedUser = new User(newUserEmail, password, newUserName);

    @Before
    public void setUp() {
        RestAssured.baseURI = STELLARBURGERS_URL;
    }

    @Test
    @DisplayName("Проверить обновление данных авторизованного пользователя")
    @Description("Убедиться, что система позволяет обновить данные авторизованного пользователя")
    public void testAuthorizedUserUpdate() {
        testAuthNameUpdate();
        testAuthEmailUpdate();
        testAuthNameAndEmailUpdate();
    }

    @Step("Проверить обновление параметра \"name\" авторизованного пользователя")
    public void testAuthNameUpdate() {
        CreateUserSteps.createNewUser(userOne);
        Response response = CreateUserSteps.updateUserParameterAuth(userOne, "name", newUserName);
        CreateUserSteps.verifySuccessfulUpdateResponseData(response, OK_CODE, true, userOne.getEmail(), newUserName);
    }

    @Step("Проверить обновление параметра \"email\" авторизованного пользователя")
    public void testAuthEmailUpdate() {
        CreateUserSteps.createNewUser(userOne);
        Response response = CreateUserSteps.updateUserParameterAuth(userOne, "email", newUserEmail);
        CreateUserSteps.verifySuccessfulUpdateResponseData(response, OK_CODE, true, newUserEmail, userOne.getName());
    }

    @Step("Проверить обновление параметров \"name\" and \"email\" авторизованного пользователя")
    public void testAuthNameAndEmailUpdate() {
        CreateUserSteps.createNewUser(userOne);
        Response response = CreateUserSteps.updateWholeUserAuth(userOne, updatedUser);
        CreateUserSteps.verifySuccessfulUpdateResponseData(response, OK_CODE, true, updatedUser.getEmail(), updatedUser.getName());
    }

    @Test
    @DisplayName("Проверить обновление данных неавторизованного пользователя")
    @Description("Убедиться, что система позволяет обновить данные неавторизованного пользователя")
    public void testUnauthorizedUserUpdate() {
        testUnauthorizedNameUpdate();
        testUnauthorizedEmailUpdate();
        testUnauthorizedNameAndEmailUpdate();
    }

    @Step("Проверить обновление параметра \"name\" неавторизованного пользователя")
    public void testUnauthorizedNameUpdate() {
        CreateUserSteps.createNewUser(userOne);
        UserResponse expectedResponse = new UserResponse(false, unauthorizedError);
        Response response = CreateUserSteps.updateUserParameter("name", newUserName);
        CreateUserSteps.verifyResponseData(response, UNAUTHORIZED_CODE, expectedResponse);
        CreateUserSteps.deleteUser(userOne);
    }

    @Step("Проверить обновление параметра \"email\" неавторизованного пользователя")
    public void testUnauthorizedEmailUpdate() {
        CreateUserSteps.createNewUser(userOne);
        UserResponse expectedResponse = new UserResponse(false, unauthorizedError);
        Response response = CreateUserSteps.updateUserParameter("email", newUserEmail);
        CreateUserSteps.verifyResponseData(response, UNAUTHORIZED_CODE, expectedResponse);
        CreateUserSteps.deleteUser(userOne);
    }

    @Step("Проверить обновление параметров \"name\" and \"email\" неавторизованного пользователя")
    public void testUnauthorizedNameAndEmailUpdate() {
        CreateUserSteps.createNewUser(userOne);
        UserResponse expectedResponse = new UserResponse(false, unauthorizedError);
        Response response = CreateUserSteps.updateWholeUser(updatedUser);
        CreateUserSteps.verifyResponseData(response, UNAUTHORIZED_CODE, expectedResponse);
        CreateUserSteps.deleteUser(userOne);
    }
}
