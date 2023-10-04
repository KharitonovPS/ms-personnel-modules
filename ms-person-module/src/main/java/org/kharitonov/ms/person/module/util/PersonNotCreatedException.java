package org.kharitonov.ms.person.module.util;

public class PersonNotCreatedException extends RuntimeException {
    public PersonNotCreatedException(String message){
        super(message);
    }
}
