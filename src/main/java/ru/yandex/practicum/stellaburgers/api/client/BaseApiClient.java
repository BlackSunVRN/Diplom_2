package ru.yandex.practicum.stellaburgers.api.client;

import io.qameta.allure.Description;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;

public class BaseApiClient {

    private static final String BASE_URI = "https://stellarburgers.nomoreparties.site/";
    protected static final String API_ORDERS = "/api/orders";
    protected static final String API_AUTH_LOGIN = "/api/auth/login";
    protected static final String API_AUTH_REGISTER = "/api/auth/register";
    protected static final String API_AUTH_USER = "/api/auth/user";


    @Description("Спецификация для использования JSON в запросе")
    public static RequestSpecification getReqSpecJSON() {
        return new RequestSpecBuilder()
                .setBaseUri(BASE_URI)
                .log(LogDetail.ALL)
                .setContentType(ContentType.JSON).build();
    }

    @Description("Спецификация для использования Bearer-токена в запросе")
    public static RequestSpecification getReqSpecToken(String bearerToken) {
        return new RequestSpecBuilder()
                .setBaseUri(BASE_URI)
                .log(LogDetail.ALL)
                .build().header("Authorization", bearerToken);
    }

    @Description("Спецификация для использования Bearer-токена и JSON в запросе")
    public static RequestSpecification getReqSpecTokenJSON(String bearerToken) {
        return new RequestSpecBuilder()
                .setBaseUri(BASE_URI)
                .log(LogDetail.ALL)
                .setContentType(ContentType.JSON)
                .build().header("Authorization", bearerToken);
    }
}
