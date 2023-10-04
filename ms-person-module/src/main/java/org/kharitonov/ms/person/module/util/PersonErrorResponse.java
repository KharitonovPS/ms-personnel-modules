package org.kharitonov.ms.person.module.util;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class PersonErrorResponse {

    private String message;
    public Long timestamp;
}
