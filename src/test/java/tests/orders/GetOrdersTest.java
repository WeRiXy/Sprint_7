package tests.orders;

import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Story;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.hamcrest.Matcher;
import org.junit.*;
import requests.OrdersAPI;

import static asserts.Asserts.*;
import static org.hamcrest.Matchers.greaterThan;

@Epic(value = "Orders tests")
@Story(value = "Get orders")
public class GetOrdersTest {

    @Test
    @DisplayName("Get list of orders without param")
    @Description("Expected: status code - 200, return non-empty list orders")
    public void getListOrders() {

        Response response= OrdersAPI.getListOrders();

        String keyJson = "orders.size()";
        Matcher matcherForExpectedValueByKeyJson = greaterThan(0);
        int expectedCode = 200;

        compareStatusCode(response, expectedCode);
        compareKeyValue(response, keyJson, matcherForExpectedValueByKeyJson);
    }
}
