package ru.yandex.practicum.stellaburgers.api.client;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import ru.yandex.practicum.stellaburgers.api.model.Order;
import ru.yandex.practicum.stellaburgers.api.model.User;

import static io.restassured.RestAssured.given;

public class OrderClient extends BaseApiClient {

    @Step("Создание заказа для авторизованного пользователя")
    public Response createOrderForUser(Order order, User user) {
        String accessToken = getAccessToken(user);
        return given()
                .spec(getReqSpecTokenJSON(accessToken))
                .body(order)
                .when()
                .post(API_ORDERS);
    }

    @Step("Создание заказа без авторизации")
    public Response createOrderWithoutAuth(Order order) {
        return given()
                .spec(getReqSpecJSON())
                .body(order)
                .when()
                .post(API_ORDERS);
    }

    @Step("Получение списка заказов авторизованного пользователя")
    public Response getUserOrdersWithAuth(User user) {
        String accessToken = getAccessToken(user);
        return given()
                .spec(getReqSpecToken(accessToken))
                .when()
                .get(API_ORDERS);
    }

    @Step("Получение списка заказов без авторизации")
    public Response getUserOrdersWithoutAuth() {
        return given()
                .spec(getReqSpecJSON())
                .when()
                .get(API_ORDERS);
    }

    @Step("Получение bearer-токена авторизованного пользователя")
    private String getAccessToken(User user) {
        return given()
                .spec(getReqSpecJSON())
                .body(user)
                .when()
                .post(API_AUTH_LOGIN)
                .jsonPath()
                .getString("accessToken");
    }

}
