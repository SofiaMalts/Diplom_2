package site.nomoreparties.stellarburgers.steps.order;

import io.qameta.allure.Param;
import io.qameta.allure.Step;
import io.restassured.response.Response;
import org.hamcrest.MatcherAssert;
import responses.order.OrderResponse;
import site.nomoreparties.stellarburgers.base.BaseMethods;

import static constants.ResponseConstants.*;
import static io.qameta.allure.model.Parameter.Mode.MASKED;
import static org.hamcrest.Matchers.*;

public class OrderSteps {
    //Create order
    private static final String ordersEndpoint = "/api/orders";
    private static final String badRequestError = "Ingredient ids must be provided";
    private static final String unauthorizedError = "You should be authorised";

    @Step("Create order")
    public static Response createOrder(Object body) {
        return BaseMethods.postRequest(ordersEndpoint, body);
    }

    //Check response
    @Step("Check response to create order request")
    public static void verifyCreateOrderResponse(Response response, String expectedResult) {
        switch (expectedResult) {
            case "success":
                response.then().assertThat().statusCode(OK_CODE);
                response.then().assertThat().body("success", is(true));
                response.then().assertThat().body("name", notNullValue());
                response.then().assertThat().body("order.number", notNullValue());
                break;
            case "badRequest":
                response.then().assertThat().statusCode(BAD_REQUEST_CODE);
                response.then().assertThat().body("success", is(false));
                response.then().assertThat().body("message", equalTo(badRequestError));
                break;
            case "serverError":
                response.then().assertThat().statusCode(SERVERERROR_CODE);
                break;
        }
    }

    @Step("Get orders authorized request")

    public static Response getOrdersForUserAuthenticated(@Param(name = "authToken", mode=MASKED) String token) {

        return BaseMethods.getRequestAuthenticated(ordersEndpoint, token);
    }

    @Step("Get orders unauthorized request")
    public static Response getOrdersForUser() {
        return BaseMethods.getRequest(ordersEndpoint);
    }

    public static OrderResponse responseToObject(Response response) {
        return response.body().as(OrderResponse.class);
    }
@Step("Check response status code")
    public static void verifyGetOrdersResponseStatusCode(Response response, int expectedCode) {
        response.then().assertThat().statusCode(expectedCode);
    }

    public static void verifyGetOrderResponseNotNull(Response response) {
        OrderResponse responseAsObject = responseToObject(response);
        MatcherAssert.assertThat(responseAsObject, notNullValue());
    }
    @Step("Check authorized response body")
    public static void verifyAuthorizedGetOrdersResponseBody(Response response) {
        verifyGetOrderResponseNotNull(response);
        response.then().assertThat().body("success", is(true));
        response.then().assertThat().body("orders", notNullValue());
        response.then().assertThat().body("total", notNullValue());
        response.then().assertThat().body("totalToday", notNullValue());
    }
    @Step("Check unauthorized response body")
    public static void verifyUnauthorizedGetOrdersResponseBody(Response response) {
        verifyGetOrderResponseNotNull(response);
        response.then().assertThat().body("success", is(false));
        response.then().assertThat().body("message", equalTo(unauthorizedError));
    }

    @Step("Check response to authorized get orders request")
    public static void verifyAuthorizedGetOrdersResponse(Response response) {
        int expectedCode = OK_CODE;
        verifyGetOrdersResponseStatusCode(response, expectedCode);
        verifyAuthorizedGetOrdersResponseBody(response);
    }

    @Step("Check response to unauthorized get orders request")
    public static void verifyUnauthorizedGetOrdersResponse(Response response) {
        int expectedCode = UNAUTHORIZED_CODE;
        verifyGetOrdersResponseStatusCode(response, expectedCode);
        verifyUnauthorizedGetOrdersResponseBody(response);
    }
}
