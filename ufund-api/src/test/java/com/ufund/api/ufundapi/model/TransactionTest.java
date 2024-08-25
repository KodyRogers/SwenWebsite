package com.ufund.api.ufundapi.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;

class TransactionTest {

    @Test
    void testConstructor() {
        Transaction transaction = new Transaction("name", 1, 312432);
        assertNotNull(transaction);
    }

    @Test
    void testGet() {
        Transaction transaction = new Transaction("name", 1, 312432);
        assertEquals("name", transaction.getUser());
        assertEquals(1, transaction.getAnimal());
        assertEquals(312432, transaction.getTime());
    }

}