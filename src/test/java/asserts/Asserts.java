package asserts;

import io.qameta.allure.Allure;
import io.qameta.allure.Step;
import io.restassured.response.Response;
import org.hamcrest.CoreMatchers;
import org.hamcrest.Matcher;

import static org.hamcrest.CoreMatchers.equalTo;

public class Asserts {

    @Step("Compare value of key \"{keyJson}\" with \"{expectedValueByKeyJson}\"")
    public static <T> void compareKeyValue(Response response, String keyJson, T expectedValueByKeyJson) {
        try {
            if (Matcher.class.isAssignableFrom(expectedValueByKeyJson.getClass())) {
                response.then().assertThat().body(keyJson, (Matcher) expectedValueByKeyJson);
            } else {
                response.then().assertThat().body(keyJson, equalTo(expectedValueByKeyJson));
            }
        } catch (AssertionError e) {
            Allure.addAttachment("Response json", response.body().asPrettyString());
            throw e;
        }
    }

    @Step("Compare status code with \"{expectedCode}\"")
    public static void compareStatusCode(Response response, int expectedCode) {
        response.then().statusCode(expectedCode);
    }

    @Step("Compare the number of json keys with \"{keyCount}\"")
    public static void compareKeyCount(Response response, int keyCount) {
        try {
            response.then().body("size()", CoreMatchers.is(keyCount));
        } catch (AssertionError e) {
            Allure.addAttachment("Response json", response.body().asPrettyString());
            throw e;
        }
    }
}
