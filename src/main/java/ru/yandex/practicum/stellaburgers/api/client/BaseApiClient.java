package ru.yandex.practicum.stellaburgers.api.client;

import io.qameta.allure.Description;
import io.qameta.allure.Step;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;

public class BaseApiClient {

    @Description("Спецификация для использования JSON в запросе")
    public static RequestSpecification getReqSpecJSON() {
        return new RequestSpecBuilder()
                .setBaseUri("https://stellarburgers.nomoreparties.site/")
                .log(LogDetail.ALL)
                .setContentType(ContentType.JSON).build();
    }

    @Description("Спецификация для использования Bearer-токена в запросе")
    public static RequestSpecification getReqSpecToken(String bearerToken) {
        return new RequestSpecBuilder()
                .setBaseUri("https://stellarburgers.nomoreparties.site/")
                .log(LogDetail.ALL)
                .build().header("Authorization", bearerToken);
    }

    @Description("Спецификация для использования Bearer-токена и JSON в запросе")
    public static RequestSpecification getReqSpecTokenJSON(String bearerToken) {
        return new RequestSpecBuilder()
                .setBaseUri("https://stellarburgers.nomoreparties.site/")
                .log(LogDetail.ALL)
                .setContentType(ContentType.JSON)
                .build().header("Authorization", bearerToken);
    }
}
