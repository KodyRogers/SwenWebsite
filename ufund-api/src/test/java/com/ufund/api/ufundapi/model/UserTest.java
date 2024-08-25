package com.ufund.api.ufundapi.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;

class UserTest {
    
    @Test
    void testConstructor() {

        int[] basket = {1, 2, 3};
        User user = new User("admin","test", 0, basket);

        assertNotNull(user);

    }

    @Test
    void testName() {
        int[] basket = {1, 2, 3};
        User user = new User("admin", "test", 0, basket);

        assertEquals("admin", user.getName());
    }

    @Test
    void testPassword() {
        int[] basket = {1, 2, 3};
        User user = new User("admin", "test", 0, basket);

        assertEquals("test", user.getPassword());

        user.setPassword("test1");
        assertEquals("test1", user.getPassword());
    }

    @Test
    void testLevel() {
        int[] basket = {1, 2, 3};
        User user = new User("admin", "test", 0, basket);

        assertEquals(0, user.getLevel());

        user.setLevel(1);
        assertEquals(1, user.getLevel());
    }

    @Test
    void testBasketHelper() {
        int[] basket = {1, 2, 3};
        User user = new User("helper","test", 0, basket);

        int[] userBasket = user.getBasket();
        assertEquals(3, userBasket.length);
        for (int i = 0; i < basket.length; i++) {
            assertEquals(basket[i], userBasket[i]);
        }

        int[] newBasket = {4, 5, 6, 7};
        user.setBasket(newBasket);
        int[] userNewBasket = user.getBasket();
        assertEquals(4, userNewBasket.length);
        for (int i = 0; i < newBasket.length; i++) {
            assertEquals(newBasket[i], userNewBasket[i]);
        }
    }

    @Test
    void testAdminBasket() {
        int[] basket = {1, 2, 3};
        User user = new User("admin", "admin",1, basket);

        int[] userBasket = user.getBasket();
        assertEquals(0, userBasket.length);

        int[] newBasket = {4, 5, 6, 7};
        user.setBasket(newBasket);
        int[] userNewBasket = user.getBasket();
        assertEquals(0, userNewBasket.length);

    }

}