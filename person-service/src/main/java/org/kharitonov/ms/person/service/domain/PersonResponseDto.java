package org.kharitonov.ms.person.service.domain;

import java.util.LinkedList;

/**
 * @author Kharitonov Pavel on 08.02.2024.
 */
public record PersonResponseDto(Boolean hasNextElement, LinkedList<Person> personList) {


}
