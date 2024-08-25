package com.ufund.api.ufundapi.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.annotation.JsonProperty;

class AnimalTest {
    
    /**
     * @author Kody Rogers
     */
    @Test
    void testConstructor () {
        Animal animal = new Animal(1, "Bailey", "dog", "Retriever", "male", 1, 1000, false);
        assertNotNull(animal);
    }

    /**
     * @author Kody Rogers
     */
    @Test
    void testId() {
        Animal animal = new Animal(1, "Bailey", "dog", "Retriever", "male", 1, 1000, false);
        assertEquals(1, animal.getId());
        assertNotEquals(10, animal.getId());
    }

    /**
     * @author Kody Rogers
     */
    @Test
    void testName() {

        Animal animal = new Animal(1, "Bailey", "dog", "Retriever", "male", 1, 1000, false);
        assertEquals("Bailey", animal.getName());

        animal.setName("Finnigan");
        assertEquals("Finnigan", animal.getName());
    }   

    /**
     * @author Kody Rogers
     */
    @Test
    void testType() {

        Animal animal = new Animal(1, "Bailey", "dog", "Retriever", "male", 1, 1000, false);
        assertEquals("dog", animal.getType());

        animal.setType("cat");
        assertEquals("cat", animal.getType());

    }

    /**
     * @author Kody Rogers
     */
    @Test
    void testBreed() {

        Animal animal = new Animal(1, "Bailey", "dog", "Retriever", "male", 1, 1000, false);
        assertEquals("Retriever", animal.getBreed());

        animal.setBreed("Rotwilder");
        assertEquals("Rotwilder", animal.getBreed());

    }

    /**
     * @author Kody Rogers
     */
    @Test
    void testSex() {
        Animal animal = new Animal(1, "Bailey", "dog", "Retriever", "male", 1, 1000, false);
        assertEquals("male", animal.getSex());

        animal.setSex("female");
        assertEquals("female", animal.getSex());
    }

    /**
     * @author Kody Rogers
     */
    @Test
    void testAge() {
        Animal animal = new Animal(1, "Bailey", "dog", "Retriever", "male", 1, 1000, false);
        assertEquals(1, animal.getAge());

        animal.setAge(2);
        assertEquals(2, animal.getAge());
    }

    /**
     * @author Kody Rogers
     */
    @Test
    void testCost() {
        Animal animal = new Animal(1, "Bailey", "dog", "Retriever", "male", 1, 1000, false);
        assertEquals(1000, animal.getCost());

        animal.setCost(100);
        assertEquals(100, animal.getCost());
    }

    /**
     * @author Kody Rogers
     */
    @Test
    void testSold() {
        Animal animal = new Animal(1, "Bailey", "dog", "Retriever", "male", 1, 1000, false);
        assertEquals(false, animal.isSold());

        animal.setSold(true);
        assertEquals(true, animal.isSold());
        assertNotEquals(false, animal.isSold());
    }

    /**
     * @author Kody Rogers
     */
    @Test
    void testToString() {
        Animal animal = new Animal(1, "Bailey", "dog", "Retriever", "male", 1, 1000, false);
        assertEquals("Animal [id=1, name=Bailey, type=dog, breed=Retriever, sex=male, age=1, cost=1000.00, sold=false]", animal.toString());
    }

}
