package com.ufund.api.ufundapi.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doThrow;
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
import com.ufund.api.ufundapi.model.User;
import com.ufund.api.ufundapi.persistence.AnimalDAO;
import com.ufund.api.ufundapi.persistence.AnimalFileDAO;

/**
 * User Controller Test
 * 
 * @author Kody Rogers
 */
class UserControllerTest {
    
    private UserController controller;
    private UserController eUserController;

    private AnimalDAO mockAnimalDAO;
    private Animal[] testAnimals;
    private User[] testUsers;

    /**
     * @author Kody Rogers
     * @throws IOException
     */
    @BeforeEach
    void initController() throws IOException {
        ObjectMapper mockObjectMapper = mock(ObjectMapper.class);
        
        this.testAnimals = new Animal[3];
        testAnimals[0] = new Animal(1, "dogname", "dog", "retriever", "male", 1, 1000, false );
        testAnimals[1] = new Animal(2, "catname", "cat", "orange", "female", 2, 2000, true);
        testAnimals[2] = new Animal(3, "fishname", "fish", "goldfish", "female", 3, 3000, false);

        when(mockObjectMapper.readValue(new File("test_filename.txt"), Animal[].class))
            .thenReturn(testAnimals);
        
        int[] basket = {};
        testUsers = new User[2];
        testUsers[0] = new User("admin","admin", 1, basket);
        testUsers[1] = new User("helper","test", 0, new int[] {1,2});

        when(mockObjectMapper.readValue(new File("test_user.txt"), User[].class))
            .thenReturn(testUsers);
        
        AnimalFileDAO animalFileDAO = new AnimalFileDAO("test_filename.txt", "test_user.txt", "file_name.txt", mockObjectMapper);

        this.controller = new UserController(animalFileDAO);

        this.mockAnimalDAO = mock(AnimalDAO.class);
        this.eUserController = new UserController(mockAnimalDAO);
    }

    /**
     * @author Kody Rogers
     * @throws IOException
     */
    @Test
    void testGetUsers() throws IOException {
        User[] users = this.controller.getUsers().getBody();

        for (int i = 0; i < users.length; i++) {
            assertEquals(testUsers[i], users[i]);
        } 
        assertEquals(HttpStatus.OK, this.controller.getUsers().getStatusCode());

        doThrow(new IOException()).when(mockAnimalDAO).getUsers();
        ResponseEntity<User[]> responseEntity = eUserController.getUsers();
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());
    }

    /**
     * @author Kody Rogers
     * @throws IOException
     */
    @Test
    void testGetUser() throws IOException {
        assertEquals(testUsers[0], this.controller.getUser("admin").getBody());
        assertEquals(testUsers[1], this.controller.getUser("helper").getBody());

        assertEquals(HttpStatus.OK, this.controller.getUser("admin").getStatusCode());
        assertEquals(HttpStatus.NOT_FOUND, this.controller.getUser("asdad").getStatusCode());

        String name = "random";
        doThrow(new IOException()).when(mockAnimalDAO).getUser(name);
        ResponseEntity<User> responseEntity = eUserController.getUser(name);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());
    }

    /**
     * @author Kody Rogers
     * @throws IOException
     */
    @Test
    void testCreateUser() throws IOException {
        int[] basket = {};
        User userA = new User("test", "test", 0, basket);

        when(mockAnimalDAO.createUser(userA.getName(), userA.getPassword())).thenReturn(userA);
        ResponseEntity<User> responseEntity = this.eUserController.createUser(userA);
        assertEquals(userA, responseEntity.getBody());
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

        User userB = new User("test", "test", 0, basket);
        when(mockAnimalDAO.createUser(userB.getName(), userB.getPassword())).thenReturn(null);
        ResponseEntity<User> responseEntity1 = this.eUserController.createUser(userB);
        assertEquals(HttpStatus.CONFLICT, responseEntity1.getStatusCode());

        User userC = new User("test", "test", 0, basket);
        doThrow(new IOException()).when(mockAnimalDAO).createUser(userC.getName(), userC.getPassword());
        ResponseEntity<User> responseEntity2 = this.eUserController.createUser(userB);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity2.getStatusCode());
    }

    /**
     * @author Kody Rogers
     * @throws IOException
     */
    @Test
    void testUpdateUser() throws IOException {
        int[] basket = {};
        User userA = new User("test", "test", 0, basket);
        ResponseEntity<User> response1 = this.controller.updateUser(userA);
        assertEquals(null, response1.getBody());
        assertEquals(HttpStatus.NOT_FOUND, response1.getStatusCode());

        int[] basketUpdated = {1,2};
        User userB =  new User(testUsers[1].getName(), testUsers[1].getPassword(), testUsers[1].getLevel(), basketUpdated);
        ResponseEntity<User> response2 = this.controller.updateUser(userB);
        int[] userBasket = response2.getBody().getBasket();
        assertEquals(basketUpdated[0], userBasket[0]);
        assertEquals(HttpStatus.OK, response2.getStatusCode());

        User userC =  new User(testUsers[0].getName(), testUsers[0].getPassword(), testUsers[0].getLevel(), basketUpdated);
        doThrow(new IOException()).when(mockAnimalDAO).updateUser(userC);
        ResponseEntity<User> responseEntity = this.eUserController.updateUser(userC);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());
    }

    /**
     * @author Kody Rogers
     * @throws IOException
     */
    @Test
    void testCheckout() throws IOException {

        when(mockAnimalDAO.checkout(testUsers[0])).thenReturn(null);
        assertEquals(HttpStatus.OK, this.controller.checkout(testUsers[0]).getStatusCode());

        ResponseEntity<Animal[]> a = this.controller.checkout(testUsers[1]);
        assertEquals(1, a.getBody().length);
        assertEquals(HttpStatus.CONFLICT, a.getStatusCode());

        doThrow(new IOException()).when(mockAnimalDAO).checkout(testUsers[1]);
        ResponseEntity<Animal[]> responseEntity = this.eUserController.checkout(testUsers[1]);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());

    }
}
