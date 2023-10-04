package org.kharitonov.ms.person.module.util;

public class PersonNotFoundException extends  RuntimeException{

    public PersonNotFoundException(Long id){
        super("Could not find person with id - " + id);
    }
    
}
