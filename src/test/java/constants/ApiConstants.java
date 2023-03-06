package constants;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;

public class ApiConstants {
    public static final String BASE_URI = "http://qa-scooter.praktikum-services.ru";

    public static final String ENDPOINT_CREATE_COURIER = "/api/v1/courier";
    public static final String ENDPOINT_LOGIN_COURIER = "/api/v1/courier/login";
    public static final String ENDPOINT_DELETE_COURIER ="/api/v1/courier/{id}";


    public static final String ENDPOINT_CREATE_ORDER = "/api/v1/orders";
    public static final String ENDPOINT_GET_LIST_ORDERS ="/api/v1/orders";


    public static final RequestSpecification  REQUEST_SPEC = new RequestSpecBuilder()
            .setContentType(ContentType.JSON)
            .setBaseUri(BASE_URI)
            .build();

}
