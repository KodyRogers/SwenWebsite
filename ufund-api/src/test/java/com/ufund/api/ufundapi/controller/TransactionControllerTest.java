package com.ufund.api.ufundapi.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.File;
import java.io.IOException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ufund.api.ufundapi.model.Animal;
import com.ufund.api.ufundapi.model.Transaction;
import com.ufund.api.ufundapi.model.User;
import com.ufund.api.ufundapi.persistence.AnimalDAO;
import com.ufund.api.ufundapi.persistence.AnimalFileDAO;

class TransactionControllerTest {
    
    private TransactionController controller;
    private TransactionController errorController;

    private AnimalDAO mockAnimalDAO;
    private Animal[] testAnimals;
    private User[] testUsers;
    private Transaction[] testTrans;

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


        testTrans = new Transaction[2];
        testTrans[0] = new Transaction("name", 1, 23123);
        testTrans[1] = new Transaction("name2", 2, 1234);

        when(mockObjectMapper.readValue(new File("aaaa.txt"), Transaction[].class)).thenReturn(testTrans);

        AnimalFileDAO animalFileDAO = new AnimalFileDAO("test_filename.txt", "test_user.txt", "aaaa.txt", mockObjectMapper);
        
        this.controller = new TransactionController(animalFileDAO);

        this.mockAnimalDAO = mock(AnimalDAO.class);
        this.errorController = new TransactionController(mockAnimalDAO);
    }

    @Test
    void testGetTransactions_Success() throws IOException {
        ResponseEntity<Transaction[]> response = controller.getTransactions();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(2, response.getBody().length); 
        assertEquals(testTrans[0].getAnimal(), response.getBody()[0].getAnimal());

        when(mockAnimalDAO.getTransactions()).thenThrow(new IOException());
        ResponseEntity<Transaction[]> ResponseEntity = errorController.getTransactions();

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, ResponseEntity.getStatusCode());
    }

}
