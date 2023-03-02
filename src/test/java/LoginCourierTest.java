import io.qameta.allure.Allure;
import io.qameta.allure.Description;
import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.hamcrest.Matcher;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.Matchers.greaterThan;

public class LoginCourierTest {
    private final String login = "ya";
    private final String password = "1234";
    private String endpointLoginCourier = "/api/v1/courier/login";

    @Before
    public void setUp() {
        RestAssured.baseURI = "http://qa-scooter.praktikum-services.ru/";
        given()
                .header("Content-type", "application/json")
                .body(String.format("{\"login\": \"%s\", \"password\": \"%s\", \"firstName\": \"%s\"}",login,password,"ya"))
                .expect()
                .statusCode(anyOf(is(201),is(409)))
                .when()
                .post("/api/v1/courier");
    }
    @Test
    @DisplayName("Login Courier With Valid Data Return Success") // имя теста
    @Description("Checking the logging of a courier") // описание теста
    public void loginCourierWithValidDataReturnSuccess200() {
        String jsonPattern = "{\"login\": \"%s\", \"password\": \"%s\"}";
        String json = String.format(jsonPattern, login, password);

        String keyJson = "id";
        Matcher<Integer> matcherForExpectedValueByKeyJson = greaterThan(0);
        int expectedCode = 200;
        int keyCount = 1;

        allStepOfSendRequestAndAsserts(json,expectedCode,keyJson,matcherForExpectedValueByKeyJson,keyCount);
    }
    @Test
    public void loginCourierWithoutPasswordReturn400() {
        String jsonPattern = "{\"login\": \"%s\"}";
        String json = String.format(jsonPattern, login);

        String keyJson = "message";
        Matcher<Integer> matcherForExpectedValueByKeyJson = greaterThan(0);
        int expectedCode = 400;
        int keyCount = 1;

        allStepOfSendRequestAndAsserts(json,expectedCode,keyJson,matcherForExpectedValueByKeyJson,keyCount);
    }
    @Test
    public void loginCourierWithoutLoginReturn400() {
        String jsonPattern = "{\"login\":null, \"password\": \"%s\"}";
        String json = String.format(jsonPattern, password);

        String keyJson = "message";
        Matcher<String> matcherForExpectedValueByKeyJson = equalTo("Недостаточно данных для входа");
        int expectedCode = 400;
        int keyCount = 1;

        allStepOfSendRequestAndAsserts(json,expectedCode,keyJson,matcherForExpectedValueByKeyJson,keyCount);
    }
    @Test
    public void loginCourierWithoutLoginPasswordReturn400() {
        String json = "{\"login\":null, \"password\": null}";

        String keyJson = "message";
        Matcher<String> matcherForExpectedValueByKeyJson = equalTo("Недостаточно данных для входа");
        int expectedCode = 400;
        int keyCount = 1;

        allStepOfSendRequestAndAsserts(json,expectedCode,keyJson,matcherForExpectedValueByKeyJson,keyCount);
    }
    @Test
    public void loginCourierWitWrongLoginReturn404() {
        String jsonPattern = "{\"login\": \"%s\", \"password\": \"%s\"}";
        String json = String.format(jsonPattern, login+1, password);

        String keyJson = "message";
        Matcher<String> matcherForExpectedValueByKeyJson = equalTo("Учетная запись не найдена");
        int expectedCode = 404;
        int keyCount = 1;

        allStepOfSendRequestAndAsserts(json,expectedCode,keyJson,matcherForExpectedValueByKeyJson,keyCount);
    }
    @Test
    public void loginCourierWitWrongPasswordReturn404() {
        String jsonPattern = "{\"login\": \"%s\", \"password\": \"%s\"}";
        String json = String.format(jsonPattern, login, password+1);

        String keyJson = "message";
        Matcher<String> matcherForExpectedValueByKeyJson = equalTo("Учетная запись не найдена");
        int expectedCode = 404;
        int keyCount = 1;

        allStepOfSendRequestAndAsserts(json,expectedCode,keyJson,matcherForExpectedValueByKeyJson,keyCount);
    }
    @Test
    public void loginCourierWitWrongLoginPasswordReturn404() {
        String jsonPattern = "{\"login\": \"%s\", \"password\": \"%s\"}";
        String json = String.format(jsonPattern, login+1, password+1);

        String keyJson = "message";
        Matcher<String> matcherForExpectedValueByKeyJson = equalTo("Учетная запись не найдена");
        int expectedCode = 404;
        int keyCount = 1;

        allStepOfSendRequestAndAsserts(json,expectedCode,keyJson,matcherForExpectedValueByKeyJson,keyCount);
    }

    public <T> void allStepOfSendRequestAndAsserts(String json,int expectedCode, String keyJson,Matcher<T> matcherForExpectedValueByKeyJson,int keyCount) {
        Response response = sendPostRequest(json);
        printResponseBodyToConsole(response);
        compareStatusCode(response, expectedCode);
        compareKeyValue(response, keyJson, matcherForExpectedValueByKeyJson);
        compareKeyCount(response,keyCount);
    }

    @Step("Send POST request")
    public Response sendPostRequest(String json) {
        Response response = given()
                .header("Content-type", "application/json")
                .body(json)
                .when()
                .post(endpointLoginCourier);
        return response;
    }

    @Step("Compare value of key \"{keyJson}\"")
    public <T> void compareKeyValue(Response response, String keyJson,Matcher<T> matcherForExpectedValueByKeyJson) {
        response.then().assertThat().body(keyJson, matcherForExpectedValueByKeyJson);
    }

    @Step("Compare status code of  \"{expectedCode}\"")
    public void compareStatusCode(Response response, int expectedCode) {
        response.then().statusCode(expectedCode);
    }
    @Step("Compare the number of json keys")
    public void compareKeyCount(Response response, int keyCount) {
        response.then().body("size()", is(keyCount));
    }

    @Step("Print received response json")
    public void printResponseBodyToConsole(Response response) {
        System.out.println(response.body().asString());
        Allure.addAttachment("Received response json", response.body().asString());
    }

    @After
    public void deleteCourierWithValidDataReturnSuccess() {
        String jsonLogin = String.format("{\"login\": \"%s\", \"password\": \"%s\"}", login, password);
        Integer id = given()
                .header("Content-type", "application/json")
                .body(jsonLogin)
                .when()
                .post("/api/v1/courier/login")
                .then().extract().body().path("id");
        if (id != null) {
            String json = String.format("{\"id\": \"%s\"}", id);
            given()
                    .header("Content-type", "application/json")
                    .pathParam("Id",id)
                    .body(json)
                    .delete("/api/v1/courier/{Id}")
                    .then().assertThat().body("ok", is(true))
                    .and().statusCode(200);
        }
    }
}
