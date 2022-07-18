package ru.yandex.practicum;

import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Before;
import org.junit.Test;
import ru.yandex.practicum.stellaburgers.api.client.UserClient;
import ru.yandex.practicum.stellaburgers.api.model.SuccessResponse;
import ru.yandex.practicum.stellaburgers.api.model.User;

import static org.apache.http.HttpStatus.*;
import static org.junit.Assert.*;
import static ru.yandex.practicum.stellaburgers.api.model.User.getRandomUser;

public class UserUpdateTest {

    User user;

    UserClient userClient;

    @Before
    public void init() {
        user = getRandomUser();
        userClient = new UserClient();
    }

    @Test
    @DisplayName("Изменение почты и пароля пользователя")
    public void userEmailPasswordUpdateTest() {
        Response responseCreate = userClient.createUser(user);
        assertEquals(SC_OK, responseCreate.statusCode());

        String newEmail = RandomStringUtils.randomAlphabetic(8) + "@ya.ru";
        String newPassword = RandomStringUtils.randomAlphabetic(8);

        Response responseUpdate = userClient.updateUserEmailPasswordName(user, newEmail, newPassword);
        assertEquals(SC_OK, responseUpdate.statusCode());

        SuccessResponse updateUserResponse = responseUpdate.as(SuccessResponse.class);
        assertTrue(updateUserResponse.success);
        userClient.deleteUser(newEmail, newPassword);
    }

    @Test
    @DisplayName("Изменение почты, пароля и имени пользователя")
    public void userEmailPasswordNameUpdateTest() {
        Response responseCreate = userClient.createUser(user);
        assertEquals(SC_OK, responseCreate.statusCode());

        User updUser = getRandomUser();

        Response responseUpdate = userClient.updateUserEmailPasswordName(user, updUser.getEmail(), updUser.getPassword(), updUser.getName());
        assertEquals(SC_OK, responseUpdate.statusCode());

        SuccessResponse updateUserResponse = responseUpdate.as(SuccessResponse.class);
        assertTrue(updateUserResponse.success);
        userClient.deleteUser(updUser);
    }

    @Test
    @DisplayName("Изменение пароля и имени пользователя")
    public void userPasswordNameUpdateTest() {
        Response responseCreate = userClient.createUser(user);
        assertEquals(SC_OK, responseCreate.statusCode());

        String newPassword = RandomStringUtils.randomAlphabetic(8);
        String newName = RandomStringUtils.randomAlphabetic(8);


        Response responseUpdate = userClient.updateUserPasswordName(user, newPassword, newName);
        assertEquals(SC_OK, responseUpdate.statusCode());

        SuccessResponse updateUserResponse = responseUpdate.as(SuccessResponse.class);
        assertTrue(updateUserResponse.success);
        userClient.deleteUser(user.getEmail(), newPassword);
    }

    @Test
    @DisplayName("Изменение почты и имени пользователя")
    public void userEmailNameUpdateTest() {
        Response responseCreate = userClient.createUser(user);
        assertEquals(SC_OK, responseCreate.statusCode());

        String newEmail = RandomStringUtils.randomAlphabetic(8) + "@ya.ru";
        String newName = RandomStringUtils.randomAlphabetic(8);


        Response responseUpdate = userClient.updateUserEmailName(user, newEmail, newName);
        assertEquals(SC_OK, responseUpdate.statusCode());

        SuccessResponse updateUserResponse = responseUpdate.as(SuccessResponse.class);
        assertTrue(updateUserResponse.success);
        userClient.deleteUser(newEmail, user.getPassword());
    }

    @Test
    @DisplayName("Изменение почты пользователя")
    public void userEmailUpdateTest() {
        Response responseCreate = userClient.createUser(user);
        assertEquals(SC_OK, responseCreate.statusCode());

        String newEmail = RandomStringUtils.randomAlphabetic(8) + "@ya.ru";

        Response responseUpdate = userClient.updateUserEmail(user, newEmail);
        assertEquals(SC_OK, responseUpdate.statusCode());

        SuccessResponse updateUserResponse = responseUpdate.as(SuccessResponse.class);
        assertTrue(updateUserResponse.success);
        userClient.deleteUser(newEmail, user.getPassword());
    }

    @Test
    @DisplayName("Изменение пароля пользователя")
    public void userPasswordUpdateTest() {
        Response responseCreate = userClient.createUser(user);
        assertEquals(SC_OK, responseCreate.statusCode());

        String newPassword = RandomStringUtils.randomAlphabetic(8);

        Response responseUpdate = userClient.updateUserPassword(user, newPassword);
        assertEquals(SC_OK, responseUpdate.statusCode());

        SuccessResponse updateUserResponse = responseUpdate.as(SuccessResponse.class);
        assertTrue(updateUserResponse.success);
        userClient.deleteUser(user.getEmail(), newPassword);
    }

    @Test
    @DisplayName("Изменение имени пользователя")
    public void userNameUpdateTest() {
        Response responseCreate = userClient.createUser(user);
        assertEquals(SC_OK, responseCreate.statusCode());

        String newName = RandomStringUtils.randomAlphabetic(8);

        Response responseUpdate = userClient.updateUserName(user, newName);
        assertEquals(SC_OK, responseUpdate.statusCode());

        SuccessResponse updateUserResponse = responseUpdate.as(SuccessResponse.class);
        assertTrue(updateUserResponse.success);
        userClient.deleteUser(user);
    }

    @Test
    @DisplayName("Изменение почты, пароля и имени пользователя без авторизации")
    public void userUpdateWithoutAuth() {
        Response responseCreate = userClient.createUser(user);
        assertEquals(SC_OK, responseCreate.statusCode());

        User updUser = getRandomUser();

        Response responseUpdate = userClient.updateUserInfoWithoutAuth(updUser.getEmail(), updUser.getPassword(), updUser.getName());
        assertEquals(SC_UNAUTHORIZED, responseUpdate.statusCode());

        String errorText = responseUpdate.body().jsonPath().getString("message");
        assertEquals("You should be authorised", errorText);

        SuccessResponse updateUserResponse = responseUpdate.as(SuccessResponse.class);
        assertFalse(updateUserResponse.success);

        userClient.deleteUser(user);
    }
}
