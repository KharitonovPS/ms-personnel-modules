package org.kharitonov.ms.person.service.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.kharitonov.ms.person.service.service.PersonService;
import org.kharitonov.ms.person.service.util.PersonErrorResponse;
import org.kharitonov.ms.person.service.util.PersonNotCreatedException;
import org.kharitonov.ms.person.service.util.PersonNotFoundException;
import org.kharitonov.person.model.dto.PersonDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/persons")
@RequiredArgsConstructor
public class PersonController {

    private final PersonService personService;

    @GetMapping
    public Page<PersonDTO> getAll(Pageable pageable) {
        return personService.getPages(pageable);
    }

    @GetMapping("/{id}")
    public PersonDTO getById(@PathVariable("id") Long id) {
        return personService.getElementById(id);
    }

    @PostMapping
    public ResponseEntity<HttpStatus> create(@RequestBody @Valid PersonDTO personDTO){
        personService.save(personDTO);
        return ResponseEntity.ok(HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<HttpStatus> update(
            @PathVariable("id") Long id,
            @RequestBody @Valid PersonDTO personDTO
    ) {
        personService.updatePerson(id, personDTO);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> delete(@PathVariable("id") Long id) {
        personService.deleteById(id);
        return ResponseEntity.ok(HttpStatus.OK);
    }
}
