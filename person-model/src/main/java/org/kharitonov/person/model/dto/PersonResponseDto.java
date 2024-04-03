package org.kharitonov.person.model.dto;

import java.util.LinkedList;

/**
 * @author Kharitonov Pavel on 08.02.2024.
 */
public record PersonResponseDto(Boolean hasNextElement, LinkedList<PersonDTO> personList) {


}
