import io.qameta.allure.Allure;
import io.qameta.allure.Description;
import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.*;

public class CreateCourierTest {
    private final String login = "ya";
    private final String password = "1234";
    private final String firstName = "ya";
    private String endpointCreateCourier = "/api/v1/courier";

    @Before
    public void setUp() {
        RestAssured.baseURI = "http://qa-scooter.praktikum-services.ru/";
    }

    @Test
    @DisplayName("create Courier With Valid Data Return Success") // имя теста
    @Description("Checking the creation of a courier") // описание теста
    public void createCourierWithValidDataReturnSuccess() {
        String jsonPattern = "{\"login\": \"%s\", \"password\": \"%s\", \"firstName\": \"%s\"}";
        String json = String.format(jsonPattern, login, password, firstName);

        String keyJson = "ok";
        boolean expectedValueByKeyJson = true;
        int expectedCode = 201;
        int keyCount = 1;

        allStepOfSendRequestAndAsserts(json,expectedCode,keyJson,expectedValueByKeyJson,keyCount);
    }

    @Test
    public void createCourierWhenItAlreadyExistsReturnFailed() {
        String jsonPattern = "{\"login\": \"%s\", \"password\": \"%s\", \"firstName\": \"%s\"}";
        String json = String.format(jsonPattern, login, password, firstName);

        String keyJson = "message";
        String expectedValueByKeyJson = "Этот логин уже используется";
        int expectedCode = 409;
        int keyCount = 1;

        sendPostRequest(json);
        allStepOfSendRequestAndAsserts(json,expectedCode,keyJson,expectedValueByKeyJson,keyCount);
    }

    @Test
    public void createCourierWithoutPasswordReturnFailed() {
        String jsonPattern = "{\"login\": \"%s\", \"firstName\": \"%s\"}";
        String json = String.format(jsonPattern, login, firstName);

        String keyJson = "message";
        String expectedValueByKeyJson = "Недостаточно данных для создания учетной записи";
        int expectedCode = 400;
        int keyCount = 1;

        allStepOfSendRequestAndAsserts(json,expectedCode,keyJson,expectedValueByKeyJson,keyCount);
    }

    @Test
    public void createCourierWithoutLoginReturnFailed() {
        String jsonPattern = "{\"password\": \"%s\", \"firstName\": \"%s\"}";
        String json = String.format(jsonPattern, password, firstName);

        String keyJson = "message";
        String expectedValueByKeyJson = "Недостаточно данных для создания учетной записи";
        int expectedCode = 400;
        int keyCount = 1;

        allStepOfSendRequestAndAsserts(json,expectedCode,keyJson,expectedValueByKeyJson,keyCount);
    }

    @Test
    public void createCourierWithoutFirstNameReturnSuccess() {
        String jsonPattern = "{\"login\": \"%s\", \"password\": \"%s\"}";
        String json = String.format(jsonPattern, login, password);

        String keyJson = "ok";
        boolean expectedValueByKeyJson = true;
        int expectedCode = 201;
        int keyCount = 1;

        allStepOfSendRequestAndAsserts(json, expectedCode, keyJson, expectedValueByKeyJson, keyCount);
    }

    public <T> void allStepOfSendRequestAndAsserts(String json,int expectedCode, String keyJson,T expectedValueByKeyJson,int keyCount) {
        Response response = sendPostRequest(json);
        printResponseBodyToConsole(response);
        compareStatusCode(response, expectedCode);
        compareKeyValue(response, keyJson, expectedValueByKeyJson);
        compareKeyCount(response,keyCount);
    }

    @Step("Send POST request")
    public Response sendPostRequest(String json) {
        Response response = given()
                .header("Content-type", "application/json")
                .body(json)
                .when()
                .post(endpointCreateCourier);
        return response;
    }

    @Step("Compare value of key \"{keyJson}\"")
    public <T> void compareKeyValue(Response response, String keyJson, T expectedValue) {
        response.then().assertThat().body(keyJson, equalTo(expectedValue));
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
       // System.out.println(response.body().asString());
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
