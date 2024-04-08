package org.kharitonov.ms.person.service.domain.person.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.kharitonov.ms.person.service.domain.AuditMetadata;


@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(indexes = {@Index(name = "idx_name", columnList = "name", unique = true)})
//@Document(indexName = "resume")
public class Person extends AuditMetadata {

    @NotEmpty(message = "Name should not be empty!")
    @Size(min = 2, max = 30, message = "Name length should be between 2 and 30 characters")
    private String name;

    @Min(value = 0, message = "Age should be greater than 0")
    @Max(value = 150, message = "Age must be less than or equal to 150")
    private int age;

//    @Size(max = 10000, message = "Resume must be less than or equal to 10_000 chars")
//    private String resume;

}
