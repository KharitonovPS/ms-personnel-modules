package org.kharitonov.ms.person.module.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.kharitonov.ms.person.module.domain.Person;
import org.kharitonov.ms.person.module.dto.PersonDTO;
import org.kharitonov.ms.person.module.mapper.PersonDTOMapper;
import org.kharitonov.ms.person.module.service.PersonService;
import org.kharitonov.ms.person.module.util.BindingResultMessageBuilder;
import org.kharitonov.ms.person.module.util.PersonErrorResponse;
import org.kharitonov.ms.person.module.util.PersonNotCreatedException;
import org.kharitonov.ms.person.module.util.PersonNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/persons")
@RequiredArgsConstructor
public class PersonRESTController {

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
    public PersonDTO getPerson(@PathVariable("id") Long id) {
        return personDTOMapper
                .personToDto(personService.findById(id)
                );
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

    @GetMapping("/filterMultiplyEven")
    public List<PersonDTO> filterMultiplyEven() {
        return personService.filterMultiplyEven()
                .stream()
                .map(personDTOMapper::personToDto)
                .collect(Collectors.toList());
    }

    @GetMapping("/sortByAge")
    public List<PersonDTO> sortByAge() {
        return personService.sortByAge()
                .stream()
                .map(personDTOMapper::personToDto)
                .collect(Collectors.toList());
    }

    @GetMapping("/sortByFirstCharOfName")
    public Map<Character, List<PersonDTO>> sortByFirstCharOfName() {
        Map<Character, List<Person>> map = personService.sortByFirstCharOfName();

        return map.entrySet()
                .stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        entry -> entry.getValue()
                                .stream()
                                .map(personDTOMapper::personToDto)
                                .collect(Collectors.toList())
                ));
    }

    @GetMapping("/findMaxAge")
    public PersonDTO findMaxAge() {
        return personDTOMapper
                .personToDto(personService.findMaxAge()
                );
    }

    @GetMapping("/skipAndLimitation")
    public List<PersonDTO> skipAndLimitation() {
        return personService.skipAndLimitation()
                .stream()
                .map(personDTOMapper::personToDto)
                .collect(Collectors.toList());
    }

    @GetMapping("/personNamesAsString")
    public String personNamesAsString() {
        return personService.personNamesAsString();
    }

    @GetMapping("/isUnderage")
    public Boolean isUnderage() {
        return personService.isUnderage();
    }

    @GetMapping("/increaseAgeByTenPercent")
    public List<PersonDTO> increaseAgeByTenPercent() {
        return personService.increaseAgeByTenPercent()
                .stream()
                .map(personDTOMapper::personToDto)
                .collect(Collectors.toList());
    }

    @GetMapping("/deleteDuplicate")
    public List<PersonDTO> deleteDuplicate() {
        return personService.deleteDuplicate()
                .stream()
                .map(personDTOMapper::personToDto)
                .collect(Collectors.toList());
    }

    @GetMapping("/getStatistic")
    public Map<String, Integer> getStatistic() {
        return personService.getStatistic();
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
