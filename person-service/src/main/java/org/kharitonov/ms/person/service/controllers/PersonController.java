package org.kharitonov.ms.person.service.controllers;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.kharitonov.ms.person.service.service.PersonService;
import org.kharitonov.person.model.dto.PersonDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@Slf4j
@RestController
@RequestMapping("/persons")
@RequiredArgsConstructor
public class PersonController {

    private final PersonService personService;

    @GetMapping
    public Page<PersonDTO> getAll(Pageable pageable) {
        log.info("getAll() request = {}, response = {}", pageable,
                personService.getPages(pageable)
                .stream()
                .toList());
        return personService.getPages(pageable);
    }

    @GetMapping("/{name}")
    public PersonDTO getByName(@PathVariable("name") String name) {
        log.info("getByName(): request = {}, response = {}", name, personService.getElementByName(name));
        return personService.getElementByName(name);

    }

    @PostMapping
    public ResponseEntity<HttpStatus> create(@RequestBody @Valid PersonDTO personDTO){
        log.info("crete() request = {}, response = {}", personDTO, HttpStatus.CREATED);
        personService.save(personDTO);
    return ResponseEntity.ok(HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<HttpStatus> update(
            @PathVariable("id") Long id,
            @RequestBody @Valid PersonDTO personDTO
    ) {
        log.info("update() request = id {}, response = {}", id, HttpStatus.OK);
        personService.updatePerson(id, personDTO);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> delete(@PathVariable("id") Long id) {
        log.info("delete() request = id {}, response = {}", id, HttpStatus.OK);
        personService.deleteById(id);
        return ResponseEntity.ok(HttpStatus.OK);
    }
}
