package tests.order;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import site.nomoreparties.stellarburgers.model.order.Order;
import site.nomoreparties.stellarburgers.steps.order.OrderSteps;

import java.util.List;

import static constants.Url.STELLARBURGERS_URL;

@RunWith(Parameterized.class)
public class CreateOrderTest {
    private final String expectedResult;
    private final List<String> ingredient;


    public CreateOrderTest(String expectedResult, List<String> ingredient) {
        this.expectedResult = expectedResult;
        this.ingredient = ingredient;

    }

    @Parameterized.Parameters
    public static Object[][] getTestData() {
        return new Object[][]{
                {"success", List.of("640880d09ed280001b2e91b1", "61c0c5a71d1f82001bdaaa71")},
                {"badRequest", List.of()},
                {"serverError", List.of("60d3b41a454353buuuuu0026a733c6", "609646euu543543uuuu6e00276b2870")},
        };
    }

    @Before
    public void setUp() {
        RestAssured.baseURI = STELLARBURGERS_URL;
    }

    @Test
    @DisplayName("Проверить создание заказа")
    @Description("Проверить создание заказа с валидными id ингредиентов, невалидными id ингредиентов и без id ингредиентов.")
    public void testCreateOrder() {
        Order order = new Order(ingredient);
        Response response = OrderSteps.createOrder(order);
        OrderSteps.verifyCreateOrderResponse(response, expectedResult);
    }
}