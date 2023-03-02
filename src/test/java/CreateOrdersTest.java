import io.qameta.allure.Allure;
import io.qameta.allure.Step;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.hamcrest.Matcher;
import org.junit.*;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.ArrayList;
import java.util.List;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.Matchers.greaterThan;

@RunWith(Parameterized.class)
public class CreateOrdersTest {
    private final String login = "ya";
    private final String password = "1234";
    private String endpointCreateOrders = "/api/v1/orders";

    private String jsonPattern = "{\"firstName\": \"%s\", \"lastName\": \"%s\", \"address\": \"%s\", \"metroStation\": %d, \"phone\": \"%s\", \"rentTime\": %d, \"deliveryDate\": \"%s\", \"comment\": \"%s\", \"color\": [ %s ]}";

    private final String firstName;
    private final String lastName;
    private final String address;
    private final int metroStation;
    private final String phone;
    private final int rentTime;
    private final String deliveryDate;
    private final String comment;
    private final String color;
    private static List<Integer> orders = new ArrayList<>();



    public CreateOrdersTest(String firstName, String lastName, String address, int metroStation, String phone, int rentTime, String deliveryDate, String comment,  String color) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        this.metroStation = metroStation;
        this.phone = phone;
        this.rentTime = rentTime;
        this.deliveryDate = deliveryDate;
        this.comment = comment;
        this.color = color;


    }
    @Parameterized.Parameters
    public static Object[][] getData() {
        return new Object[][] {
                { "Naruto", "Uchiha", "Konoha, 142 apt.",4,"+7 800 355 35 35",5,"2020-06-06","Saske, come back to Konoha","\"BLACK\""},
                { "Naruto", "Uchiha", "Konoha, 142 apt.",4,"+7 800 355 35 35",5,"2020-06-06","Saske, come back to Konoha","\"BLACK\",\"GRAY\""},
                { "Naruto", "Uchiha", "Konoha, 142 apt.",4,"+7 800 355 35 35",5,"2020-06-06","Saske, come back to Konoha",""},
        };
    }

    @Before
    public void setUp() {
        RestAssured.baseURI = "http://qa-scooter.praktikum-services.ru/";
    }
    @Test
    public void createOrderWithValidDataReturnSuccess201() {
        String json = String.format(jsonPattern,
                firstName, lastName, address, metroStation, phone, rentTime, deliveryDate, comment, color);
     //   System.out.println(json);
        String keyJson = "track";
        Matcher<Integer> matcherForExpectedValueByKeyJson = greaterThan(0);
        int expectedCode = 201;
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
                .post(endpointCreateOrders);
        Integer id = response
                .then().extract().body().path("track");
        if (id != null) {
            orders.add(id);
        }
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
       //  System.out.println(response.body().asString());
        Allure.addAttachment("Received response json", response.body().asString());
    }

    @AfterClass
    public static void deleteCourierWithValidDataReturnSuccess() {
        System.out.println(orders);
            if (orders.size() != 0) {
                for(int order:orders) {
            String json = String.format("{\"track\": %d}", order);
            given()
                    .header("Content-type", "application/json")
                    .body(json)
                    .put("/api/v1/orders/cancel");
                  /*  .then().assertThat().body("ok", is(true))
                    .and().statusCode(200);*/
                    //сервис возвращает ошибку 400
                    //оставил для наглядности
                }
        }
    }
}
