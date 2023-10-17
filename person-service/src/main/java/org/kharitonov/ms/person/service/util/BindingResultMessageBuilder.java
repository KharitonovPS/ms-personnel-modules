package org.kharitonov.ms.person.service.util;

import lombok.NoArgsConstructor;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.util.List;
@NoArgsConstructor
public class BindingResultMessageBuilder {

    public String makeErrorMessage(BindingResult bindingResult){
        List<FieldError> errors = bindingResult.getFieldErrors();
        StringBuilder errorBuilder = new StringBuilder();

        for (FieldError error : errors) {
            errorBuilder.append(error.getField())
                    .append(" - ")
                    .append(error.getDefaultMessage())
                    .append(";");
        }
        return errorBuilder.toString();
    }
}
