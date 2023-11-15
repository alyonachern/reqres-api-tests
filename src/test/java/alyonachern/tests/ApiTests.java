package alyonachern.tests;

import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.JSON;
import static org.hamcrest.Matchers.is;

public class ApiTests extends BaseTest {

    @Test
    void checkSupportTextInListUsersTest() {
        given()
                .log().uri()
                .log().method()
                .when()
                .get("users?page=2")
                .then()
                .log().status()
                .log().body()
                .body("support.text",is("To keep ReqRes free, contributions towards server costs are appreciated!"));
    }

    @Test
    void checkSingleSourceNotFoundTest() {
        given()
                .log().uri()
                .log().method()
                .when()
                .get("/users/23")
                .then()
                .log().status()
                .log().body()
                .statusCode(404);
    }

    @Test
    void checkSuccessfulRegisterTest() {
        given()
                .log().uri()
                .log().method()
                .body("{\"email\": \"eve.holt@reqres.in\",\n\"password\": \"pistol\"}")
                .contentType(JSON)
                .when()
                .post("/register")
                .then()
                .log().status()
                .log().body()
                .statusCode(200)
                .body("id",is(4))
                .body("token", is("QpwL5tke4Pnpja7X4"));
    }

    @Test
    void checkSuccessfulUpdateTest() {
        given()
                .log().uri()
                .log().method()
                .body("{\"name\": \"morpheus\",\n\"job\": \"zion resident\"}")
                .contentType(JSON)
                .when()
                .put("/users/2")
                .then()
                .log().status()
                .log().body()
                .statusCode(200);
    }

    @Test
    void checkSuccessfulDelete() {
        given()
                .log().uri()
                .log().method()
                .when()
                .delete("/users/2")
                .then()
                .log().status()
                .log().body()
                .statusCode(204);
    }
}
