package tests.courier;

import general_steps.CourierGeneralSteps;
import general_steps.RegisteredCourier;
import generators.GeneratorAccountData;
import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import jsonobjects.AccountData;
import jsonobjects.Courier;
import org.hamcrest.Matcher;
import org.junit.*;
import requests.CourierAPI;

import static asserts.Asserts.*;
import static org.hamcrest.Matchers.greaterThan;

@Epic(value = "Courier tests")
@Feature(value = "Login courier")
@Story(value = "Positive tests")
public class LoginCourierPositiveTest {
    private Courier courier;

    @Before
    public void setUp() {
        courier = new RegisteredCourier().courier;
    }

    @Test
    @DisplayName("Login courier with valid account data")
    @Description("Expected status code - 200")
    public void  loginCourierWithValidAccountData() {
        AccountData accountData = GeneratorAccountData.getAccountDataFromCourier(courier);
        Response response = CourierAPI.loginCourier(accountData);

        String keyJson = "id";
        Matcher expectedValueByKeyJson = greaterThan(0);
        int expectedCode = 200;
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
