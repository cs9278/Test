package uk.co.huntersix.spring.rest.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import uk.co.huntersix.spring.rest.model.Person;
import uk.co.huntersix.spring.rest.model.request.PersonRequest;
import uk.co.huntersix.spring.rest.referencedata.PersonDataService;

@RestController
public class PersonController {
    private PersonDataService personDataService;

    public PersonController(@Autowired PersonDataService personDataService) {
        this.personDataService = personDataService;
    }

    @GetMapping("/person/{lastName}/{firstName}")
    public Person person(@PathVariable(value="lastName") String lastName,
                         @PathVariable(value="firstName") String firstName) {
        return personDataService.findPerson(lastName, firstName)
        		.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Person not found"));
    }
    
    @GetMapping("/person/{lastName}")
    public List<Person> getPeopleByLastName(@PathVariable(value="lastName") String lastName) {
        return personDataService.findPeopleByLastName(lastName);
    }
    
    @PostMapping("/person")
    public ResponseEntity<?> addPerson(@RequestBody @Valid PersonRequest personRequest) {
    	return personDataService.findPerson(personRequest.getLastName(), personRequest.getFirstName())
    		.map(person -> new ResponseEntity<>(new Error("Person is already added"), HttpStatus.UNPROCESSABLE_ENTITY))
    		.orElseGet(() -> {
    			personDataService.addPerson(PersonRequest.toPerson(personRequest));
    			return new ResponseEntity<>(HttpStatus.CREATED);
    	});
    }
}