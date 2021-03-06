package uk.co.huntersix.spring.rest.referencedata;

import org.springframework.stereotype.Service;
import uk.co.huntersix.spring.rest.model.Person;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PersonDataService {
    public static final List<Person> PERSON_DATA = Arrays.asList(
        new Person("Mary", "Smith"),
        new Person("Brian", "Archer"),
        new Person("Collin", "Brown")
    );

    public Optional<Person> findPerson(String lastName, String firstName) {
        return PERSON_DATA.stream()
            .filter(p -> p.getFirstName().equalsIgnoreCase(firstName)
                && p.getLastName().equalsIgnoreCase(lastName))
            .findFirst();
    }
    
    public List<Person> findPeopleByLastName(String lastName) {
        return PERSON_DATA.stream()
            .filter(p -> p.getLastName().equalsIgnoreCase(lastName))
            .collect(Collectors.toList());
    }
    
    public void addPerson(Person person) {
        PERSON_DATA.add(person);
    }
  
}
