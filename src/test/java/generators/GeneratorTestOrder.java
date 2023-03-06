package generators;

import com.github.javafaker.Faker;
import jsonobjects.Orders;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class GeneratorTestOrder {
    private static String firstName() { return new Faker().name().firstName(); };
    private static String lastName() { return new Faker().name().lastName(); };
    private static String address() { return new Faker().address().fullAddress(); };
    private static int metroStation() { return new Faker().number().numberBetween(1,10);}
    private static String phone() { return new Faker().phoneNumber().phoneNumber();}
    private static int rentTime() { return new Faker().number().numberBetween(1,10);}
    private static String deliveryDate() { return new SimpleDateFormat("yyyy-MM-dd").format(new Faker().date().future(10,1, TimeUnit.DAYS));}
    private static String comment() { return new Faker().internet().uuid();}

    public static Orders getOrder(List<String> color) {
        return new Orders(firstName(), lastName(), address(),metroStation(), phone(), rentTime(), deliveryDate(),
                comment(), color);
    }
}
