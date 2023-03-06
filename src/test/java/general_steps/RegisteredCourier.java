package general_steps;

import generators.GeneratorAccountData;
import generators.GeneratorTestCourier;
import jsonobjects.AccountData;
import jsonobjects.Courier;
import requests.CourierAPI;

public class RegisteredCourier {
    public final Courier courier;
    public final AccountData accountData;
    {
        courier = GeneratorTestCourier.getValidCourier();
        accountData = GeneratorAccountData.getAccountDataFromCourier(courier);
        CourierAPI.createCourier(courier).then().statusCode(201);
    }
}
