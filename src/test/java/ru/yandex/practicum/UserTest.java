package ru.yandex.practicum;

import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import ru.yandex.practicum.stellaburgers.api.client.UserClient;
import ru.yandex.practicum.stellaburgers.api.model.*;

import static org.apache.http.HttpStatus.*;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.*;
import static ru.yandex.practicum.stellaburgers.api.model.User.getRandomUser;

public class UserTest {

    User user;

    UserClient userClient;

    @Before
    public void init() {
        user = getRandomUser();
        userClient = new UserClient();
    }

    @After
    public void deleteUser() {
        userClient.deleteUser(user);
    }

    @Test
    @DisplayName("Создание уникального пользователя")
    public void userCreateTest() {
        Response responseCreate = userClient.createUser(user);
        assertEquals(SC_OK, responseCreate.statusCode());

        SuccessResponse createUserResponse = responseCreate.as(SuccessResponse.class);
        assertTrue(createUserResponse.success);
        responseCreate.then().assertThat().body("accessToken", notNullValue());
        responseCreate.then().assertThat().body("refreshToken", notNullValue());
    }

    @Test
    @DisplayName("Создание уже зарегистрированного пользователя")
    public void userCreateSameDataTest() {
        Response responseCreate = userClient.createUser(user);
        assertEquals(SC_OK, responseCreate.statusCode());

        Response responseCreateSameData = userClient.createUser(user);
        SuccessResponse createUserResponse = responseCreateSameData.as(SuccessResponse.class);
        assertFalse(createUserResponse.success);
        assertEquals(SC_FORBIDDEN, responseCreateSameData.statusCode());
        String errorText = responseCreateSameData.body().jsonPath().getString("message");
        assertEquals("User already exists", errorText);
    }

    @Test
    @DisplayName("Логин зарегистрированного пользователя")
    public void userLoginTest() {
        Response responseCreate = userClient.createUser(user);
        assertEquals(SC_OK, responseCreate.statusCode());

        Response responseLogin = userClient.loginUser(user.getEmail(), user.getPassword());
        assertEquals(SC_OK, responseLogin.statusCode());

        SuccessResponse loginUserResponse = responseLogin.as(SuccessResponse.class);
        assertTrue(loginUserResponse.success);
        responseLogin.then().assertThat().body("accessToken", notNullValue());
        responseLogin.then().assertThat().body("refreshToken", notNullValue());
    }
}
