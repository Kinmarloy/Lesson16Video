package tests;

import io.restassured.response.Response;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Cookie;

import java.util.Map;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.$x;
import static com.codeborne.selenide.Selenide.open;
import static com.codeborne.selenide.WebDriverRunner.getWebDriver;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;

public class LoginTests extends TestBase{

    @Test
    void loginWithUiTest (){
        //autharize
        // qaguru@qa.guru qaguru@qa.guru1
        open("/login");
        $x("//input[@id = 'Email']").val("qaguru@qa.guru");
        $x("//input[@id = 'Password']").val("qaguru@qa.guru1").pressEnter();

        //verification
        $x("//a[@class = 'account']").shouldHave(text("qaguru@qa.guru"));
    }


    @Test
    void loginWithCookiesTest (){
        //autharize
        // qaguru@qa.guru qaguru@qa.guru1

        Map<String, String> cookiesMap =
                given()
                        .contentType("application/x-www-form-urlencoded; charset=UTF-8")
                        .formParam("Email", "qaguru@qa.guru")
                        .formParam("Password", "qaguru@qa.guru1")
                        .when()
                        .post("/login")
                        .then()
                        .statusCode(302)
                        .log().body()
                     //   .body("success", is(true))
                        .extract().cookies();


        //verification
        open("http://demowebshop.tricentis.com/Themes/DefaultClean/Content/images/logo.png");
        getWebDriver().manage().addCookie(new Cookie("Nop.customer",cookiesMap.get("Nop.customer")));
        getWebDriver().manage().addCookie(new Cookie("NOPCOMMERCE.AUTH", cookiesMap.get("NOPCOMMERCE.AUTH")));
        getWebDriver().manage().addCookie(new Cookie("ARRAffinity", cookiesMap.get("ARRAffinity")));

        open("");
        $x("//a[@class = 'account']").shouldHave(text("qaguru@qa.guru"));
    }
}
