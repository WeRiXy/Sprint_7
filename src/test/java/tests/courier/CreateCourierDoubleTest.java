package tests.courier;

import general_steps.CourierGeneralSteps;
import generators.GeneratorTestCourier;
import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import jsonobjects.Courier;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import requests.CourierAPI;

import static asserts.Asserts.*;

@Epic(value = "Courier tests")
@Feature(value = "Create courier")
@Story(value = "Negative tests")
public class CreateCourierDoubleTest<T> {
    private Courier courier;


    @Before
    public void setUp() {
        courier = GeneratorTestCourier.getValidCourier();
        CourierAPI.createCourier(courier).then().statusCode(201);
    }

    @Test
    @DisplayName("Сreatе the same courier twice")
    @Description("Check of cannot create courier when it already exists in BD")
    public void createCourierDouble() {
        Response response = CourierAPI.createCourier(courier);

        String keyJson = "message";
        String expectedValueByKeyJson = "Этот логин уже используется";
        int expectedCode = 409;
        int keyCount = 1;

        compareStatusCode(response, expectedCode);
        compareKeyValue(response, keyJson, expectedValueByKeyJson);
        compareKeyCount(response,keyCount);
    }

    @After
    public void deleteTestData() {
        CourierGeneralSteps.delete(courier);
    }
}
