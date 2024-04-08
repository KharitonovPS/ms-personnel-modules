package org.kharitonov.person.model.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.*;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class PersonDTO {

    private Long id;

    @NotEmpty(message = "Name should not be empty!")
    @Size(min = 2, max = 30, message = "Name length should be between 2 and 30 characters")
    private String name;

    @Min(value = 0, message = "Age should be greater than 0")
    @Max(value = 150, message = "Age must be less than or equal to 150")
    private int age;
}
