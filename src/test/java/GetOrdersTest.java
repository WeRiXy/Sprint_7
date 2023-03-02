import io.restassured.RestAssured;
import org.junit.*;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.Matchers.greaterThan;

public class GetOrdersTest {
    private final String login = "ya";
    private final String password = "1234";
    private String endpoint = "/api/v1/orders";


    @Before
    public void setUp() {
        RestAssured.baseURI = "http://qa-scooter.praktikum-services.ru/";
    }
    @Test
    public void getOrderWithoutParamsReturnAllListOrders() {

        given()
                .get(endpoint)
                .then()
                .body("orders", notNullValue())
                .and().statusCode(200);
    }

}
