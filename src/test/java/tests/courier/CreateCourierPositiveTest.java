package tests.courier;

import general_steps.CourierGeneralSteps;
import generators.GeneratorTestCourier;
import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import io.restassured.response.Response;
import jsonobjects.Courier;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import requests.CourierAPI;

import static asserts.Asserts.*;

@Epic(value = "Courier tests")
@Feature(value = "Create courier")
@Story(value = "Positive tests")
@RunWith(Parameterized.class)
public class CreateCourierPositiveTest<T> {
    private Courier courier;
    private int expectedCode;
    private String keyJson;
    private T expectedValueByKeyJson;
    private int keyCount;

    public CreateCourierPositiveTest(Courier courier, int expectedCode, String keyJson, T expectedValueByKeyJson, int keyCount) {
        this.courier = courier;
        this.expectedCode = expectedCode;
        this.keyJson = keyJson;
        this.expectedValueByKeyJson = expectedValueByKeyJson;
        this.keyCount = keyCount;
    }


    @Parameterized.Parameters(name = "{2} - {0}")
    public static Object[][] getData() {
        return new Object[][] {
                { GeneratorTestCourier.getValidCourier(), 201,"ok",true,1 },
                { GeneratorTestCourier.getWithoutFirstNameCourier(), 201,"ok",true,1 },
        };
    }
    @Test
    @Description("Check of create courier with valid data")
    public void  createCourier() {
        Response response = CourierAPI.createCourier(courier);

        compareStatusCode(response, expectedCode);
        compareKeyValue(response, keyJson, expectedValueByKeyJson);
        compareKeyCount(response,keyCount);
    }

    @After
    public void deleteTestData() {
        CourierGeneralSteps.delete(courier);
    }
}