package org.kharitonov.person.http.client;

import org.kharitonov.person.http.client.util.CustomPageImpl;
import org.kharitonov.person.model.dto.PersonDTO;

public interface PersonClient {
    CustomPageImpl<PersonDTO> findAllPerson();

    CustomPageImpl<PersonDTO> findAllPerson(int page, int size);

    PersonDTO findByName(String name);

    String create(PersonDTO personDTO);

    String deletePerson(Long id);

    String updatePerson(PersonDTO personDTO);
}
