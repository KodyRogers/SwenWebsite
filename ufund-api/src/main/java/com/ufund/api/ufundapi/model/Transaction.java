package com.ufund.api.ufundapi.model;

import com.fasterxml.jackson.annotation.JsonProperty;


/**
 * Represents a Transaction entity
 * 
 * @author Team 5
 * @author Aiden Bewley
 */
public class Transaction {
    // Package private for tests
    static final String STRING_FORMAT = "Transaction [user=%d, animal=%d, time=%d]";

    @JsonProperty("user") private String user; // name of user who completed the purchase
    @JsonProperty("animal") private int animal; // id of animal sold
    @JsonProperty("time") private long time; // time of transaction
    
    /**
     * Create a user with the following atributes
     * 
     * @param name name of user
     * @param level permission level (0 for helper, 1 for admin)
     * @param basket funding basket (list of animal id's)
     * 
     * {@literal @}JsonProperty is used in serialization and deserialization
     * of the JSON object to the Java object in mapping the fields.  If a field
     * is not provided in the JSON object, the Java field gets the default Java
     * value, i.e. 0 for int
     */
    public Transaction(@JsonProperty("user") String user, @JsonProperty("animal") int animal, @JsonProperty("time") long time)
    {
        this.user = user;
        this.animal = animal;
        this.time = time;
    }

    /**
     * Retrieves the user id
     * @return The name of the animal
     */
    public String getUser() {
        return this.user;
    }

    /**
     * Retrieves the animal id
     * @return The animal id
     */
    public int getAnimal() {
        return this.animal;
    }

    /**
     * Retrieves the name of the animal
     * @return The name of the animal
     */
    public long getTime() {
        return this.time;
    }
}