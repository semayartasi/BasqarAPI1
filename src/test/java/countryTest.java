
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.http.Cookies;
import model.Country;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;
public class countryTest {
    private Cookies cookies;
@BeforeClass
    public void authenticate(){
       RestAssured.baseURI="https://test-basqar.mersys.io";
        Map<String,String> credentials=new HashMap<>();
        credentials.put("username","nigeria_tenant_admin");
        credentials.put("password","TnvLOl54WxR75vylop2A");

        cookies = given()
            .body(credentials)
            .contentType(ContentType.JSON)
            .when()
            .post("/auth/login")
            .then()
            .statusCode(200)
            .extract().response().getDetailedCookies()
            ;
        System.out.println(cookies.asList());
}

    @Test
    public void getBasePath(){
        given()

                .when()
                .get("https://test-basqar.mersys.io/")
                .then()
                .statusCode(200)

                ;
    }


    @Test
    public void getCountries(){
        given()
                .cookies(cookies)
                .when()
                .get("https://test-basqar.mersys.io/school-service/api/countries")
                .then()
                .statusCode(200)
        ;
    }
@Test
    public void createCountry(){
    Country country=new Country();
    country.setName("Sema");
    country.setCode("SY");
//creating country
    String countryID=given()
            .cookies(cookies)
            .body(country)
            .contentType(ContentType.JSON)
            .when()
            .post("/school-service/api/countries")
            .then()
            .statusCode(201)
            .extract().jsonPath().getString("id")//because if when delete it is going to need
            ;



//delete part
    given()
            .cookies(cookies)
            .when()
            .delete("/school-service/api/countries/"+countryID)
            .then()
            .statusCode(200)
            ;


}
    @Test
    public void editCountry(){
        Country country=new Country();
        country.setName("Sema");
        country.setCode("SY");
//creating country
        String countryID=given()
                .cookies(cookies)
                .body(country)
                .contentType(ContentType.JSON)
                .when()
                .post("/school-service/api/countries")
                .then()
                .statusCode(201)
                .extract().jsonPath().getString("id")//because if when delete it is going to need
                ;
        //Editing country
        country.setId(countryID);
        country.setName("Sema Edited");
        country.setCode("Code Edited");
        given()
                .cookies(cookies)
                .body(country)
                .contentType(ContentType.JSON)
                .when()
                .put("/school-service/api/countries")
                .then()
                .statusCode(200)
        .body("name",equalTo(country.getName()))
        .body("code",equalTo(country.getCode()))
        ;

//delete part
        given()
                .cookies(cookies)
                .when()
                .delete("/school-service/api/countries/"+countryID)
                .then()
                .statusCode(200)
        ;

    }

    @Test
    public void negativeCreateCountry(){
        Country country=new Country();
        country.setName("Sema");
        country.setCode("SY");
//creating country
        String countryID=given()
                .cookies(cookies)
                .body(country)
                .contentType(ContentType.JSON)
                .when()
                .post("/school-service/api/countries")
                .then()
                .statusCode(201)
                .extract().jsonPath().getString("id")//because if when delete it is going to need
                ;



//delete part
        given()
                .cookies(cookies)
                .when()
                .delete("/school-service/api/countries/"+countryID)
                .then()
                .statusCode(200)
        ;


    }

}
