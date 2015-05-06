package rso.core.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import rso.core.model.PersonGroup;
import rso.core.model.PersonGroupPK;

import java.util.Date;
import java.util.List;

/**
 * Created by kometa on 06.05.2015.
 */
public interface PersonGroupRepository extends JpaRepository<PersonGroup, PersonGroupPK> {

    @EntityGraph(value = "PersonGroup.details", type = EntityGraph.EntityGraphType.LOAD)
    List<PersonGroup> findByTimestampGreaterThan(Date date);
}
