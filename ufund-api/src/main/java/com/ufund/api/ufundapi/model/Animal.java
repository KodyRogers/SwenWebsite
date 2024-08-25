package com.ufund.api.ufundapi.model;
import com.fasterxml.jackson.annotation.JsonProperty;


/**
 * Represents a Animal entity
 * 
 * @author Team 5
 * @author Kody Rogers
 */
public class Animal {
    // Package private for tests
    static final String STRING_FORMAT = "Animal [id=%d, name=%s, type=%s, breed=%s, sex=%s, age=%d, cost=%.2f, sold=%b]";

    @JsonProperty("id") private int id;
    @JsonProperty("name") private String name;
    @JsonProperty("type") private String type; //Dog or Cat etc
    @JsonProperty("breed") private String breed;
    @JsonProperty("sex") private String sex;
    @JsonProperty("age") private int age;
    @JsonProperty("cost") private double cost;
    @JsonProperty("sold") private boolean sold;
    
    /**
     * Create a animal with the following atributes
     * 
     * @param id The id of the animal
     * @param name The name of the animal
     * @param type The type of the animal
     * @param breed The breed of the animal
     * @param sex The sex of the animal
     * @param age The age of the animal
     * @param cost The cost of the animal
     * @param sold The sell status of the animal
     * 
     * {@literal @}JsonProperty is used in serialization and deserialization
     * of the JSON object to the Java object in mapping the fields.  If a field
     * is not provided in the JSON object, the Java field gets the default Java
     * value, i.e. 0 for int
     */
    public Animal(@JsonProperty("id") int id, @JsonProperty("name") String name, @JsonProperty("type") String type, 
                    @JsonProperty("breed") String breed, @JsonProperty("sex") String sex, 
                    @JsonProperty("age") int age, @JsonProperty("cost") double cost, @JsonProperty("sold") boolean sold) 
    {
        this.id = id;
        this.name = name;
        this.type = type;
        this.breed = breed;
        this.sex = sex;
        this.age = age;
        this.cost = cost;
        this.sold = sold;
    }

    public int getId() {
        return id;
    }

    /**
     * Sets the name of the animal - necessary for JSON object to Java object deserialization
     * @param name The name of the animal
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Retrieves the name of the animal
     * @return The name of the animal
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the type of the animal
     * @param type - the type of animal (i.e cat, dog, etc)
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * Retrieves the type of the animal
     * @return the type of the animal
     */
    public String getType() {
        return type;
    }
    
    /**
     * Sets the breed of the animal
     * @param breed - the breed of animal (i.e pitbull, retriever, etc)
     */
    public void setBreed(String breed) {
        this.breed = breed;
    }

    /**
     * Retrieves the breed of the animal
     * @return the breed of the animal
     */
    public String getBreed() {
        return breed;
    }

    /**
     * Sets the sex of the animal
     * @param sex - the sex of animal
     */
    public void setSex(String sex) {
        this.sex = sex;
    }

    /**
     * Retrieves the sex of the animal
     * @return the sex of the animal
     */
    public String getSex() {
        return sex;
    }

    /**
     * Sets the age of the animal
     * @param age - the age of animal
     */
    public void setAge(int age) {
        this.age = age;
    }

    /**
     * Retrieves the age of the animal
     * @return the age of the animal
     */
    public int getAge() {
        return age;
    }

    /**
     * Sets the cost of the animal
     * @param cost - the cost of animal
     */
    public void setCost(double cost) {
        this.cost = cost;
    }

    /**
     * Retrieves the cost of the animal
     * @return the cost of the animal
     */
    public double getCost() {
        return cost;
    }

    /**
     * Sets the sell status of the animal
     * @param sold - the sell status of animal
     */
    public void setSold(boolean sold) {
        this.sold = sold;
    }

    /**
     * Checks if the animal is sold
     * @return false if not sold
     */
    public boolean isSold() {
        return sold;
    }

    
    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return String.format(STRING_FORMAT, id, name, type, breed, sex, age, cost, sold);
    }
}