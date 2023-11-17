package org.kharitonov.ms.person.service.repository;

import org.kharitonov.ms.person.service.domain.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PersonRepo extends JpaRepository<Person, Long>, PagingAndSortingRepository<Person, Long> {

    Optional<Person> findByName(String name);

    List<Person> findAllByName(String name);
}
