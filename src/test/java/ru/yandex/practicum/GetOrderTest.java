package ru.yandex.practicum;

import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.Before;
import org.junit.Test;
import ru.yandex.practicum.stellaburgers.api.client.OrderClient;
import ru.yandex.practicum.stellaburgers.api.client.UserClient;
import ru.yandex.practicum.stellaburgers.api.model.Order;
import ru.yandex.practicum.stellaburgers.api.model.User;

import static org.apache.http.HttpStatus.SC_OK;
import static org.apache.http.HttpStatus.SC_UNAUTHORIZED;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertEquals;

public class GetOrderTest {

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
    @DisplayName("Получение списка заказов авторизованного пользователя")
    public void getUserOrdersWithAuth() {
        Response responseCreateUser = userClient.createUser(user);
        assertEquals(SC_OK, responseCreateUser.statusCode());

        String[] ingredients = {"61c0c5a71d1f82001bdaaa6c",
                "61c0c5a71d1f82001bdaaa71", "61c0c5a71d1f82001bdaaa73"};
        order = new Order(ingredients);
        Response responseCreateOrder = orderClient.createOrderForUser(order, user);
        assertEquals(SC_OK, responseCreateOrder.statusCode());

        Response responseGetOrder = orderClient.getUserOrdersWithAuth(user);
        responseGetOrder.then().assertThat().body("orders.ingredients", notNullValue());
        responseGetOrder.then().assertThat().body("orders.status", notNullValue());

        userClient.deleteUser(user);
    }

    @Test
    @DisplayName("Получение списка заказов без авторизации")
    public void getUserOrdersWithoutAuth() {
        Response responseGetOrder = orderClient.getUserOrdersWithoutAuth();
        assertEquals(SC_UNAUTHORIZED, responseGetOrder.statusCode());

        String errorText = responseGetOrder.body().jsonPath().getString("message");
        assertEquals("You should be authorised", errorText);
    }
}
