package org.kharitonov.ms.person.service.domain.person.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.kharitonov.ms.person.service.domain.person.service.PersonService;
import org.kharitonov.person.model.dto.PersonDTO;
import org.kharitonov.person.model.dto.PersonResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.kharitonov.ms.person.service.domain.person.constants.PersonConstants.LIKE;
import static org.kharitonov.ms.person.service.domain.person.constants.PersonConstants.PERSON_API;


@Slf4j
@RestController
@RequestMapping(PERSON_API)
@RequiredArgsConstructor
public class PersonController {

    private final PersonService personService;

    @GetMapping()
    public Page<PersonDTO> getAll(Pageable pageable) {
        Page<PersonDTO> pageResponse = personService.getPages(pageable);
        log.info("getAll() request = {}, response = {}",
                pageable, pageResponse.toList());
        return pageResponse;
    }

    @GetMapping(LIKE + "{name}")
    public PersonDTO getByName(@PathVariable("name") String name) {
        PersonDTO singlePersonResponse = personService.getElementByName(name);
        log.info("getByName(): request = {}, response = {}", name, singlePersonResponse);
        return singlePersonResponse;
    }


    @PostMapping()
    public ResponseEntity<HttpStatus> create(@RequestBody @Valid PersonDTO personDTO) {
        log.info("crete() request = {}, response = {}", personDTO, HttpStatus.CREATED);
        personService.addPersonToQueue(personDTO);
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

    @GetMapping("/page")
    public PersonResponseDto takePages(
            @RequestParam("limit") int limit,
            @RequestParam("offset") int offset
    ) {
        return personService.findByPage(limit, offset);
    }
}
