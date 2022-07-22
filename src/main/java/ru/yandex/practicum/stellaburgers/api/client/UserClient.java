package ru.yandex.practicum.stellaburgers.api.client;

import com.google.gson.JsonObject;
import io.qameta.allure.Step;
import io.restassured.response.Response;
import ru.yandex.practicum.stellaburgers.api.model.User;

import static io.restassured.RestAssured.given;

public class UserClient extends BaseApiClient {

    @Step("Создание пользователя")
    public Response createUser(User user) {
        return given()
                .spec(getReqSpecJSON())
                .body(user)
                .when()
                .post(API_AUTH_REGISTER);
    }

    @Step("Создание пользователя только с почтой и паролем")
    public Response createUserManual(String email, String password) {
        User user = new User(email, password);
        return given()
                .spec(getReqSpecJSON())
                .body(user)
                .when()
                .post(API_AUTH_REGISTER);
    }

    @Step("Логин пользователя по почте и паролю")
    public Response loginUser(String email, String password) {
        User user = new User(email, password);
        return given()
                .spec(getReqSpecJSON())
                .body(user)
                .when()
                .post(API_AUTH_LOGIN);
    }

    @Step("Получение bearer-токена авторизованного пользователя")
    public String getAccessToken(User user) {
        return given()
                .spec(getReqSpecJSON())
                .body(user)
                .when()
                .post(API_AUTH_LOGIN)
                .jsonPath()
                .getString("accessToken");
    }

    @Step("Получение refresh-токена авторизованного пользователя")
    public String getRefreshToken(User user) {
        return given()
                .spec(getReqSpecJSON())
                .body(user)
                .when()
                .post(API_AUTH_LOGIN)
                .jsonPath()
                .getString("refreshToken");
    }

    @Step("Удаление пользователя")
    public Response deleteUser(User user) {
        String accessToken = getAccessToken(user);
        return given()
                .spec(getReqSpecToken(accessToken))
                .when()
                .delete(API_AUTH_USER);
    }

    @Step("Удаление пользователя с ручным указанием почты и пароля")
    public void deleteUser(String email, String password) {
        User user = new User(email, password);
        String accessToken = getAccessToken(user);
            given()
                .spec(getReqSpecToken(accessToken))
                .when()
                .delete(API_AUTH_USER);
    }

    @Step("Получение информации об авторизованном пользователе")
    public Response getUserInfo(User user) {
        String accessToken = getAccessToken(user);
        return given()
                .spec(getReqSpecToken(accessToken))
                .when()
                .get(API_AUTH_USER);
    }

    @Step("Обновление почты, пароля и имени авторизованного пользователя")
    public Response updateUserEmailPasswordName(User user, String newEmail, String newPassword, String newName) {
        User userUpdate = new User(newEmail, newPassword, newName);
        String accessToken = getAccessToken(user);
        return given()
                .spec(getReqSpecTokenJSON(accessToken))
                .body(userUpdate)
                .when()
                .patch(API_AUTH_USER);
    }

    @Step("Обновление почты, пароля авторизованного пользователя")
    public Response updateUserEmailPasswordName(User user, String newEmail, String newPassword) {
        JsonObject requestParams = new JsonObject();
        requestParams.addProperty("email", newEmail);
        requestParams.addProperty("password", newPassword);
        String accessToken = getAccessToken(user);
        return given()
                .spec(getReqSpecTokenJSON(accessToken))
                .body(requestParams)
                .when()
                .patch(API_AUTH_USER);
    }

    @Step("Обновление почты и имени авторизованного пользователя")
    public Response updateUserEmailName(User user, String newEmail, String newName) {
        JsonObject requestParams = new JsonObject();
        requestParams.addProperty("email", newEmail);
        requestParams.addProperty("name", newName);
        String accessToken = getAccessToken(user);
        return given()
                .spec(getReqSpecTokenJSON(accessToken))
                .body(requestParams)
                .when()
                .patch(API_AUTH_USER);
    }

    @Step("Обновление пароля и имени авторизованного пользователя")
    public Response updateUserPasswordName(User user, String newPassword, String newName) {
        JsonObject requestParams = new JsonObject();
        requestParams.addProperty("password", newPassword);
        requestParams.addProperty("name", newName);
        String accessToken = getAccessToken(user);
        return given()
                .spec(getReqSpecTokenJSON(accessToken))
                .body(requestParams)
                .when()
                .patch(API_AUTH_USER);
    }

    @Step("Обновление почты авторизованного пользователя")
    public Response updateUserEmail(User user, String newEmail) {
        JsonObject requestParams = new JsonObject();
        requestParams.addProperty("email", newEmail);
        String accessToken = getAccessToken(user);
        return given()
                .spec(getReqSpecTokenJSON(accessToken))
                .body(requestParams)
                .when()
                .patch(API_AUTH_USER);
    }

    @Step("Обновление пароля авторизованного пользователя")
    public Response updateUserPassword(User user, String newPassword) {
        JsonObject requestParams = new JsonObject();
        requestParams.addProperty("password", newPassword);
        String accessToken = getAccessToken(user);
        return given()
                .spec(getReqSpecTokenJSON(accessToken))
                .body(requestParams)
                .when()
                .patch(API_AUTH_USER);
    }

    @Step("Обновление имени авторизованного пользователя")
    public Response updateUserName(User user, String newName) {
        JsonObject requestParams = new JsonObject();
        requestParams.addProperty("name", newName);
        String accessToken = getAccessToken(user);
        return given()
                .spec(getReqSpecTokenJSON(accessToken))
                .body(requestParams)
                .when()
                .patch(API_AUTH_USER);
    }

    @Step("Обновление почты, пароля и имени без авторизации")
    public Response updateUserInfoWithoutAuth(String newEmail, String newPassword, String newName) {
        User userUpdate = new User(newEmail, newPassword, newName);
        return given()
                .spec(getReqSpecJSON())
                .body(userUpdate)
                .when()
                .patch(API_AUTH_USER);
    }
}
