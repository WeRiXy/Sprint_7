package generators;

import com.github.javafaker.Faker;
import jsonobjects.AccountData;
import jsonobjects.Courier;

public class GeneratorAccountData {

    public static String login() { return new Faker().name().username();}
    public static String password() { return new Faker().internet().password(1,10);}

    public static AccountData getAccountData( String login, String password) {
        return new AccountData(login, password);
    }
    public static AccountData getAccountDataFromCourier(Courier courier) {
        return new AccountData(courier.getLogin(), courier.getPassword());
    }
}
