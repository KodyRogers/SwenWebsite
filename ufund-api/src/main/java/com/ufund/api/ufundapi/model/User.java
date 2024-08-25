package com.ufund.api.ufundapi.model;
import com.fasterxml.jackson.annotation.JsonProperty;


/**
 * Represents a User entity
 * 
 * @author Team 5
 * @author Aiden Bewley
 */
public class User {
    // Package private for tests
    static final String STRING_FORMAT = "User [name=%s, level=%d]";

    @JsonProperty("name") private String name; // name of user
    @JsonProperty("password") private String password; // password for user
    @JsonProperty("level") private int level; // permission level
    @JsonProperty("basket") private int[] basket; // funding basket (list of animal id's)
    
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
    public User(@JsonProperty("name") String name, @JsonProperty("password") String password, @JsonProperty("level") int level, @JsonProperty("basket") int[] basket)
    {
        this.name = name;
        this.password = password;
        this.level = level;
        this.basket = basket;
    }

    /**
     * Retrieves the name of the user
     * @return The name of the user
     */
    public String getName() {
        return name;
    }

    /**
     * Retrieves the Users Password
     * @return String for password
     */
    public String getPassword() {
        return password;
    }

    /**
     * sets the password
     * @param password - the password
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Retrieves the level of the user
     * @return The level of the user
     */
    public int getLevel() {
        return level;
    }

    /**
     * Sets the level of the user
     * @param level - the current level
     */
    public void setLevel(int level) {
        this.level = level;
    }

    /**
     * Retrieves the basket of the user
     * @return The basket of the user
     */
    public int[] getBasket() {
        if (level > 0) {
            return new int[0];
        }
        return basket;
    }

    /**
     * Sets the basket of the user
     * @param newBasket The basket of the user
     * @return boolean - true if the operation was succesful, false if it was not
     */
    public boolean setBasket(int[] newBasket) {
        if (level > 0) {
            return false;
        }
        
        this.basket = newBasket;
        return true;
    }
}