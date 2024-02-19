package org.kharitonov.ms.person.service.repository;

import org.kharitonov.ms.person.service.domain.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PersonRepo extends JpaRepository<Person, Long>, PagingAndSortingRepository<Person, Long> {

    Optional<Person> findByName(String name);

    List<Person> findAllByNameIn(List<String> names);

    @Query(value = "SELECT * FROM Person p " +
            "ORDER BY p.updated_at DESC " +
            "LIMIT :limit " +
            "OFFSET :offset", nativeQuery = true)
    List<Person> findByPage(@Param("limit") int limit, @Param("offset") int offset);



    List<Person> findPersonByNameContaining (String name);
}
