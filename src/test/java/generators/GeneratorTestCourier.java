package generators;

import jsonobjects.Courier;
import com.github.javafaker.Faker;

public class GeneratorTestCourier {

    private static String login() { return new Faker().name().username();}
    private static String password() { return new Faker().internet().password(1,10);}
    private static String username() { return new Faker().name().firstName();}

    public static Courier getValidCourier() {
        return new Courier(login(), password(), username());
    }

    public static Courier getWithoutPasswordCourier() {
        return new Courier(login(), null, username());
    }

    public static Courier getWithoutLoginCourier() {
        return new Courier(null, password(), username());
    }

    public static Courier getWithoutFirstNameCourier() {
        return new Courier(login(), password(), null);

    }
}
