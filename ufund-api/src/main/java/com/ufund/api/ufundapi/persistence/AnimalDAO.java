package com.ufund.api.ufundapi.persistence;

import java.io.IOException;
import com.ufund.api.ufundapi.model.Animal;
import com.ufund.api.ufundapi.model.User;
import com.ufund.api.ufundapi.model.Transaction;

/**
 * Defines the interface for Animal object persistence
 * 
 * @author Team 5
 */
public interface AnimalDAO {
    /**
     * Retrieves all {@linkplain Transaction transactions}
     */
    Transaction[] getTransactions() throws IOException;

    /**
     * Adds a new {@linkplain Transaction transactions}
     */
    void addTransaction(String username, int animalID) throws IOException;

    /**
     * Checkout the cart of the given {@linkplain User user}
     * @param user
     * @return list of animals that could not be checked out, null if it worked
     */
    Animal[] checkout(User user) throws IOException;

    /**
     * Retrieves all {@linkplain User users}
     */
    User[] getUsers() throws IOException;

    User createUser(String name, String password) throws IOException;

    /**
     * Retrieves a specific {@linkplain User user}
     */
    User getUser(String name) throws IOException;

    /**
     * Updates and saves a {@linkplain User user}
     */
    User updateUser(User user) throws IOException;

    /**
     * Retrieves all {@linkplain Animal animales}
     * 
     * @return An array of {@link Animal animal} objects, may be empty
     * 
     * @throws IOException if an issue with underlying storage
     */
    Animal[] getAnimals() throws IOException;

    Animal[] validateBasket(User user) throws IOException;

    /**
     * Finds all {@linkplain Animal animales} whose name contains the given text
     * 
     * @param containsText The text to match against
     * 
     * @return An array of {@link Animal animales} whose nemes contains the given text, may be empty
     * 
     * @throws IOException if an issue with underlying storage
     */
    Animal[] findAnimals(String containsText) throws IOException;


    /**
     * Finds all {@linkplain Animal animales} whose name contains the given text
     * 
     * @param type - the type to look up
     *  
     * @return An Array of {@link Animal animales} whose nemes contains the given type, can be empty
     * 
     * @throws IOException if an issue with underlying storage
     */
    Animal[] getAnimalType(String type) throws IOException;

    /**
     * Retrieves a {@linkplain Animal animal} with the given id
     * 
     * @param id The id of the {@link Animal animal} to get
     * 
     * @return a {@link Animal animal} object with the matching id
     * <br>
     * null if no {@link Animal animal} with a matching id is found
     * 
     * @throws IOException if an issue with underlying storage
     */
    Animal getAnimal(int id) throws IOException;

    /**
     * Creates and saves a {@linkplain Animal animal}
     * 
     * @param animal {@linkplain Animal animal} object to be created and saved
     * <br>
     * The id of the animal object is ignored and a new uniqe id is assigned
     *
     * @return new {@link Animal animal} if successful, false otherwise 
     * 
     * @throws IOException if an issue with underlying storage
     */
    Animal createAnimal(Animal animal) throws IOException;

    /**
     * Updates and saves a {@linkplain Animal animal}
     * 
     * @param {@link Animal animal} object to be updated and saved
     * 
     * @return updated {@link Animal animal} if successful, null if
     * {@link Animal animal} could not be found
     * 
     * @throws IOException if underlying storage cannot be accessed
     */
    Animal updateAnimal(Animal animal) throws IOException;

    /**
     * Deletes a {@linkplain Animal animal} with the given id
     * 
     * @param id The id of the {@link Animal animal}
     * 
     * @return true if the {@link Animal animal} was deleted
     * <br>
     * false if animal with the given id does not exist
     * 
     * @throws IOException if underlying storage cannot be accessed
     */
    boolean deleteAnimal(int id) throws IOException;
}