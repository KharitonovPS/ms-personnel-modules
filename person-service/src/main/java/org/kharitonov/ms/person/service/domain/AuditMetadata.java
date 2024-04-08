package org.kharitonov.ms.person.service.domain;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.Instant;

@EqualsAndHashCode(of = "id", callSuper = false)
@ToString(of = "id")
@Data
@AllArgsConstructor
@NoArgsConstructor
@MappedSuperclass
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@EntityListeners(AuditingEntityListener.class)
public class AuditMetadata {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    Long id;

    public static final String SYSTEM_USER = "System";

    @CreatedDate
    @Column(name = "created_at", nullable = false)
    Instant createdAt = Instant.now();

    @LastModifiedDate
    @Column(name = "updated_at", nullable = false)
    Instant updatedAt = Instant.now();

//    @CreatedBy
    @Column(name = "created_by", nullable = false)
    String createdBy = SYSTEM_USER;

//    @LastModifiedBy
    @Column(name = "updated_by", nullable = false)
    String updatedBy =  SYSTEM_USER;


}
