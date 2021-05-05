package uk.co.huntersix.spring.rest.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import uk.co.huntersix.spring.rest.model.Person;
import uk.co.huntersix.spring.rest.model.request.PersonRequest;
import uk.co.huntersix.spring.rest.referencedata.PersonDataService;

@RunWith(SpringRunner.class)
@WebMvcTest(PersonController.class)
public class PersonControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PersonDataService personDataService;

    @Test
    public void shouldReturnPersonFromService() throws Exception {
        when(personDataService.findPerson(any(), any())).thenReturn(Optional.of(new Person("Mary", "Smith")));
        this.mockMvc.perform(get("/person/smith/mary"))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("id").exists())
            .andExpect(jsonPath("firstName").value("Mary"))
            .andExpect(jsonPath("lastName").value("Smith"));
    }
    
    @Test
    public void shouldReturn404FromServiceWhenPersonIsNotFound() throws Exception {
    	when(personDataService.findPerson(any(), any())).thenReturn(Optional.empty());
        this.mockMvc.perform(get("/person/smith/mary"))
            .andDo(print())
            .andExpect(status().isNotFound());
    }
    
    @Test
    public void shouldReturnAllPeopleByLastName() throws Exception {
    	List<Person> people = new ArrayList<>();
    	people.add(new Person("Mary","Smith"));
    	people.add(new Person("Lilly","Smith"));
    	people.add(new Person("Lock","Smith"));
    	when(personDataService.findPeopleByLastName(any())).thenReturn(people);
        this.mockMvc.perform(get("/person/smith"))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(3)))
            .andExpect(jsonPath("$[0].firstName").value("Mary"))
            .andExpect(jsonPath("$[0].lastName").value("Smith"));
    }
    
    @Test
    public void shouldReturnEmptyListWhenLastNameNotFound() throws Exception {
    	List<Person> people = new ArrayList<>();
    	when(personDataService.findPeopleByLastName(any())).thenReturn(people);
        this.mockMvc.perform(get("/person/mark"))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(content().string("[]"));
    }
    
    @Test
    public void shouldAddPersonFromService() throws Exception {
    	PersonRequest request = new PersonRequest("Raj","Kumar");
        doNothing().when(personDataService).addPerson(PersonRequest.toPerson(request));
        this.mockMvc.perform(post("/person")
        	.content(asJsonString(request))
        	.contentType(MediaType.APPLICATION_JSON))
            .andDo(print())
            .andExpect(status().isCreated());
    }
    
    @Test
    public void shouldReturnStatus422FromServiceWhenPersonIsAlreadyAdded() throws Exception {
    	PersonRequest request = new PersonRequest("Raj","Kumar");
        when(personDataService.findPerson(any(), any())).thenReturn(Optional.of(new Person("Mary", "Smith")));
        this.mockMvc.perform(post("/person")
        	.content(asJsonString(request))
        	.contentType(MediaType.APPLICATION_JSON))
            .andDo(print())
            .andExpect(status().isUnprocessableEntity());
    }
    
    public static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }  
}

