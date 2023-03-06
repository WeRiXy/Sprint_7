package requests;

import io.qameta.allure.Step;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import jsonobjects.Orders;

import static constants.ApiConstants.*;

public class OrdersAPI {
    @Step("Send POST request create order")
    public static Response createOrder(Orders orders) {
        return RestAssured.given()
                .spec(REQUEST_SPEC)
                .body(orders)
                .post(ENDPOINT_CREATE_ORDER);
    }
    @Step("Send GET request to get list orders")
    public static Response getListOrders() {
        return RestAssured.given()
                .spec(REQUEST_SPEC)
                .get(ENDPOINT_GET_LIST_ORDERS);
    }
}
