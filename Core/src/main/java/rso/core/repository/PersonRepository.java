package rso.core.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import rso.core.model.Person;

import java.util.Date;
import java.util.List;

/**
 * Created by kometa on 05.05.2015.
 */
public interface PersonRepository extends JpaRepository<Person, Long> {

    @EntityGraph(value = "Person.classes", type = EntityGraph.EntityGraphType.LOAD)
    List<Person> findAll();

    @EntityGraph(value = "Person.classes", type = EntityGraph.EntityGraphType.LOAD)
    Person findByUuid(String Uuid);

    @EntityGraph(value = "Person.classes", type = EntityGraph.EntityGraphType.LOAD)
    List<Person> findByUuidIsNull();

    @EntityGraph(value = "Person.classes", type = EntityGraph.EntityGraphType.LOAD)
    List<Person> findByTimestampGreaterThan(Date date);


    @Query(value = "Select * from student pg order by pg.timestamp desc limit 0, 1", nativeQuery = true)
    Person findNewestRecord();
}
