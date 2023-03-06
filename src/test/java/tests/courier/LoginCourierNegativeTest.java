package tests.courier;

import general_steps.CourierGeneralSteps;
import general_steps.RegisteredCourier;
import generators.GeneratorAccountData;
import io.qameta.allure.*;
import io.restassured.response.Response;
import jsonobjects.AccountData;
import org.junit.*;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import requests.CourierAPI;

import static asserts.Asserts.*;

@Epic(value = "Courier tests")
@Feature(value = "Login courier")
@Story(value = "Negative tests")
@RunWith(Parameterized.class)
public class LoginCourierNegativeTest <T> {
    private static AccountData accountData = new RegisteredCourier().accountData;
    private AccountData newAccountData;
    private String login;
    private String password;
    private int expectedCode;

    private String keyJson;
    private T expectedValueByKeyJson;
    private int keyCount;

    public LoginCourierNegativeTest(String login, String password, int expectedCode, String keyJson, T expectedValueByKeyJson, int keyCount) {
        this.login = login;
        this.password = password;
        this.expectedCode = expectedCode;
        this.keyJson = keyJson;
        this.expectedValueByKeyJson = expectedValueByKeyJson;
        this.keyCount = keyCount;
    }


    @Parameterized.Parameters(name = "{2} - Login: {0}, password: {1}")
    public static Object[][] getData() {
        return new Object[][] {
                { accountData.getLogin(), null, 400,"message","Недостаточно данных для входа",1 },
                { null, accountData.getPassword(), 400,"message","Недостаточно данных для входа",1 },
                { null, null, 400,"message","Недостаточно данных для входа",1 },
                { GeneratorAccountData.login(), accountData.getPassword(), 404,"message","Учетная запись не найдена",1 },
                { accountData.getLogin(), GeneratorAccountData.password(), 404,"message","Учетная запись не найдена",1 },
                { GeneratorAccountData.login(), GeneratorAccountData.password(), 404,"message","Учетная запись не найдена",1 }

        };
    }

    @BeforeClass
    public static void addAttachment() {
        Allure.addAttachment("Account data of registered courier", accountData.toString());
    }

    @Before
    public void setUp() {
        newAccountData = GeneratorAccountData.getAccountData(login,password);
        Allure.addAttachment("Account data of registered courier", accountData.toString());
    }

    @Test
    @Description("Check for the impossibility of logging with invalid account data")
    public void  loginCourier() {

        Response response = CourierAPI.loginCourier(newAccountData);

        compareStatusCode(response, expectedCode);
        compareKeyValue(response, keyJson, expectedValueByKeyJson);
        compareKeyCount(response,keyCount);
    }

    @AfterClass
    public static void deleteTestData() {
        CourierGeneralSteps.delete(accountData);
    }
}
