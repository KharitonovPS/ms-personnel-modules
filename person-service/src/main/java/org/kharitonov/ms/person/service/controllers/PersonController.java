package org.kharitonov.ms.person.service.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.kharitonov.ms.person.service.domain.Person;
import org.kharitonov.ms.person.service.mapper.PersonDTOMapper;
import org.kharitonov.ms.person.service.service.PersonService;
import org.kharitonov.ms.person.service.util.BindingResultMessageBuilder;
import org.kharitonov.ms.person.service.util.PersonErrorResponse;
import org.kharitonov.ms.person.service.util.PersonNotCreatedException;
import org.kharitonov.ms.person.service.util.PersonNotFoundException;
import org.kharitonov.person.model.dto.PersonDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/persons")
@RequiredArgsConstructor
public class PersonController {

    private final PersonService personService;
    private final PersonDTOMapper personDTOMapper;
    private final BindingResultMessageBuilder bindingResultMessageBuilder;

    @GetMapping
    public List<PersonDTO> findAllPerson() {
        return personService.findAll().stream()
                .map(personDTOMapper::personToDto)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public PersonDTO getById(@PathVariable("id") Long id) {
        return personService.getById(id);
    }

    @PostMapping
    public ResponseEntity<HttpStatus> create(
            @RequestBody @Valid PersonDTO personDTO,
            BindingResult bindingResult
    ) {
        if (bindingResult.hasErrors()) {
            String errorMessage = bindingResultMessageBuilder
                    .makeErrorMessage(bindingResult);

            throw new PersonNotCreatedException(errorMessage);
        }
        personService.save(personDTOMapper.dtoToPerson(personDTO));

        return ResponseEntity.ok(HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<HttpStatus> updatePerson(
            @PathVariable("id") Long id,
            @RequestBody @Valid PersonDTO personDTO,
            BindingResult bindingResult
    ) {
        if (bindingResult.hasErrors()) {
            String errorMessage = bindingResultMessageBuilder
                    .makeErrorMessage(bindingResult);

            throw new PersonNotCreatedException(errorMessage);
        }
        Person person = personDTOMapper.dtoToPerson(personDTO);
        personService.updatePerson(id, person);

        return ResponseEntity.ok(HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> deleteById(@PathVariable("id") Long id) {
        personService.deleteById(id);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @ExceptionHandler
    private ResponseEntity<PersonErrorResponse> handler(PersonNotFoundException e) {
        PersonErrorResponse response = new PersonErrorResponse(
                e.getMessage(),
                System.currentTimeMillis()
        );
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler
    private ResponseEntity<PersonErrorResponse> handler(PersonNotCreatedException e) {
        PersonErrorResponse response = new PersonErrorResponse(
                e.getMessage(),
                System.currentTimeMillis()
        );
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
}
