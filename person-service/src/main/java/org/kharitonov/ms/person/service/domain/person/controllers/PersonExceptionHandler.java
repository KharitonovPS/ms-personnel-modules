package org.kharitonov.ms.person.service.domain.person.controllers;

import org.kharitonov.ms.person.service.exceptions.PersonErrorResponse;
import org.kharitonov.ms.person.service.exceptions.PersonNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class PersonExceptionHandler {

    @ExceptionHandler
    private ResponseEntity<PersonErrorResponse> handler(PersonNotFoundException e) {
        PersonErrorResponse response = new PersonErrorResponse(
                e.getMessage(),
                System.currentTimeMillis()
        );
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

}
