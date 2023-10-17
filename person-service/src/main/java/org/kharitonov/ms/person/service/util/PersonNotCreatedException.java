package org.kharitonov.ms.person.service.util;

public class PersonNotCreatedException extends RuntimeException {
    public PersonNotCreatedException(String message){
        super(message);
    }
}
