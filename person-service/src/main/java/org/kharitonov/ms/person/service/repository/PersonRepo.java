package org.kharitonov.ms.person.service.repository;

import org.kharitonov.ms.person.service.domain.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PersonRepo extends JpaRepository<Person, Long> {

    List<Person> findAll();
}
