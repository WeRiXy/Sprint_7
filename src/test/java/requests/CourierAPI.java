package requests;

import constants.ApiConstants;
import io.qameta.allure.Step;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import jsonobjects.AccountData;
import jsonobjects.Courier;
import java.util.Map;

public class CourierAPI {

    @Step("Send POST request create Courier")
    public static Response createCourier(Courier courier) {
        return RestAssured.given()
                .spec(ApiConstants.REQUEST_SPEC)
                .body(courier)
                .post(ApiConstants.ENDPOINT_CREATE_COURIER);
    }

    @Step("Send POST request login Courier")
    public static Response loginCourier(AccountData accountData) {
        return RestAssured.given()
                .spec(ApiConstants.REQUEST_SPEC)
                .body(accountData)
                .post(ApiConstants.ENDPOINT_LOGIN_COURIER);
    }

    @Step("Send DELETE request delete Courier by id")
    public static Response deleteCourier(int id) {
        return RestAssured.given()
                .spec(ApiConstants.REQUEST_SPEC)
                .pathParam("id", id)
                .body(Map.of("id", String.valueOf(id)))
                .delete(ApiConstants.ENDPOINT_DELETE_COURIER);
    }
}