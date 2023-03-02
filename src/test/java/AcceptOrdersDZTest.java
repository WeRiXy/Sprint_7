import io.restassured.RestAssured;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThan;

public class AcceptOrdersDZTest {
    private String jsonOrder;
    private static int id;
    private static int courierId;

    {
         String jsonPattern = "{\"firstName\": \"%s\", \"lastName\": \"%s\", \"address\": \"%s\", \"metroStation\": %d, \"phone\": \"%s\", \"rentTime\": %d, \"deliveryDate\": \"%s\", \"comment\": \"%s\", \"color\": [ \"%s\" ]}";
        jsonOrder = String.format(jsonPattern,
                "Naruto", "Uchiha", "Konoha, 142 apt.",4,"+7 800 355 35 35",5,"2020-06-06","Saske, come back to Konoha","BLACK");
    }
    @Before
    public void initSet() {
        RestAssured.baseURI = "http://qa-scooter.praktikum-services.ru";
    }

    @Before
    public void createOrderAndCourier() {
        given()
                .header("Content-type","application/json")
                .body("{\"login\": \"ya\", \"password\": \"1234\",\"firstName\": \"ya\"}")
                .expect().statusCode(anyOf(is(201),is(409)))
                .when()
                .post("/api/v1/courier");

        courierId = given()
                .header("Content-type","application/json")
                .body("{\"login\": \"ya\", \"password\": \"1234\"}")
                .expect().statusCode(200).body("id", greaterThan(0))
                .when()
                .post("/api/v1/courier/login")
                .then()
                .extract().path("id");

        id = given()
               .get("/api/v1/orders")
               .then()
               .statusCode(200).extract().path("orders[0].id");
    }


    @Test
    public void acceptOrderValidIdReturnTrue200() {
        System.out.println(id);
        System.out.println(courierId);

        given()
                .pathParam("id", id)
                .queryParam("courierId",courierId)
                .when()
                .put("/api/v1/orders/accept/{id}")
                .then()
                .statusCode(200)
                .body("ok",is(true));
    }

    @Test
    public void acceptOrderWithoutCourierIdReturnFailed400() {
        System.out.println(id);
        System.out.println(courierId);

        given()
                .pathParam("id", id)
               // .queryParam("courierId",courierId)
                .when()
                .put("/api/v1/orders/accept/{id}")
                .then()
                .statusCode(400)
                .body("message",equalTo("Недостаточно данных для поиска"));
    }

    @Test
    public void acceptOrderInvalidCourierIdReturnFailed404() {
        System.out.println(id);
        System.out.println(courierId);

        given()
                .pathParam("id", id)
                 .queryParam("courierId",courierId+1)
                .when()
                .put("/api/v1/orders/accept/{id}")
                .then()
                .statusCode(404)
                .body("message",equalTo("Курьера с таким id не существует"));
    }

    @Test
    public void acceptOrderWithoutIdOrderReturnFailed400() {
        System.out.println(id);
        System.out.println(courierId);

        given()
              //  .pathParam("id", id)
                .queryParam("courierId",courierId)
                .when()
                .put("/api/v1/orders/accept")
                .then()
                .statusCode(400)
                .body("message",equalTo("Недостаточно данных для поиска"));
    }

    @Test
    public void acceptOrderInvalidIdOrderReturnFailed400() {
        System.out.println(id);
        System.out.println(courierId);

        given()
                .pathParam("id", 0)
                .queryParam("courierId",courierId)
                .when()
                .put("/api/v1/orders/accept/{id}")
                .then()
                .statusCode(404)
                .body("message",equalTo("Заказа с таким id не существует"));
    }

    @After
    public void deleteCourier() {

        if(courierId != 0) {
            System.out.println("After " +
                    given()
                            .header("Content-type", "application/json")
                            .pathParam("id", courierId)
                            .body(String.format("{\"id\": \"%d\"}", id))
                            .expect()
                            .statusCode(200).body("ok", is(true))
                            .when()
                            .delete("/api/v1/courier/{id}").then().extract().statusCode());
        }
    }
}

