package uk.co.huntersix.spring.rest.model.request;


import javax.validation.constraints.NotNull;

import uk.co.huntersix.spring.rest.model.Person;

public class PersonRequest {

    @NotNull private String firstName;
    @NotNull private String lastName;

    private PersonRequest() {
    }

    public PersonRequest(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
    
    public static Person toPerson(PersonRequest personRequest){
    	return new Person(personRequest.getFirstName(), personRequest.getLastName());
    }
}