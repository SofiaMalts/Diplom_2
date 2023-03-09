package tests.order;

import io.qameta.allure.Description;
import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.Before;
import org.junit.Test;
import site.nomoreparties.stellarburgers.model.user.User;
import site.nomoreparties.stellarburgers.steps.order.OrderSteps;
import site.nomoreparties.stellarburgers.steps.user.CreateUserSteps;

import static constants.Url.STELLARBURGERS_URL;

public class GetUserOrderListTest {
    private final String userEmail = "apiTestUser6@tstmail.com";
    private final String userName = "SofiaTestUser6";
    private final String password = "P@ssW0rd137";

    User user = new User(userEmail, password, userName);

    @Before
    public void setUp() {
        RestAssured.baseURI = STELLARBURGERS_URL;
    }

    @Test
    @DisplayName("Test getting order list for user")
    @Description("Test getting order list for authorized and unauthorized users")
    public void testGetOrderListForUser() {
        testAuthorizedGetOrderListForUser();
        testUnauthorizedGetOrderListForUser();
    }

    @Step("Check getting order list for authorized user")
    public void testAuthorizedGetOrderListForUser() {
        CreateUserSteps.createNewUser(user);
        String token = CreateUserSteps.getUserTokenByUser(user);
        Response response = OrderSteps.getOrdersForUserAuthenticated(token);
        OrderSteps.verifyAuthorizedGetOrdersResponse(response);
        CreateUserSteps.deleteByToken(token);
    }

    @Step("Check getting order list for unauthorized user")
    public void testUnauthorizedGetOrderListForUser() {
        Response response = OrderSteps.getOrdersForUser();
        OrderSteps.verifyUnauthorizedGetOrdersResponse(response);
    }
}
