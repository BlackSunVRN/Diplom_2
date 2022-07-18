package ru.yandex.practicum;

import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Before;
import org.junit.Test;
import ru.yandex.practicum.stellaburgers.api.client.UserClient;
import ru.yandex.practicum.stellaburgers.api.model.SuccessResponse;
import ru.yandex.practicum.stellaburgers.api.model.User;

import static org.apache.http.HttpStatus.SC_FORBIDDEN;
import static org.apache.http.HttpStatus.SC_UNAUTHORIZED;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static ru.yandex.practicum.stellaburgers.api.model.User.getRandomUser;

public class UserErrorsTest {

    User user;

    UserClient userClient;

    @Before
    public void init() {
        user = getRandomUser();
        userClient = new UserClient();
    }

    @Test
    @DisplayName("Создание пользователя без имени")
    public void userCreateWithoutNameTest() {
        Response responseCreate = userClient.createUserManual(user.getEmail(), user.getPassword());
        assertEquals(SC_FORBIDDEN, responseCreate.statusCode());
        SuccessResponse createUserResponse = responseCreate.as(SuccessResponse.class);
        assertFalse(createUserResponse.success);
        String errorText = responseCreate.body().jsonPath().getString("message");
        assertEquals("Email, password and name are required fields", errorText);
    }

    @Test
    @DisplayName("Логин пользователя с некорректными почтой и паролем")
    public void userLoginWithIncorrectLoginPasswordTest() {
        Response responseLogin = userClient.loginUser(RandomStringUtils.randomAlphabetic(8) + "@gmail.com", RandomStringUtils.randomAlphabetic(10));
        assertEquals(SC_UNAUTHORIZED, responseLogin.statusCode());
        SuccessResponse loginUserResponse = responseLogin.as(SuccessResponse.class);
        assertFalse(loginUserResponse.success);
        String errorText = responseLogin.body().jsonPath().getString("message");
        assertEquals("email or password are incorrect", errorText);
    }
}
