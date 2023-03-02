import io.restassured.RestAssured;
import org.junit.*;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.Matchers.greaterThan;

public class DeleteCourierDZTest {
    private String endpoint = "/api/v1/orders";
    private static int id;

    @Before
    public void CreateCourier() {
        RestAssured.baseURI = "http://qa-scooter.praktikum-services.ru/";
        System.out.println( given()
                .header("Content-type","application/json")
                .body("{\"login\": \"ya\", \"password\": \"1234\",\"firstName\": \"ya\"}")
                .expect().statusCode(anyOf(is(201),is(409)))
                .when()
                .post("/api/v1/courier")
                .then()
                .extract().asString()
        );

        id = given()
                .header("Content-type","application/json")
                .body("{\"login\": \"ya\", \"password\": \"1234\"}")
                .expect().statusCode(200).body("id", greaterThan(0))
                .when()
                .post("/api/v1/courier/login")
                .then()
                .extract().path("id");
        System.out.println(id);
    }

    @Test
    public void deleteCourierReturnTrue200() {

        given()
                .header("Content-type","application/json")
                .pathParam("id",id)
                .body(String.format("{\"id\": \"%d\"}",id))
                .when()
                .delete("/api/v1/courier/{id}")
                .then()
                .statusCode(200).body("ok",is(true));
    }

    @Test
    public void deleteCourierWithoutIdReturnFailed400() {

        given()
                .header("Content-type","application/json")
               // .body(String.format("{\"id\": \"%d\"}",id))
                .when()
                .delete("/api/v1/courier/")
                .then()
                .statusCode(400).body("message",equalTo("Недостаточно данных для удаления курьера"));
    }

    @Test
    public void deleteCourierWrongIdReturnFailed404() {

        given()
                .header("Content-type","application/json")
                .pathParam("id",id+1)
                .body(String.format("{\"id\": \"%d\"}",id))
                .when()
                .delete("/api/v1/courier/{id}")
                .then()
                .statusCode(404).body("message",equalTo("Курьера с таким id нет"));
    }

    @After
    public void deleteData() {
        Integer id = given()
                .header("Content-type","application/json")
                .body("{\"login\": \"ya\", \"password\": \"1234\"}")
                .when()
                .post("/api/v1/courier/login")
                .then()
                .extract().path("id");

        if(id != null) {

            System.out.println("After " +
                    given()
                            .header("Content-type", "application/json")
                            .pathParam("id", id)
                            .body("{\"id\": \"{id}\"}")
                            .expect()
                            .statusCode(200).body("ok", is(true))
                            .when()
                            .delete("/api/v1/courier/{id}").then().extract().statusCode());
        }
    }

}
