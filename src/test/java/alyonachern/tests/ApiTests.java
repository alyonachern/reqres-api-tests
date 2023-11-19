package alyonachern.tests;

import alyonachern.models.*;
import org.junit.jupiter.api.Test;

import static alyonachern.specs.RegisterSpecs.registerRequestSpecs;
import static alyonachern.specs.RegisterSpecs.registerResponseSpecs;
import static alyonachern.specs.UsersSpecs.userRequestSpecs;
import static alyonachern.specs.UsersSpecs.userResponseSpecs;
import static io.qameta.allure.Allure.step;
import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class ApiTests extends BaseTest {

    @Test
    void checkSupportTextInListUsersTest() {
        UserListResponse response = step("Make request for list of user", () ->
                given(userRequestSpecs)
                        .when()
                        .get("users?page=2")
                        .then()
                        .spec(userResponseSpecs)
                        .extract().as(UserListResponse.class));

        step("Verify text in support", () ->
                assertEquals("To keep ReqRes free, contributions towards server costs are appreciated!",
                        response.getSupport().getText()));
    }

    @Test
    void checkSingleSourceNotFoundTest() {
        step("Make request for not found", () ->
                given(userRequestSpecs)
                        .when()
                        .get("/users/23")
                        .then()
                        .spec(userResponseSpecs)
                        .statusCode(404));
    }

    @Test
    void checkSuccessfulRegisterTest() {
        RegisterRequestModel regBody = new RegisterRequestModel();
        regBody.setEmail("eve.holt@reqres.in");
        regBody.setPassword("pistol");

        RegisterResponseModel response = step("Make register request", () ->
                given(registerRequestSpecs)
                        .body(regBody)
                        .when()
                        .post("/register")
                        .then()
                        .spec(registerResponseSpecs)
                        .extract().as(RegisterResponseModel.class));

        step("Verify Response", () ->
                assertAll(
                        () -> assertEquals("4", response.getId()),
                        () -> assertEquals("QpwL5tke4Pnpja7X4", response.getToken())
                ));
    }

    @Test
    void checkSuccessfulUpdateTest() {
        UserRequestModel userBody = new UserRequestModel();
        userBody.setName("morpheus");
        userBody.setJob("zion resident");

        UserResponseModel response = step("Make update request", () ->
                given(userRequestSpecs)
                        .body(userBody)
                        .when()
                        .put("/users/2")
                        .then()
                        .spec(userResponseSpecs)
                        .extract().as(UserResponseModel.class));

        step("Verify changes", () ->
                assertAll(
                        () -> assertEquals("morpheus", response.getName()),
                        () -> assertEquals("zion resident", response.getJob())
                ));
    }

    @Test
    void checkSuccessfulDelete() {
        step("Make delete request", () ->
                given(userRequestSpecs)
                        .when()
                        .delete("/users/2")
                        .then()
                        .spec(userResponseSpecs)
                        .statusCode(204));
    }
}
