package com.ufund.api.ufundapi.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.mockito.internal.matchers.Null;
import org.junit.jupiter.api.BeforeEach;

import com.ufund.api.ufundapi.model.Animal;
import com.ufund.api.ufundapi.model.Transaction;
import com.ufund.api.ufundapi.model.User;
import com.ufund.api.ufundapi.persistence.AnimalDAO;
import com.ufund.api.ufundapi.persistence.AnimalFileDAO;

import com.fasterxml.jackson.databind.ObjectMapper;
import static org.mockito.Mockito.*;

import java.awt.List;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

/**
 * Animal Controller test
 * 
 * @author Aiden Bewley
 */
class AnimalControllerTests {
    private AnimalController controller;
    private AnimalController errorController;

    private AnimalDAO mockAnimalDAO;
    private Animal[] testAnimals;
    private User[] testUsers;
    ArrayList<Transaction> testTrans;

    /**
     * @author Aiden Bewley
     */
    @BeforeEach
    void initController() throws IOException {
        ObjectMapper mockObjectMapper = mock(ObjectMapper.class);

        this.testAnimals = new Animal[3];
        testAnimals[0] = new Animal(1, "dogname", "dog", "retriever", "male", 1, 1000, false);
        testAnimals[1] = new Animal(2, "catname", "cat", "orange", "female", 2, 2000, true);
        testAnimals[2] = new Animal(3, "fishname", "fish", "goldfish", "female", 3, 3000, false);

        when(mockObjectMapper.readValue(new File("test_filename.txt"), Animal[].class))
            .thenReturn(testAnimals);
        
        int[] basket = {};
        testUsers = new User[3];
        testUsers[0] = new User("admin","admin", 1, basket);
        testUsers[1] = new User("helper","test", 0, new int[] {1,2});
        testUsers[2] = new User("helper1","test", 0, new int[] {1});

        when(mockObjectMapper.readValue(new File("test_user.txt"), User[].class))
            .thenReturn(testUsers);

        AnimalFileDAO animalFileDAO = new AnimalFileDAO("test_filename.txt", "test_user.txt", "aaaa.txt", mockObjectMapper);
        
        this.controller = new AnimalController(animalFileDAO);

        this.mockAnimalDAO = mock(AnimalDAO.class);
        this.errorController = new AnimalController(mockAnimalDAO);
    }

    /**
     * @author Aiden Bewley
     */
    @Test
    void testGetAnimal() throws Exception {
        assertEquals(this.testAnimals[0], this.controller.getAnimal(1).getBody());
        assertEquals(this.testAnimals[1], this.controller.getAnimal(2).getBody());
        assertEquals(this.testAnimals[2], this.controller.getAnimal(3).getBody());
        assertEquals(HttpStatus.OK, this.controller.getAnimal(1).getStatusCode());
        assertEquals(HttpStatus.NOT_FOUND, this.controller.getAnimal(10).getStatusCode());

        int animalID = 99;
        doThrow(new IOException()).when(mockAnimalDAO).getAnimal(animalID);
        ResponseEntity<Animal> response = errorController.getAnimal(animalID);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());

    }

    /**
     * @author Aiden Bewley
     */
    @Test
    void testGetAnimals() throws IOException {
        Animal[] animals = this.controller.getAnimals().getBody();

        for (int i = 0;i < testAnimals.length;i++) {
            assertEquals(testAnimals[i], animals[i]);
        }
        assertEquals(HttpStatus.OK, this.controller.getAnimals().getStatusCode());

        doThrow(new IOException()).when(mockAnimalDAO).getAnimals();
        ResponseEntity<Animal[]> responseEntity = errorController.getAnimals();
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());

    }

    /**
     * @author Kody Rogers
     */
    @Test
    void testSearchAnimalsByType() throws IOException {
        assertEquals(this.testAnimals[0], this.controller.searchAnimalsTypes("dog").getBody()[0]);
        assertEquals(this.testAnimals[1], this.controller.searchAnimalsTypes("cat").getBody()[0]);
        assertEquals(this.testAnimals[2], this.controller.searchAnimalsTypes("fish").getBody()[0]);

        String searchString = "dogasd";
        doThrow(new IOException()).when(mockAnimalDAO).getAnimalType(searchString);
        ResponseEntity<Animal[]> responseEntity = errorController.searchAnimalsTypes(searchString);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());
    }

    /**
     * @author Aiden Bewley
     */
    @Test
    void testSearchAnimals() throws IOException {
        assertEquals(this.testAnimals[0], this.controller.searchAnimals("d").getBody()[0]);
        assertEquals(this.testAnimals[2], this.controller.searchAnimals("f").getBody()[0]);

        String searchString = "o";
        doThrow(new IOException()).when(mockAnimalDAO).findAnimals(searchString);
        ResponseEntity<Animal[]> responseEntity = errorController.searchAnimals(searchString);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());
    }

    @Test
    void testValidateSoldAnimals() throws IOException {
        ResponseEntity<Animal[]> soldAnimals = this.controller.validateSoldAnimals("helper");

        assertEquals(1, soldAnimals.getBody().length);
        assertEquals(testAnimals[1], soldAnimals.getBody()[0]);
        assertEquals(HttpStatus.CONFLICT, soldAnimals.getStatusCode());

        ResponseEntity<Animal[]> response2 = this.controller.validateSoldAnimals("helper11");
        assertEquals(HttpStatus.NOT_FOUND, response2.getStatusCode());

        ResponseEntity<Animal[]> response3 = this.controller.validateSoldAnimals(testUsers[2].getName());
        assertEquals(HttpStatus.OK, response3.getStatusCode());

        when(mockAnimalDAO.getUser(testUsers[2].getName())).thenThrow(new IOException());
        ResponseEntity<Animal[]> response4 = this.errorController.validateSoldAnimals(testUsers[2].getName());
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response4.getStatusCode());

    }

    /**
     * @author Aiden Bewley
     */
    @Test
    void testCreateAnimal() throws IOException {
        Animal testA = new Animal(4, "horsey", "horse", "horsebreed", "male", 1, 1000, false);

        when(mockAnimalDAO.createAnimal(testA)).thenReturn(testA);
        ResponseEntity<Animal> response1 = this.errorController.createAnimal(testA);
        assertEquals(testA, response1.getBody());
        assertEquals(HttpStatus.OK, response1.getStatusCode());

        Animal testB = new Animal(4, "horsey", "horse", "horsebreed", "male", 1, 1000, false);
        when(mockAnimalDAO.createAnimal(testB)).thenReturn(null);
        ResponseEntity<Animal> response2 = this.errorController.createAnimal(testB);
        assertEquals(HttpStatus.CONFLICT, response2.getStatusCode());
        
        Animal testC = new Animal(4, "horsey", "horse", "horsebreed", "male", 1, 1000, false);
        doThrow(new IOException()).when(mockAnimalDAO).createAnimal(testC);
        ResponseEntity<Animal> response3 = this.errorController.createAnimal(testC);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response3.getStatusCode());

    }

    /**
     * @author Aiden Bewley
     */
    @Test
    void testUpdateAnimal() throws IOException{
        Animal testA = new Animal(4, "horsey", "horse", "horsebreed", "male", 1, 1000, false);
        ResponseEntity<Animal> response1 = this.controller.updateAnimal(testA);
        assertEquals(null, response1.getBody());
        assertEquals(HttpStatus.NOT_FOUND, response1.getStatusCode());

        // Animal testB = new Animal(this.testAnimals[0].getId(), "apple");
        Animal testB = new Animal(this.testAnimals[0].getId(), this.testAnimals[0].getName(), this.testAnimals[0].getType(), 
                        this.testAnimals[0].getBreed(), this.testAnimals[0].getSex(), 2, 1500, false);
        ResponseEntity<Animal> response2 = this.controller.updateAnimal(testB);
        assertEquals(1500, response2.getBody().getCost());

        Animal testC = new Animal(this.testAnimals[0].getId(), this.testAnimals[0].getName(), this.testAnimals[0].getType(), 
                        this.testAnimals[0].getBreed(), this.testAnimals[0].getSex(), 2, 1500, false);
        doThrow(new IOException()).when(mockAnimalDAO).updateAnimal(testC);
        ResponseEntity<Animal> response3 = this.errorController.updateAnimal(testC);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response3.getStatusCode());
    }

    /**
     * @author Aiden Bewley
     */
    @Test
    void testDeleteAnimal() throws IOException {
        assertEquals(HttpStatus.NOT_FOUND, this.controller.deleteAnimal(213).getStatusCode());
        assertEquals(HttpStatus.OK, this.controller.deleteAnimal(1).getStatusCode());
        assertEquals(HttpStatus.NOT_FOUND, this.controller.getAnimal(1).getStatusCode());

        doThrow(new IOException()).when(mockAnimalDAO).deleteAnimal(99);
        ResponseEntity<Animal> response = errorController.deleteAnimal(99);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());

    }
}
