package rso.core.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import rso.core.model.Person;

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
}
