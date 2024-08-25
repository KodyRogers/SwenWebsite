package com.ufund.api.ufundapi.persistence;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.*;

import java.io.File;
import java.io.IOException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ufund.api.ufundapi.model.Animal;
import com.ufund.api.ufundapi.model.User;

/**
 * Animal File Test
 * 
 * @author Kody Rogers
 */

@Tag("Persistence-tier")
class AnimalFileDAOTest {

    ObjectMapper mockObjectMapper;
    AnimalFileDAO animalFileDAO;
    Animal[] testAnimals;
    User[] testUsers;

    /**
     * @author Kody Rogers
     */
    @BeforeEach
    void setUp() throws IOException {
        mockObjectMapper = mock(ObjectMapper.class);

        testAnimals = new Animal[3];
        testAnimals[0] = new Animal(1, "Bailey", "dog", "Retriever", "male", 1, 1000, false);
        testAnimals[1] = new Animal(2, "Zeus", "dog", "Golden", "female", 5, 200, false);
        testAnimals[2] = new Animal(3, "Zally", "cat", "Orange", "male", 8, 500, false);; //Needs to be fosh for a method

        when(mockObjectMapper.readValue(new File("test_filename.txt"), Animal[].class))
                .thenReturn(testAnimals);

        int[] basket = {};
        testUsers = new User[2];
        testUsers[0] = new User("admin", "test",1, basket);
        testUsers[1] = new User("helper", "test",0, new int[] {1,2});
        when(mockObjectMapper.readValue(new File("test_user.txt"), User[].class))
                .thenReturn(testUsers);
                
        animalFileDAO = new AnimalFileDAO("test_filename.txt", "test_user.txt", "test_trans_file.txt", mockObjectMapper);

    }

    /**
     * @author Kody Rogers
     */
    @Test
    void testGetAnimals() {

        Animal[] animals = animalFileDAO.getAnimals();

        assertEquals(testAnimals.length, animals.length);
        for (int i = 0; i < testAnimals.length; i++) {
            assertEquals(testAnimals[i], animals[i]);
        }
    }
    
    /**
     * @author Kody Rogers
     */
    @Test
    void testGetUsers() throws IOException {

        User[] test = assertDoesNotThrow(() -> animalFileDAO.getUsers(),
                "Unexpected exception thrown");

        assertNotNull(test);
        User[] users = animalFileDAO.getUsers();
        assertEquals(testUsers.length, users.length);
        for (int i = 0; i < testUsers.length; i++) {
            assertEquals(testUsers[i], users[i]);
        }
    }

    /**
     * @author Kody Rogers
     */
    @Test
    void testGetAnimal() {
        Animal animal1 = animalFileDAO.getAnimal(1);
        assertEquals(animal1, testAnimals[0]);

        Animal animal2 = animalFileDAO.getAnimal(7);
        assertEquals(null, animal2);
    }

    /**
     * @author Kody Rogers
     */
    @Test
    void testGetUser() throws IOException {
        User user1 = animalFileDAO.getUser("helper");
        assertEquals(testUsers[1], user1);
    }

    /**
     * @author Kody Rogers
     */
    @Test
    void testSearchAnimals() {
        Animal[] animals = animalFileDAO.findAnimals("a");

        assertEquals(2, animals.length);
        assertEquals(animals[0], testAnimals[0]);
        assertEquals(animals[1], testAnimals[2]);
    }

    /**
     * @author Kody Rogers
     */
    @Test
    void testCreateAnimal() throws IOException {

        Animal animal = new Animal(4, "ChickenFinger", "dog", "lab", "male", 5, 300, false);

        Animal test = assertDoesNotThrow(() -> animalFileDAO.createAnimal(animal),
                "Unexpected exception thrown");

        assertNotNull(test);
        Animal actual = animalFileDAO.getAnimal(animal.getId());
        assertEquals(actual.getName(), animal.getName());
        assertEquals(actual.getType(), animal.getType());
        assertEquals(actual.getBreed(), animal.getBreed());
        assertEquals(actual.getSex(), animal.getSex());
        assertEquals(actual.getAge(), animal.getAge());
        assertEquals(actual.getCost(), animal.getCost());

    }

    /**
     * @author Kody Rogers
     */
    @Test
    void testGetAnimalType() throws IOException {

        Animal[] animalType = animalFileDAO.getAnimalType("dog");
        
        assertEquals(2, animalType.length);
        assertEquals(testAnimals[0], animalType[0]);
        assertEquals(testAnimals[1], animalType[1]);

    }

    /**
     * @author Kody Rogers
     * @author edited by: Aiden Bewley
     */
    @Test
    void testDeleteAnimal() {

        boolean notfound = assertDoesNotThrow(() -> animalFileDAO.deleteAnimal(10), "Unexpected Exception");
        assertEquals(false, notfound);
        assertEquals(animalFileDAO.animals.size(), testAnimals.length);

        boolean result = assertDoesNotThrow(() -> animalFileDAO.deleteAnimal(this.testAnimals[0].getId()), "Unexpected Exception");
        assertEquals(true, result);
        assertEquals(animalFileDAO.animals.size(), testAnimals.length - 1);

    }

    /**
     * @author Kody Rogers
     */
    @Test
    void testCreateUser() throws IOException {
        int mt[] = {};
        User user = new User("aaaaa","test", 0, mt);
        
        User test = assertDoesNotThrow(() -> animalFileDAO.createUser("helper", "test"),
                 "Unexpected exception thrown");
        
        
        User actual = animalFileDAO.createUser(user.getName(), user.getPassword());
        assertEquals(user.getName(), actual.getName());
        assertEquals(user.getLevel(), actual.getLevel());
    }

    /**
     * @author Kody Rogers
     */
    @Test
    void testUpdateUser() throws IOException {
        int[] basket = {};
        User updateUser = new User("admin","test", 3, basket); 
        User updatedTest = animalFileDAO.updateUser(updateUser);

        assertEquals(testUsers[0].getName(), updatedTest.getName());
        assertEquals(3, testUsers[0].getLevel());

        User test3 = new User("sda","test", 3, basket); 
        User test2 = animalFileDAO.updateUser(test3);
        assertNull(test2);
    }

    @Test
    void testAddTransation() throws IOException {
        animalFileDAO.addTransaction(this.testUsers[0].getName(), 0);

        assertEquals(this.testUsers[0].getName(), animalFileDAO.transArr.get(animalFileDAO.transArr.size() - 1).getUser());
    }

}