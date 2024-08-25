package com.ufund.api.ufundapi.persistence;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.ufund.api.ufundapi.model.Animal;
import com.ufund.api.ufundapi.model.Transaction;
import com.ufund.api.ufundapi.model.User;

import java.util.Date;

/**
 * Implements the functionality for JSON file-based peristance for Animals
 * 
 * {@literal @}Component Spring annotation instantiates a single instance of this
 * class and injects the instance into other classes as needed
 * 
 * @author Team 5
 */
@Component
public class AnimalFileDAO implements AnimalDAO {
    Map<Integer,Animal> animals;        // Provides a local cache of the animal objects
                                        // so that we don't need to read from the file
                                        // each time
    User[] userArray;                   // List of users
    private final Object userArrLock = new Object();  // Lock for user arr
    ArrayList<Transaction> transArr;    // List of transactions
    private ObjectMapper objectMapper;  // Provides conversion between Animal
                                        // objects and JSON text format written
                                        // to the file
    private static int nextId;          // The next Id to assign to a new animal                                    
    private String filename;            // Filename to read from and write to
    private String userFilename;        // Filename to read from and write to
    private String transFilename;       // Filename to read from and write to

    /**
     * Creates a Animal File Data Access Object
     * 
     * @param filename Filename to read from and write to
     * @param objectMapper Provides JSON Object to/from Java Object serialization and deserialization
     * 
     * @throws IOException when file cannot be accessed or read from
     */
    public AnimalFileDAO(@Value("${animals.file}") String filename,@Value("${users.file}") String userFilename,@Value("${transactions.file}") String transFilename,ObjectMapper objectMapper) throws IOException {
        this.filename = filename;
        this.userFilename = userFilename;
        this.transFilename = transFilename;
        this.transArr = new ArrayList<Transaction>();
        
        this.objectMapper = objectMapper;
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
        load();  // load the animals from the file
    }
    
    /**
     * Generates the next id for a new {@linkplain Animal animal}
     * 
     * @return The next id
     */
    private static synchronized int nextId() {
        int id = nextId;
        ++nextId;
        return id;
    }

    /**
     * Reset the next id for a new {@linkplain Animal animal}
     * 
     * @return The next id
     */
    private static synchronized void resetId(int id) {
        nextId = id;
    }

    public Animal[] checkout(User user) throws IOException {
        Animal[] invalidAnimals = this.validateBasket(user);
        if (invalidAnimals != null) {
            return invalidAnimals;
        }

        for (int i = 0; i < user.getBasket().length; i++) {
            this.addTransaction(user.getName(), user.getBasket()[i]);
            Animal a = this.getAnimal(user.getBasket()[i]);
            synchronized (this.animals) {
                a.setSold(true);
            }
        }
        synchronized (this.animals) {
            this.save();
        }
        synchronized (this.userArrLock) {
            int[] basket = {};
            User u = this.getUser(user.getName());
            u.setBasket(basket);
            saveUserData();
        }
        synchronized (this.transArr) {
            this.saveTransactions();
        }

        return invalidAnimals;
    }

    /**
     ** {@inheritDoc}
     */
    @Override
    public Transaction[] getTransactions() {
        synchronized(transArr) {
            return this.transAsArr();
        }
    }

    /**
     ** {@inheritDoc}
     */
    @Override
    public void addTransaction(String userName, int animalID) throws IOException {
        synchronized(transArr) {
            this.transArr.add(new Transaction(userName, animalID, (new Date()).getTime()));
        }
    }

    /**
     * Get the transaction list as an array
     * @return the transaction array
     */
    private Transaction[] transAsArr() {
        Transaction[] arr = new Transaction[this.transArr.size()];

        for (int i = 0; i < this.transArr.size(); i++) {
            arr[i] = this.transArr.get(i);
        }

        return arr;
    }

    /**
     * Generates an array of {@linkplain Animal animals} from the tree map
     * 
     * @return  The array of {@link Animal animals}, may be empty
     */
    private Animal[] getAnimalsArray() {
        return getAnimalsArray(null);
    }

    /**
     * Generates an array of {@linkplain Animal animals} from the tree map for any
     * {@linkplain Animal animals} that contains the text specified by containsText
     * <br>
     * If containsText is null, the array contains all of the {@linkplain Animal animals}
     * in the tree map
     * 
     * @return  The array of {@link Animal animals}, may be empty
     */
    private Animal[] getAnimalsArray(String containsText) { // if containsText == null, no filter
        ArrayList<Animal> animalArrayList = new ArrayList<>();

        for (Animal animal : animals.values()) {
            if (containsText == null || (animal.getName().toLowerCase().contains(containsText.toLowerCase()) && !animal.isSold())) {
                animalArrayList.add(animal);
            }
        }

        Animal[] animalArray = new Animal[animalArrayList.size()];
        animalArrayList.toArray(animalArray);
        return animalArray;
    }

    /**
     * Saves the {@linkplain Animal animals} from the map into the file as an array of JSON objects
     * 
     * @return true if the {@link Animal animals} were written successfully
     * 
     * @throws IOException when file cannot be accessed or written to
     */
    private boolean save() throws IOException {
        Animal[] animalArray = getAnimalsArray();

        // Serializes the Java Objects to JSON objects into the file
        // writeValue will thrown an IOException if there is an issue
        // with the file or reading from the file
        objectMapper.writeValue(new File(filename),animalArray);
        return true;
    }

    /**
     * Saves the {@linkplain User users} from the map into the file as an array of JSON objects
     * 
     * @return true if the {@link User users} were written successfully
     * 
     * @throws IOException when file cannot be accessed or written to
     */
    private boolean saveUserData() throws IOException {
        // Serializes the Java Objects to JSON objects into the file
        // writeValue will thrown an IOException if there is an issue
        // with the file or reading from the file
        objectMapper.writeValue(new File(userFilename),this.userArray);
        return true;
    }

    /**
     * Saves the {@linkplain Transaction transactions} from the map into the file as an array of JSON objects
     * 
     * @return true if the {@link Transaction transactions} were written successfully
     * 
     * @throws IOException when file cannot be accessed or written to
     */
    private boolean saveTransactions() throws IOException {
        // Serializes the Java Objects to JSON objects into the file
        // writeValue will thrown an IOException if there is an issue
        // with the file or reading from the file
        objectMapper.writeValue(new File(this.transFilename), this.transAsArr());
        return true;
    }

    /**
     * Loads {@linkplain Animal animals} from the JSON file into the map
     * <br>
     * Also sets next id to one more than the greatest id found in the file
     * 
     * @return true if the file was read successfully
     * 
     * @throws IOException when file cannot be accessed or read from
     */
    private boolean load() throws IOException {
        animals = new TreeMap<>();
        resetId(0);

        // Deserializes the JSON objects from the file into an array of animals
        // readValue will throw an IOException if there's an issue with the file
        // or reading from the file
        Animal[] animalArray = objectMapper.readValue(new File(filename),Animal[].class);
        this.userArray = objectMapper.readValue(new File(userFilename),User[].class);
        Transaction[] transArray = objectMapper.readValue(new File(transFilename),Transaction[].class);

        // Add each animal to the tree map and keep track of the greatest id
        for (Animal animal : animalArray) {
            animals.put(animal.getId(),animal);
            if (animal.getId() > nextId)
                resetId(animal.getId());
        }
        
        if (transArray != null) {
            for (Transaction tran : transArray) {
                this.transArr.add(tran);
            }
        }
        

        nextId();
        return true;
    }

    /**
    ** {@inheritDoc}
     */
    @Override
    public Animal[] getAnimals() {
        synchronized(animals) {
            return getAnimalsArray();
        }
    }

    public Animal[] validateBasket(User user) {

        int[] userBasket = user.getBasket();
        ArrayList<Animal> soldAnimals = new ArrayList<>();

        for (int i = 0; i < userBasket.length; i++) {
            Animal a = getAnimal(userBasket[i]);
            if (a.isSold()) {
                soldAnimals.add(a);
            }
        }

        if (soldAnimals.isEmpty()) {
            return null;
        }

        Animal[] invalidAnimals = new Animal[soldAnimals.size()];
        invalidAnimals = soldAnimals.toArray(invalidAnimals);
        return invalidAnimals;

    }

    public User createUser(String name, String password) throws IOException {
        synchronized (this.userArrLock) {
            User[] temp = new User[this.userArray.length + 1];
    
            for (int i = 0; i < this.userArray.length; i++) {
                temp[i] = this.userArray[i];
                if (temp[i].getName().equals(name)) {
                    return null;
                }
            }
            
            int[] mt = {};
            
            temp[temp.length - 1] = new User(name, password, 0, mt);
    
            this.userArray = temp;
            saveUserData();
            return temp[temp.length - 1];
        }
    }

    /**
     ** {@inheritDoc}
     */
    @Override
    public User getUser(String name) {
        synchronized(userArray) {
            for (int i = 0; i < this.userArray.length; i++) {
                User u = this.userArray[i];
                if (u.getName().equals(name)) {
                    return u;
                }
            }

             return null; // user does not exist
        }
    }

    /**
     ** {@inheritDoc}
     */
    @Override
    public User[] getUsers() {
        synchronized(userArray) {
            return this.userArray.clone();
        }
    }

    @Override
    public User updateUser(User user) throws IOException {
        synchronized(userArray) {
            for (int i = 0; i < this.userArray.length; i++) {
                User u = this.userArray[i];
                if (u.getName().equals(user.getName())) { // this means usernames cannot be modified
                    this.userArray[i] = user;
                    saveUserData();
                    return this.userArray[i]; // return the updated user
                }
            }
            return null; // user does not exist
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Animal[] findAnimals(String containsText) {
        synchronized(animals) {
            return getAnimalsArray(containsText);
        }
    }

    /**
     ** {@inheritDoc}
     */
    @Override
    public Animal[] getAnimalType(String type) throws IOException {
        ArrayList<Animal> animalArrayList = new ArrayList<>();

        for (Animal animal : animals.values()) {
            if (animal.getType().equals(type)) {
                animalArrayList.add(animal);
            }
        }

        Animal[] animalArray = new Animal[animalArrayList.size()];
        animalArrayList.toArray(animalArray);
        return animalArray;
    }

    /**
    ** {@inheritDoc}
    */
    @Override
    public Animal getAnimal(int id) {
        synchronized(animals) {
            if (animals.containsKey(id))
                return animals.get(id);
            else
                return null;
        }
    }

    /**
    ** {@inheritDoc}
     */
    @Override
    public Animal createAnimal(Animal animal) throws IOException {
        synchronized(animals) {
            Animal newAnimal = new Animal(nextId(), animal.getName(), animal.getType(), animal.getBreed(),animal.getSex(), animal.getAge(), animal.getCost(), false);
            animals.put(newAnimal.getId(),newAnimal);
            save();
            return newAnimal;
        }
    }

    /**
    ** {@inheritDoc}
     */
    @Override
    public Animal updateAnimal(Animal animal) throws IOException {
        synchronized(animals) {
            if (!animals.containsKey(animal.getId()))
                return null;  // animal does not exist

            animals.put(animal.getId(),animal);
            save(); // may throw an IOException
            return animal;
        }
    }

    /**
    ** {@inheritDoc}
     */
    @Override
    public boolean deleteAnimal(int id) throws IOException {
        synchronized(animals) {
            if (animals.containsKey(id)) {
                animals.remove(id);
                return save();
            }
            else
                return false;
        }
    }
}