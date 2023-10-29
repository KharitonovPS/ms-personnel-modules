package org.kharitonov.ms.person.service.util;

public class PersonNotFoundException extends  RuntimeException{

    public PersonNotFoundException(Long id){
        super("Could not find person with id - " + id);
    }
    public PersonNotFoundException(String name){
        super("Could not find person with name - " + name);
    }
    
}
