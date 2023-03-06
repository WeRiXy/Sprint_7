package tests.orders;

import generators.GeneratorTestOrder;
import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Story;
import io.restassured.response.Response;
import jsonobjects.Orders;
import org.hamcrest.Matcher;
import org.junit.*;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import requests.OrdersAPI;
import java.util.ArrayList;
import java.util.List;

import static asserts.Asserts.*;
import static org.hamcrest.Matchers.greaterThan;

@Epic(value = "Orders tests")
@Story(value = "Create order")
@RunWith(Parameterized.class)
public class CreateOrdersTest {
    private List<String> color;
    private Orders orders;
    private String keyJson;
    private Matcher matcherForExpectedValueByKeyJson;
    private int expectedCode;
    private int keyCount;


    public CreateOrdersTest(List<String> color, int expectedCode, String keyJson, Matcher matcherForExpectedValueByKeyJson, int keyCount) {
        this.color=color;
        this.keyJson = keyJson;
        this.matcherForExpectedValueByKeyJson = matcherForExpectedValueByKeyJson;
        this.expectedCode = expectedCode;
        this.keyCount = keyCount;
    }

    @Parameterized.Parameters(name = "COLOR: {0}")
    public static Object[][] getData() {
        return new Object[][] {
                {List.of("BACK"),201,"track", greaterThan(0),1 },
                {List.of("GRAY"),201,"track", greaterThan(0),1 },
                {List.of("BACK","GRAY"),201,"track", greaterThan(0),1 },
                {new ArrayList<>(),201,"track", greaterThan(0),1 },
        };
    }


    @Before
    public void setUp() {
        orders = GeneratorTestOrder.getOrder(color);
    }

    @Test
    @Description("Expected: status code - 201, valid track number, valid response json")
    public void createOrder() {
        Response response= OrdersAPI.createOrder(orders);;

        compareStatusCode(response, expectedCode);
        compareKeyValue(response, keyJson, matcherForExpectedValueByKeyJson);
        compareKeyCount(response,keyCount);
    }
}
