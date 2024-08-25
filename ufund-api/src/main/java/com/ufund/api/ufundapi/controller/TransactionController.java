package com.ufund.api.ufundapi.controller;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ufund.api.ufundapi.model.Transaction;
import com.ufund.api.ufundapi.persistence.AnimalDAO;

/**
 * Handles the REST API requests for the Transaction resource
 * <p>
 * {@literal @}RestController Spring annotation identifies this class as a REST API
 * method handler to the Spring framework
 * 
 * @author Aiden Bewley
 */

@RestController
@RequestMapping("transactions")
public class TransactionController {
    
    private static final Logger LOG = Logger.getLogger(TransactionController.class.getName());
    private AnimalDAO animalDao;


    public TransactionController(AnimalDAO animalDao) {
        this.animalDao = animalDao;
    }

    /**
     * Responds to the GET request for all {@linkplain User users}
     * 
     * @return ResponseEntity with array of {@link User user} objects (may be empty) and
     * HTTP status of OK<br>
     * ResponseEntity with HTTP status of INTERNAL_SERVER_ERROR otherwise
     */
    @GetMapping("")
    public ResponseEntity<Transaction[]> getTransactions() {
        LOG.info("GET /transactions");
        try {
            return new ResponseEntity<>(this.animalDao.getTransactions(), HttpStatus.OK);
        } catch (IOException e) {
            LOG.log(Level.SEVERE,e.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
