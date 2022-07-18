package ru.yandex.practicum;

import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.Before;
import org.junit.Test;
import ru.yandex.practicum.stellaburgers.api.client.OrderClient;
import ru.yandex.practicum.stellaburgers.api.client.UserClient;
import ru.yandex.practicum.stellaburgers.api.model.SuccessResponse;
import ru.yandex.practicum.stellaburgers.api.model.Order;
import ru.yandex.practicum.stellaburgers.api.model.User;

import static org.apache.http.HttpStatus.*;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.*;

public class CreateOrderTest {

    User user;
    Order order;

    UserClient userClient;
    OrderClient orderClient;

    @Before
    public void init() {
        user = User.getRandomUser();
        orderClient = new OrderClient();
        userClient = new UserClient();
    }

    @Test
    @DisplayName("Создание заказа с игредиентами авторизованным пользователем")
    public void createOrderWithAuthWithIngredientsTest() {
        Response responseCreateUser = userClient.createUser(user);
        assertEquals(SC_OK, responseCreateUser.statusCode());

        String[] ingredients = {"61c0c5a71d1f82001bdaaa6c",
                "61c0c5a71d1f82001bdaaa71", "61c0c5a71d1f82001bdaaa73"};
        order = new Order(ingredients);
        Response responseCreateOrder = orderClient.createOrderForUser(order, user);
        assertEquals(SC_OK, responseCreateOrder.statusCode());
        SuccessResponse createOrderResponse = responseCreateOrder.as(SuccessResponse.class);
        assertTrue(createOrderResponse.success);
        responseCreateOrder.then().assertThat().body("order.number", notNullValue());
        userClient.deleteUser(user);
    }

    @Test
    @DisplayName("Создание заказа без игредиентов авторизованным пользователем")
    public void createOrderWithAuthWithoutIngredients() {
        Response responseCreateUser = userClient.createUser(user);
        assertEquals(SC_OK, responseCreateUser.statusCode());

        String[] ingredients = {};
        order = new Order(ingredients);
        Response responseCreateOrder = orderClient.createOrderForUser(order, user);
        assertEquals(SC_BAD_REQUEST, responseCreateOrder.statusCode());
        SuccessResponse createOrderResponse = responseCreateOrder.as(SuccessResponse.class);
        assertFalse(createOrderResponse.success);
        String errorText = responseCreateOrder.body().jsonPath().getString("message");
        assertEquals("Ingredient ids must be provided", errorText);
        userClient.deleteUser(user);
    }

    @Test
    @DisplayName("Создание заказа с игредиентами без авторизации")
    public void createOrderWithoutAuthWithIngredients() {
        String[] ingredients = {"61c0c5a71d1f82001bdaaa6c",
                "61c0c5a71d1f82001bdaaa71", "61c0c5a71d1f82001bdaaa73"};
        order = new Order(ingredients);
        Response responseCreateOrder = orderClient.createOrderWithoutAuth(order);
        assertEquals(SC_OK, responseCreateOrder.statusCode());
        SuccessResponse createOrderResponse = responseCreateOrder.as(SuccessResponse.class);
        assertTrue(createOrderResponse.success);
    }

    @Test
    @DisplayName("Создание заказа без ингредиентов и авторизации")
    public void createOrderWithoutAuthWithoutIngredients() {
        String[] ingredients = {};
        order = new Order(ingredients);
        Response responseCreateOrder = orderClient.createOrderWithoutAuth(order);
        assertEquals(SC_BAD_REQUEST, responseCreateOrder.statusCode());
        SuccessResponse createOrderResponse = responseCreateOrder.as(SuccessResponse.class);
        assertFalse(createOrderResponse.success);
        String errorText = responseCreateOrder.body().jsonPath().getString("message");
        assertEquals("Ingredient ids must be provided", errorText);
    }

    @Test
    @DisplayName("Создание заказа с невалидным хэшем игредиентов авторизованным пользователем")
    public void createOrderWithAuthWithInvalidHashTest() {
        Response responseCreateUser = userClient.createUser(user);
        assertEquals(SC_OK, responseCreateUser.statusCode());

        String[] ingredients = {"61c0caaa6c",
                "61c0c5a71ddaaa71", "61c0c5a71ddaaa73"};
        order = new Order(ingredients);
        Response responseCreateOrder = orderClient.createOrderForUser(order, user);
        assertEquals(SC_INTERNAL_SERVER_ERROR, responseCreateOrder.statusCode());
        userClient.deleteUser(user);
    }

    @Test
    @DisplayName("Создание заказа с невалидным хэшем игредиентов без авторизации")
    public void createOrderWithoutAuthWithInvalidHashTest() {
        String[] ingredients = {"61c0caaa6c",
                "61c0c5a71ddaaa71", "61c0c5a71ddaaa73"};
        order = new Order(ingredients);
        Response responseCreateOrder = orderClient.createOrderWithoutAuth(order);
        assertEquals(SC_INTERNAL_SERVER_ERROR, responseCreateOrder.statusCode());
    }

}
