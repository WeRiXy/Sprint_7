import io.restassured.RestAssured;
import org.junit.Before;
import org.junit.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;

public class GetOrderByTrackDZTest {
    private static int id;


    @Before
    public void setUp() {
        RestAssured.baseURI = "http://qa-scooter.praktikum-services.ru";
        id = given()
                .get("/api/v1/orders")
                .then()
                .statusCode(200).extract().path("orders[0].track");
        System.out.println(id);
    }

    @Test
    public void getOrderValidTrackReturnSuccess200() {
        given()
                .queryParam("t", id)
                .get("/api/v1/orders/track")
                .then()
                .statusCode(200).body("order.track", is(id));
    }

    @Test
    public void getOrderWithoutTrackReturnFailed400() {
        given()
              //  .queryParam("t", id)
                .get("/api/v1/orders/track")
                .then()
                .statusCode(400).body("message", equalTo("Недостаточно данных для поиска"));
    }

    @Test
    public void getOrderInvalidTrackReturnFailed404() {
        given()
                .queryParam("t", 0)
                .get("/api/v1/orders/track")
                .then()
                .statusCode(404).body("message", equalTo("Заказ не найден"));
    }
}