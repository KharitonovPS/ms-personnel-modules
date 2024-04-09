package org.kharitonov.ms.person.service.domain.person.constants;

public final class PersonConstants {
    public static final String PERSON_API = "/api/v1/persons";

    public static final String LIKE = "/like/";
    private PersonConstants() {
        throw new IllegalStateException("Utility class");
    }
}
