package general_steps;

import generators.GeneratorAccountData;
import io.qameta.allure.Step;
import jsonobjects.AccountData;
import jsonobjects.Courier;
import requests.CourierAPI;

import static requests.CourierAPI.loginCourier;

public class CourierGeneralSteps {
    @Step("Get id and delete Courier")
    public static void delete(Courier courier) {
        delete(GeneratorAccountData.getAccountDataFromCourier(courier));
    }
    @Step("Get id and delete Courier")
    public static void delete(AccountData accountData) {
        if(accountData.getLogin() != null && accountData.getPassword() != null) {
            Integer id = loginCourier(accountData).path("id");
            if (id != null) {
                CourierAPI.deleteCourier(id).then().statusCode(200);
            }
        }
    }
}
