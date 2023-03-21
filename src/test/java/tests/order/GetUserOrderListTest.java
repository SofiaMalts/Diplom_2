package tests.order;

import io.qameta.allure.Description;
import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Before;
import org.junit.Test;
import site.nomoreparties.stellarburgers.model.user.User;
import site.nomoreparties.stellarburgers.steps.order.OrderSteps;
import site.nomoreparties.stellarburgers.steps.user.CreateUserSteps;

import static constants.Url.STELLARBURGERS_URL;

public class GetUserOrderListTest {
    private final String userEmail = RandomStringUtils.random(6, true, true)+"@tstmail.com";
    private final String userName = RandomStringUtils.random(9, true, true);
    private final String password = RandomStringUtils.random(6, true, true);

    User user = new User(userEmail, password, userName);

    @Before
    public void setUp() {
        RestAssured.baseURI = STELLARBURGERS_URL;
    }

    @Test
    @DisplayName("Проверить получение списка заказов пользователя")
    @Description("Проверить получение списка заказов авторизованного и неавторизованного пользователя")
    public void testGetOrderListForUser() {
        testAuthorizedGetOrderListForUser();
        testUnauthorizedGetOrderListForUser();
    }

    @Step("Проверить получение списка заказов авторизованного пользователя")
    public void testAuthorizedGetOrderListForUser() {
        CreateUserSteps.createNewUser(user);
        String token = CreateUserSteps.getUserTokenByUser(user);
        Response response = OrderSteps.getOrdersForUserAuthenticated(token);
        OrderSteps.verifyAuthorizedGetOrdersResponse(response);
        CreateUserSteps.deleteByToken(token);
    }

    @Step("Проверить получение списка заказов неавторизованного пользователя")
    public void testUnauthorizedGetOrderListForUser() {
        Response response = OrderSteps.getOrdersForUser();
        OrderSteps.verifyUnauthorizedGetOrdersResponse(response);
    }
}
