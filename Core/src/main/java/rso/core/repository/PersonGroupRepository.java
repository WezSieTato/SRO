package rso.core.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import rso.core.model.PersonGroup;
import rso.core.model.PersonGroupPK;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;

/**
 * Created by kometa on 06.05.2015.
 */
public interface PersonGroupRepository extends JpaRepository<PersonGroup, PersonGroupPK> {

    @EntityGraph(value = "PersonGroup.details", type = EntityGraph.EntityGraphType.LOAD)
    List<PersonGroup> findByTimestampGreaterThan(Date date);

    @Query(value = "Select * from student_class pg order by pg.timestamp desc limit 0, 1", nativeQuery = true)
    PersonGroup findNewestRecord();

    PersonGroup findByUuid(String uuid);

    @Transactional
    @Modifying

    @Query(value = "insert into student_class values (:id_person, :id_group,  FROM_UNIXTIME(:timestamp),:uuid)", nativeQuery = true)
    void save(@Param("id_person") int id_person, @Param("id_group") int id_group, @Param("uuid") String uuid, @Param("timestamp") long timestamp);
}
