package ru.yandex.practicum;

import io.qameta.allure.junit4.DisplayName;
import io.restassured.path.json.exception.JsonPathException;
import io.restassured.response.Response;
import org.junit.Before;
import org.junit.Test;
import ru.yandex.practicum.stellaburgers.api.client.UserClient;
import ru.yandex.practicum.stellaburgers.api.model.SuccessResponse;
import ru.yandex.practicum.stellaburgers.api.model.User;

import static org.apache.http.HttpStatus.*;
import static org.junit.Assert.*;
import static ru.yandex.practicum.stellaburgers.api.model.User.getRandomUser;

public class UserDeleteTest {

    User user;

    UserClient userClient;

    @Before
    public void init() {
        user = getRandomUser();
        userClient = new UserClient();
    }

    @Test
    @DisplayName("Удаление зарегистрированного пользователя")
    public void userDeleteTest() {
        Response responseCreate = userClient.createUser(user);
        assertEquals(SC_OK, responseCreate.statusCode());

        SuccessResponse createUserResponse = responseCreate.as(SuccessResponse.class);
        assertTrue(createUserResponse.success);

        Response responseGetUser = userClient.getUserInfo(user);
        assertEquals(SC_OK, responseGetUser.statusCode());

        // Удаление пользователя
        Response responseDelete = userClient.deleteUser(user);
        assertEquals(SC_ACCEPTED, responseDelete.statusCode());

        // Попытка логина под пользователем, которого удалили
        Response responseLogin = userClient.loginUser(user.getEmail(), user.getPassword());
        assertEquals(SC_UNAUTHORIZED, responseLogin.statusCode());
    }
}
