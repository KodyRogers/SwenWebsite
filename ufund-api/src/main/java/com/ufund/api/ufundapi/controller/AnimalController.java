package com.ufund.api.ufundapi.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.ufund.api.ufundapi.persistence.AnimalDAO;
import com.ufund.api.ufundapi.model.Animal;
import com.ufund.api.ufundapi.model.User;

/**
 * Handles the REST API requests for the Animal resource
 * <p>
 * {@literal @}RestController Spring annotation identifies this class as a REST API
 * method handler to the Spring framework
 * 
 * @author Team 5
 */

@RestController
@RequestMapping("animals")
public class AnimalController {
    private static final Logger LOG = Logger.getLogger(AnimalController.class.getName());
    private AnimalDAO animalDao;

    /**
     * Creates a REST API controller to reponds to requests
     * 
     * @param animalDao The {@link AnimalDAO Animal Data Access Object} to perform CRUD operations
     * <br>
     * This dependency is injected by the Spring Framework
     */
    public AnimalController(AnimalDAO animalDao) {
        this.animalDao = animalDao;
    }

    /**
     * Responds to the GET request for a {@linkplain Animal animal} for the given id
     * 
     * @param id The id used to locate the {@link Animal animal}
     * 
     * @return ResponseEntity with {@link Animal animal} object and HTTP status of OK if found<br>
     * ResponseEntity with HTTP status of NOT_FOUND if not found<br>
     * ResponseEntity with HTTP status of INTERNAL_SERVER_ERROR otherwise
     */
    @GetMapping("/{id}")
    public ResponseEntity<Animal> getAnimal(@PathVariable int id) {
        LOG.info("GET /animals/" + id);
        try {
            Animal animal = animalDao.getAnimal(id);
            if (animal != null)
                return new ResponseEntity<>(animal,HttpStatus.OK);
            else
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        catch(IOException e) {
            LOG.log(Level.SEVERE,e.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Responds to the GET request for all {@linkplain Animal ufund}
     * 
     * @return ResponseEntity with array of {@link Animal animal} objects (may be empty) and
     * HTTP status of OK<br>
     * ResponseEntity with HTTP status of INTERNAL_SERVER_ERROR otherwise
     */
    @GetMapping("")
    public ResponseEntity<Animal[]> getAnimals() {
        LOG.info("GET /animals");
        try {
            return new ResponseEntity<>(this.animalDao.getAnimals(), HttpStatus.OK);
        } catch (IOException e) {
            LOG.log(Level.SEVERE,e.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Responds to the GET request for all {@linkplain Animal ufund} whose name contains
     * the text in name
     * 
     * @param name The name parameter which contains the text used to find the {@link Animal ufund}
     * 
     * @return ResponseEntity with array of {@link Animal animal} objects (may be empty) and
     * HTTP status of OK<br>
     * ResponseEntity with HTTP status of INTERNAL_SERVER_ERROR otherwise
     * <p>
     * Example: Find all ufund that contain the text "ma"
     * GET http://localhost:8080/ufund/?name=ma
     */
    @GetMapping("/")
    public ResponseEntity<Animal[]> searchAnimals(@RequestParam String name) {
        LOG.info("GET /animals/?name="+name);
        try {
            return new ResponseEntity<>(this.animalDao.findAnimals(name), HttpStatus.OK);
        } catch (IOException e) {
            LOG.log(Level.SEVERE,e.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Responds to the GET request for all {@linkplain Animal ufund} whose type contains
     * the text in name
     * 
     * @param type The name parameter which contains the type used to find the {@link Animal ufund}
     * 
     * @return ResponseEntity with array of {@link Animal animal} objects (may be empty) and
     * HTTP status of OK<br>
     * ResponseEntity with HTTP status of INTERNAL_SERVER_ERROR otherwise
     * <p>
     * Example: Find all ufund that contain the text "ma"
     * GET http://localhost:8080/ufund/?type=dog
     */

    //Yeah I need to figure this all out later
    
    @GetMapping("/type/")
    public ResponseEntity<Animal[]> searchAnimalsTypes(@RequestParam("type") String type) {
        LOG.info("GET /type/?type="+type);
        try {
            return new ResponseEntity<>(this.animalDao.getAnimalType(type), HttpStatus.OK);
        } catch (IOException e) {
            LOG.log(Level.SEVERE,e.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/sold/")
    public ResponseEntity<Animal[]> validateSoldAnimals(@RequestParam("name") String name) {
        LOG.info("GET /sold/?name="+name);
        try {
            User user = animalDao.getUser(name);
            if (user == null) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            
            Animal[] soldAnimals = animalDao.validateBasket(user);
            if (soldAnimals == null) {
                return new ResponseEntity<>(soldAnimals, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(soldAnimals, HttpStatus.CONFLICT);
            }
        } catch (IOException e) {
            LOG.log(Level.SEVERE,e.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Creates a {@linkplain Animal animal} with the provided animal object
     * 
     * @param animal - The {@link Animal animal} to create
     * 
     * @return ResponseEntity with created {@link Animal animal} object and HTTP status of CREATED<br>
     * ResponseEntity with HTTP status of CONFLICT if {@link Animal animal} object already exists<br>
     * ResponseEntity with HTTP status of INTERNAL_SERVER_ERROR otherwise
     */
    @PostMapping("")
    public ResponseEntity<Animal> createAnimal(@RequestBody Animal animal) {
        LOG.info("POST /animals " + animal);
        try {
            Animal h = this.animalDao.createAnimal(animal);
            if (h != null) {
                return new ResponseEntity<>(h, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(h, HttpStatus.CONFLICT);
            }
        } catch (IOException e) {
            LOG.log(Level.SEVERE,e.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Updates the {@linkplain Animal animal} with the provided {@linkplain Animal animal} object, if it exists
     * 
     * @param animal The {@link Animal animal} to update
     * 
     * @return ResponseEntity with updated {@link Animal animal} object and HTTP status of OK if updated<br>
     * ResponseEntity with HTTP status of NOT_FOUND if not found<br>
     * ResponseEntity with HTTP status of INTERNAL_SERVER_ERROR otherwise
     */
    @PutMapping("")
    public ResponseEntity<Animal> updateAnimal(@RequestBody Animal animal) {
        LOG.info("PUT /animals " + animal);
        try {
            Animal h = this.animalDao.updateAnimal(animal);
            if (h != null) {
                return new ResponseEntity<>(h, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(h, HttpStatus.NOT_FOUND);
            }
        } catch (IOException e) {
            LOG.log(Level.SEVERE,e.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Deletes a {@linkplain Animal animal} with the given id
     * 
     * @param id The id of the {@link Animal animal} to deleted
     * 
     * @return ResponseEntity HTTP status of OK if deleted<br>
     * ResponseEntity with HTTP status of NOT_FOUND if not found<br>
     * ResponseEntity with HTTP status of INTERNAL_SERVER_ERROR otherwise
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Animal> deleteAnimal(@PathVariable int id) {
        LOG.info("DELETE /animals/" + id);
        try {
            if (this.animalDao.deleteAnimal(id)) {
                return new ResponseEntity<>(HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (IOException e) {
            LOG.log(Level.SEVERE,e.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
